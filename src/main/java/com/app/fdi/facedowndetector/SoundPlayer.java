package com.app.fdi.facedowndetector;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SoundPlayer {

    private final ScheduledExecutorService executor;
    private final MediaPlayer mediaPlayer;
    private final Runnable task;

    @Nullable private ScheduledFuture<?> scheduledFuture;

    public SoundPlayer(Context context) {
        executor = Executors.newSingleThreadScheduledExecutor();
        mediaPlayer = MediaPlayer.create(context, R.raw.simple_tone);
        task = new Runnable() {
            public void run() {
                mediaPlayer.start();
            }
        };
        scheduledFuture = null;
    }

    public void startAdvertising() {
        if (isAdvertising()) {
            Log.d(Constant.TAG, "Cannot start advertising, because already advertising.");
            return;
        }
        scheduledFuture =
                executor.scheduleAtFixedRate(task, 0L, 1L, TimeUnit.SECONDS);
        Log.d(Constant.TAG, "Started advertising.");
    }

    public void stopAdvertising() {
        if (!isAdvertising()) {
            Log.d(Constant.TAG, "Cannot stop advertising, because not advertising.");
            return;
        }
        scheduledFuture.cancel(false);
        scheduledFuture = null;
        Log.d(Constant.TAG, "Stopped advertising.");
    }

    private boolean isAdvertising() {
        return scheduledFuture != null;
    }
}
