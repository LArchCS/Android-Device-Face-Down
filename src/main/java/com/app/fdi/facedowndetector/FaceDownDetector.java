package com.app.fdi.facedowndetector;

import android.hardware.SensorEvent;
import android.util.Log;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;

public class FaceDownDetector extends GestureDetector {

    private static final float MAX_Z_ACCELERATION = 9;
    private static final float MIN_Z_ACCELERATION = 6;

    private final FaceDownListener listener;

    private boolean isFacingDown;

    public FaceDownDetector(FaceDownListener listener) {
        super(TYPE_ACCELEROMETER);
        this.listener = listener;
        this.isFacingDown = false;
    }

    @Override
    void onSensorEvent(SensorEvent sensorEvent) {
        if (!isFacingDown && isFacingDown(sensorEvent)) {
            Log.d(Constant.TAG, "Detected face-down.");
            isFacingDown = true;
            listener.onFaceDown();
            return;
        }

        if (isFacingDown && isTiltingUp(sensorEvent)) {
            Log.d(Constant.TAG, "Detected tilt-up.");
            isFacingDown = false;
            listener.onTiltUp();
        }
    }

    @Override
    public void reset() {
        isFacingDown = false;
    }

    private boolean isFacingDown(SensorEvent sensorEvent) {
        return sensorEvent.values[2] < -MAX_Z_ACCELERATION;
    }

    private boolean isTiltingUp(SensorEvent sensorEvent) {
        return sensorEvent.values[2] > -MIN_Z_ACCELERATION;
    }

    public interface FaceDownListener {
        void onFaceDown();

        void onTiltUp();
    }
}
