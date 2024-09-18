package com.xinglan.webdavserver.webdavserverpro;

import android.content.Context;
import android.content.Intent;
import com.xinglan.webdavserver.andromilton.BerryUtil;
import com.xinglan.webdavserver.webdavserverlib.WebdavService;
import com.xinglan.webdavserver.widgetutil.WidgetUtilIntentReceiver;

public class WidgetWebDavProIntentReceiver extends WidgetUtilIntentReceiver {
    public static final String ChangeStatusAction = "com.xinglan.webdavserver.webdavserverpro.widget.action.CHANGE_STATUS";
    public static final String UpdateStatusAction = "com.xinglan.webdavserver.webdavserverpro.widget.action.UPDATE_STATUS";

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        int image = bin.mt.plus.TranslationData.R.drawable.on;
        BerryUtil server = WebdavService.getServer();
        if (intent.getAction().equals(ChangeStatusAction)) {
            if (server != null) {
                image = bin.mt.plus.TranslationData.R.drawable.off;
            }
            String sendAction = server != null ? "com.theolivetree.webdavserver.StopWebDavServerPro" : "com.theolivetree.webdavserver.StartWebDavServerPro";
            onWidgetClick(context, WidgetWebDavProProvider.class, ChangeStatusAction, UpdateStatusAction, image, sendAction);
            return;
        }
        if (intent.getAction().equals(UpdateStatusAction)) {
            if (server == null) {
                image = bin.mt.plus.TranslationData.R.drawable.off;
            }
            onWidgetUpdate(context, WidgetWebDavProProvider.class, ChangeStatusAction, image);
            boolean startedFromWidget = intent.getBooleanExtra("startedFromWidget", false);
            boolean serviceStartOk = intent.getBooleanExtra("serviceStartOk", true);
            if (server == null && startedFromWidget && !serviceStartOk) {
                Intent i = new Intent(context, (Class<?>) WebdavserverproActivity.class);
                i.addFlags(268435456);
                i.putExtra("startFromWidgetError", true);
                context.startActivity(i);
            }
        }
    }
}
