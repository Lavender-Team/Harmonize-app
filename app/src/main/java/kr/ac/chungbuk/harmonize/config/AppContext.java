package kr.ac.chungbuk.harmonize.config;

import android.app.Application;
import android.content.Context;

public class AppContext extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext.context = getApplicationContext();
    }

    public static Context getAppContext() {
        System.out.println("getAppContext");
        System.out.println(AppContext.context);
        return AppContext.context;
    }
}
