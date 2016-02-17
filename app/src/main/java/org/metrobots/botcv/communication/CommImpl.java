package org.metrobots.botcv.communication;

import android.app.AlertDialog;
import android.widget.Toast;

import org.metrobots.botcv.MainActivity;

/**
 * Implementation of the CommInterface.
 */
public class CommImpl implements CommInterface {
    private MainActivity activity;

    public CommImpl(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public float[] getGyro() {
        return activity.getPeripheralManager().getGyroData();
    }

    @Override
    public float[] getAccelerometer() {
        return activity.getPeripheralManager().getAccelerometerData();
    }
}
