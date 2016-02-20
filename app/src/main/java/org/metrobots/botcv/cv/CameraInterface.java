package org.metrobots.botcv.cv;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener;
import org.opencv.core.*;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

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
    Point centroid = new Point();
    private List<Moments> mu = new ArrayList<Moments>(contours.size());

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
        frame.empty(); hsv.empty(); hierarchy.empty(); contours.clear();
        Imgproc.cvtColor(mat, hsv, Imgproc.COLOR_BGR2HSV);
        //Core.inRange(hsv, new Scalar(55, 40, 125), new Scalar(70, 255, 255), frame);
        Core.inRange(hsv, new Scalar(46, 112, 115), new Scalar(70, 255, 255), frame);
        Imgproc.medianBlur(frame, frame, 5);

        frame.copyTo(contourFrame);
        Rect place = Imgproc.boundingRect(contours.get(0));

        Imgproc.rectangle(mat, place.tl(), place.br(), new Scalar(255,0,0));


        return mat;
    }

}
