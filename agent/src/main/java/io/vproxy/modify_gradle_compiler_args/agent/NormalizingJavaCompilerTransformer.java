package io.vproxy.modify_gradle_compiler_args.agent;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

public class NormalizingJavaCompilerTransformer extends AbstractTransformer {
    @Override
    protected boolean transform(ClassNode node) {
        String classname = node.name;
        if (!classname.equals("org/gradle/api/internal/tasks/compile/NormalizingJavaCompiler")) {
            return false;
        }
        boolean modified = false;
        for (MethodNode meth : node.methods) {
            if (meth.name.equals("delegateAndHandleErrors") && meth.desc.equals("(Lorg/gradle/api/internal/tasks/compile/JavaCompileSpec;)Lorg/gradle/api/tasks/WorkResult;")) {
                InsnList insns = new InsnList();

                insns.add(new VarInsnNode(Opcodes.ALOAD, 1)); // aload 1: JavaCompileSpec
                // if (spec instanceof DefaultJvmLanguageCompileSpec)
                insns.add(new TypeInsnNode(Opcodes.INSTANCEOF, DefaultJvmLanguageCompileSpec()));
                insns.add(new LdcInsnNode(0));
                LabelNode if1 = new LabelNode();
                insns.add(new JumpInsnNode(Opcodes.IF_ICMPEQ, if1)); // {

                getRelease(insns);
                // if (spec.getRelease() != null)
                LabelNode if2 = new LabelNode();
                insns.add(new JumpInsnNode(Opcodes.IFNULL, if2)); // {

                loadDefaultJvmLanguageCompileSpec(insns); // spec
                getReleaseThenToString(insns); // spec.getRelease().toString()
                // spec.setSourceCompatibility(spec.getRelease().toString())
                insns.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, DefaultJvmLanguageCompileSpec(), "setSourceCompatibility", "(Ljava/lang/String;)V", false));

                loadDefaultJvmLanguageCompileSpec(insns); // spec
                getReleaseThenToString(insns); // spec.getRelease().toString()
                // spec.setTargetCompatibility(spec.getRelease().toString())
                insns.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, DefaultJvmLanguageCompileSpec(), "setTargetCompatibility", "(Ljava/lang/String;)V", false));

                insns.add(if2); // }

                loadDefaultJvmLanguageCompileSpec(insns);
                insns.add(new InsnNode(Opcodes.ACONST_NULL)); // null
                // spec.setRelease(null)
                insns.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, DefaultJvmLanguageCompileSpec(), "setRelease", "(Ljava/lang/Integer;)V", false));

                insns.add(new VarInsnNode(Opcodes.ALOAD, 0)); // this
                insns.add(new VarInsnNode(Opcodes.ALOAD, 1)); // aload 1: JavaCompileSpec
                // this.logCompilerArguments(spec)
                insns.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "org/gradle/api/internal/tasks/compile/NormalizingJavaCompiler", "logCompilerArguments", "(Lorg/gradle/api/internal/tasks/compile/JavaCompileSpec;)V", false));

                insns.add(if1); // }

                modified = true;
                meth.instructions.insert(insns);
            }
        }
        return modified;
    }

    private String DefaultJvmLanguageCompileSpec() {
        return "org/gradle/api/internal/tasks/compile/DefaultJvmLanguageCompileSpec";
    }

    private void loadDefaultJvmLanguageCompileSpec(InsnList insns) {
        insns.add(new VarInsnNode(Opcodes.ALOAD, 1)); // aload 1: JavaCompileSpec
        insns.add(new TypeInsnNode(Opcodes.CHECKCAST, DefaultJvmLanguageCompileSpec())); // (DefaultJvmLanguageCompileSpec) spec
    }

    private void getRelease(InsnList insns) {
        loadDefaultJvmLanguageCompileSpec(insns);
        // spec.getRelease()
        insns.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, DefaultJvmLanguageCompileSpec(), "getRelease", "()Ljava/lang/Integer;", false));
    }

    private void getReleaseThenToString(InsnList insns) {
        getRelease(insns);
        // spec.getRelease().toString()
        insns.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;", false));
    }
}
