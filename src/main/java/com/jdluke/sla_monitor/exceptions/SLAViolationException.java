package com.jdluke.sla_monitor.exceptions;

public class SLAViolationException extends Exception {
    String name;
    long millisecondsBeforeError;
    long duration;

    public SLAViolationException(String name, long millisecondsBeforeError, long duration) {
        this.name = name;
        this.millisecondsBeforeError = millisecondsBeforeError;
        this.duration = duration;
    }

    public String toString() {
        return "SLA Violation " + name + ".  Time allowed is " + millisecondsBeforeError + " ms. but actual time was " + duration + " ms.";
    }
}
