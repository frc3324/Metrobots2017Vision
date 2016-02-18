package org.metrobots.botcv.peripheral;

import android.hardware.Sensor;
import android.hardware.SensorManager;

/**
 * Gyro class.
 */
public class Gyro {
    private float[] gyro, accel, mag;

    public Gyro() {}

    public void updateGyro(float[] gyro) {
        this.gyro = gyro;
    }

    public void updateAccel(float[] accel) {
        this.accel = accel;
    }

    public void updateMag(float[] mag) {
        this.mag = mag;
    }

    public float[] getAngle(boolean outputAngle) {
        float[] rotationMatrix = null, angles = null;
        if (SensorManager.getRotationMatrix(rotationMatrix, null, accel, mag)) {
            SensorManager.getOrientation(rotationMatrix, angles);

            if (outputAngle)
                return new float[] {(float) Math.toDegrees(angles[0]),
                                    (float)Math.toDegrees(angles[1]),
                                    (float) Math.toDegrees(angles[2])};
            else
                return angles;
        }
        return null;
    }
}
