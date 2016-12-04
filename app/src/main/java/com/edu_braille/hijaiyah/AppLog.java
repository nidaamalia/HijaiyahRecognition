package com.edu_braille.hijaiyah;

/**
 * Created by Nida Amalia on 12/3/2016.
 */
import android.util.Log;

public class AppLog {
    private static final String APP_TAG = "AudioRecorder";

    public static int logString(String message){
        return Log.i(APP_TAG,message);
    }
}