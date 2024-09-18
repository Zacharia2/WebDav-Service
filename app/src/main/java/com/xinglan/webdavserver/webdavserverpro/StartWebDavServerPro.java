package com.xinglan.webdavserver.webdavserverpro;

import android.content.Context;
import android.content.Intent;

import com.xinglan.webdavserver.webdavserverlib.StartWebDavServerLib;

public class StartWebDavServerPro extends StartWebDavServerLib {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        onReceiveImplementation(context, intent, WebdavserverproActivity.class, WidgetWebDavProIntentReceiver.UpdateStatusAction);
    }
}
