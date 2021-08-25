/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rts.version.pkg10;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jared
 */
public class SensorGenerator extends TimerTask{
    SmartWatch display;
    int numSensor = 1;
    boolean dangerTime;
    Timer t,t1;

    public SensorGenerator(SmartWatch display) {
        this.display = display;
        t = new Timer();
        t1 = new Timer();
    }
    
    class createThread extends TimerTask {

        int numSensorThread;

        createThread(int numSensor) {
            this.numSensorThread = numSensor;
        }

        @Override
        public void run() {
            Sensor sensor = new Sensor(display, numSensorThread);
            sensor.run();
            t.cancel();
            t1.cancel();

        }

    }

    @Override
    public void run() {

        t.schedule(new createThread(1), 0);
        t1.schedule(new createThread(2), 1000);

    }
}
