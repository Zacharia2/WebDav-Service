package com.xinglan.webdavserver.webdavserverlib;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.xinglan.webdavserver.utilities.CustomResultReceiver;
import com.xinglan.webdavserver.utilities.Net;
import com.xinglan.webdavserver.utilities.ServiceServer;
import com.xinglan.webdavserver.webdavserverlib.WebdavService;
import com.xinglan.webdavserver.viewflow.ViewFlow;

public abstract class MainActivity extends AboutActivity implements CustomResultReceiver.Receiver {
    private CustomResultReceiver mReceiver;
    protected ViewFlow viewFlow = null;
    private ServiceConnection mConnection = new ServiceConnection() { 
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName className, IBinder service) {
            WebdavService.WebdavBinder serviceBinder = (WebdavService.WebdavBinder) service;
            MainActivity.this.setViewsStarted(serviceBinder.configurationString);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName className) {
            MainActivity.this.setViewsStopped();
        }
    };

    protected abstract String getUpdateWidgetAction();

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDefaultValues(false);
        Intent startIntent = getIntent();
        boolean startFromWidgetError = startIntent.getBooleanExtra("startFromWidgetError", false);
        if (startFromWidgetError) {
            Net.showAlert(this, R.string.ok, -1, R.string.app_name, R.string.notConnect, null, null);
        }
    }

    @Override // com.xinglan.webdavserver.webdavserverlib.AboutActivity
    public void postOnCreate(Bundle savedInstanceState) {
        super.postOnCreate(savedInstanceState);
    }

    @Override // android.app.Activity
    public void onDestroy() {
        super.onDestroy();
    }

    @Override // android.app.Activity
    public void onStart() {
        super.onStart();
        this.mReceiver = new CustomResultReceiver(new Handler());
        this.mReceiver.setReceiver(this);
        if (!bindService(new Intent(this, (Class<?>) WebdavService.class), this.mConnection, 0) || WebdavService.getServer() == null) {
            setViewsStopped();
        }
    }

    @Override // android.app.Activity
    public void onStop() {
        super.onStop();
        unbindService(this.mConnection);
    }

    @Override // android.app.Activity
    public void onResume() {
        super.onResume();
        this.mReceiver.setReceiver(this);
    }

    @Override // android.app.Activity
    public void onPause() {
        super.onPause();
        this.mReceiver.setReceiver(null);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(1);
    }

    public void startStopClickHandler(View view) {
        String updateWidgetAction = getUpdateWidgetAction();
        if (WebdavService.getServer() == null) {
            startClickHandler(updateWidgetAction);
        } else {
            stopClickHandler(updateWidgetAction);
        }
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.viewFlow.onConfigurationChanged(newConfig);
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_preference) {
            Intent intent = new Intent(this, (Class<?>) PrefsActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 82) {
            return super.onKeyDown(keyCode, event);
        }
        startActivityForResult(new Intent(this, (Class<?>) PrefsActivity.class), 0);
        return true;
    }

    @Override // android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                switch (resultCode) {
                    case 2:
                        setDefaultValues(true);
                        return;
                    default:
                        return;
                }
            default:
                return;
        }
    }

    @Override // com.xinglan.webdavserver.utilities.CustomResultReceiver.Receiver
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if (resultCode == 0) {
            Net.showAlert(this, R.string.ok, -1, R.string.app_name, R.string.errorRunServer, null, null);
        }
    }

    protected void setDefaultValues(boolean readAgain) {
        if (readAgain) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit().clear().commit();
        }
        PreferenceManager.setDefaultValues(this, R.xml.preference, readAgain);
    }

    public void startClickHandler(String updateWidgetAction) {
        try {
            Context context = WebdavserverApp.getAppContext();
            if (Helper.StartService(context, getClass(), this.mReceiver, updateWidgetAction, false)) {
                bindService(new Intent(context, (Class<?>) WebdavService.class), this.mConnection, 0);
            } else {
                Net.showAlert(this, R.string.ok, -1, R.string.app_name, R.string.notConnect, null, null);
            }
        } catch (Exception e) {
            Net.showAlert(this, R.string.ok, -1, R.string.app_name, R.string.errorRunServer, null, null);
        }
    }

    public void stopClickHandler(String updateWidgetAction) {
        setViewsStopped();
        Intent intent = new Intent(this, (Class<?>) WebdavService.class);
        stopService(intent);
        ServiceServer.updateWidgets(getApplicationContext(), updateWidgetAction, false, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setViewsStarted(String connectString) {
        ImageView button = (ImageView) findViewById(R.id.imageView1);
        TextView textStartStop = (TextView) findViewById(R.id.textView3);
        TextView text2 = (TextView) findViewById(R.id.textView2);
        text2.setText(connectString);
        button.setImageResource(R.drawable.on);
        textStartStop.setText(R.string.str_stop);
        text2.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setViewsStopped() {
        ImageView button = (ImageView) findViewById(R.id.imageView1);
        TextView textStartStop = (TextView) findViewById(R.id.textView3);
        TextView text2 = (TextView) findViewById(R.id.textView2);
        button.setImageResource(R.drawable.off);
        textStartStop.setText(R.string.str_start);
        text2.setVisibility(4);
    }
}
