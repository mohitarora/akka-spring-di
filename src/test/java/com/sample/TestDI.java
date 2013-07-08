package com.sample;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.util.Timeout;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;


import java.util.concurrent.TimeUnit;

import static com.sample.SpringExtension.SpringExtProvider;
import static akka.pattern.Patterns.ask;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfiguration.class)
public class TestDI {

    @Autowired
    private ActorSystem system;

    @Test
    public void testDependencyInjection() throws Exception {
        // use the Spring Extension to create props for a named actor bean
        ActorRef counter = system.actorOf(
                SpringExtProvider.get(system).props("countingActor"), "counter");
        // tell it to count three times
        counter.tell(new CountingActor.Count(), null);
        counter.tell(new CountingActor.Count(), null);
        counter.tell(new CountingActor.Count(), null);

        // check that it has counted correctly
        FiniteDuration duration = FiniteDuration.create(3, TimeUnit.SECONDS);
        Future<Object> result = ask(counter, new CountingActor.Get(),
                Timeout.durationToTimeout(duration));
        assertEquals(3, Await.result(result, duration));

    }

    @After
    public void shutDown() {
        // shut down the actor system
        system.shutdown();
        system.awaitTermination();
    }
}
