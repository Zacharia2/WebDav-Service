package com.xinglan.webdavserver.utilities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;

public abstract class ServiceServer extends Service {
    private static final int NOTIFICATION_STARTED_ID = 1;
    public static final int SCREEN_DIM_WAKE_LOCK = 0;
    public static final int WIFI_MODE_FULL = 1;
    public static final int WIFI_MODE_FULL_HIGH_PERF = 2;
    protected IBinder mBinder;
    private PowerManager.WakeLock wakeLock;
    private NotificationManager notifyManager = null;
    private WifiManager.WifiLock wifiLock = null;

    protected abstract IBinder createServiceBinder();

    public ServiceServer() {
        this.mBinder = null;
        this.wakeLock = null;
        this.mBinder = createServiceBinder();
        this.wakeLock = null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.notifyManager = (NotificationManager) getSystemService("notification");
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        this.notifyManager.cancel(1);
        this.notifyManager = null;
        if (this.wakeLock != null) {
            this.wakeLock.release();
            this.wakeLock = null;
        }
        if (this.wifiLock != null) {
            this.wifiLock.release();
            this.wifiLock = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void ensureNotificationCancelled() {
        ((NotificationManager) getSystemService("notification")).cancel(1);
    }

    protected void getScreenLock(String tag) {
        if (tag != null) {
            PowerManager pm = (PowerManager) getSystemService("power");
            this.wakeLock = pm.newWakeLock(6, tag);
            this.wakeLock.acquire();
        }
    }

    protected void getWifiLock(int lockType, String tag) {
        if (tag != null) {
            WifiManager wm = (WifiManager) getSystemService("wifi");
            this.wifiLock = wm.createWifiLock(lockType, tag);
            this.wifiLock.acquire();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleStart(Intent intent, int flags, int startId, int notificationTextId, int notificationIconId, int notificationStartedTitleId, int notificationStartedTextId) {
        String tag = intent.getStringExtra("WakeLockTag");
        int lock = intent.getIntExtra("WakeLock", 0);
        boolean foreground = intent.getBooleanExtra("Foreground", false);
        String ipDetail = intent.getStringExtra("IpDetail");
        String className = intent.getStringExtra("className");
        switch (lock) {
            case 1:
                getWifiLock(1, tag);
                break;
            case 2:
                getWifiLock(3, tag);
                break;
            default:
                getScreenLock(tag);
                break;
        }
        showNotification(className, notificationTextId, notificationIconId, notificationStartedTitleId, notificationStartedTextId, ipDetail, foreground);
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    private void showNotification(String className, int notificationTextId, int notificationIconId, int notificationStartedTitleId, int notificationStartedTextId, String ipDetail, boolean startForeground) {
        Notification notification;
        Intent startIntent = new Intent();
        startIntent.setClassName(this, className);
        startIntent.setFlags(536870912);
        PendingIntent intent = PendingIntent.getActivity(this, 0, startIntent, 268435456);
        if (Build.VERSION.SDK_INT < 16) {
            if (Build.VERSION.SDK_INT >= 11) {
                notification = new Notification.Builder(this).setTicker(getString(notificationTextId)).setContentTitle(getString(notificationStartedTitleId)).setContentText(ipDetail).setSmallIcon(notificationIconId).setContentIntent(intent).setWhen(System.currentTimeMillis()).getNotification();
            } else {
                notification = new Notification(notificationIconId, getString(notificationTextId), System.currentTimeMillis());
                notification.setLatestEventInfo(this, getString(notificationStartedTitleId), ipDetail, intent);
            }
        } else {
            notification = new Notification.BigTextStyle(new Notification.Builder(this).setTicker(getString(notificationTextId)).setContentTitle(getString(notificationStartedTitleId)).setContentText(ipDetail).setSmallIcon(notificationIconId).setContentIntent(intent)).bigText(ipDetail).build();
        }
        notification.flags |= 34;
        if (startForeground) {
            startForeground(1, notification);
        } else {
            this.notifyManager.notify(1, notification);
        }
    }

    public static void updateWidgets(Context context, String updateAction, boolean startedFromWidget, boolean startOk) {
        Intent intentUpdate = new Intent();
        intentUpdate.setAction(updateAction);
        if (Build.VERSION.SDK_INT >= 12) {
            intentUpdate.addFlags(32);
        }
        intentUpdate.putExtra("startedFromWidget", startedFromWidget);
        intentUpdate.putExtra("serviceStartOk", startOk);
        context.sendBroadcast(intentUpdate);
    }
}
