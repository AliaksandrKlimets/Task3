package com.epam.callcenter.run;

import com.epam.callcenter.entity.CallCenter;
import com.epam.callcenter.entity.Client;
import com.epam.callcenter.entity.Operator;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class is used to launch our application
 */

public class Runner {
    public static final Logger LOGGER = Logger.getLogger("com.epam.callcenter.run");
    public static void main(String[] args) {
        LOGGER.setResourceBundle(ResourceBundle.getBundle("log4j"));
        ArrayList<Operator> operatorList = new ArrayList<>();
        ReentrantLock lock = new ReentrantLock();
        Operator operator1 = new Operator("Alex");
        Operator operator2 = new Operator("Bob");
        Operator operator3 = new Operator("Nick");

        operatorList.add(operator1);
        operatorList.add(operator2);
        operatorList.add(operator3);

        CallCenter.getInstance().setOperators(operatorList);
        LOGGER.info("CallCenter has been started");
        new Thread(new Client("Misha",lock)).start();
        new Thread(new Client("Pasha",lock)).start();
        new Thread(new Client("Dasha",lock)).start();
        new Thread(new Client("Grisha",lock)).start();
        new Thread(new Client("Alyona",lock)).start();
    }
}
