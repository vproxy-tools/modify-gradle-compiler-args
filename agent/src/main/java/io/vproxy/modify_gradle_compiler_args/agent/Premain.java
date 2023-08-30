package io.vproxy.modify_gradle_compiler_args.agent;

import java.lang.instrument.Instrumentation;

public class Premain {
    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new NormalizingJavaCompilerTransformer(), false);
        inst.addTransformer(new PipedOutputStreamTransformer(), false);
    }
}
