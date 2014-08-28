/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.java8.feature.lambda;

/**
 *
 * @author viktor
 */
public class LambdaServiceTest {

    interface HelloService {

        String hello(String firstname, String lastname);
    }

    public static void main(String[] args) {

        HelloService helloService = (String firstname, String lastname) -> {
            String hello = "Hello " + firstname + " " + lastname;
            return hello;
        };
        System.out.println(helloService.hello(args[0], args[1]));

    }

}
