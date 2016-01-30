package org.metrobots.botcv;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.SeekBar;

import org.metrobots.botcv.cv.BotCameraView;
import org.metrobots.botcv.cv.CameraInterface;
import org.metrobots.botcv.cv.LimiterSlider;

public class MainActivity extends AppCompatActivity {
    @SuppressWarnings("FieldCanBeLocal")
    private BotCameraView cameraView;
    private SeekBar hueBarMax;
    private SeekBar satBarMax;
    private LimiterSlider limiterSlider = new LimiterSlider();
    private CameraInterface cameraInterface = new CameraInterface(limiterSlider);

    static {
        System.loadLibrary("opencv_java3");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSliders();

        cameraView = (BotCameraView) findViewById(R.id.cameraView);
        cameraView.setVisibility(SurfaceView.VISIBLE);
        cameraView.setCvCameraViewListener(cameraInterface);
        cameraView.enableView();
    }

    public void initSliders() {
        ((SeekBar) findViewById(R.id.hueBarMin)).setOnSeekBarChangeListener(limiterSlider.minSliders[0]);
        ((SeekBar) findViewById(R.id.satBarMin)).setOnSeekBarChangeListener(limiterSlider.minSliders[1]);
        ((SeekBar) findViewById(R.id.valBarMin)).setOnSeekBarChangeListener(limiterSlider.minSliders[2]);

        ((SeekBar) findViewById(R.id.hueBarMax)).setOnSeekBarChangeListener(limiterSlider.maxSliders[0]);
        ((SeekBar) findViewById(R.id.satBarMax)).setOnSeekBarChangeListener(limiterSlider.maxSliders[1]);
        ((SeekBar) findViewById(R.id.valBarMax)).setOnSeekBarChangeListener(limiterSlider.maxSliders[2]);
    }
}