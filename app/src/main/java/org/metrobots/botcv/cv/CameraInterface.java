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
        Core.inRange(hsv, new Scalar(46, 112, 100), new Scalar(70, 255, 255), frame);
        Imgproc.medianBlur(frame, frame, 5);

        frame.copyTo(contourFrame);

        Imgproc.findContours(contourFrame, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(mat, contours, -2, new Scalar(0, 0, 255), 5, 8, hierarchy, Imgproc.INTER_MAX, offset);
        /*
        try{
            Rect place = Imgproc.boundingRect(contours.get(0));
            Imgproc.rectangle(mat, place.tl(), place.br(), new Scalar(255,0,0), 10, Imgproc.LINE_8, 0);
        }
        catch(Exception e) {
            System.out.println(e);
        }
*/
        try {
            int max = 0;
            for (int a=0;a<contours.size();a++){

                    double s2 = Imgproc.contourArea(contours.get(a));
                    if (s2 > Imgproc.contourArea(contours.get(max))) {
                        max = a;
                    }
            }

            try{
                Rect place = Imgproc.boundingRect(contours.get(max));
                Point center = new Point();

                Point topleft = place.tl();
                Point bottomright = place.br();

                double width = (bottomright.x - topleft.x);
                if (width < 90){
                    /* Tells rio its position so it can
                    move forward when in targeting mode. */
                }
                if (width >110){
                    // Tells Rio to move closer during Targeting mode

                }

                center.x = (topleft.x+bottomright.x)/2;
                center.y = (topleft.y+bottomright.y)/2;

                Imgproc.circle(mat, center, 25, new Scalar(255,0,255), 10, Imgproc.LINE_8, 0);
                Imgproc.rectangle(mat, place.tl(), place.br(), new Scalar(255,0,0), 10, Imgproc.LINE_8, 0);
            }
            catch(Exception e) {
                System.out.println(e);
            }
        }
        catch (Exception e) {
        }
        return mat;
    }

}
