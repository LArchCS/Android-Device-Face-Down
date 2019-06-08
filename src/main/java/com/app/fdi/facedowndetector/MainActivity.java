package com.app.fdi.facedowndetector;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textMessage;
    private BottomNavigationView navigation;

    @Nullable private SensorManager sensorManager;
    @Nullable private Sensor sensor;
    private FaceDownDetector faceDownDetector;
    private SoundPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textMessage = findViewById(R.id.message);
        player = new SoundPlayer(this);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager == null ?
                null : sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        navigation = findViewById(R.id.navigation);

        BottomNavigationView.OnNavigationItemSelectedListener onClickListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.play_sound:
                                playSound();
                                Log.d(Constant.TAG, "State changed to play_sound.");
                                return true;
                            case R.id.stop_sound:
                                stopSound();
                                Log.d(Constant.TAG, "State changed to stop_sound.");
                                return true;
                        }
                        return false;
                    }
                };

        faceDownDetector = new FaceDownDetector(new FaceDownDetector.FaceDownListener() {
            @Override
            public void onFaceDown() {
                navigation.setSelectedItemId(R.id.play_sound);
            }

            @Override
            public void onTiltUp() {
                navigation.setSelectedItemId(R.id.stop_sound);
            }
        });

        navigation.setOnNavigationItemSelectedListener(onClickListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (sensorManager != null && sensor != null) {
            faceDownDetector.reset();
            sensorManager.registerListener(faceDownDetector, sensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(Constant.TAG, "Registered FaceDownDetector.");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (sensorManager != null && sensor != null) {
            sensorManager.unregisterListener(faceDownDetector);
            stopSound();
            Log.d(Constant.TAG, "Unregistered FaceDownDetector.");
        }
    }

    private void playSound() {
        textMessage.setText(R.string.play_sound);
        player.startAdvertising();
    }

    private void stopSound() {
        textMessage.setText(R.string.stop_sound);
        player.stopAdvertising();
        faceDownDetector.reset();
    }
}
