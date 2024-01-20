package ru.ie.phase.foundation.net;

public enum VoltageLevel {

    NONE(0),

    LV(30),
    MV(220),
    HV(600),
    EV(3000);

    private final int voltage;
    VoltageLevel(int voltage){
        this.voltage = voltage;
    }

    public static float transform(float amperage, VoltageLevel source, VoltageLevel target){
        return source.getPower(amperage) / target.getVoltage();
    }

    public int getVoltage(){
        return voltage;
    }

    public float getPower(float amperage){
        return amperage * voltage;
    }

}
