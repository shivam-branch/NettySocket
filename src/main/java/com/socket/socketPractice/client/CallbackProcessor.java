package com.socket.socketPractice.client;

@FunctionalInterface
public interface CallbackProcessor {
    public void process(Object obj);
}
