/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.assignment.servicemonitor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author Badrika
 */
public class LogFileCreator {

    public static void writeToInfoLog(String msg) {

        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();

        String line = "\n_______________________________________________________________________________________ " + localDate + " > " + localTime + "\n";
        String filename = "SERVICE_MONITOR_LOG";
        BufferedWriter bw = null;
        String path = "";

        path = System.getProperty("user.dir") + "\\LOGS\\Info";
        System.out.println("path > > " +path);

        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        filename = path + "/" + filename + ".txt";

        msg = line + String.valueOf(System.currentTimeMillis()) + "\n" + msg;

        try {
            bw = new BufferedWriter(new FileWriter(filename, true));
            bw.write(msg);
            bw.newLine();
            bw.flush();
        } catch (Exception ioe) {
            System.out.println("Error: writing to log file");
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static void writeToErrorLog(String msg) {
        
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();

        String line = "\n_______________________________________________________________________________________ " + localDate + " > " + localTime + "\n";
        
        String filename = "SERVICE_MONITOR_LOG";
        BufferedWriter bw = null;
        String path = "";

        path = System.getProperty("user.dir") + "\\LOGS\\Error";
        
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        filename = path + "/" + filename + ".txt";

        msg = line + String.valueOf(System.currentTimeMillis()) + "\n" + msg;

        try {
            bw = new BufferedWriter(new FileWriter(filename, true));
            bw.write(msg);
            bw.newLine();
            bw.flush();
        } catch (IOException ioe) {
            System.out.println("Error: writing to log file");
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                }
            }
        }
    }

}
