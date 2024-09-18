package com.xinglan.webdavserver.utils;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;

import com.xinglan.webdavserver.R;
import com.xinglan.webdavserver.corefunc.BerryUtil;
import com.xinglan.webdavserver.corefunc.Helper;

public class WebdavService extends ServiceServer {
    private static BerryUtil server = null;

    public static class WebdavBinder extends Binder {
        public String configurationString;

        public WebdavBinder() {
            this.configurationString = null;
        }

        public BerryUtil getServer() {
            return WebdavService.server;
        }

        public void setServer(BerryUtil par) {
            WebdavService.server = par;
        }
    }

    @Override // com.xinglan.webdavserver.utilities.ServiceServer
    protected IBinder createServiceBinder() {
        return new WebdavBinder();
    }

    @Override // android.app.Service
    public void onStart(Intent intent, int startId) {
        onStartCommand(intent, 0, startId);
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        ((WebdavBinder) this.mBinder).configurationString = intent.getStringExtra("Data");
        String ip = intent.getStringExtra("sticky_ip");
        int port = intent.getIntExtra("sticky_port", 0);
        String homeDir = intent.getStringExtra("sticky_homeDir");
        boolean passwordEnabled = intent.getBooleanExtra("sticky_passwordEnabled", false);
        String userName = intent.getStringExtra("sticky_userName");
        String userPass = intent.getStringExtra("sticky_userPass");
        ResultReceiver receiver = intent.getParcelableExtra("sticky_resultReceiver");
        String widgetUpdateAction = intent.getStringExtra("widgetUpdateAction");
        boolean startedFromWidget = intent.getBooleanExtra("startedFromWidget", false);
        try {
            server = Helper.StartServerOnly(ip, port, homeDir, passwordEnabled, userName, userPass);
        } catch (Exception e) {
            server = null;
        }
        if (server == null) {
            ensureNotificationCancelled();
            stopSelf();
            if (receiver != null) {
                Bundle b = new Bundle();
                receiver.send(0, b);
            }
            if (widgetUpdateAction != null) {
                updateWidgets(this, widgetUpdateAction, startedFromWidget, false);
            }
            return Service.START_NOT_STICKY;
        }
        if (receiver != null) {
            Bundle b2 = new Bundle();
            receiver.send(1, b2);
        }
        if (widgetUpdateAction != null) {
            updateWidgets(this, widgetUpdateAction, startedFromWidget, true);
        }
        handleStart(intent, flags, startId, R.string.service_started, R.mipmap.on, R.string.notification_started_title, R.string.notification_started_text);
        return Service.START_REDELIVER_INTENT;
    }

    @Override // com.xinglan.webdavserver.utilities.ServiceServer, android.app.Service
    public void onDestroy() {
        super.onDestroy();
        if (server != null) {
            server.stopBerry();
            server = null;
        }
    }

    public static BerryUtil getServer() {
        return server;
    }
}
