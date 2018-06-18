package com.epam.callcenter.entity;

import com.epam.callcenter.control.ClientManager;
import org.apache.log4j.Logger;

import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Client implements Runnable {
    public static final Logger LOGGER = Logger.getLogger("com.epam.callcenter.control");
    private String name;
    private ReentrantLock lock;
    private Operator operator;
    private boolean isWaiting;

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

    public boolean isWaiting() {
        return isWaiting;
    }

    public void setWaiting(boolean waiting) {
        isWaiting = waiting;
    }


    @Override
    public void run() {
        LOGGER.setResourceBundle(ResourceBundle.getBundle("log4j"));
        Random random = new Random();
        boolean isConnected = ClientManager.call(this);
        int time = 0;
        if (isConnected) {
            try {
                time = random.nextInt(400) + 100;
                Thread.sleep(time);
            } catch (Exception e) {
                LOGGER.error("I can't sleep :("+e);
            }
            ClientManager.endCall(this);
        } else {
            try {
                time = random.nextInt(400) + 100;
                Thread.sleep(time);
            } catch (Exception e) {
                LOGGER.error("I can't sleep :("+e);
            }
            if (this.isWaiting()) {
                LOGGER.info(this.getName() + " is waiting in queue for " + time + " mills");
                ClientManager.disconnect(this);
            }
        }

    }
}
