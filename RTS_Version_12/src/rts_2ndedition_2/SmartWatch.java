/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rts_2ndedition_2;

import java.util.Timer;
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
    Timer t = new Timer();
    Timer t1 = new Timer();
    Timer t2 = new Timer();

    public SmartWatch(){
        SensorGenerator senseGen = new SensorGenerator(this);
        t.scheduleAtFixedRate(clock,0,6*1000);
        t1.schedule(senseGen,0);
        t2.schedule(notification,0);
    }
}
