package com.xinglan.webdavserver.lib;

import android.content.Context;
import android.content.Intent;

public class StopWebDavServerPro extends StopWebDavServerLib {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        onReceiveImplementation(context, intent, WidgetWebDavProIntentReceiver.UpdateStatusAction);
    }
}
