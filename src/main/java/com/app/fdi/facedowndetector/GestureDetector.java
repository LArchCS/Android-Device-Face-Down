package com.app.fdi.facedowndetector;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

abstract class GestureDetector implements SensorEventListener {

    private final int sensorType;

    GestureDetector(int sensorType) {
        this.sensorType = sensorType;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == sensorType) {
            onSensorEvent(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // no-op
    }

    abstract void onSensorEvent(SensorEvent sensorEvent);

    abstract public void reset();
}
