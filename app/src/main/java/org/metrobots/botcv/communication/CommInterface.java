package org.metrobots.botcv.communication;

/**
 * Main interface class fot communication.
 */
public interface CommInterface {
    float[] getOrientation(boolean returnAngle);
    float[] getAccelerometer();
    int getFiringStatus();
    int getDirection();
    int getMagnitude();
}
