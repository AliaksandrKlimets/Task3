package com.epam.callcenter.entity;

import com.epam.callcenter.control.ClientManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is a singleton.
 * CallCenter has list of operators, which work for our CallCenter, and list of clients, which are in a queue.
 */

public class CallCenter {
    public static final Logger LOGGER = Logger.getLogger("com.epam.callcenter.entity");
    private List<Operator> operators;
    private List<Client> clientQueue;
    private ClientManager manager;
    private static volatile CallCenter instance;

    private CallCenter() {
        operators = new ArrayList<>();
        clientQueue = new LinkedList<>();
        manager = new ClientManager();
    }

    /**
     * This method guarantees that this class has only one exemplar.
     *
     * @return singleton object of CallCenter
     */

    public static CallCenter getInstance() {
        if (instance == null) {
            synchronized (CallCenter.class) {
                if (instance == null) {
                    instance = new CallCenter();
                }
            }
        }
        return instance;
    }

    public List<Operator> getOperators() {
        return operators;
    }

    public void setOperators(List<Operator> operators) {
        this.operators = operators;
    }

    /**
     * @return free operator or null if there are no free operators
     */

    public boolean getOperator(Client client) {
        boolean result = false;
        for (Operator op : operators) {
            if (op.isAvailable()) {
                client.setOperator(op);
                op.setAvailable(false);
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * This method returns operator into free status
     *
     * @param operator it is a operator who come back from client
     */

    public void returnOperator(Operator operator) {
        if (clientQueue.size() != 0) {
            LOGGER.info(clientQueue.get(0).getName() + " leave queue and talking to " + operator.getName());
            manager.connectToOperator(acceptClientFromQueue(), operator);
        } else {
            operator.setAvailable(true);
        }
    }


    public void addClientToQueue(Client client) {
        clientQueue.add(client);
        client.setWaiting(true);
    }

    /**
     * This method delete client from queue and return him
     *
     * @return client from queue
     */

    private Client acceptClientFromQueue() {
        Client result = clientQueue.get(0);
        result.setWaiting(false);
        clientQueue.remove(clientQueue.get(0));
        return result;
    }

    public void deleteClientFromQueue(Client client) {
        client.setWaiting(false);
        clientQueue.remove(client);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Our operators: ");
        for (Operator operator : operators) {
            result.append(operator.getName());
            result.append("; ");
        }
        return result.toString();
    }
}
