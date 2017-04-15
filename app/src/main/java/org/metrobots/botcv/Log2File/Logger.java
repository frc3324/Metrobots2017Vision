package org.metrobots.botcv.Log2File;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

import android.os.Environment;
import android.util.Log;

/**
 * Created by emilypang on 4/6/17.
 */

public class Logger {
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
        long time = System.currentTimeMillis();
        SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy  HH:mm:SS");

        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/"+directory+"/");
        dir.getParentFile().mkdirs();
        file = new File(dir, formater.format(time) + ".txt"); //was fileName
        file.getParentFile().mkdir();
        file.setReadOnly();
        file.setWritable(false);
    }


    /**
     * Write to file with custom tag.
     *
     * @param data Tag to be written in file row.
     * @param data Data to be written in file row.
     **/

    public static void log(String tag, String data) {
    Log.d(tag,data);
    if(file == null){
    init();
    }

    writeStringToAFile(tag, data, file);

        System.out.println("File successfully saved!");
        Log.i("Tag", "File successfully saved!");
    }


    /**
     * Write to file with default tag: Log2File.
     *
     * @param data Data to be written in file row.
     * /
    public static void log(String data){
    log("Log2File",data);
    }
    /**
     * Methog for writing data to file.
     *
     * @param tag
     * @param data
     * @param file
     */
    private static void writeStringToAFile(String tag, final String data, File file) {
        long time = System.currentTimeMillis();

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:SS");

        BufferedWriter out=null;
        try {
            out = new BufferedWriter(new FileWriter(file,true));
            String lineToWriteToFile = formatter.format(time) + " --- " + "[" + tag + "] " + data + "\n\r";
            out.write(lineToWriteToFile);
            Log.d("Log2File","" + data);

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

}
