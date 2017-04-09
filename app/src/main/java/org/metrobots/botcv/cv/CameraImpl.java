package org.metrobots.botcv.cv;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Path;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.metrobots.botcv.Log2File.Logger;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener;
import org.opencv.core.*;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

/*
 * Interface class for the camera
 * Created by Tasgo on 1/16/16.
 * Last modified: 02/25/17
 */
public class CameraImpl implements CvCameraViewListener {

    //Initializing variables for future use

    private Mat frame = new Mat();
    private Mat hsv = new Mat();
    private Mat hierarchy = new Mat();
    private ArrayList<MatOfPoint> contours = new ArrayList<>();
    private Mat contourFrame = new Mat();
    private Point offset = new Point();
    private int xOffset = 0;
    private int yOffset = 0;
    private int status = 2;

    private int direction = 3;
    private int magnitude = 0;

    private Mat hsv2 = new Mat();
    private long oldMillis = 0;
    private int thresholSet = 0;
    private static final String MEASURE = "Rectangle measurement";
    private String measureInfoWidth = "Width";
    private String measureInfoHeight = "Height";


    private static double relativeDeltaX = 0.0;
    private static double relativeDeltaY = 0.0;
    private static final double PERFECT_X = 360; //temporary values
    private static final double PERFECT_Y = 220;

    private static final String DIRECTION = "Direction";
    private String seeDirection = "The direction";

    private static final String MAGNITUDE = "Magnitude";
    private String seeMagnitude = "The magnitude";


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
        frame.empty();
        hsv.empty();
        hsv2.empty();
        hierarchy.empty();
        contours.clear();

        //Converts the RGB frame to the HSV frame

        Imgproc.cvtColor(mat, hsv, Imgproc.COLOR_BGR2HSV);

        if (Math.abs(System.currentTimeMillis() - oldMillis) > 1000 && thresholSet < 5) {
            oldMillis = System.currentTimeMillis();
            thresholSet++;
        } else if (Math.abs(System.currentTimeMillis() - oldMillis) > 1000) {
            oldMillis = System.currentTimeMillis();
            thresholSet = 0;
        }

        //creates a copy so the original is unaffected
        hsv.copyTo(hsv2);

        //tries to remove random splotches of contours

         Imgproc.bilateralFilter(hsv, hsv2, 3, 10, 10);
        //Imgproc.medianBlur(hsv, hsv, 5); //changed from 3 to 5
        Imgproc.blur(hsv, hsv, new Size(10,10));
        //Imgproc.blur(hsv, hsv, new Size(10,10));
        //Imgproc.GaussianBlur(hsv, hsv, new Size(5, 5), 2);

        //Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));


        //further tries to remove contours

        //further tries to remove contours

        //Imgproc.erode(hsv, hsv, element);
        //Imgproc.dilate(hsv, hsv, element);
//        Imgproc.dilate(hsv, hsv, element);
//        Imgproc.erode(hsv, hsv, element);

        /*int hueLow = (0 * 360) / 255;
        int hueHigh = (255 * 360) /255;

        int satLow = (0 * 100) / 255;
        int satHigh = (100 * 100) / 255;

        int valLow = (175 * 100) / 255;
        int valHigh = (255 * 100) / 255*/

        int goodH = 80;
        int goodS = 93;
        int goodV = 100;
        int thresholdH = 40;  //was 40
        int thresholdS = 10; //was 40
        int thresholdV = 10; //was 30

        goodH = goodH * 255 / 360;
        goodS = goodS * 255 / 100;
        goodV = goodV * 255 / 100;


        int lowH = goodH - thresholdH;
        int lowS = goodS - thresholdS;
        int lowV = goodV - thresholdV;

        int highH = goodH + thresholdH;
        int highS = goodS + thresholdS;
        int highV = goodV + thresholdV;

        //filters out colors outside of the set range of hsv //*100/255
        // Good color is 150, 180, 20
        //Core.inRange(hsv, new Scalar(45, 100, 150), new Scalar(70, 255, 255), frame);
        //Core.inRange(hsv, new Scalar(0, 0, 175), new Scalar(255, 100, 255), frame);
        //Core.inRange(hsv, new Scalar(0, 0, 175), new Scalar(255, 100, 255), frame);
        Core.inRange(hsv, new Scalar(lowH, lowS, lowV), new Scalar(highH, highS, highV), frame);


        //Copies the black and white image to a new frame to prevent messing up the original
        frame.copyTo(contourFrame);

        //Point center1 = new Point(PERFECT_X, PERFECT_Y);

        //Log.i("Center color = ", frame.get((int)center1.x, (int)center1.y)[0] + frame.get((int)center1.x, (int)center1.y)[1] + frame.get((int)center1.x, (int)center1.y)[2] + "");
        int centery = hsv.width() / 2;
        int centerx = hsv.height() / 2;
        double[] hsvvalue = hsv.get(centerx, centery);

        hsvvalue[0] = hsvvalue[0] * 360 / 255;
        hsvvalue[1] = hsvvalue[1] * 100 / 255;
        hsvvalue[2] = hsvvalue[2] * 100 / 255;

        Log.i("H", "" + hsvvalue[0]);
        Log.i("S", "" + hsvvalue[1]);
        Log.i("V", "" + hsvvalue[2]);

        Logger.log("HSV", "H: " + (int)hsvvalue[0] + " S: " + (int)hsvvalue[1] + " V: " + (int)hsvvalue[2]);

        //Finds the contours in the thresholded frame
        Imgproc.findContours(contourFrame, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        //Draws the contours found on the original camera feed
        Imgproc.drawContours(mat, contours, -2,
                new Scalar(0, 0, 255), 5, 8, hierarchy, Imgproc.INTER_MAX, offset);

        //Draws circle at the center of the feed
        Imgproc.circle(mat, new Point((mat.size().width) / 2, (mat.size().height) / 2),
                5, new Scalar(255, 255, 0), 15, Imgproc.LINE_8, 0);

        /*for (int i = 0; i < contours.size(); i++) {
            RotatedRect rect = Imgproc.minAreaRect(new MatOfPoint2f(contours.get(i).toArray()));
            if ((rect.size * rect.width - Imgproc.contourArea(contours.get(i))) < 1000) {
            Imgproc.rectangle(mat, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 255), 10, Imgproc.LINE_8, 0);
        }*/


        try {
            //Creates the max variable
            int max = 0;
            int max2 = 0;
            double maxArea = 0;
            double maxArea2 = 0;
            //Sets up loop to go through all contours
            for (int a = 0; a < contours.size(); a++) { //was <
                //Gets the area of all of the contours
                double s2 = Imgproc.contourArea(contours.get(a));
                //Doesn't look at contours lower than 900
                if (s2 < 900) { //900=arbitrary number
                    continue;
                }
                //Checks the area against the other areas of the contours to find out which is largest
                if (s2 > maxArea) {
                    //Sets largest contour equal to max variable
                    max = a;
                    maxArea = Imgproc.contourArea(contours.get(max));
                } else if (s2 > maxArea2){
                    max2 = a;
                    maxArea2 = Imgproc.contourArea(contours.get(max2));
                }
            }

            //System.out.println("");
            try {
                //System.out.println(Imgproc.contourArea(contours.get(max)));
                //Gets the minimum area vertical(non titlted) rectangle that outlines the contour
                Rect place = Imgproc.boundingRect(contours.get(max));
                Rect place2 = Imgproc.boundingRect(contours.get(max2));


                //System.out.println("Top Left Coordinate: " + place.tl());

                //Creates variable for center point
                Point center = new Point();

                //Sets variable fpr screen center so now we adjust the X and Y axis
                //Point screenCenter = new Point();
                //Creates top left point variable
                Point topleft = place.tl();
                Point topleft2 = place2.tl();

                //Creates bottom right point variable
                Point bottomright = place.br();
                Point bottomright2 = place2.br();

                double distanceFromC = bottomright.y - 360;
                double distanceFromC2 = bottomright2.y - 360;

                if (distanceFromC < distanceFromC2) {
                    place = place2;
                    bottomright = bottomright2;
                    topleft = topleft2;
                    }

                //Finds the width of rectangle
                double width = (bottomright.x - topleft.x);
                double height = (bottomright.y - topleft.y);

                center.x = topleft.x + width/2;
                center.y = topleft.y + height/2;


                relativeDeltaX = (PERFECT_X - center.x);
                relativeDeltaY = (PERFECT_Y - center.y); //print out message in logcat so there is no error if no contour found

                xOffset = (int) relativeDeltaX;
                yOffset = (int) relativeDeltaY;

                //Direction is the course of the robot (robot orientated)
                //if ((Math.abs(relativeDeltaX)) >= 50) { //5 = arbutrary number //was Math.abs((mat.size().width / 2) - center.x
                if (relativeDeltaY < -50) { //was (mat.size().width / 2) - center.x)
                    //Tells the rio to move the robot left
                    direction = -1;
                } else if (relativeDeltaY > 50) { //was (mat.size().width / 2) - center.x)
                        //Tells the rio to move the robot right
                        direction = 1;
                } else {//((Math.abs(relativeDeltaX)) < 50 ) {
                    //Tells the rio that the robot is within the margin of error
                    direction = 0;
                }

                seeDirection = "The direction " + direction;
                Log.i(DIRECTION, seeDirection);
                System.out.print(direction);

                String widthSee = "Direction Thing: " + relativeDeltaX;
                Log.i(DIRECTION, widthSee);

                //Magnitude is the duration of the movement moving forward
               //10 = arbitrary number //was Math.abs((mat.size().width / 2) - center.x
                if (Math.abs(relativeDeltaX) >= 170) { //was (mat.size().width / 2) - center.x)
                    //Tells the rio that the robot needs to move slow speed
                    magnitude = 3;
                } else if (Math.abs(relativeDeltaX) >= 100) { //was (mat.size().width / 2) - center.x)
                    //Tells the rio to that the robot needs to move medium speed
                    magnitude = 2;
                } else if (Math.abs(relativeDeltaX) >= 30) {
                    //Tells the rio to that the robot needs to move high speed
                    magnitude = 1;
                } else {
                    magnitude = 0;
                }

                Logger.log("xOffset", "value: " + relativeDeltaX);
                Logger.log("yOffset", "value: " + relativeDeltaY);

                seeMagnitude = "The magnitude " + magnitude;
                Log.i(MAGNITUDE, seeMagnitude);
                //System.out.println(magnitude);

                //Finding the middle of the countoured area on the screen
                center.x = (topleft.x + bottomright.x) / 2;


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
                center.x = (topleft.x + bottomright.x) / 2;
                center.y = (topleft.y + bottomright.y) / 2;



                //Draws the circle at center of contoured object
                Imgproc.circle(mat, center, 5, new Scalar(255, 0, 255),
                        5, Imgproc.LINE_8, 0);
                //Draws rectangle around the recognized contour
                //Draws the circle at center of contoured object

                //Draws rectangle around the recognized contour
                Imgproc.rectangle(mat, place.tl(), place.br(),
                        new Scalar(255, 0, 0), 10, Imgproc.LINE_8, 0);

            } catch (Exception e) {
                //This is
                //status = 2;
            }
            return mat; //frame
        } catch (Exception e) {
            //In case no contours are found, returns the error status
            status = 2;
        }
        //Returns the original image with drawn contours and shape identifiers
        return mat; //was mat
    }

    public int getStatus() {
        return status;
    }

    public int getMagnitude() {
        return magnitude;
    }

    public int getDirection() {
        return direction;
    }

    public int getXOffset() {return xOffset;}

    public int getYOffset() {return yOffset;}

}

