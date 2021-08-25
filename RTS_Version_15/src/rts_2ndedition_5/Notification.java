/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rts_2ndedition_5;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jared
 */
public class Notification implements Runnable{
    boolean danger;
    SmartWatch smartwatch;
    String notiName = "Notification";
    int countHeart, countBlood, totalRecord;
    float totalHeart, totalBlood;
    float bloodOxyAvg, heartRateAvg;
    
    public Notification(SmartWatch smartwatch) {
        this.smartwatch = smartwatch;
    }

    @Override
    public void run() {
        while(!danger){
            try {
                check(this);
            } catch (InterruptedException ex) {
                Logger.getLogger(Notification.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Iterator<Integer> listOfHeartRec = smartwatch.heartRate.iterator();
        Iterator<Integer> listOfBloodRec = smartwatch.bloodOxygen.iterator();
        
        while (listOfHeartRec.hasNext()){
            totalHeart = totalHeart + listOfHeartRec.next();
            countHeart+=1;
        }
        while (listOfBloodRec.hasNext()){
            totalBlood = totalBlood + listOfBloodRec.next();
            countBlood+=1;
        }
        bloodOxyAvg = calculateAvg(totalBlood,countBlood);
        heartRateAvg = calculateAvg(totalHeart, countHeart);
        totalRecord = countHeart + countBlood;
        System.out.println(notiName + " : Danger Detected Notified User and Calling Ambulance");
        System.out.println(notiName + " : Average Heart Rate is " + heartRateAvg);
        System.out.println(notiName + " : Average Blood Oxygen is " + bloodOxyAvg);
        System.out.println(notiName +" : Total collected data is "+ totalRecord);  
    }

    public void check(Notification notification) throws InterruptedException {
        while (!smartwatch.HeartRateChecker && !smartwatch.BloodOxygenChecker) {
            synchronized (smartwatch.notiWait) {
                smartwatch.notiWait.wait();
            }
        }

        if (smartwatch.HeartCheckFin && smartwatch.BloodCheckFin) {
            System.out.println(notification.notiName + " : notifying details to user");
            smartwatch.HeartRateChecker = false;
            smartwatch.BloodOxygenChecker = false;
            smartwatch.HeartCheckFin = false;
            smartwatch.BloodCheckFin = false;

        } else if (smartwatch.danger) {
            this.danger = true;
        } else {
            synchronized (smartwatch.notiWait) {
                smartwatch.notiWait.wait();
            }
        }
    }
    
    public float calculateAvg(float total, int count) {
        float average;
        average = total / count;
        return average;
    }

}
