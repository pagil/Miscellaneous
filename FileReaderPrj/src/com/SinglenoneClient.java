package com;

public class SinglenoneClient {

    public static void main(String[] args) {
        SingletoneEnum.INSTANCE.sayHello();
        System.out.println(SingletoneEnum.INSTANCE.getField());
    }

}
