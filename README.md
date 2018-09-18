# Example fluentd configuration for message routing

*Spring Boot logging with logback, JSON logging to the standard out from a docker container.*

Docker logging with docker fluentd logger settings, fluentd writes messages to the standard out.

*Goal: you don't need to add fluent dependency to your code,* just logging to standard output.
You can route your log messages with _dest: journal_ key, and it will be saved to journal database, any others will be saved to the log database.

* mvn clean install

* Start with ```docker-compose up -d --build```

* Log some message with ```curl -X GET http://localhost:4000/greeting```

* Tail fluentd log with ```docker logs --follow test_fluentd```

* Stop with ```CTRL-C``` and ```docker-compose down --remove-orphans```

When you logging with _dest_ key with _journal_ value, then output wil be saved into the journal DB.

```java
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
```

```java
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
```

## Logging JSON

pom.xml

```xml
<dependency>
    <groupId>net.logstash.logback</groupId>
    <artifactId>logstash-logback-encoder</artifactId>
    <version>4.10</version>
</dependency>
```

and add ```logback-spring.xml```

```xml
<configuration>
  <appender name="consoleAppender" class="ch.qos.logback.core.ConsoleAppender">
    <encoder class="net.logstash.logback.encoder.LogstashEncoder"/>
  </appender>
  <logger name="jsonLogger" additivity="false" level="DEBUG">
    <appender-ref ref="consoleAppender"/>
  </logger>
  <root level="INFO">
    <appender-ref ref="consoleAppender"/>
  </root>
</configuration>
```

## Advices

Turn off Spring Boot banner

```
spring.main.banner-mode=off
```

Remove unnecessary keys from the log

```javascript
remove_keys ["@timestamp", "thread_name", "level_value", "@version"]
```
