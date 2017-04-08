package org.metrobots.botcv.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

import android.os.Environment;
import android.util.Log;


/**
 * Library originally created by Kursulla https://github.com/Kursulla/Log2File
 * Modified by tiger on 17/4/6.
 * Note: the original methods were modified in order to improve data log accessibility (or readability)
 */

public class log2file {
    private static File file;

    /**
     * Init with default settings.
     * Log file will be named "log_file.txt" and will be stored in directory named "Log2File" in root of SDCard.
     */
    public static void init() {
        init("log_file.txt");
    }

    /**
     * Init Log2File with log filename.
     * Directory would be default one "Log2File"
     *
     * @param fileName file name
     */
    public static void init(String fileName) {
        init("Log2File",fileName);
    }

    /**
     * Init Log2File with log filename and directory.
     *
     * @param directory path to directory
     * @param fileName file name
     */
    public static void init(String directory, String fileName) {
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/"+directory+"/");
        dir.getParentFile().mkdirs();
        file = new File(dir, fileName);
        file.getParentFile().mkdir();
        Log.i("LOG2FILE","Init Complete!");
    }

    /**
     * Write to file with custom tag.
     *
     * @param data Tag to be written in file row.
     * @param data Data to be written in file row.
     */
    public static void log(String tag, String data){
        Log.d(tag,data);
        if(file == null){
            init();
        }
        writeStringToAFile(tag, data, file);
        //Log.i("LOG2FILE","Saved!");
    }
    /**
     * Write to file with default tag: Log2File.
     *
     * @param data Data to be written in file row.
     */
    public static void log(String data){
        log("Log2File",data);
    }
    /**
     * Methog for writing data to file.
     *
     * @param tag this is a tag
     * @param data	this is data
     * @param file this is file
     */
    private static void writeStringToAFile(String tag, final String data, File file) {
        long time = System.currentTimeMillis();
        //SimpleDateFormat formater = new SimpleDateFormat("HH:mm:SS  dd/MM/yyyy [Z]");
        SimpleDateFormat formater = new SimpleDateFormat("HH:mm:SS ");

        BufferedWriter out=null;
        try {
            out = new BufferedWriter(new FileWriter(file,true));
            String lineToWriteToFile = formater.format(time) + data +"\n\r";
            out.write(lineToWriteToFile);
            Log.d("Log2File",""+data);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            try {
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    ///////////////////// This is a boundary line ////////////////////////////////////

    /**
     * Following methods added by Yuyi on 04/07/17
     * Initialize Vision Log directory and log file
     */

    /**
     * visionLogInit
     * Initialize vision log file
     * File will be saved under SdCard/Vision Logs/, the file name will be dd-MM-yy HH:mm:SS
     */
    public static void visionLogInit() {
        long time = System.currentTimeMillis();
        SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yy HH:mm:SS");
        formater.format(time);

        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/Vision Logs/");
        dir.getParentFile().mkdirs();
        file = new File(dir, formater.format(time) + ".txt");
        file.getParentFile().mkdir();
        Log.i("LOG2FILE","Init Vision Log Complete!");
    }

    /**
     * visionLog
     * Save log into .text file while print the message using logcat
     * The method save logs to the file initialize previously using visionLogInit()
     * @param msg Message to be saved in the file
     */

    // TODO: reduce logging rate to avoid potential memory problem
    // TODO: add necessary formatter for array value output
    public static void visionLog(String TAG, String msg) {
        log("[ " + TAG + " ]" + "  " + msg);   // Not the log for math!
        Log.i(TAG, msg);
    }

    /**
     * Tutorial: How to call visionLog() method?
     *  1. Find which value you want to save into log file
     *  2. Insert log2file.visionLog(Put TAG here, Put Value here)
     *  3. Run the program, make sure you have initialized vision log in the MainActivity (visionLogInit())
     *
     *  Example
     *  centerHsv[] = {1, 2, 3}
     *  // To save the hsv to the file
     *  log2file(TAG, centerHsv[]);
     *  // Try to make the log neat and clean by apply good typesetting
     */
}
