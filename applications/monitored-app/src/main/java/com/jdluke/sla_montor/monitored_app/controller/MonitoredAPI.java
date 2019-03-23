package com.jdluke.sla_montor.monitored_app.controller;

import com.jdluke.sla_monitor.Metered;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MonitoredAPI {

    @GetMapping("/monitored_ok")
    @Metered
    public String MonitoredOK() {
        return "OK";
    }

    @GetMapping("/monitored_warn")
    @Metered(millisecondsBeforeWarning = 0)
    public String MonitoredWarn() {
        long total = 0;
        for (int i = 0; i < 10000; i++) {
            total += i;
        }
        return "Warn: " + total;
    }
}
