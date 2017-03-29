package org.metrobots.botcv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    @SuppressWarnings("FieldCanBeLocal")
    private BotCameraView cameraView;
    private LimiterSlider limiterSlider = new LimiterSlider();
    private CameraImpl cameraImpl = new CameraImpl();
    private PeripheralManager peripheralManager;
    private boolean isAllianceRed = true;
    private final String TAG = "MainActivity";
    // Test
    private double centerHsv[] = new CameraImpl().getCenterHsv();
    //private int hsvVal[3] = new getCenterHsv(); // TODO: Get center HSV value from cameraImpl

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    // Known issue: Surface: getSlotFromBufferLocked  No influence on app, no crash
    public boolean onOptionsItemSelected(MenuItem item) {
        // Get which elements in the menu was clicked
        int id = item.getItemId();
        String TAG = "Menu Item";

        switch (id) {
            case R.id.usb_tethering:
                // TODO: Add jump to usb tethering
                Log.i(TAG,"Jump to USB Tethering Setting");
                break;
            case R.id.color_change:
                Log.i(TAG,"Change color!");
                if(isAllianceRed) {
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blueAlliance)));
                    isAllianceRed = !isAllianceRed;
                }
                else {
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.redAlliance)));
                }
                break;
            case R.id.center_hsv:
                // TODO: Display hsv value to screen
                // TODO: Find a way to display the HSV value
                // Currently, print center hsv value via adb
                Log.d(TAG, "HSV: "+ centerHsv[0]);
            case R.id.hsv_display:

                break;
        }

        return super.onOptionsItemSelected(item);
    }

//    // TODO: Dynamically update title of R.id.hsv_display
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        MenuItem hsvVal = menu.findItem(R.id.hsv_display);
//    }

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

    public void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public PeripheralManager getPeripheralManager() {
        return peripheralManager;
    }

    public CameraImpl getCameraImpl() {
        return cameraImpl;
    }

//    public CameraImpl getCenterHsv() {
//        return centerHsv;
//    }

}