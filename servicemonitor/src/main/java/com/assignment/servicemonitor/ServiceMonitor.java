/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.assignment.servicemonitor;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//import java.util.logging.Logger;
//import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author Badrika
 * ServiceMonitor manages Monitor(s) (starts them and stop them accordingly)
 */
public class ServiceMonitor extends Thread {

    public static final int THREAD_INTERVAL = 1000;
//    public static final String PROPERTY_LOG4J = "log4j.properties";
    protected boolean isThreadInRunning = true;
    protected ServiceConfigInit configInit = ServiceConfigInit.getInstance();

//    protected Logger logger;
    protected ExecutorService threadPool = Executors.newCachedThreadPool();


    public ServiceMonitor() {
        LogFileCreator.writeToInfoLog("Service Monitor started at " + String.valueOf(System.currentTimeMillis()));
    }

    protected boolean isTimeToRun(ServiceConfigBean configuration) {
        long timestamp = System.currentTimeMillis();
        return configuration.isUpAndRunning()
                && !configuration.isInServiceOutage(timestamp)
                && (timestamp >= configuration.getLastTimestamp() + configuration.getPollingFrequency() * 1000);
    }

    @Override
    public void run() {
        try {
            while (isThreadInRunning) {
                try {
                    for (String serviceName : configInit.getServices()) {
                        ServiceConfigBean configBean = configInit.getConfiguration(serviceName);
                        if (!isTimeToRun(configBean)) {
                            continue;
                        }

                        threadPool.submit(new Monitor(serviceName));
                    }

                    Thread.sleep(THREAD_INTERVAL);
                } catch (IOException | InterruptedException e) {
                    LogFileCreator.writeToErrorLog(e.toString());
                }
            }
        } catch (Exception e) {
            LogFileCreator.writeToErrorLog(e.toString());
        }
        LogFileCreator.writeToInfoLog("Service monitor stopped at " + String.valueOf(System.currentTimeMillis()));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        final String SERVICE_ONE = "SERVICE_ONE";
        final String SERVICE_TWO = "SERVICE_TWO";
        final String SERVICE_THREE = "SERVICE_THREE";
//        final String LOCAL_HOST = "127.0.0.1";
        final String LOCAL_HOST = "www.google.com";

        ServiceMonitor server = new ServiceMonitor();
        server.configInit.addConfiguration(new ServiceConfigBean(SERVICE_ONE, LOCAL_HOST, 9991, true, 1, 2));
        server.configInit.addConfiguration(new ServiceConfigBean(SERVICE_TWO, LOCAL_HOST, 9992, true, 1, 3));
        server.configInit.addConfiguration(new ServiceConfigBean(SERVICE_THREE, LOCAL_HOST, 9993, true, 1, 4));

        server.start();

    }

}
