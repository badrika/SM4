/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.assignment.servicemonitor;

/**
 *
 * @author Badrika
 * Interface of main monitoring events: server up and server down
 */
public interface ServiceListner {
    void serviceUp(String name, long timestamp); //Service URL ,query interval
    void serviceDown(String name, long timestamp); 
    
}
