package com.xinglan.webdavserver.webdavserverlib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xinglan.webdavserver.andromilton.BerryUtil;
import com.xinglan.webdavserver.utilities.ServiceServer;

public abstract class StopWebDavServerLib extends BroadcastReceiver {
    public void onReceiveImplementation(Context context, Intent intent, String widgetUpdateAction) {
        try {
            Intent intentServer = new Intent(WebdavserverApp.getAppContext(), WebdavService.class);
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
