package com.example.logging;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import org.springframework.stereotype.Component;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Example endpoint.
 * @author Pató István <istvan.pato@gmail.com>
 */
@Component
public class GreetingHandler {
    
    Journal journal;

    @Autowired
    public GreetingHandler(Journal journal) {
        this.journal = journal;
    }
    
    private static final org.slf4j.Logger Logger =
            org.slf4j.LoggerFactory.getLogger(GreetingHandler.class);

    @GetMapping("/greeting")
    Mono<ServerResponse> greeting(ServerRequest request) {
        journal.log("Message sent to journal DB");
        Logger.error("Something else is wrong here");
        return ServerResponse.ok().contentType(TEXT_PLAIN).body(fromObject("Hello World!"));
    }
}
