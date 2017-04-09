package org.metrobots.botcv;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
import org.metrobots.botcv.logger.log2file;

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
    private AlertDialog.Builder builder;

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


        log2file.visionLogInit();
        log2file.log("Hello Vision Log!");

        //setWifiTetheringEnabled(true);


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
        String hsvType = "Screen Center / Contour Center";

        switch (id) {
            // Once the Set USB Tethering is clicked, jump to tethering setting
            case R.id.usb_tethering:
                Log.i(TAG,"Jump to USB Tethering Setting");
                connectUSB();
                break;
            // Once clicked, change layout color
            case R.id.color_change:
                Log.i(TAG,"Change color!");
                if(isAllianceRed) {
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blueAlliance)));
                    isAllianceRed = !isAllianceRed;
                }
                else {
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.redAlliance)));
                    isAllianceRed = !isAllianceRed;
                }
                break;
            // Once clicked, print HSV value of the center point
            case R.id.center_hsv:
                hsvType = "Screen Center";
                double centerHsv[] = new CameraImpl().getCenterHsv();
                String hsvMsg = "H: " + (int)centerHsv[0] + " S: " + (int)centerHsv[1] + " V: " + (int)centerHsv[2];
                Toast.makeText(this, hsvMsg, Toast.LENGTH_LONG).show();     // Print a toast message to the screen

                DispHsv(hsvType, centerHsv);     // Display center hav value in a new dialog window

                break;
            case R.id.contour_center_hsv:
                hsvType = "Contour Center";
                // TODO: Finish getContourCenterHsv() Method
                double Hsv[] = {222,333,444};//new CameraImpl().getContourCenterHsv();
                String contourHsv = "H: " + (int)Hsv[0] + " S: " + (int)Hsv[1] + " V: " + (int)Hsv[2];
                Toast.makeText(this, contourHsv, Toast.LENGTH_LONG).show();     // Print a toast message to the screen

                //DisplayHsvValue(HsvVal);
                DispHsv(hsvType, Hsv);     // Display center hav value in a new dialog window

                break;
        }
        return super.onOptionsItemSelected(item);
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

    public void Toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public PeripheralManager getPeripheralManager() {
        return peripheralManager;
    }

    public CameraImpl getCameraImpl() {
        return cameraImpl;
    }

    /**
     * Display HSV value using dialog window
     *
     * This method creates a dialog window on the screen. The dialog window is built using AlertDialog.Builder.
     * The method also sets dialog's icon, title, and message to be displayed.
     * The dialog will be closed if the button "Got it!" was clicked.
     *
     * The method can be modified to satisfy different situations that require different value output.
     *
     * @param TYPE HSV value type, can be either Screen Center or Contour Center
     * @param hsvVal hsv value array
     */
    private void DispHsv(final String TYPE, final double hsvVal[]) {
        String str = TYPE + " H: " + (int)hsvVal[0] + " S: " +(int)hsvVal[1] + " V: " +(int)hsvVal[2];

        builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setIcon(R.mipmap.ic_launcher);  // Just for fun
        builder.setTitle(R.string.hsv_disp_title);
        builder.setMessage(str);  //R.string.hsv_disp_msg

        // Once click on "Got it!" button, close dialog window
        builder.setPositiveButton(R.string.hsv_disp_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();  // Close dialog
            }
        });

        // Display the dialog window
        AlertDialog dialog=builder.create();
        dialog.show();
    }
}