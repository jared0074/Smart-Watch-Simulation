/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rts_2ndedition_5;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jared
 */
public class Sensor implements Runnable{
    int sensorID;
    String sensorName;
    SmartWatch display;
    boolean danger = false;
    int HeartRate, BloodOxygen;
    Semaphore sem = new Semaphore(1);
    Semaphore sem1 = new Semaphore(1);
    
    
    public Sensor(SmartWatch display, int sensorNum){
        this.sensorID = sensorNum;
        sensorName = "Sensor " + sensorNum;
        this.display = display;
    }
    
    public void checkVitals(Sensor sensor) throws IOException, InterruptedException{
        while(!danger){
            if (!display.HeartRateChecker) {
                display.HeartRateChecker = true;
                checkHeartRate(sensor);
                addDetails("Heart Rate", HeartRate);
                writeDetails(sensor, "Heart Rate", HeartRate);

            } else if (!display.BloodOxygenChecker) {
                display.BloodOxygenChecker = true;
                checkBloodOxygen(sensor);
                addDetails("Blood Oxygen", BloodOxygen);
                writeDetails(sensor, "Blood Oxygen", BloodOxygen);
            }

            if (display.HeartCheckFin || display.BloodCheckFin) {
                synchronized (display.notiWait) {
                    display.notiWait.notify();
                }
            }
        }
        
        System.out.println(sensor.sensorName + " : Emergency Detected, Saving Details and Stop All Work");
        synchronized (display.notiWait) {
            display.notiWait.notify();
        }

    }
    
    public void checkHeartRate(Sensor sensor) throws InterruptedException {
        System.out.println(sensor.sensorName + " : Checking Heart Rate");
        Thread.sleep(2000);
        HeartRate = getRandomInteger(display.min, display.max);
        if (HeartRate >= 30) {
            System.out.println(sensor.sensorName + " : Normal Heart Rate Detected");
            Thread.sleep(3000);
            System.out.println(sensor.sensorName + " : Heart Rate Check Completed");
            Thread.sleep(2000);
            display.HeartCheckFin = true;
        } else {
            System.out.println(sensor.sensorName + " : Abnormal Heart Rate Detected");
            Thread.sleep(3000);
            danger = true;
            display.danger = true;
        }
    }
    
    public void checkBloodOxygen(Sensor sensor) throws InterruptedException {
        System.out.println(sensor.sensorName + " : Checking Blood Oxygen");
        Thread.sleep(2000);
        BloodOxygen = getRandomInteger(display.min, display.max);
        if (BloodOxygen >= 30) {
            System.out.println(sensor.sensorName + " : Normal Blood Oxygen Detected");
            Thread.sleep(3000);
            System.out.println(sensor.sensorName + " : Blood Oxygen Check Completed");
            Thread.sleep(2000);
            display.BloodCheckFin = true;
        } else {
            System.out.println(sensor.sensorName + " : Abnormal Blood Oxygen Detected");
            Thread.sleep(3000);
            danger = true;
            display.danger = true;
        }

    }
    
    public static int getRandomInteger(int minimum, int maximum) {
        return ((int) (Math.random() * (maximum - minimum))) + minimum;
    }

    public void addDetails(String dataType, int data) throws InterruptedException {
        sem.acquire();

        if (dataType == "Heart Rate") {
            display.heartRate.add(HeartRate);

        } else {
            display.bloodOxygen.add(BloodOxygen);
        }

        sem.release();
    }
    
    public void writeDetails(Sensor sensor, String dataType, int data) throws IOException, InterruptedException {
        sem1.acquire();
        System.out.println(sensor.sensorName + " : writing details to system");
        File file = new File("src/rts_2ndedition_5/VitalsDetails.txt");
        FileWriter writer = null;
        writer = new FileWriter(file, true);
        PrintWriter printer = new PrintWriter(writer);
        printer.append(sensor.sensorName + " : " + dataType + " = " + data + "\n");
        printer.close();

        System.out.println(sensor.sensorName + " : recorded " + dataType + " = " + data + " to system");
        sem1.release();
    }

    @Override
    public void run() {
        while(!danger){
            try {
                this.checkVitals(this);
            } catch (IOException ex) {
                Logger.getLogger(Sensor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Sensor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
}
