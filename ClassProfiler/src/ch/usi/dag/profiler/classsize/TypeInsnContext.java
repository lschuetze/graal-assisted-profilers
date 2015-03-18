package ch.usi.dag.profiler.classsize;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

import ch.usi.dag.disl.staticcontext.BytecodeStaticContext;

public class TypeInsnContext extends BytecodeStaticContext {

	public String getTypeName() {
		AbstractInsnNode instruction = staticContextData.getRegionStart();

		if (instruction instanceof TypeInsnNode) {
			return ((TypeInsnNode) instruction).desc;
		} else {
			throw new IllegalArgumentException("support only TypeInsnNode");
		}
	}

}
