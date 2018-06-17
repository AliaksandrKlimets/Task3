package com.epam.callcenter.entity;

import com.epam.callcenter.control.ClientManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CallCenter {

    private List<Operator> operators;
    private List<Client> clientQueue;
    private static volatile CallCenter instance;

    private CallCenter() {
        operators = new ArrayList<>();
        clientQueue = new LinkedList<Client>();
    }

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

    public Operator getOperator() {
        Operator operator = null;
        for (Operator op : operators) {
            if (op.isAvailable()) {
                operator = op;
                op.setAvailable(false);
                break;
            }
        }
        return operator;
    }

    public void returnOperator(Operator operator) {
        for (Operator op : operators) {
            if (op.equals(operator)) {
                if (clientQueue.size() != 0) {
                    ClientManager.connectToOperator(clientQueue.get(0));
                    clientQueue.remove(clientQueue.get(0));
                } else {
                    operator.setAvailable(true);
                }
                break;
            }
        }
    }


    public void addClientToQueue(Client client) {
        clientQueue.add(client);
    }

    public Client acceptClientFromQueue() {
        Client result = clientQueue.get(0);
        clientQueue.remove(clientQueue.get(0));
        return result;
    }

    public void deleteClientFromQueue(Client client) {
        clientQueue.remove(client);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Our operators: ");
        for (Operator operator : operators) {
            result.append(operator.getName() + "; ");
        }
        return result.toString();
    }
}
