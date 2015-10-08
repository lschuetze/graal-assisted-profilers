package ch.usi.dag.profiler.staticcontext;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.util.Printer;

import ch.usi.dag.disl.staticcontext.BytecodeStaticContext;

public class EnhancedBytecodeStaticContext extends BytecodeStaticContext {

	public String typeName() {
		AbstractInsnNode instruction = staticContextData.getRegionStart();

		if (instruction instanceof TypeInsnNode) {
			return ((TypeInsnNode) instruction).desc;
		} else {
			throw new IllegalArgumentException("support only TypeInsnNode");
		}
	}

	public String invokeTarget() {
		AbstractInsnNode instruction = staticContextData.getRegionStart();

		if (instruction instanceof MethodInsnNode) {
			StringBuilder builder = new StringBuilder();
			MethodInsnNode min = (MethodInsnNode) instruction;
			builder.append(Printer.OPCODES[instruction.getOpcode()]);
			builder.append(' ');
			builder.append(min.owner.replace('/', '.'));
			builder.append('.');
			builder.append(min.name);
			builder.append(min.desc);
			return builder.toString();
		} else {
			throw new IllegalArgumentException("support only MethodInsnNode");
		}
	}

	public String bciGraalGeneral() {
		ClassNode classNode = staticContextData.getClassNode();
		MethodNode methodNode = staticContextData.getMethodNode();
		AbstractInsnNode instruction = staticContextData.getRegionStart();

		StringBuilder builder = new StringBuilder();
		builder.append(classNode.name.replace('/', '.'));
		builder.append('.');
		builder.append(methodNode.name);
		builder.append('@');
		builder.append(instruction.getOffset());
		builder.append(':');
		builder.append(' ');
		builder.append(methodNode.desc);

		return builder.toString();
	}

	public String bciGraal() {
		AbstractInsnNode instruction = staticContextData.getRegionStart();
		if (instruction instanceof TypeInsnNode) {
			return bciGraalGeneral() + " " + typeName();
		} else if (instruction instanceof MethodInsnNode) {
			return bciGraalGeneral() + " " + invokeTarget();
		}
		return bciGraalGeneral();
	}

}
