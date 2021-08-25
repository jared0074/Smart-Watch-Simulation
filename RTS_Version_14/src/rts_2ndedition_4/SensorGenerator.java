/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rts_2ndedition_4;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author jared
 */
public class SensorGenerator extends TimerTask{
    SmartWatch display;
    boolean dangerTime;
    int numSensor = 1;
    Timer t, t1;
        
    public SensorGenerator(SmartWatch display){
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
        display.t.cancel();
    }
    
    
}
