package com.epam.callcenter.control;

import com.epam.callcenter.entity.CallCenter;
import com.epam.callcenter.entity.Client;
import com.epam.callcenter.entity.Operator;
import org.apache.log4j.Logger;

import java.util.Random;

/**
 * This class describes client logic
 */

public class ClientManager {
    public static final Logger LOGGER = Logger.getLogger("com.epam.callcenter.control");

    public ClientManager() {
    }

    /**
     * This method returns true when client connects to operator or false, if each operator is not free
     *
     * @param client
     * @return true or false
     */


    public boolean call(Client client) {
        LOGGER.debug("Client try to connect to operator");
        boolean isConnected;
        client.getLock().lock();
        boolean isCalled = CallCenter.getInstance().getOperator(client);

        if (isCalled == false) {
            CallCenter.getInstance().addClientToQueue(client);
            isConnected = false;
            LOGGER.info(client.getName() + " in queue");
        } else {
            isConnected = true;
            LOGGER.info(client.getName() + " connected to operator " + client.getOperator().getName());
        }
        client.getLock().unlock();
        return isConnected;
    }

    /**
     * This method connects client from queue to operator.
     *
     * @param client
     * @param operator
     */

    public void connectToOperator(Client client, Operator operator) {
        LOGGER.debug("Client connects to operator after waiting");
        Random random = new Random();
        client.setOperator(operator);
        try {
            Thread.sleep(random.nextInt(400) + 100);
        } catch (InterruptedException e) {
            LOGGER.error("I can't sleep :(", e);
        }
        endCall(client);
    }

    /**
     * This method ends waiting in the queue
     *
     * @param client
     */

    public void disconnect(Client client) {
        LOGGER.debug("Client disconnect from queue");
        LOGGER.info(client.getName() + " leave queue and end call");
        CallCenter.getInstance().deleteClientFromQueue(client);
    }


    /**
     * This method ends call and makes operator free
     *
     * @param client
     */

    public void endCall(Client client) {
        LOGGER.debug("Client ends call after talking");
        client.getLock().lock();
        Operator operator = client.getOperator();
        client.setOperator(null);
        LOGGER.info(client.getName() + " end call with operator " + operator.getName());
        CallCenter.getInstance().returnOperator(operator);
        client.getLock().unlock();
    }

}
