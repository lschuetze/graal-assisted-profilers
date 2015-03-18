package ch.usi.dag.merlin;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

import ch.usi.dag.disl.staticcontext.AbstractStaticContext;

public class MerlinContext extends AbstractStaticContext {

    public String getClassName() {
        AbstractInsnNode instruction = staticContextData.getRegionStart();

        if (instruction.getOpcode() == Opcodes.NEW) {
            return ((TypeInsnNode) instruction).desc;
        } else {
            throw new IllegalArgumentException("support only new bytecode");
        }
    }

    public String getFieldName() {
        AbstractInsnNode instruction = staticContextData.getRegionStart();

        if (instruction instanceof FieldInsnNode) {
            return ((FieldInsnNode) instruction).name;
        } else {
            throw new IllegalArgumentException("support only FieldInsnNode");
        }
    }

    public String getFieldDesc() {
        AbstractInsnNode instruction = staticContextData.getRegionStart();

        if (instruction instanceof FieldInsnNode) {
            return ((FieldInsnNode) instruction).desc;
        } else {
            throw new IllegalArgumentException("support only FieldInsnNode");
        }
    }

}
