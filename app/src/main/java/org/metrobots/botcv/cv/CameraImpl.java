package org.metrobots.botcv.cv;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
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
            // Blur image
        //Imgproc.medianBlur(frame, frame, 9);
            //Color ranges for in the Workshop
        //Core.inRange(hsv, new Scalar(55, 40, 125), new Scalar(70, 255, 255), frame);
        //Core.inRange(hsv, new Scalar(45, 100, 100), new Scalar(70, 200, 200), frame);
            //Color ranges for in the PAST Foundation
        if (Math.abs(System.currentTimeMillis()-oldMillis) > 1000 && thresholSet < 5) {
            oldMillis = System.currentTimeMillis();
            thresholSet++;
        } else if (Math.abs(System.currentTimeMillis()-oldMillis) > 1000){
            oldMillis = System.currentTimeMillis();
            thresholSet = 0;
        }
    /*
        if (thresholSet == 0){
            Core.inRange(hsv, new Scalar(48, 152, 122), new Scalar(70, 255, 255), frame);
        }
        else if (thresholSet == 1){
            Core.inRange(hsv, new Scalar(20, 100, 150), new Scalar(40, 200, 255), frame);
        }
        else if (thresholSet == 2){
            Core.inRange(hsv, new Scalar(55, 125, 150), new Scalar(70, 200, 200), frame);
        }
        else if (thresholSet == 3){
            Core.inRange(hsv, new Scalar(45, 100, 100), new Scalar(70, 200, 255), frame);
        }
        else if (thresholSet == 4){
            Core.inRange(hsv, new Scalar(60, 125, 122), new Scalar(80, 175, 255), frame);
        }
        else if (thresholSet == 5){
            Core.inRange(hsv, new Scalar(48, 50, 122), new Scalar(70, 150, 255), frame);
        }*/

        /*if (thresholSet == 0){
            Core.inRange(hsv, new Scalar(30, 100, 100), new Scalar(70, 200, 255), frame);
        }
        else if (thresholSet == 1){
            Core.inRange(hsv, new Scalar(45, 60, 100), new Scalar(70, 150, 255), frame);
        }
        else if (thresholSet == 2){
            Core.inRange(hsv, new Scalar(45, 100, 100), new Scalar(70, 200, 255), frame);
        }
        else if (thresholSet == 3){
            Core.inRange(hsv, new Scalar(45, 100, 150), new Scalar(70, 200, 255), frame);
        }
        else if (thresholSet == 4){
            Core.inRange(hsv, new Scalar(45, 100, 100), new Scalar(70, 200, 255), frame);
        }
        else if (thresholSet == 5) {
            Core.inRange(hsv, new Scalar(45, 100, 100), new Scalar(70, 200, 255), frame);
        }*/
        //Blurs the black and white image to eliminate all noise
        //Imgproc.bilateralFilter(hsv, hsv, 5, 200, 200);
        hsv.copyTo(hsv2);
        Imgproc.bilateralFilter(hsv, hsv2, 5, 10, 10);
        Imgproc.medianBlur(hsv, hsv, 5);
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(7, 7));
        Imgproc.erode(hsv, hsv, element);
        Imgproc.dilate(hsv, hsv, element);
        Core.inRange(hsv, new Scalar(45, 100, 150), new Scalar(70, 255, 255), frame);

            System.out.println(thresholSet);
        //Core.inRange(hsv, new Scalar(48, 152, 122), new Scalar(70, 255, 255), frame);
        //Core.inRange(hsv, new Scalar(46, 112, 100), new Scalar(70, 255, 255), frame);
            //Bilatersl FIltering
        //mat.copyTo(biMat);
            //Copies the black and white image to a new frame to prevent messing up the original
        frame.copyTo(contourFrame);
            //Finds the contours in the thresholded frame
        Imgproc.findContours(contourFrame, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
            //Draws the contours found on the original camera feed
        Imgproc.drawContours(mat, contours, -2, new Scalar(0, 0, 255), 5, 8, hierarchy, Imgproc.INTER_MAX, offset);

        try {
                //Creates the max variable
            int max = 0;
                //Sets up loop to go through all contuors
            for (int a=0;a<contours.size();a++){
                        //Gets the area of all of the contours
                    double s2 = Imgproc.contourArea(contours.get(a));
                        //Checks the area against the other areas of the contours to find out which is largest
                    if (s2 > Imgproc.contourArea(contours.get(max))) {
                            //Sets largest contour equal to max variable
                        max = a;
                    }
            }

            try{
                    //Gets the minimum area vertical(non titlted) rectangle that outlines the contour
                Rect place = Imgproc.boundingRect(contours.get(max));
                    //Creates variable for center point
                Point center = new Point();
                    //Sets variale fpr screen center so now we adjust the X and Y axis
                Point screenCenter = new Point();
                    //Creates top left point variable
                Point topleft = place.tl();
                    //Cerates bottom right point variable
                Point bottomright = place.br();
                    //Finds the width of rectangle
                double width = (bottomright.x - topleft.x);
                if (width < 90){
                    //Tells Rio to move further away during Targeting modes
                    status = 1;
                }
                else if (width > 110){
                    // Tells Rio to move robot closer during Targeting modes
                    status = -1;
                }
                else{
                    //Tell Rio not to move robot during Targeting modes
                    status = 0;
                }
                    //Finding the middle of the countoured area on the screen
                center.x = (topleft.x+bottomright.x)/2;
                center.y = (topleft.y+bottomright.y)/2;
                    //Draws the circle at center of contoured object
                Imgproc.circle(mat, center, 25, new Scalar(255,0,255), 10, Imgproc.LINE_8, 0);
                    //Draws rectangle around the recognized contour
                Imgproc.rectangle(mat, place.tl(), place.br(), new Scalar(255,0,0), 10, Imgproc.LINE_8, 0);
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
        return mat;
    }

    public int getStatus() {return status;}
}
