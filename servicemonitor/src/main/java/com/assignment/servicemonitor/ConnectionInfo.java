/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.assignment.servicemonitor;

import java.util.Observable;

/**
 *
 * @author Badrika
 * This class is not used for the moment
 */
public class ConnectionInfo extends Observable {

//    private long serverUpTimestamp;
//    private long serverDownTimestamp;
    private String name;
    private long lastTimestamp;
    

    public ConnectionInfo() {
    }

    public void stateChanged() {
        setChanged();
        notifyObservers();

    }

    public void setDetails(String name, long lastTimestamp) {
        this.name = name;
        this.lastTimestamp = lastTimestamp;
        stateChanged();

    }

//    public long getServerUpTimestamp() {
//        return serverUpTimestamp;
//    }
//
//    public long getServerDownTimestamp() {
//        return serverDownTimestamp;
//    }

    public long getLastTimestamp() {
        return lastTimestamp;
    }

    public String getName() {
        return name;
    }
    
    

}
