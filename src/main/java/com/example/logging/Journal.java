package com.example.logging;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * Journaling message.
 * @author Pató István <istvan.pato@gmail.com>
 */
@Component
public class Journal {
    
    private static final org.slf4j.Logger Logger =
            org.slf4j.LoggerFactory.getLogger(Journal.class);
    
    public void log(String message) {
        // fluentd config filter
        MDC.put("dest", "journal");
        Logger.info(message);
    }
}
