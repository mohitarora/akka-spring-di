package com.sample;

import akka.actor.ActorSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static com.sample.SpringExtension.SpringExtProvider;

@Configuration
@ComponentScan("com.sample")
public class AppConfiguration {

    /**
     * Inject Application context so that it can be passed to Actor System Extension Provider.
     * http://doc.akka.io/api/akka/2.2.0-RC2/index.html#akka.actor.ExtendedActorSystem
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Actor system singleton for this application.
     */
    @Bean
    public ActorSystem actorSystem() {
        ActorSystem system = ActorSystem.create("AkkaJavaSpringDI");
        // initialize the application context in the Akka Spring Extension.
        SpringExtProvider.get(system).initialize(applicationContext);
        return system;
    }
}
