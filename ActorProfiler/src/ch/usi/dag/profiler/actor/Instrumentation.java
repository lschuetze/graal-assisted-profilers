package ch.usi.dag.profiler.actor;

import akka.actor.ActorCell;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BodyMarker;

public class Instrumentation {

	@Before(marker = BodyMarker.class, scope = "akka.dispatch.Mailbox.enqueue")
	public static void enqueue(DynamicContext dc) {
		Profiler.enqueue(dc.getMethodArgumentValue(0, Object.class));
	}

	@Before(marker = BodyMarker.class, scope = "akka.actor.ActorCell.receiveMessage")
	public static void dequeue(DynamicContext dc) {
		Profiler.dequeue(((ActorCell) dc.getThis()).self());
	}

}
