package com.xinglan.webdavserver.webdavserverpro;

import android.content.Context;
import android.content.Intent;

import com.xinglan.webdavserver.webdavserverlib.StopWebDavServerLib;

public class StopWebDavServerPro extends StopWebDavServerLib {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        onReceiveImplementation(context, intent, WidgetWebDavProIntentReceiver.UpdateStatusAction);
    }
}
