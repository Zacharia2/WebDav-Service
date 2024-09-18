package com.xinglan.webdavserver.lib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xinglan.webdavserver.activities.WebdavServerApp;
import com.xinglan.webdavserver.corefunc.BerryUtil;
import com.xinglan.webdavserver.corefunc.Helper;
import com.xinglan.webdavserver.utils.ServiceServer;
import com.xinglan.webdavserver.utils.WebdavService;

public abstract class StartWebDavServerLib extends BroadcastReceiver {
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
}
