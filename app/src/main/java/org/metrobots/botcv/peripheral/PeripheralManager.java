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
    private Sensor acceleremoterSensor, gyroSensor, magneticSensor;
    private float[] gyroData = new float[3], accelerometerData = new float[3], magneticData = new float[3];
    private Gyro gyro;

    public PeripheralManager(Context parent) {
        manager = (SensorManager) parent.getSystemService(Context.SENSOR_SERVICE);
        acceleremoterSensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroSensor = manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        magneticSensor = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                accelerometerData = sensorEvent.values;
                gyro.updateAccel(accelerometerData);
                break;
            case Sensor.TYPE_GYROSCOPE:
                gyroData = sensorEvent.values;
                gyro.updateGyro(gyroData);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                magneticData = sensorEvent.values;
                gyro.updateMag(magneticData);
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public Gyro getGyro() {
        return gyro;
    }

    public float[] getAccelerometerData() {
        return accelerometerData;
    }
}
