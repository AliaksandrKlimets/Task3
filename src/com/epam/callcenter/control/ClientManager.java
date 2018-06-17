package com.epam.callcenter.control;

import com.epam.callcenter.entity.CallCenter;
import com.epam.callcenter.entity.Client;
import com.epam.callcenter.entity.Operator;

public class ClientManager {
    public static boolean call(Client client) {
        boolean isConnected;
        client.getLock().lock();
        try {
            Operator operator = CallCenter.getInstance().getOperator();

            if (operator == null) {
                CallCenter.getInstance().addClientToQueue(client);
                isConnected = false;
            } else {
                client.setOperator(operator);
                isConnected = true;
            }
        } finally {
            client.getLock().unlock();
        }
        return isConnected;
    }

    public static void connectToOperator(Client client) {

    }

    public void disconnectFromOperator(Client client) {

    }

    public static void endCall(Client client) {
        Operator operator = client.getOperator();
        client.setOperator(null);
        CallCenter.getInstance().returnOperator(operator);
    }

}
