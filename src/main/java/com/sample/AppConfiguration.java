package com.sample;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import static com.sample.SpringExtension.SpringExtProvider;

@Configuration
@ComponentScan("com.sample")
public class AppConfiguration {

    private static final String ACTOR_SYSTEM = "AkkaJavaSpringDI";

    private ActorSystem actorSystem;

    /**
     * Inject Application context so that it can be passed to Actor System Extension Provider.
     * http://doc.akka.io/api/akka/2.2.0-RC2/index.html#akka.actor.ExtendedActorSystem
     */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Actor system singleton for this application.
     */
    @Bean(name = ACTOR_SYSTEM, destroyMethod = "shutdown")
    public ActorSystem actorSystem() {
        actorSystem = ActorSystem.create(ACTOR_SYSTEM);
        // initialize the application context in the Akka Spring Extension.
        SpringExtProvider.get(actorSystem).initialize(applicationContext);
        return actorSystem;
    }

    /**
     * Following bean definition will expose the actor ref as spring bean as well, so we can inject actor ref in spring bean as well.
     * <p/>
     * In this example
     * <p/>
     * counter is the bean name of actorRef
     * countingActor is the bean name of Actor
     * beanCounter is the actor name in akka world.
     *
     * @return
     */
    @Bean(name = "counter")
    @DependsOn({ACTOR_SYSTEM})
    public ActorRef countingActor() {
        return actorSystem.actorOf(SpringExtProvider.get(actorSystem).props("countingActor"), "beanCounter");
    }
}
