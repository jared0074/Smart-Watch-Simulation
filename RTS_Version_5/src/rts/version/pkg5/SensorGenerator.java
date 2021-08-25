/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rts.version.pkg5;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jared
 */
public class SensorGenerator implements Runnable{
    SmartWatch display;
    int numSensor = 1;
    boolean dangerTime;

    public SensorGenerator(SmartWatch display) {
        this.display = display;
    }

    @Override
    public void run() {
        while (numSensor != 3) {
            Sensor sensor = new Sensor(display, numSensor);
            display.execute.execute(sensor);
            numSensor++;
        }
    }
}
