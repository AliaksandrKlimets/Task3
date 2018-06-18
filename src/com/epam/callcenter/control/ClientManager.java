package com.epam.callcenter.control;

import com.epam.callcenter.entity.CallCenter;
import com.epam.callcenter.entity.Client;
import com.epam.callcenter.entity.Operator;
import org.apache.log4j.Logger;

import java.util.Random;
import java.util.ResourceBundle;

/**
 * This class describes client logic
 */

public class ClientManager {
    public static final Logger LOGGER = Logger.getLogger("com.epam.callcenter.control");


    /**
     * This method returns true when client connects to operator or false, if each operator is not free
     * @param client
     * @return true or false
     */
    public static boolean call(Client client) {
        LOGGER.setResourceBundle(ResourceBundle.getBundle("log4j"));
        LOGGER.debug("Client try to connect to operator");
        boolean isConnected;
        client.getLock().lock();
        Operator operator = CallCenter.getInstance().getOperator();

        if (operator == null) {
            CallCenter.getInstance().addClientToQueue(client);
            isConnected = false;
            LOGGER.info(client.getName() + " in queue");
        } else {
            client.setOperator(operator);
            isConnected = true;
            LOGGER.info(client.getName() + " connected to operator " + operator.getName());
        }
        client.getLock().unlock();
        return isConnected;
    }

    /**
     * This method connects client from queue to operator.
     * @param client
     * @param operator
     */

    public static void connectToOperator(Client client, Operator operator) {
        LOGGER.setResourceBundle(ResourceBundle.getBundle("log4j"));
        LOGGER.debug("Client connects to operator after waiting");
        Random random = new Random();
        client.setOperator(operator);
        try {
            Thread.sleep(random.nextInt(400) + 100);
        } catch (Exception e) {
        }
        endCall(client);
    }

    /**
     * This method ends waiting in the queue
     * @param client
     */

    public static void disconnect(Client client) {
        LOGGER.setResourceBundle(ResourceBundle.getBundle("log4j"));
        LOGGER.debug("Client disconnect from queue");
        LOGGER.info(client.getName() + " leave queue and end call");
        CallCenter.getInstance().deleteClientFromQueue(client);
    }


    /**
     * This method ends call and makes operator free
     * @param client
     */

    public static void endCall(Client client) {
        LOGGER.setResourceBundle(ResourceBundle.getBundle("log4j"));
        LOGGER.debug("Client ends call after talking");
        client.getLock().lock();
        Operator operator = client.getOperator();
        client.setOperator(null);
        LOGGER.info(client.getName() + " end call");
        CallCenter.getInstance().returnOperator(operator);
        client.getLock().unlock();
    }

}
