/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.assignment.servicemonitor;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Badrika
 */
public class ServiceMonitorTest {
    
    public static final String SERVICE_ONE = "SERVICE_ONE";
    public static final String SERVICE_TWO = "SERVICE_TWO";
    public static final String SERVICE_THREE = "SERVICE_THREE";

    private ServiceMonitor serviceMonitor;
    
    ServiceListner listner = new ServiceListner(){
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
    };
    
    public ServiceMonitorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Test
    public void testProperConnections() throws Exception {
        LogFileCreator.writeToInfoLog("Testing Proper Connections");
        ServiceConfigBean configBean = new ServiceConfigBean(SERVICE_ONE, "www.google.com", 80, true, 1, 3);
        serviceMonitor = new ServiceMonitor();
        serviceMonitor.configInit.addConfiguration(configBean);

        // register listener
        for (String name : serviceMonitor.configInit.getServices()) {
            ServiceConfigBean scb = serviceMonitor.configInit.getConfiguration(name);
            scb.addListener(listner);
        }
        serviceMonitor.start();
        Thread.sleep(10*1000);
    }
    
    @Test
    public void testWrongConnections() throws Exception {
        
        LogFileCreator.writeToInfoLog("Testing Wrong Connections");
        serviceMonitor = new ServiceMonitor();
        ServiceConfigBean configBean = new ServiceConfigBean(SERVICE_TWO, "www.wrong.connection", 80, true, 1, 3);
        serviceMonitor.configInit.addConfiguration(configBean);
        
        configBean = new ServiceConfigBean(SERVICE_THREE, "www.wrongsecond.connection", 80, true, 1, 3);
        serviceMonitor.configInit.addConfiguration(configBean);

        // register listener
        for (String name : serviceMonitor.configInit.getServices()) {
            ServiceConfigBean scb = serviceMonitor.configInit.getConfiguration(name);
            scb.addListener(listner);
        }
        serviceMonitor.start();
        Thread.sleep(20*1000);
    }

    @Test
    public void testGraceTime() throws Exception {
        LogFileCreator.writeToInfoLog("Testing grace time");
        
        ServiceConfigBean configBean = new ServiceConfigBean(SERVICE_ONE, "www.google.com", 80, true, 1, 3);
        configBean.setPollingFrequency(10);
        configBean.setGraceTime(5);
        assertEquals(5, configBean.getPollingFrequency());
    }
    
    @Test
    public void testCorrectServiceOutage() throws Exception {
        LogFileCreator.writeToInfoLog("Testing correct service outage");
        
        ServiceConfigBean configBean = new ServiceConfigBean(SERVICE_ONE, "www.google.com", 80, true, 1, 3);
        
        configBean.setServiceOutageStartTime(5000);
        configBean.setServiceOutageEndTime(6000);
        assertEquals(true, configBean.isInServiceOutage(5500));     
        // assertTrue(configBean.isInServiceOutage(5500));        this also can be used. 
    }

    @Test
    public void testIncorrectServiceOutage() throws Exception {
        LogFileCreator.writeToInfoLog("Testing incorrect service outage");
        
        ServiceConfigBean configBean = new ServiceConfigBean(SERVICE_ONE, "www.google.com", 80, true, 1, 3);
       
        configBean.setServiceOutageStartTime(5000);
        configBean.setServiceOutageEndTime(6000);
        assertEquals(false, configBean.isInServiceOutage(8500));     
        // assertFalse(configBean.isInServiceOutage(8500));        this also can be used
    }

    /**
     * Test of isTimeToRun method, of class ServiceMonitor.
     */
    @Ignore
    @Test
    public void testIsTimeToRun() {
        System.out.println("isTimeToRun");
        ServiceConfigBean configuration = null;
        ServiceMonitor instance = new ServiceMonitor();
        boolean expResult = false;
        boolean result = instance.isTimeToRun(configuration);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of run method, of class ServiceMonitor.
     */
    @Ignore
    @Test
    public void testRun() {
        System.out.println("run");
        ServiceMonitor instance = new ServiceMonitor();
        instance.run();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class ServiceMonitor.
     */
    @Ignore
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        ServiceMonitor.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
