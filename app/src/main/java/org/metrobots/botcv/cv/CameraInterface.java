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
        //Mat frame = new Mat();
        //frame.
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
        //processImage(inputFrame);
        //Mat newMa = new Mat();
        //Imgproc.cvtColor(frame, newMa, Imgproc.COLOR_BGR2HSV);
        //frame = new Mat();
        //cameraFrame(inputFrame.gray()).copyTo(frame);
        return cameraFrame(inputFrame.rgba());
    }

    /*public Mat getThreshold(){
        Mat newMa = new Mat();
        Imgproc.cvtColor(frame, newMa, Imgproc.COLOR_BGR2HSV);
        //Scalar max = Scalar(38, 255, 255);
        //Scalar min = Scalar(16, 216, 100);
        //Size Imgproc.erodeVals = (10, 10);
        Mat threshold;
        //Core.inRange(newMa, min, max, threshold);
        int ErosionElement = 0;
        int ErosionSize = 0;
        int DialationELement = 0;
        int DialationSize = 0;
        int MaxElement = 2;
        int MaxKernelSize = 21;

        Size sze = (2*ErosionSize+1,2*ErosionSize+1);
        Point pts = (ErosionSize,ErosionSize);
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE,
               sze,
                pts);

        Imgproc.erode(threshold, threshold, element);//Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, erodeVals));
        Imgproc.dilate(threshold,  threshold, element);//Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, erodeVals));
        Imgproc.dilate(threshold, threshold, element);//Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, erodeVals));
        Imgproc.erode(threshold, threshold, element);//Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, erodeVals));
        return threshold;
        //return newMa;
    }*/

    public Mat cameraFrame(Mat mat) {
        frame.empty(); hsv.empty(); hierarchy.empty(); contours.clear();
        Imgproc.cvtColor(mat, hsv, Imgproc.COLOR_BGR2HSV);
        Core.inRange(hsv, new Scalar(45, 112, 115), new Scalar(67, 255, 255), frame);
        //41,112,115 87,255,255
        //Core.inRange(hsv,limiterSlider.getMin(), limiterSlider.getMax() , frame);
        //frame.copyTo(contourFrame);//frame.copyTo(contourFrame);

        //clearing up the small useless bits of 'green' that are irrelevent
        //but leaving the original mat unaffected
        //
        Size erdVal = new Size(3, 3);
        Imgproc.erode(frame, frame, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, erdVal));
        Imgproc.dilate(frame, frame, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, erdVal));
        Imgproc.dilate(frame, frame, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, erdVal));
        Imgproc.erode(frame, frame, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, erdVal));

        //*/
        Imgproc.findContours(frame, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(mat, contours, -2, new Scalar(0, 0, 255), 5, 8, hierarchy, Imgproc.INTER_MAX, offset);
        /*
        Scalar color = new Scalar(255, 0, 0); // color for outlining the contours
        double max = 1000;

        for (int a = 0; a < contours.size()-1; a++) {
            List<Point> l = contours.get(a).toList();
            int s = l.size();
            for (int b = 0; b < s - 1; b++) {
                double s2 = Imgproc.contourArea(contours.get(a));
                if (s2 > max) {
                    Imgproc.line(mat, l.get(b), l.get(b + 1), color, 7);
                }
            }
        }
        */
        return mat;
    }
}
