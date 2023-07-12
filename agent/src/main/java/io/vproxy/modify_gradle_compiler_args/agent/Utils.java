package io.vproxy.modify_gradle_compiler_args.agent;

public class Utils {
    private Utils() {
    }

    public static void log(String msg) {
        System.out.println(msg);
    }

    public static void log(Throwable t) {
        t.printStackTrace(System.out);
    }
}
