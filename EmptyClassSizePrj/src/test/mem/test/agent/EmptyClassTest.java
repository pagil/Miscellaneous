/*
 * This class depends on the agent.jar which is created
 * from project InstrumentationPrj.
 * I have not added dependency on the project, because it is easy to build the
 * agent.jar and I wanted to decouple the projects to allow changes in the
 * InstrimentationPrj project.
 * See: http://www.javamex.com/tutorials/memory/instrumentation.shtml
 */
package test.mem.test.agent;

import mem.test.MyAgent;

/**
 *
 * @author viktor
 */
public class EmptyClassTest {

    public static void main(String[] args) {
        EmptyClass instance;
        /*long afterCreation, beforeCreation = Runtime.getRuntime().freeMemory();
         System.out.println("Free memory before creation: " + beforeCreation);*/
        instance = new EmptyClass();
        /*afterCreation = Runtime.getRuntime().freeMemory();
         System.out.println("Free memory after creation of" + instance + ": " + afterCreation);
         System.out.println("Total memory for instance of EmptyClass: " + (beforeCreation - afterCreation));*/
        System.out.println("MyAgent.getObjectSize(instance): " + MyAgent.getObjectSize(instance));
        System.out.println("MyAgent.getObjectSize(EmptyClass.class): " + MyAgent.getObjectSize(EmptyClass.class));
    }

}

class EmptyClass {
}
