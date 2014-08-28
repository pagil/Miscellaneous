/*
 * This is implementation of the example published here:
 * http://www.javamex.com/tutorials/memory/instrumentation.shtml
 * Note: this project is independent and decoupled however, EmptyClassSizePrj
 * uses the jar created from this project to calculate size of empty class.
 */
package mem.test;

import java.lang.instrument.Instrumentation;

/**
 *
 * @author viktor
 */
public class MyAgent {

    private static volatile Instrumentation globalInstr;

    public static void premain(String args, Instrumentation instr) {
        globalInstr = instr;
    }

    public static long getObjectSize(Object obj) {
        if (globalInstr == null) {
            throw new IllegalStateException("Instrumentation object is missing!");
        }
        return globalInstr.getObjectSize(obj);
    }
}
