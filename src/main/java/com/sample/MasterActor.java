package com.sample;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.UntypedActor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.sample.SpringExtension.SpringExtProvider;

/**
 * Master Actor of application which acts as root supervisor for actors
 */
@Component
@Scope("prototype")
public class MasterActor extends UntypedActor {

    public static final String MASTER_ACTOR_NAME = "masterActor";

    private final ActorRef countingActor;

    @Autowired
    public MasterActor(ActorSystem actorSystem) {
        countingActor = getContext().actorOf(SpringExtProvider.get(actorSystem).props(CountingActor.COUNTING_ACTOR_NAME),
                CountingActor.COUNTING_ACTOR_NAME);
    }


    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof CountingActor.Count) {
            countingActor.forward(message, context());
        } else if (message instanceof CountingActor.Get) {
            countingActor.forward(message, context());
        } else {
            unhandled(message);
        }
    }
}
