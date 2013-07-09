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

    public static final String MASTER_ACTOR_REF = "masterActorRef";

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
     * This actor is created so that actor system gets initialized with all the actors.
     *
     * @return
     */
    @Bean(name = MASTER_ACTOR_REF)
    @DependsOn(ACTOR_SYSTEM)
    public ActorRef masterActor() {
        return actorSystem.actorOf(SpringExtProvider.get(actorSystem).props(MasterActor.MASTER_ACTOR_NAME), MasterActor.MASTER_ACTOR_NAME);
    }
}
