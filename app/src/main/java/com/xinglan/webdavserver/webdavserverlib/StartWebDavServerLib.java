package com.xinglan.webdavserver.webdavserverlib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.xinglan.webdavserver.andromilton.BerryUtil;
import com.xinglan.webdavserver.utilities.ServiceServer;

public abstract class StartWebDavServerLib extends BroadcastReceiver {
    public void onReceiveImplementation(Context context, Intent intent, Class<?> activity, String widgetUpdateAction) {
        try {
            BerryUtil server = WebdavService.getServer();
            if (server == null) {
                boolean startedFromWidget = intent.getBooleanExtra("startedFromWidget", false);
                boolean result = Helper.StartService(WebdavserverApp.getAppContext(), activity, null, widgetUpdateAction, startedFromWidget);
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
