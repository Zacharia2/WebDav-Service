package com.xinglan.webdavserver.webdavserverpro;

import android.os.Bundle;
import android.widget.LinearLayout;
import com.xinglan.webdavserver.andromilton.BerryUtil;
import com.xinglan.webdavserver.webdavserverlib.MainActivity;
import com.xinglan.webdavserver.webdavserverlib.WebdavAdapter;
import org.taptwo.android.widget.TitleFlowIndicator;
import org.taptwo.android.widget.ViewFlow;

public class WebdavserverproActivity extends MainActivity {
    @Override // com.xinglan.webdavserver.webdavserverlib.MainActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(bin.mt.plus.TranslationData.R.layout.title_layout);
        BerryUtil.init();
        LinearLayout bg = (LinearLayout) findViewById(bin.mt.plus.TranslationData.R.id.titleParent);
        bg.getBackground().setDither(true);
        this.viewFlow = (ViewFlow) findViewById(bin.mt.plus.TranslationData.R.id.viewflow);
        WebdavAdapter adapter = new WebdavAdapter(this, bin.mt.plus.TranslationData.R.layout.main_common, bin.mt.plus.TranslationData.R.layout.about);
        this.viewFlow.setAdapter(adapter);
        TitleFlowIndicator indicator = (TitleFlowIndicator) findViewById(bin.mt.plus.TranslationData.R.id.viewflowindic);
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
        return WidgetWebDavProIntentReceiver.UpdateStatusAction;
    }
}
