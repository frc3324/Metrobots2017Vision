package org.metrobots.botcv.peripheral;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Gyro and accelerometer manager.
 */
public class PeripheralManager implements SensorEventListener {
    private SensorManager manager;
    private Sensor acceleremoter, gyro;
    private float[] gyroData = new float[3], accelerometerData = new float[3];

    public PeripheralManager(Context parent) {
        manager = (SensorManager) parent.getSystemService(Context.SENSOR_SERVICE);
        acceleremoter = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyro = manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                accelerometerData = sensorEvent.values;
                break;
            case Sensor.TYPE_GYROSCOPE:
                gyroData = sensorEvent.values;
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public float[] getGyroData() {
        return gyroData;
    }

    public float[] getAccelerometerData() {
        return accelerometerData;
    }
}
