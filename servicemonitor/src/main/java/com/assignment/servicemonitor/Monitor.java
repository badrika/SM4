/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.assignment.servicemonitor;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Badrika
 * Connects to service and updates timestamp of the last successful attempt. 
 * In case if waiting threshold is exceeded - an event of ServerUp or ServerDown is fired.
 */
public class Monitor implements Runnable{

    protected ServiceConfigBean configBean;

    private void connected() {

        LogFileCreator.writeToInfoLog("Connected to " + configBean.getName() + " @ " +configBean.getHost());

        long timestamp = System.currentTimeMillis();
        configBean.setLastTimestamp(timestamp);
        boolean stateChanged = configBean.getStatus().checkDownToUp(timestamp, configBean.getGraceTime());
        if (stateChanged) {
            configBean.serviceUpEvent(timestamp);
        }
    }

    private void notConnected() {
        LogFileCreator.writeToInfoLog("Not connected to " + configBean.getName() + " @ " +configBean.getHost());

        long timestamp = System.currentTimeMillis();
        configBean.setLastTimestamp(timestamp);
        boolean stateChanged = configBean.getStatus().checkUpToDown(timestamp, configBean.getGraceTime());
        if (stateChanged) {
            configBean.serviceDownEvent(timestamp);
        }
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
            socket = new Socket(configBean.getHost(), configBean.getPort());
            if (socket.isConnected()) {
                connected();
            } else {
                notConnected();
            }
        } catch (Exception e) {
            notConnected();
            LogFileCreator.writeToErrorLog(e.toString());
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                LogFileCreator.writeToErrorLog(e.toString());
            }
        }
    }

    public Monitor(String name) throws IOException {
        configBean = ServiceConfigInit.getInstance().getConfiguration(name);
    }

}
