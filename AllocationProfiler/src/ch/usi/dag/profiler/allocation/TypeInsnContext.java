package ch.usi.dag.profiler.allocation;

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

	public String bci() {
		StringBuilder builder = new StringBuilder(super.bci());
		AbstractInsnNode instruction = staticContextData.getRegionStart();

		if (instruction instanceof TypeInsnNode) {
			TypeInsnNode tin = (TypeInsnNode) instruction;
			builder.append(' ');
			builder.append(tin.desc);
		}

		return builder.toString();
	}

}
