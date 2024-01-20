package ru.ie.phase.foundation.net;

import ru.ie.phase.Phase;

import java.util.*;
import java.util.function.Predicate;

public class ElectricalNetSpace {

    public static final HashRegistry<NetNode> nodes = new HashRegistry<>();
    private static final HashRegistry<ICable> cables = new HashRegistry<>();

    private static final Map<UUID, UUID> nodeJoints = new HashMap<>();

    public static UUID createCable(ICable cable){
        Phase.LOGGER.debug("creating new cable");
        return cables.create(cable);
    }

    public static void addCable(UUID id, ICable cable){
        cables.put(id, cable);
    }

    public static UUID createNode(NetNode node){
        return nodes.create(node);
    }

    public static void addNode(UUID id, NetNode node){
        nodes.put(id, node);
    }

    public static void connectCable2Cable(UUID c1, UUID c2)
    {
        ICable cable1 = cables.get(c1);
        ICable cable2 = cables.get(c2);

        cables.get(c1).createLink(c2);
        cables.get(c2).createLink(c1);

        cable1.lossmap().keySet().forEach(uuid -> {
            if(cable2.lossmap().containsKey(uuid))
                partialRemapLosses(uuid, c1, c2);
            else
                partialRemapLosses(uuid, c1);
        });

        cable2.lossmap().keySet().forEach(uuid -> {
            if(cable1.lossmap().containsKey(uuid)) return;
            remapLosses(uuid, c2);
        });

        Phase.LOGGER.debug("c2c %s - %s".formatted(c1, c2));
    }

    public static void connectCable2Node(UUID cableId, UUID nodeId)
    {
        if(nodeJoints.containsKey(nodeId)) return;

        cables.get(cableId).nodes().add(nodeId);
        nodeJoints.put(nodeId, cableId);

        if(nodes.get(nodeId) instanceof NetGenerator)
            remapLosses(nodeId, cableId);
        else
            updatePowerStatement(nodeId);

        Phase.LOGGER.debug("c2n %s - %s".formatted(cableId, nodeId));
    }

    public static void removeCable(UUID id)
    {
        ICable cable = cables.get(id);

        cable.nodes().forEach(ElectricalNetSpace::disconnect);

        if(cable.links().size() == 1){
            Phase.LOGGER.debug("removed ep-c %s".formatted(id));
            cables.get(cable.links().get(0)).links().remove(id);
        }else {
            Phase.LOGGER.debug("removed c %s".formatted(id));

            Set<UUID> generators = new HashSet<>(cable.lossmap().keySet());

            generators.forEach(ElectricalNetSpace::clearGeneratorLosses);
            cable.links().forEach(s -> cables.get(s).links().remove(id));
            generators.forEach(ElectricalNetSpace::remapLosses);
        }

        cables.remove(id);
    }

    public static void removeNode(UUID nodeId){
        UUID cableId = nodeJoints.get(nodeId);
        Phase.LOGGER.debug("removed c %s".formatted(nodeId));
        if(cables.containsKey(cableId)){
            cables.get(cableId).nodes().remove(nodeId);
            disconnect(nodeId);
            nodeJoints.remove(nodeId);
        }
        nodes.remove(nodeId);
    }

    public static void updatePowerStatement(UUID consumerId){
        NetConsumer consumer = (NetConsumer) nodes.get(consumerId);
        ICable cable = cables.get(nodeJoints.get(consumerId));
        if(cable.lossmap().isEmpty()) return;

        Map<UUID, Float> voltageMap = applyVoltage(cable.lossmap());
        float voltage = calculateVoltage(voltageMap);

        consumer.updateVoltage(voltage);

        Map<UUID, Float> powerMap = getBalancedPowerMap(voltageMap, consumer.getAmperage() * voltage);
        powerMap.forEach((uuid, aFloat) -> ((NetGenerator) nodes.get(uuid)).updateConsumerStatement(consumerId, aFloat));
    }

    private static void clearGeneratorLosses(UUID generatorId){
        clearGeneratorLosses(generatorId, nodeJoints.get(generatorId));
    }

    private static void clearGeneratorLosses(UUID generatorId, UUID cableid){
        clearGeneratorLosses(generatorId, cableid, new HashSet<>());
    }

    private static void clearGeneratorLosses(UUID generatorId, UUID cableid, HashSet<UUID> checked)
    {
        UUID current = cableid;
        for(;;){
            ICable cable = cables.get(current);
            cable.lossmap().remove(generatorId);
            updateConsumers(cable);
            checked.add(current);

            Phase.LOGGER.debug("removing generator %s losses at %s".formatted(generatorId, current));

            List<UUID> filtrateLinks = cable.links().stream()
                    .filter(Predicate.not(checked::contains))
                    .toList();

            if(filtrateLinks.isEmpty())
                break;
            else if(filtrateLinks.size() > 1)
                filtrateLinks.forEach(s -> clearGeneratorLosses(generatorId, s, checked));
            current = filtrateLinks.get(0);
        }
    }

    private static void remapLosses(UUID generatorId){
        remapLosses(generatorId, nodeJoints.get(generatorId));
    }

    private static void remapLosses(UUID generatorId, UUID cableId)
    {
        List<UUID> activeNodes = new ArrayList<>();
        activeNodes.add(cableId);
        initCableLoss(generatorId, cableId);
        remapLosses(generatorId, activeNodes, true);
    }

    private static void partialRemapLosses(UUID generatorId, UUID... points){
        remapLosses(generatorId, new ArrayList<>(List.of(points)), false);
    }

    private static void remapLosses(UUID generatorId, List<UUID> activeNodes, boolean isFull)
    {
        Phase.LOGGER.debug("remapping...");

        HashSet<UUID> checked;

        if(isFull) checked = new HashSet<>();
        else checked = null;

        while(!activeNodes.isEmpty()){

            UUID nearestCableId = activeNodes.stream()
                    .min(Comparator.comparing(s -> cables.get(s).lossmap().get(generatorId)))
                    .get();

            ICable nearestCable = cables.get(nearestCableId);

            nearestCable.links().forEach(s -> {
                ICable cable = cables.get(s);
                float loss = nearestCable.lossmap().get(generatorId) + nearestCable.loss();
                if(
                        !cable.lossmap().containsKey(generatorId) ||
                         cable.lossmap().get(generatorId) > loss ||
                         (checked != null && !checked.contains(s))
                ) {
                    cable.lossmap().put(generatorId, loss);
                    activeNodes.add(s);
                    if(checked != null) checked.add(s);
                    Phase.LOGGER.debug("%s updated loss to %f".formatted(s, loss));
                }
            });

            activeNodes.remove(nearestCableId);
        }

    }

    private static void initCableLoss(UUID generatorId, UUID cableId){
        ICable cable = cables.get(cableId);
        cable.lossmap().put(generatorId, cable.loss());
    }

    private static Map<UUID, Float> applyVoltage(Map<UUID, Float> lossmap)
    {
        Map<UUID, Float> voltageMap = new HashMap<>();

        lossmap.forEach((uuid, aFloat) -> {
            NetGenerator generator = (NetGenerator) nodes.get(uuid);
            voltageMap.put(uuid, Math.max(0, generator.getVoltage() - aFloat));
        });

        return voltageMap;
    }

    private static Map<UUID, Float> getBalancedPowerMap(Map<UUID, Float> voltageMap, float power)
    {
        Map<UUID, Float> powerMap = new HashMap<>();
        Map<UUID, Float> ratioMap = new HashMap<>();

        voltageMap.forEach((uuid, aFloat) -> {
            NetGenerator generator = (NetGenerator) nodes.get(uuid);
            powerMap.put(uuid, Math.max(0, generator.getAmperage() * aFloat));
        });

        float absolutePower = (float) powerMap.values().stream()
                .mapToDouble(Float::doubleValue)
                .sum();

        powerMap.forEach((uuid, aFloat) -> ratioMap.put(uuid, power / absolutePower * aFloat));

        return ratioMap;
    }

    private static void updateConsumers(ICable cable){
        cable.nodes().forEach(uuid -> {
            if(nodes.get(uuid) instanceof NetConsumer) updatePowerStatement(uuid);
        });
    }

    private static float calculateVoltage(Map<UUID, Float> voltageMap){
        return (float) voltageMap.values().stream()
                .mapToDouble(Float::floatValue)
                .max()
                .orElseThrow();
    }

    private static void disconnect(UUID nodeId){
        NetNode node = nodes.get(nodeId);
        ICable cable = cables.get(nodeJoints.get(nodeId));
        if(node instanceof NetGenerator) {
            clearGeneratorLosses(nodeId);
        }else {
            cable.lossmap().forEach((uuid, aFloat) ->
                    ((NetGenerator)nodes.get(uuid)).removeConsumer(nodeId)
            );
        }
    }

}