package com.jdluke.sla_monitor.monitor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The intention here is to provide an annotation for gathering runtime metrics in a simple,
 * automated fashion which can be analyzed in any industry standard log file aggregator or just
 * a spreadsheet.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Monitor {}
