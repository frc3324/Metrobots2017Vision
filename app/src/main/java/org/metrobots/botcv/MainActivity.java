package org.metrobots.botcv;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;

import org.opencv.android.CameraBridgeViewBase.*;
import org.opencv.android.JavaCameraView;
import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity implements CvCameraViewListener {
    private boolean paused = false;
    private CycleRunnable cycleRunnable;
    private JavaCameraView cameraView;

    static {
        System.loadLibrary("opencv_java3");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraView = (JavaCameraView) findViewById(R.id.cameraView);
        cameraView.setVisibility(SurfaceView.VISIBLE);
        cameraView.setCvCameraViewListener(this);
        cameraView.enableView();
    }

    public void onCameraViewStarted(int width, int height) {
    }

    public void onCameraViewStopped() {
    }

    @Override
    public Mat onCameraFrame(Mat inputFrame) {
        return CvUtils.rotate(inputFrame, 90);
    }

    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        return CvUtils.rotate(inputFrame.rgba(), 90);
    }
}