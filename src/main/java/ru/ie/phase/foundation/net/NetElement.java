package ru.ie.phase.foundation.net;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NetElement {
    String netTypeId();
}
