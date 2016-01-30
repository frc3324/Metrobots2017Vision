package org.metrobots.botcv.cv;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener;
import org.opencv.core.*;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Interface class for the camera
 * Created by Tasgo on 1/16/16.
 */
public class CameraInterface implements CvCameraViewListener {
    private Mat frame = new Mat();
    private Mat gray = new Mat();
    private Mat hsv = new Mat();
    private LimiterSlider limiterSlider;

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
        frame.empty();
        gray.empty();
        hsv.empty();
        float hueMin = 80;
        float hueMax = 155;
        float satMin = 100;
        float satMax = 255;
        float valMin = 100;
        float valMax = 255;
        Scalar min = new Scalar(hueMin, satMin, valMin);
        Scalar max = new Scalar(hueMax, satMax, valMax);
        //Imgproc.cvtColor(mat, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(mat, hsv, Imgproc.COLOR_BGR2HSV_FULL);
        //Imgproc.adaptiveThreshold(gray, frame, 555, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 33, -3);
        //Imgproc.threshold(gray, frame, 225, 255, Imgproc.THRESH_BINARY);
        Core.inRange(hsv, min, max, frame);
        //Imgproc.
        //processMat(mat);
        Core.inRange(mat, limiterSlider.getMin(), limiterSlider.getMax(), frame);

        return frame;
    }
}
