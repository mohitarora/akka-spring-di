package com.sample;

import akka.actor.ActorRef;
import akka.util.Timeout;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.ask;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfiguration.class)
public class TestDI {

    /**
     * Note Actor Ref is directly injected.
     */
    @Autowired
    @Qualifier(AppConfiguration.MASTER_ACTOR_REF)
    private ActorRef masterActor;

    @Test
    public void testDependencyInjection() throws Exception {
        // tell it to count three times
        masterActor.tell(new CountingActor.Count(), null);
        masterActor.tell(new CountingActor.Count(), null);
        masterActor.tell(new CountingActor.Count(), null);

        // check that it has counted correctly
        FiniteDuration duration = FiniteDuration.create(3, TimeUnit.SECONDS);
        Future<Object> result = ask(masterActor, new CountingActor.Get(),
                Timeout.durationToTimeout(duration));
        assertEquals(3, Await.result(result, duration));

    }

}
