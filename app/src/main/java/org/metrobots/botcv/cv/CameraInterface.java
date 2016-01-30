package org.metrobots.botcv.cv;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener;
import org.opencv.core.Core;
import org.opencv.core.Mat;

/**
 * Interface class for the camera
 * Created by Tasgo on 1/16/16.
 */
public class CameraInterface implements CvCameraViewListener {
    public boolean paused = false;
    private boolean freezed = false;
    private Mat frame = new Mat(), buf = new Mat();
    private LimiterSlider limiterSlider;

    public CameraInterface(LimiterSlider limiterSlider) {
        this.limiterSlider = limiterSlider;
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(Mat inputFrame) {
        //Mat mRgba = inputFrame;
        //Mat mRgbaT = mRgba.t();
        //Core.flip(mRgba.t(), mRgbaT, 1);
        //Imgproc.resize(mRgbaT, mRgbaT, mRgba.size());
        //return mRgbaT;
        return cameraFrame(inputFrame);
    }


    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        return cameraFrame(inputFrame.rgba());
    }

    public Mat cameraFrame(Mat mat) {
        Core.inRange(mat, limiterSlider.getMin(), limiterSlider.getMax(), frame);

        return frame;
    }

    public void switchPause() {
        paused = !paused;
        if (paused)
            freezed = true;
        else
            frame = null;
    }
}
