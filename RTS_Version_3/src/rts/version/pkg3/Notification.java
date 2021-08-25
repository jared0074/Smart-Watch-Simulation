/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rts.version.pkg3;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jared
 */
public class Notification implements Runnable{
    boolean dangerTime = false;
    String notiName = "Notification";
    SmartWatch smartwatch;
    int countHeart = 0, countBlood = 0;
    float totalHeart, totalBlood;
    float heartRateAvg, bloodOxyAvg;
        
    public Notification(SmartWatch smartwatch){
        this.smartwatch = smartwatch;
    }

    @Override
    public void run() {
        while(!dangerTime){
            try {
                smartwatch.check(this);
            } catch (InterruptedException ex) {
                Logger.getLogger(Notification.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println(notiName + " : Danger Detected Notified User and Calling Ambulance");
        smartwatch.execute.shutdown();
        System.out.println(notiName + " : Average Heart Rate is "+ heartRateAvg);
        System.out.println(notiName +" : Average Blood Oxygen is "+ bloodOxyAvg);  
        System.out.println(notiName +" : Total collected data is "+ smartwatch.totalRecord);
   }
    
    
}
