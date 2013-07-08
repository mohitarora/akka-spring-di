package com.sample;

import akka.actor.UntypedActor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class CountingActor extends UntypedActor {

    private final CountingService countingService;

    public static class Count {
    }

    public static class Get {
    }

    private int count = 0;

    @Autowired
    public CountingActor(CountingService countingService) {
        this.countingService = countingService;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Count) {
            count = countingService.increment(count);
        } else if (message instanceof Get) {
            getSender().tell(count, getSelf());
        } else {
            unhandled(message);
        }
    }
}
