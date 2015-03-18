package ch.usi.dag.merlin;

import org.objectweb.asm.Type;

import ch.usi.dag.disl.annotation.GuardMethod;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;

public class Guard {

    private static boolean typeIsReference(String typeDescriptor) {
        final int sort = Type.getType(typeDescriptor).getSort();
        return sort == Type.OBJECT || sort == Type.ARRAY;
    }

    public static final class PrimitiveFields {
        @GuardMethod
        public static boolean isApplicable(MerlinContext mc) {
            return !typeIsReference(mc.getFieldDesc());
        }
    }

    public static final class ReferenceFields {
        @GuardMethod
        public static boolean isApplicable(MerlinContext mc) {
            return typeIsReference(mc.getFieldDesc());
        }
    }

    public static final class InstanceMethodsExceptConstructors {
        @GuardMethod
        public static boolean isInstanceMethodButNotConstructor(final MethodStaticContext msc) {
            return !msc.isMethodStatic() && !"<init>".equals(msc.thisMethodName());
        }
    }
}
