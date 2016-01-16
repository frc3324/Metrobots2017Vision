package org.metrobots.botcv;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @SuppressWarnings("FieldCanBeLocal")
    private BotCameraView cameraView;
    private CameraInterface cameraInterface = new CameraInterface();

    static {
        System.loadLibrary("opencv_java3");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraView = (BotCameraView) findViewById(R.id.cameraView);
        cameraView.setVisibility(SurfaceView.VISIBLE);
        cameraView.setCvCameraViewListener(cameraInterface);
        cameraView.enableView();
    }

    public void pause(View view) {
        System.out.println("Pause flipped.");
        cameraInterface.switchPause();
    }
}