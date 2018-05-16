/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.assignment.servicemonitor;

import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Badrika
 */
public class Caller implements Observer, ServiceListner{
    
    Observable observable;
    private String name;
    private long lastTimestamp;
    private ServiceMonitor serviceMonitor;

    public Caller(Observable observable) {
        this.observable = observable;
        observable.addObserver(this);
    }
    
    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof ConnectionInfo){
        ConnectionInfo info = (ConnectionInfo)o;
        this.name = info.getName();
        this.lastTimestamp = info.getLastTimestamp();
             serviceUp(name, lastTimestamp);
             serviceDown(name, lastTimestamp);                    
        }
    }


    @Override
    public void serviceUp(String name, long timestamp) {
        ServiceConfigBean configBean = serviceMonitor.configInit.getConfiguration(name);

            LogFileCreator.writeToInfoLog(configBean.getName() + " service @ " 
                    + configBean.getHost() + ":" + configBean.getPort() + " is UP at " + String.valueOf(timestamp));
        
    }

    @Override
    public void serviceDown(String name, long timestamp) {
        ServiceConfigBean configBean = serviceMonitor.configInit.getConfiguration(name);
            LogFileCreator.writeToInfoLog(configBean.getName() + " service @ " 
                    + configBean.getHost() + ":" + configBean.getPort() + " is DOWN at " + String.valueOf(timestamp));
        
    }

    
}
