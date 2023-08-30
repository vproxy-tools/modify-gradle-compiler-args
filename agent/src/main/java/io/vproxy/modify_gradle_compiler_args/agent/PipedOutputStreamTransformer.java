package io.vproxy.modify_gradle_compiler_args.agent;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.util.ArrayList;

public class PipedOutputStreamTransformer extends AbstractTransformer {
    @Override
    protected boolean transform(ClassNode node) {
        String classname = node.name;
        if (!classname.equals("java/io/PipedOutputStream")) {
            return false;
        }
        boolean modified = false;
        for (MethodNode meth : node.methods) {
            if (meth.name.equals("write")
                && ((meth.access & Opcodes.ACC_STATIC) == 0)
                && ((meth.access & Opcodes.ACC_PUBLIC) == Opcodes.ACC_PUBLIC)) {
                modify(classname, meth);
                modified = true;
            }
        }
        return modified;
    }

    private void modify(String owner, MethodNode meth) {
        var returns = new ArrayList<AbstractInsnNode>();
        for (int i = 0, size = meth.instructions.size(); i < size; ++i) {
            var insn = meth.instructions.get(i);
            if (insn.getOpcode() == Opcodes.RETURN) {
                returns.add(insn);
            }
        }
        for (var r : returns) {
            var nodes = new InsnList();
            nodes.add(new VarInsnNode(Opcodes.ALOAD, 0));
            nodes.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, owner, "flush", "()V", false));
            meth.instructions.insertBefore(r, nodes);
        }
    }
}
