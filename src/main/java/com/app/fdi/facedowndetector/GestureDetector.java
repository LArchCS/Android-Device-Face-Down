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
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == sensorType) {
            onSensorEvent(sensorEvent);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // no-op
    }

    abstract void onSensorEvent(SensorEvent sensorEvent);

    abstract public void reset();
}
