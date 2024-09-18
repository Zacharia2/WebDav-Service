package com.xinglan.webdavserver.webdavserverlib;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

public class WebdavserverApp extends Application {
    private static Context context;

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    @Override // android.app.Application, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override // android.app.Application, android.content.ComponentCallbacks
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override // android.app.Application
    public void onTerminate() {
        super.onTerminate();
    }

    public static Context getAppContext() {
        return context;
    }
}
