package com.example.yonunca;

import android.app.Application;
import android.content.Context;

public class G  extends Application {
    public static Context context;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
