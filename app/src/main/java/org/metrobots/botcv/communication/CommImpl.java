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
    public float[] getOrientation(boolean returnAngle) {
        return activity.getPeripheralManager().getGyro().getAngle(returnAngle);
    }

    @Override
    public float[] getAccelerometer() {
        return activity.getPeripheralManager().getAccelerometerData();
    }

    @Override
    public int getFiringStatus() {
        return activity.getCameraImpl().getStatus();
    }
}
