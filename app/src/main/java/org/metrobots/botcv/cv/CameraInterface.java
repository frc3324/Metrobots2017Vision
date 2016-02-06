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
        Core.inRange(hsv, new Scalar(41, 112, 115), new Scalar(87, 255, 255), frame);
        //frame.copyTo(contourFrame);//frame.copyTo(contourFrame);
        Imgproc.findContours(frame, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        //Imgproc.drawContours(mat, contours, -2, new Scalar(0, 0, 255), 5, 8, hierarchy, Imgproc.INTER_MAX, offset);
        if(contours.size() != 0){
            //System.out.println(contours.size());//varied
            //System.out.println(contours.get(contours.size() - 1).width());//did not vary
            //System.out.println(contours.get(0).height());//height varies so its useful?
            //double[] d = contours.get(0).get(0,0);
            //System.out.println(d[0]);//with the tablet laying on the tape, gives the value 1
            //Mat maxContour = null;
            /*double maxContourarea=0;
            for (int idx = 0; idx < contours.size(); idx++) {
                Mat contour = contours.get(idx);
                double contourarea = Imgproc.contourArea(contour);
                System.out.println(contourarea);
            }*/
            //System.out.println(maxContour.get(0,0));
            // System.exit(0);
            int sum = 0;
            for(int i = 0; i < contours.size(); i++){
                List<Point> l = contours.get(i).toList();
                int s = l.size();
                for(int a = 0; a < s-1; a++){
                    Imgproc.line(mat, l.get(a), l.get(a+1), new Scalar(0, 0, 255));
                    //System.out.println(l.get(a).toString());

                }
            }
            //System.out.println(sum);
            //System.out.println(contours.get(0).depth());
            //System.exit(0);
        }
        else{
            System.out.println("Sorry No Contours Avaiable.");
        }
        //System.out.println(limiterSlider.getMin());
        //System.out.println(limiterSlider.getMax());
        return mat;
    }
}
