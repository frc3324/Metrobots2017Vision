package org.metrobots.botcv;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Interface class for the camera
 * Created by Tasgo on 1/16/16.
 */
public class CameraInterface implements CvCameraViewListener {
    public boolean paused = false;
    private boolean freezed = false;
    private Mat frame = null;

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
        if (freezed) {
            System.out.println("Freezed true, saving mat");
            frame = new Mat();
            mat.copyTo(frame);
            freezed = false;
        }
        if (frame != null) {
            System.out.println("frame not null, returning it");
            return frame;
        }
        return mat;
    }

    public void switchPause() {
        paused = !paused;
        System.out.println(paused);
        if (paused)
            freezed = true;
        else
            frame = null;
    }
}
