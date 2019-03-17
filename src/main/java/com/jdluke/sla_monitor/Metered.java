package com.jdluke.sla_monitor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Use this to monitor and report on the duration of processes at run time.
 *
 * Annotate your method like so:
 * @Metered()
 *
 * Set the maximum execution time for SLA violations like this (default is five seconds):
 * @Metered(millisecondsBeforeError = 3200)
 *
 * If you would also like warning messages to be displayed above a different, lower threshold you can do so like this:
 * @Metered(millisecondsBeforeWarning = 750)
 *
 * If you want to not only note the SLA violation but also throw an exception as a result, you can do this:
 * @Metered(throwExceptionOnViolation = true)
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Metered {
    long millisecondsBeforeWarning() default  -1l;
    long millisecondsBeforeError() default  5000l;
    boolean throwExceptionOnViolation() default false;
}
