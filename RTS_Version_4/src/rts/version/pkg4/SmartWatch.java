/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rts.version.pkg4;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author jared
 */
public class SmartWatch {
    Sensor sensor;
    Notification notification = new Notification(this);
    Clock clock = new Clock (this);
    Lock lock = new ReentrantLock();
    Lock lock1 = new ReentrantLock();
    Lock lock2 = new ReentrantLock();
    int min = 30; int max = 100;
    ExecutorService execute = Executors.newCachedThreadPool();
    boolean HeartRateAbnormal = false;
    boolean BloodOxygenAbnormal = false;
    boolean StepsAbnormal = false;
    boolean HeartRateChecker = false;
    boolean BloodOxygenChecker = false;
    boolean BloodCheckFin = false;
    boolean HeartCheckFin = false;
    int HeartRate,BloodOxygen;
    Object sensorWait = new Object();
    Object notiWait = new Object();
    boolean danger;
    LinkedBlockingQueue<Integer> heartRate = new LinkedBlockingQueue();
    LinkedBlockingQueue<Integer> bloodOxygen = new LinkedBlockingQueue();
    int countHeart = 0, countBlood = 0, totalRecord = 0; 
    float totalHeart, totalBlood;


    
    public SmartWatch() {
        SensorGenerator senseGen = new SensorGenerator(this);
        execute.execute(clock); 
        execute.execute(senseGen);
        execute.execute(notification);
        
    }
    
    public void checkVitals(Sensor sensor) throws InterruptedException, IOException {
        while (!danger) {
            if (!HeartRateChecker) {
                HeartRateChecker = true;
                checkHeartRate(sensor);
                addDetails("Heart Rate", HeartRate);
                writeDetails(sensor, "Heart Rate", HeartRate);
                countRecord();

            } else if (!BloodOxygenChecker) {
                BloodOxygenChecker = true;
                checkBloodOxygen(sensor);
                addDetails("Blood Oxygen", BloodOxygen);
                writeDetails(sensor, "Blood Oxygen", BloodOxygen);
                countRecord();
            }

            if (HeartCheckFin || BloodCheckFin) {
                synchronized (notiWait) {
                    notiWait.notify();
                }
            }

        }
        execute.shutdown();
        synchronized(notiWait){
            notiWait.notify();
        }
        System.out.println(sensor.sensorName + " : Emergency Detected, Saving Details and Stop All Work");

    }

    public void check(Notification notification) throws InterruptedException {
        while (!HeartRateChecker && !BloodOxygenChecker) {
            synchronized (notiWait) {
                notiWait.wait();
            }
        }

        if (HeartCheckFin && BloodCheckFin) {
            System.out.println(notification.notiName + " : notifying details to user");
            HeartRateChecker = false;
            BloodOxygenChecker = false;
            HeartCheckFin = false;
            BloodCheckFin = false;

        } else if (danger) {
            notification.dangerTime = true;
            notification.heartRateAvg = calculateAvg(totalHeart,countHeart);
            notification.bloodOxyAvg = calculateAvg(totalBlood,countBlood);
        } else {
            synchronized (notiWait) {
                notiWait.wait();
            }
        }

    }
    
    public static int getRandomInteger(int minimum, int maximum) {
        return ((int) (Math.random() * (maximum - minimum))) + minimum;
    }

    public boolean checkHeartRate(Sensor sensor) throws InterruptedException {
        System.out.println(sensor.sensorName + " : Checking Heart Rate");
        Thread.sleep(2000);
        HeartRate = getRandomInteger(min, max);
        if (HeartRate >= 30) {
            System.out.println(sensor.sensorName + " : Normal Heart Rate Detected");
            Thread.sleep(3000);
            System.out.println(sensor.sensorName + " : Heart Rate Check Completed");
            Thread.sleep(2000);
            HeartCheckFin = true;
        } else {
            System.out.println(sensor.sensorName + " : Abnormal Heart Rate Detected");
            Thread.sleep(3000);
            danger = true;
        }
        
        return true;

    }

    public boolean checkBloodOxygen(Sensor sensor) throws InterruptedException {

        System.out.println(sensor.sensorName + " : Checking Blood Oxygen");
        Thread.sleep(2000);
        BloodOxygen = getRandomInteger(min, max);
        if (BloodOxygen >= 30) {
            System.out.println(sensor.sensorName + " : Normal Blood Oxygen Detected");
            Thread.sleep(3000);
            System.out.println(sensor.sensorName + " : Blood Oxygen Check Completed");
            Thread.sleep(2000);
            BloodCheckFin = true;
        } else {
            System.out.println(sensor.sensorName + " : Abnormal Blood Oxygen Detected");
            Thread.sleep(3000);
            danger = true;
        }
        
        return true;
    }

    public void  writeDetails(Sensor sensor, String dataType, int data) throws IOException, InterruptedException {
        lock.lock();
        System.out.println(sensor.sensorName + " : writing details to system");
        File file = new File("src/rts/version/pkg4/VitalsDetails.txt");
        FileWriter writer = null;
        writer = new FileWriter(file, true);
        PrintWriter printer = new PrintWriter(writer);
        printer.append(sensor.sensorName + " : " + dataType + " = " + data + "\n");
        printer.close();
        
        System.out.println(sensor.sensorName + " : recorded " + dataType + " = " + data + " to system");
        lock.unlock();
    }
    
    public void addDetails(String dataType, int data) throws InterruptedException{
        lock1.lock();
  
        if (dataType == "Heart Rate"){
            heartRate.add(HeartRate);
            totalHeart = totalHeart + data;
            countHeart+=1;
            
        }else{
            bloodOxygen.add(BloodOxygen);
            totalBlood = totalBlood + data;
            countBlood+=1;
        }

        lock1.unlock();
    }
    
    public void countRecord() throws InterruptedException{
        lock2.lock();
        totalRecord+=1;
        lock2.unlock();
    }
    
    public float calculateAvg(float total, int count){
        float average;
        average = total / count;
        return average;
    }

}
