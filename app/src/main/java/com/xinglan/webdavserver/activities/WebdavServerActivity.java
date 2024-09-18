package com.xinglan.webdavserver.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.xinglan.webdavserver.R;
import com.xinglan.webdavserver.corefunc.BerryUtil;
import com.xinglan.webdavserver.intent.WidgetWebDavReceiver;
import com.xinglan.webdavserver.widget.viewflow.TitleFlowIndicator;

public class WebdavServerActivity extends MainActivity {
    @Override // com.xinglan.webdavserver.webdavserverlib.MainActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_layout_example);
        BerryUtil.init();
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout bg = findViewById(R.id.titleParent);
        bg.getBackground().setDither(true);
        this.viewFlow = findViewById(R.id.view_flow);
        WebdavAdapter adapter = new WebdavAdapter(this, R.layout.main_common, R.layout.about);
        this.viewFlow.setAdapter(adapter);
        TitleFlowIndicator indicator = findViewById(R.id.view_flow_indic);
        indicator.setTitleProvider(adapter);
        this.viewFlow.setFlowIndicator(indicator);
        super.postOnCreate(savedInstanceState);
    }

    @Override // com.xinglan.webdavserver.webdavserverlib.MainActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
    }

    @Override // com.xinglan.webdavserver.webdavserverlib.MainActivity
    protected String getUpdateWidgetAction() {
        return WidgetWebDavReceiver.UpdateStatusAction;
    }
}
