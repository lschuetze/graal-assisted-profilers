package ch.usi.dag.profiler.lock;

import ch.usi.dag.disl.annotation.GuardMethod;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;

public abstract class MethodIsSynchronizedGuard {

    @GuardMethod
    public static boolean isApplicable(MethodStaticContext msc) {
        return msc.isMethodSynchronized();
    }

}
