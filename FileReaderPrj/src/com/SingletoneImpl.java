package com;

public class SingletoneImpl {
    private SingletoneImpl() {
    }

    private static class InnerInstanceHolder {
        private static SingletoneImpl instance = new SingletoneImpl();
    }

    public static SingletoneImpl getInstance() {
        return InnerInstanceHolder.instance;
    }
}
