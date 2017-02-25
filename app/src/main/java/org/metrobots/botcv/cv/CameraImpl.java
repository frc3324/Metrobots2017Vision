package org.metrobots.botcv.cv;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener;
import org.opencv.core.*;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

/*
 * Interface class for the camera
 * Created by Tasgo on 1/16/16.
 */
public class CameraImpl implements CvCameraViewListener{
        //Initializing variables for future use
    private Mat frame = new Mat();
    private Mat hsv = new Mat();
    private Mat hierarchy = new Mat();
    private ArrayList<MatOfPoint> contours = new ArrayList<>();
    private Mat contourFrame = new Mat();
    private Point offset = new Point();
    private int status = 2;
    private Mat hsv2 = new Mat();
    private long oldMillis = 0;
    private int thresholSet = 0;
    private static final String MEASURE = "Rectangle measurement";
    private String measureInfoWidth = "Width";
    private String measureInfoHeight = "Height";


    //temp code
    private LimiterSlider limiterSlider;

    public CameraImpl() {
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
            //Empty's frames and lists to ensure the app does not crash and reduces lag
        frame.empty(); hsv.empty(); hsv2.empty(); hierarchy.empty(); contours.clear();

            //Converts the RGB frame to the HSV frame
        Imgproc.cvtColor(mat, hsv, Imgproc.COLOR_BGR2HSV);

        if (Math.abs(System.currentTimeMillis()-oldMillis) > 1000 && thresholSet < 5) {
            oldMillis = System.currentTimeMillis();
            thresholSet++;
        } else if (Math.abs(System.currentTimeMillis()-oldMillis) > 1000){
            oldMillis = System.currentTimeMillis();
            thresholSet = 0;
        }
            //creates a copy so the original is unaffected
        hsv.copyTo(hsv2);

            //tries to remove random splotches of contours
        Imgproc.bilateralFilter(hsv, hsv2, 3, 10, 10);
        Imgproc.medianBlur(hsv, hsv, 5); //changed from 3 to 5
        //Imgproc.GaussianBlur(hsv, hsv, new Size(5,5), 2);

        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));

            //further tries to remove contours
        Imgproc.erode(hsv, hsv, element);
        Imgproc.dilate(hsv, hsv, element);


            //filters out colors outside of the set range of hsv
        //Core.inRange(hsv, new Scalar(45, 100, 150), new Scalar(70, 255, 255), frame);
        //Core.inRange(hsv, new Scalar(45 , 110, 150), new Scalar(70, 255, 255), frame);
        //Core.inRange(hsv, new Scalar(40, 90, 150), new Scalar(75, 255, 255), frame); //88.5
        //Core.inRange(hsv, new Scalar(0, 0, 240), new Scalar(0, 0, 255), frame); //this is the color white; only seems to work on field
        //Core.inRange(hsv, new Scalar(40, 90, 150), new Scalar(75, 255, 255), frame);

        int hueLow = (80*255)/360; //80 is the value from colorizer.org
        int hueHigh = (135*255)/360;

        int satLow = (50*255)/100;
        int satHigh = (100*255)/100;

        int valLow = (50*255)/100;
        int valHigh = (100*255)/100;
        //these lines convert the values from colorizer.org to the scale in the code

        //Core.inRange(hsv, new Scalar(0, 225, 250), new Scalar(100, 245, 255), frame);
        Core.inRange(hsv, new Scalar(hueLow, satLow, valLow), new Scalar(hueHigh, satHigh, valHigh), frame);

        //Core.inRange(hsv, limiterSlider.getMin(), limiterSlider.getMax(), frame);

            //Copies the black and white image to a new frame to prevent messing up the original
        frame.copyTo(contourFrame);



            //Finds the contours in the thresholded frame
        Imgproc.findContours(contourFrame, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
            //Draws the contours found on the original camera feed
        Imgproc.drawContours(mat, contours, -2,
                new Scalar(0, 0, 255), 5, 8, hierarchy, Imgproc.INTER_MAX, offset);

            //Draws circle at the center of the feed
        Imgproc.circle(mat, new Point((mat.size().width) / 2, (mat.size().height) / 2),
                5, new Scalar(255, 255, 0), 15, Imgproc.LINE_8, 0);
        try {
                //Creates the max variable
            int max = 0;
            int max2 = 0;
                //Sets up loop to go through all contours
            for (int a=0;a<contours.size();a++){
                        //Gets the area of all of the contours
                    double s2 = Imgproc.contourArea(contours.get(a));
                        //Checks the area against the other areas of the contours to find out which is largest
                    if (s2 > Imgproc.contourArea(contours.get(max))) {
                            //Sets largest contour equal to max variable
                        max = a;
                    } else if (s2 > Imgproc.contourArea(contours.get(max2))) {
                        max2 = a;
                    }
            }

            try{
                    //Gets the minimum area vertical(non titlted) rectangle that outlines the contour
                Rect place = Imgproc.boundingRect(contours.get(max));
                Rect place2 = Imgproc.boundingRect(contours.get(max2));
                    //Creates variable for center point
                Point center = new Point();
                Point center2 = new Point();
                    //Sets variale fpr screen center so now we adjust the X and Y axis
                //Point screenCenter = new Point();
                    //Creates top left point variable
                Point topleft = place.tl();
                Point topleft2 = place2.tl();
                    //Creates bottom right point variable
                Point bottomright = place.br();
                Point bottomright2 = place2.br();
                    //Finds the width of rectangle
                double width = (bottomright.x - topleft.x);
                double height = (bottomright.y - topleft.y);
                System.out.println(width);
                System.out.println(height);
                measureInfoWidth = " Width " + width;
                measureInfoHeight = " Height " + height;
                Log.i(MEASURE, measureInfoWidth);
                Log.i(MEASURE, measureInfoHeight);


                if (width >= 30) {
                    if (width < 90) {
                        //Tells Rio to move closer during Targeting modes
                        status = 1;
                    } else if (width > 310) {
                        // Tells Rio to move further away during Targeting modes
                        status = -1;
                    } else {
                        //Tell Rio not to move robot during Targeting modes
                        status = 0;
                    }
                } else {
                    status = 2;
                }
                        //Finding the middle of the countoured area on the screen
                center.x = (topleft.x+bottomright.x)/2;
                center.y = (topleft.y+bottomright.y)/2;

                center2.x = (topleft2.x+bottomright2.x)/2;
                center2.y = (topleft2.y+bottomright2.y)/2;
                    //Draws the circle at center of contoured object
                Imgproc.circle(mat, center, 5, new Scalar(255, 0, 255),
                        5, Imgproc.LINE_8, 0);
                    //Draws rectangle around the recognized contour
                Imgproc.rectangle(mat, place.tl(), place.br(),
                        new Scalar(255, 0, 0), 10, Imgproc.LINE_8, 0);
                Imgproc.rectangle(mat, place2.tl(), place2.br(),
                        new Scalar(255, 0, 0), 10, Imgproc.LINE_8, 0);
                //System.out.println("X Away: " + Math.abs((mat.size().width / 2) - center.x));
                //System.out.println("Y Away: " + Math.abs((mat.size().height / 2) - center.y));
                System.out.println("Vision Status: " + status);
            }
            catch(Exception e) {
                    //This is
                status = 2;
            }
        }
        catch (Exception e) {
            //In case no contours are found
        }
            //Returns the original image with drawn contours and shape identifiers
        return mat; // Used: contourFrame
    }

    public int getStatus() {return status;}
}
