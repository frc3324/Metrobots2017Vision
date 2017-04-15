package org.metrobots.botcv;

import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.graphics.drawable.ColorDrawable;
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
import org.metrobots.botcv.Log2File.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;

import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    @SuppressWarnings("FieldCanBeLocal")
    private BotCameraView cameraView;
    private LimiterSlider limiterSlider = new LimiterSlider();
    private CameraImpl cameraImpl = new CameraImpl();
    private PeripheralManager peripheralManager;

    private final String TAG = "MainActivity";

    private boolean isAllianceRed = false;

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


        Logger.init("Log2File", "Data.txt");
        //Logger.log("Tag", );

        try {
            new CommServer(new CommImpl(this)).start(5800);
            System.out.println("Server started.");
            //Socket sock = new Socket();
            //System.out.println(sock.getRemoteSocketAddress().toString());
        } catch (Exception e) { e.printStackTrace(); }
    }

    /*private void connectUSB() {
        Intent tetherSettings = new Intent();
        tetherSettings.setClassName("com.android.settings", "com.android.settings.TetherSettings");
        tetherSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(tetherSettings);
        System.out.println("Tether success!");


        UsbManager wifiManager = (UsbManager) getSystemService(USB_SERVICE);


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
        }
    }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tethering:
                connectUSB();
                break;
            case R.id.hsv:
                printHSV();
                return true;
            case R.id.alliance:
                switchAlliance();
                return true;
        }
        return false;
    }

    private void connectUSB() {
        Intent tetherSettings = new Intent();
        tetherSettings.setClassName("com.android.settings", "com.android.settings.TetherSettings");
        tetherSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(tetherSettings);
        System.out.println("Tether success!");

        UsbManager wifiManager = (UsbManager) getSystemService(USB_SERVICE);

        Method[] methods = wifiManager.getClass().getDeclaredMethods();
        for (Method method : methods) {
            System.out.println(method.getName().toString());
            if (method.getName().equals("getDeviceList")) {
                try {
                    //method.invoke(wifiManager, null, enable);
                    System.out.println("DeviceList?" + method.invoke(wifiManager, null, 1).toString());
                } catch (Exception ex) {
                    System.out.println("tether fail:" + ex.toString());
                }
                break;
            }
        }
    }

    private void printHSV() {
        toast("Center HSV " + new CameraImpl().getHSV());
    }

    private void switchAlliance() {
        if(isAllianceRed) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blueAlliance)));
            isAllianceRed = !isAllianceRed;
        }
        else {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.redAlliance)));
            isAllianceRed = !isAllianceRed;
        }
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