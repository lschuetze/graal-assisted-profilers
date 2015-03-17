package ch.usi.dag.profiler.inlining;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.Printer;

import ch.usi.dag.disl.staticcontext.BytecodeStaticContext;

public class MethodInsnContext extends BytecodeStaticContext {

	public static ConcurrentHashMap<String, Integer> cached_indexes = new ConcurrentHashMap<>();
	public static AtomicInteger index = new AtomicInteger(0);

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
			MethodInsnNode min = (MethodInsnNode) instruction;
			builder.append(' ');
			builder.append(Printer.OPCODES[instruction.getOpcode()]);
			builder.append(' ');
			builder.append(min.owner.replace('/', '.'));
			builder.append('.');
			builder.append(min.name);
			builder.append(min.desc);
		}

		builder.append(' ');
		return builder.toString();
	}

	public int index() {
		return cached_indexes.computeIfAbsent(bci(),
				key -> index.getAndIncrement());
	}

}
