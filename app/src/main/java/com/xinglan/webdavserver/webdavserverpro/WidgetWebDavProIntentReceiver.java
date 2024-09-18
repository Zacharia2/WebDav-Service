package com.xinglan.webdavserver.webdavserverpro;

import android.content.Context;
import android.content.Intent;

import com.xinglan.webdavserver.R;
import com.xinglan.webdavserver.andromilton.BerryUtil;
import com.xinglan.webdavserver.webdavserverlib.WebdavService;
import com.xinglan.webdavserver.widgetutil.WidgetUtilIntentReceiver;

import java.util.Objects;

public class WidgetWebDavProIntentReceiver extends WidgetUtilIntentReceiver {
    public static final String ChangeStatusAction = "com.xinglan.webdavserver.webdavserverpro.widget.action.CHANGE_STATUS";
    public static final String UpdateStatusAction = "com.xinglan.webdavserver.webdavserverpro.widget.action.UPDATE_STATUS";

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        int image = R.mipmap.on;
        BerryUtil server = WebdavService.getServer();
        if (Objects.equals(intent.getAction(), ChangeStatusAction)) {
            if (server != null) {
                image = R.mipmap.off;
            }
            String sendAction = server != null ? "com.xinglan.webdavserver.StopWebDavServerPro" : "com.xinglan.webdavserver.StartWebDavServerPro";
            onWidgetClick(context, WidgetWebDavProProvider.class, ChangeStatusAction, UpdateStatusAction, image, sendAction);
            return;
        }
        if (Objects.equals(intent.getAction(), UpdateStatusAction)) {
            if (server == null) {
                image = R.mipmap.off;
            }
            onWidgetUpdate(context, WidgetWebDavProProvider.class, ChangeStatusAction, image);
            boolean startedFromWidget = intent.getBooleanExtra("startedFromWidget", false);
            boolean serviceStartOk = intent.getBooleanExtra("serviceStartOk", true);
            if (server == null && startedFromWidget && !serviceStartOk) {
                Intent i = new Intent(context, WebdavserverproActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("startFromWidgetError", true);
                context.startActivity(i);
            }
        }
    }
}
