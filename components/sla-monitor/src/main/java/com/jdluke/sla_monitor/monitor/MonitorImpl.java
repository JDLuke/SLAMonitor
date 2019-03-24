package com.jdluke.sla_monitor.monitor;

import com.jdluke.sla_monitor.exceptions.InvalidMethodException;
import com.jdluke.sla_monitor.exceptions.SLAViolationException;
import com.jdluke.sla_monitor.sla.SLA;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//import org.springframework.context.annotation.ComponentScan;

@Configuration
@ComponentScan("com.jdluke.sla_monitor")
@Aspect
public class MonitorImpl {

    private final Marker monitorMarker;

    public MonitorImpl(Marker monitorMarker) {
        this.monitorMarker = monitorMarker;
    }

    static final Logger logger = LoggerFactory.getLogger(MonitorImpl.class.getName());

    @Around("@annotation(com.jdluke.sla_monitor.monitor.Monitor)")
    public Object aroundWebController(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String name = proceedingJoinPoint.getSignature().getName();

        long start = System.currentTimeMillis();
        Object value = null;
        try {
            value = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        } catch (Throwable t) {
            logger.error(monitorMarker, "Error calling method {}", t);
            throw t;
        }
        long end = System.currentTimeMillis();

        long duration = end - start;

        logger.info("Around Web Controller {} ends after {} milliseconds", proceedingJoinPoint.toShortString(), end - start);
        return value;
    }

}

