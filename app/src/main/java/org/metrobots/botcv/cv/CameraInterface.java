package org.metrobots.botcv.cv;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener;
import org.opencv.core.*;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;


/**
 * Interface class for the camera
 * Created by Tasgo on 1/16/16.
 */
public class CameraInterface implements CvCameraViewListener {
    private Mat frame = new Mat();
    private Mat hsv = new Mat();
    private Mat hierarchy = new Mat();
    private ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
    private LimiterSlider limiterSlider;
    private Mat contourFrame = new Mat();
    private Point offset = new Point();

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
        return cameraFrame(inputFrame);
    }


    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        return cameraFrame(inputFrame.rgba());
    }

    public Mat cameraFrame(Mat mat) {
        frame.empty(); hsv.empty(); hierarchy.empty(); contours.clear();// hierarchy.empty();
        Imgproc.cvtColor(mat, hsv, Imgproc.COLOR_BGR2HSV);
        Core.inRange(hsv, limiterSlider.getMin(), limiterSlider.getMax(), frame);
        Imgproc.medianBlur(frame, frame, 5);

        frame.copyTo(contourFrame);
        Imgproc.findContours(contourFrame, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(mat, contours, -2, new Scalar(0, 0, 255), 5, 8, hierarchy, Imgproc.INTER_MAX, offset);

        return mat;
    }
}
