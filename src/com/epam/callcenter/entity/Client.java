package com.epam.callcenter.entity;

import com.epam.callcenter.control.ClientManager;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Client implements Runnable {
    private String name;
    private ReentrantLock lock;
    private Operator operator;


    public Client(String name, ReentrantLock lock) {
        this.name = name;
        this.lock = lock;
    }

    public Client() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Lock getLock() {
        return lock;
    }

    public void setLock(ReentrantLock lock) {
        this.lock = lock;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    @Override
    public void run() {
        Random random = new Random();
        boolean isConnected = ClientManager.call(this);
        if (isConnected) {
            try{
                Thread.sleep(random.nextInt(400)+100);
            }catch (Exception e){}
            ClientManager.endCall(this);
        }else {

        }

    }
}
