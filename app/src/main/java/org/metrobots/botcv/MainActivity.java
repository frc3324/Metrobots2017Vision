package org.metrobots.botcv;

import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import net.sf.lipermi.net.Server;

import org.metrobots.botcv.communication.CommImpl;
import org.metrobots.botcv.communication.CommInterface;
import org.metrobots.botcv.communication.CommServer;
import org.metrobots.botcv.cv.BotCameraView;
import org.metrobots.botcv.cv.CameraImpl;
import org.metrobots.botcv.cv.LimiterSlider;
import org.metrobots.botcv.peripheral.PeripheralManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private BotCameraView cameraView;
    private LimiterSlider limiterSlider = new LimiterSlider();
    private CameraImpl cameraImpl = new CameraImpl();
    private PeripheralManager peripheralManager;

    private final String TAG = "MainActivity";

    static {
        System.loadLibrary("opencv_java3");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        peripheralManager = new PeripheralManager(this);

        cameraView = (BotCameraView) findViewById(R.id.cameraView);
        cameraView.setVisibility(SurfaceView.VISIBLE);
        //cameraView.enableFpsMeter();
        cameraView.isHardwareAccelerated();
        cameraView.setCvCameraViewListener(cameraImpl);
        cameraView.enableView();

        //setWifiTetheringEnabled(true);
        //connectUSB();

        try {
            new CommServer(new CommImpl(this)).start(5800);
            System.out.println("Server started.");
            //Socket sock = new Socket();
            //System.out.println(sock.getRemoteSocketAddress().toString());
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void connectUSB() {
        Intent tetherSettings = new Intent();
        tetherSettings.setClassName("com.android.settings", "com.android.settings.TetherSettings");
        tetherSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(tetherSettings);
        System.out.println("Tether success!");


        /*UsbManager wifiManager = (UsbManager) getSystemService(USB_SERVICE);


        Method[] methods = wifiManager.getClass().getDeclaredMethods();
        for (Method method : methods) {
            System.out.println(method.getName().toString());
            if (method.getName().equals("getDeviceList")) {
                try {
                    //method.invoke(wifiManager, null, enable);
                    System.out.println("DeviceList?" + method.invoke(wifiManager, null, enable).toString());
                } catch (Exception ex) {
                    System.out.println("tether fail:" + ex.toString());
                }
                break;
            }
        }*/
    }

    public void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public PeripheralManager getPeripheralManager() {
        return peripheralManager;
    }

    public CameraImpl getCameraImpl() {
        return cameraImpl;
    }
}