package ch.usi.dag.profiler.receiver;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.Printer;

import ch.usi.dag.disl.staticcontext.BytecodeStaticContext;

public class MethodInsnContext extends BytecodeStaticContext {

	public String invokeTarget() {
		StringBuilder builder = new StringBuilder();
		AbstractInsnNode instruction = staticContextData.getRegionStart();

		if (instruction instanceof MethodInsnNode) {
			MethodInsnNode min = (MethodInsnNode) instruction;
			builder.append(Printer.OPCODES[instruction.getOpcode()]);
			builder.append(' ');
			builder.append(min.owner.replace('/', '.'));
			builder.append('.');
			builder.append(min.name);
			builder.append(min.desc);
		}

		return builder.toString();
	}

	public String bci() {
		StringBuilder builder = new StringBuilder();

		ClassNode classNode = staticContextData.getClassNode();
		MethodNode methodNode = staticContextData.getMethodNode();
		AbstractInsnNode instruction = staticContextData.getRegionStart();

		builder.append(classNode.name.replace('/', '.'));
		builder.append('.');
		builder.append(methodNode.name);
		builder.append('@');
		builder.append(instruction.getOffset());
		builder.append(':');
		builder.append(' ');
		builder.append(methodNode.desc);

		if (instruction instanceof MethodInsnNode) {
			builder.append(' ');
			builder.append(invokeTarget());
		}

		return builder.toString();
	}

}
