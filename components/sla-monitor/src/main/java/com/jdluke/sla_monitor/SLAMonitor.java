package com.jdluke.sla_monitor;

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
//import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Configuration
@ComponentScan("com.jdluke.sla_monitor")
@Aspect
public class SLAMonitor {

    private final Marker slaMarker;

    public SLAMonitor(Marker slaMarker) {
        this.slaMarker = slaMarker;
    }

    static final Logger logger = LoggerFactory.getLogger(SLAMonitor.class.getName());

    @Around("@annotation(com.jdluke.sla_monitor.sla.SLA)")
    public Object aroundWebController(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        SLA SLA = extractAnnotatedMethod(proceedingJoinPoint).getAnnotation(SLA.class);
        String name = proceedingJoinPoint.getSignature().getName();

        logger.debug("SLA: {} milliseconds before error.", SLA.millisecondsBeforeError());
        if (SLA.millisecondsBeforeWarning() > -1) {
            logger.debug("Allowing {} milliseconds before warning.", SLA.millisecondsBeforeWarning());
        }

        long start = System.currentTimeMillis();
        Object value = null;
        try {
            value = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        } catch (Throwable t) {
            logger.error(slaMarker, "Error calling method {}", t);
            throw t;
        }
        long end = System.currentTimeMillis();

        long duration = end - start;
        if (duration > SLA.millisecondsBeforeError()) {
            logger.error(slaMarker, "SLA Violation calling {}.  Actual response time of {} exceeded threshold of {}", name, duration, SLA.millisecondsBeforeError());
            if (SLA.throwExceptionOnViolation()) {
                throw new SLAViolationException(name, SLA.millisecondsBeforeError(), duration);
            }
        } else {
            if (SLA.millisecondsBeforeWarning() > 0) {
                if (end - start > SLA.millisecondsBeforeWarning()) {
                    logger.warn(slaMarker, "SLA Warning calling {}.  Actual response time of {} exceeded threshold of {}", name, duration, SLA.millisecondsBeforeWarning());
                }
            }
        }

        logger.info("Around Web Controller {} ends after {} milliseconds", proceedingJoinPoint.toShortString(), end - start);
        return value;
    }

    private Method extractAnnotatedMethod(ProceedingJoinPoint proceedingJoinPoint) throws InvalidMethodException {
        Class c = proceedingJoinPoint.getTarget().getClass();
        Signature signature = proceedingJoinPoint.getSignature();

        String name = signature.getName();

        Method m = null;//c.getMethod(name, proceedingJoinPoint.getArgs());
        Method[] methods = c.getMethods();

        for (Method method : methods) {
            if (method.getName().equals(name)) {
                return method;
            }
        }
        throw new InvalidMethodException();
    }

    private void DocumentJoinPoint(ProceedingJoinPoint proceedingJoinPoint) {
        logger.info("{}", proceedingJoinPoint.getTarget());             // com.ups.oms.code_standards.controller.WebController@7cc16926
        logger.info("{}", proceedingJoinPoint.getArgs());               // test3
        logger.info("{}", proceedingJoinPoint.getKind());               // method-execution
        logger.info("{}", proceedingJoinPoint.getSignature());          // String com.ups.oms.code_standards.controller.WebController.webTest(String)
        logger.info("{}", proceedingJoinPoint.getSourceLocation());     // org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint$SourceLocationImpl@3b4f14e2
        logger.info("{}", proceedingJoinPoint.getStaticPart());         // execution(String com.ups.oms.code_standards.controller.WebController.webTest(String))
        logger.info("{}", proceedingJoinPoint.getThis());               // com.ups.oms.code_standards.controller.WebController@7cc16926

        Signature signature = proceedingJoinPoint.getSignature();               // com.ups.oms.code_standards.controller.WebController@7cc16926
        logger.info("Signature.getName: {}", signature.getName());
    }

    public static <T> T getAnnotationParameter(Annotation annotation, String parameterName, Class<T> type) {
        try {
            Method m = annotation.getClass().getMethod(parameterName);
            Object o = m.invoke(annotation);
            if (o.getClass().getName().equals(type.getName())) {
                return (T) o;
            } else {
                String msg = "Wrong parameter type. Expected: " + type.getName() + " Actual: " + o.getClass().getName();
                throw new RuntimeException(msg);
            }
        } catch (NoSuchMethodException e) {
            String msg = "The specified annotation defines no parameter '" + parameterName + "'.";
            throw new RuntimeException(msg, e);
        } catch (IllegalAccessException e) {
            String msg = "Unable to get '" + parameterName + "' from " + annotation.getClass().getName();
            throw new RuntimeException(msg, e);
        } catch (InvocationTargetException e) {
            String msg = "Unable to get '" + parameterName + "' from " + annotation.getClass().getName();
            throw new RuntimeException(msg, e);
        }
    }


}

