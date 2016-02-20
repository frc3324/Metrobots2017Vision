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
    public ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
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
        frame.empty(); hsv.empty(); hierarchy.empty(); contours.clear();
        Imgproc.cvtColor(mat, hsv, Imgproc.COLOR_BGR2HSV);
        //Core.inRange(hsv, new Scalar(55, 40, 125), new Scalar(70, 255, 255), frame);
        Core.inRange(hsv, new Scalar(46, 112, 100), new Scalar(70, 255, 255), frame);
        Imgproc.medianBlur(frame, frame, 5);

        frame.copyTo(contourFrame);

        Imgproc.findContours(contourFrame, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(mat, contours, -2, new Scalar(0, 0, 255), 5, 8, hierarchy, Imgproc.INTER_MAX, offset);

        try{
            Rect place = Imgproc.boundingRect(contours.get(0));
            Imgproc.rectangle(mat, place.tl(), place.br(), new Scalar(255,0,0), 10, Imgproc.LINE_8, 0);
        }
        catch(Exception e) {
            System.out.println(e);
        }
        try {
            int max = 0;
            for (int a=0;a<contours.size();a++){
                //List<Point> l = contours.get(a).toList();
                //int s = l.size();
                //for (int b=0; b < s; b++) {
                    double s2 = Imgproc.contourArea(contours.get(a));
                    if (s2 > Imgproc.contourArea(contours.get(max))) {
                        max = a;
                    }
                    //System.out.println(a+": "+s2);
                //}
            }

            double contors = 0;
            if (Imgproc.contourArea(contours.get(0)) != 0.0){
                contors = Imgproc.contourArea(contours.get(0));
            }
            double contors1 = 0;
            if (Imgproc.contourArea(contours.get(1)) != 0.0){
                contors = Imgproc.contourArea(contours.get(1));
            }
            double contors2 = 0;
            if (Imgproc.contourArea(contours.get(2)) != 0.0){
                contors = Imgproc.contourArea(contours.get(2));
            }
            System.out.println("Contour 1: " + contors + "\nContour 2: " + contors1 + "\nContour 3: " + contors2);
            System.out.println(contours.size());
            //System.out.println(hierarchy.get(0,0).getClass().getName());
            //System.out.println("\n\n\n\n");
            /*Integer widh = contours.get(1).width();
            Integer heigt = contours.get(1).height();
            System.out.println("(" + widh + "," + heigt + ')');*/
        }
        catch (Exception e) {
            //e.printStackTrace();
        }
        return mat;
    }

}
