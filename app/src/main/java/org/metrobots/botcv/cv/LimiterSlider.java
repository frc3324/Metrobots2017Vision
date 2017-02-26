package org.metrobots.botcv.cv;

import org.opencv.core.Scalar;

public class LimiterSlider {

    boolean commitingChanges = true;

    public static final int HUE = 0, SATURATION = 1, VALUE = 2;

    /*
    public SeekBarInterface[] minSliders =
            {new SeekBarInterface(41), new SeekBarInterface(112), new SeekBarInterface(115)};
    public SeekBarInterface[] maxSliders =
            {new SeekBarInterface(87), new SeekBarInterface(255), new SeekBarInterface(255)};
    */
    public SeekBarInterface[] minSliders = {new SeekBarInterface(41), new SeekBarInterface(112), new SeekBarInterface(115)};
    public SeekBarInterface[] maxSliders = {new SeekBarInterface(87), new SeekBarInterface(255), new SeekBarInterface(255)};


    public static class WrongSizeException extends Exception {
        public WrongSizeException() {
            super("The SeekBarInterface array is not of the correct size (3).");
        }
    }

    public static Scalar getScalarfromSeekBarArray(SeekBarInterface[] seekBarInterfaces) throws WrongSizeException {
        double val[] = new double[3];

        if (seekBarInterfaces.length != 3)
            throw new WrongSizeException();

        for (int i = 0; i < 3; i++) {
            val[i] = seekBarInterfaces[i].getProgress();
        }

        return new Scalar(val);
    }

    public Scalar getMin() {
        try {
            return getScalarfromSeekBarArray(minSliders);
        } catch (WrongSizeException e) {
            return null;
        }
    }

    public Scalar getMax() {
        try {
            return getScalarfromSeekBarArray(maxSliders);
        } catch (WrongSizeException e) {
            return null;
        }
    }
}
