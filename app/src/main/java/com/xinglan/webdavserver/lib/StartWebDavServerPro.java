package com.xinglan.webdavserver.lib;

import android.content.Context;
import android.content.Intent;

import com.xinglan.webdavserver.activities.WebdavServerProActivity;

public class StartWebDavServerPro extends StartWebDavServerLib {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        onReceiveImplementation(context, intent, WebdavServerProActivity.class, WidgetWebDavProIntentReceiver.UpdateStatusAction);
    }
}
