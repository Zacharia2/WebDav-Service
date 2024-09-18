package com.xinglan.webdavserver.intent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xinglan.webdavserver.activities.WebdavServerApp;
import com.xinglan.webdavserver.activities.WebdavServerActivity;
import com.xinglan.webdavserver.corefunc.BerryUtil;
import com.xinglan.webdavserver.corefunc.Helper;
import com.xinglan.webdavserver.utils.ServiceServer;

public class WebDavServerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        onReceiveImplementation(context, intent, WebdavServerActivity.class, WidgetWebDavReceiver.UpdateStatusAction);
        onReceiveImplementation(context, intent, WidgetWebDavReceiver.UpdateStatusAction);
    }

    public void onReceiveImplementation(Context context, Intent intent, Class<?> activity, String widgetUpdateAction) {
        try {
            BerryUtil server = WebdavService.getServer();
            if (server == null) {
                boolean startedFromWidget = intent.getBooleanExtra("startedFromWidget", false);
                boolean result = Helper.StartService(WebdavServerApp.getAppContext(), activity, null, widgetUpdateAction, startedFromWidget);
                if (isOrderedBroadcast()) {
                    setResultCode(result ? 0 : 3);
                }
                if (!result) {
                    ServiceServer.updateWidgets(context, widgetUpdateAction, startedFromWidget, false);
                    return;
                }
                return;
            }
            if (isOrderedBroadcast()) {
                setResultCode(1);
            }
        } catch (Exception ex) {
            if (isOrderedBroadcast()) {
                setResult(2, ex.getMessage(), null);
            }
        }
    }

    public void onReceiveImplementation(Context context, Intent intent, String widgetUpdateAction) {
        try {
            Intent intentServer = new Intent(WebdavServerApp.getAppContext(), WebdavService.class);
            BerryUtil server = WebdavService.getServer();
            if (server != null) {
                if (server.isStarted()) {
                    server.stopBerry();
                }
                context.stopService(intentServer);
                ServiceServer.updateWidgets(context, widgetUpdateAction, false, true);
                if (isOrderedBroadcast()) {
                    setResultCode(0);
                    return;
                }
                return;
            }
            if (isOrderedBroadcast()) {
                setResultCode(1);
            }
        } catch (Exception ex) {
            if (isOrderedBroadcast()) {
                setResult(2, ex.getMessage(), null);
            }
        }
    }


}
