/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rts.version.pkg6;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jared
 */
public class Sensor extends TimerTask{
    int sensorID;
    String sensorName;
    SmartWatch display;
    boolean danger = false;
    
    
    public Sensor(SmartWatch display, int sensorNum) {
        this.sensorID = sensorNum;
        sensorName = "Sensor " + sensorNum;
        this.display = display;
    }

    @Override
    public void run() {
        try {
            display.checkVitals(this);
        } catch (InterruptedException ex) {
            Logger.getLogger(Sensor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Sensor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
