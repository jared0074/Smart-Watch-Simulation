/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rts_2ndedition_3;

/**
 *
 * @author jared
 */
public class SensorGenerator implements Runnable{
    SmartWatch display;
    boolean dangerTime;
    
    public SensorGenerator(SmartWatch display){
        this.display = display;
    }

    @Override
    public void run() {
       for(int numSensor=1; numSensor!=3; numSensor++){
           Sensor sensor = new Sensor(display, numSensor);
           display.execute.execute(sensor);
       }
    }
    
    
}
