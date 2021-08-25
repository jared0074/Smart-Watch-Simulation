/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rts_2ndedition_3;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jared
 */
public class Clock extends Thread{
    SmartWatch smartWatch;
    
    public Clock(SmartWatch smartWatch){
        this.smartWatch = smartWatch;
    }
    
    @Override
    public void run() {
        try {
            Thread.sleep(60000);
            System.out.println("One minute over, reset minimum and maximum on smartwatch");
            setDanger();
        } catch (InterruptedException ex) {
            Logger.getLogger(Clock.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setDanger() {
        smartWatch.min = 0;
        smartWatch.max = 20;
    }
}
