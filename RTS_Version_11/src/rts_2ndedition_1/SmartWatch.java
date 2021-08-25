/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rts_2ndedition_1;

import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author jared
 */
public class SmartWatch {
    Notification notification = new Notification(this);
    Clock clock = new Clock(this);
    int min = 30; int max = 100;
    LinkedBlockingQueue<Integer> heartRate = new LinkedBlockingQueue();
    LinkedBlockingQueue<Integer> bloodOxygen = new LinkedBlockingQueue();
    boolean BloodCheckFin = false;
    boolean HeartCheckFin = false;
    Object sensorWait = new Object();
    Object notiWait = new Object();
    boolean HeartRateChecker,BloodOxygenChecker;
    boolean danger = false;
    
    public SmartWatch(){
        SensorGenerator senseGen = new SensorGenerator(this);
        Thread thClock = new Thread(clock);
        thClock.start();
        
        Thread thSenseGen = new Thread(senseGen);
        thSenseGen.start();
        
        Thread thNotification = new Thread(notification);
        thNotification.start();
    }
}
