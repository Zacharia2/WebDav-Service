package com.xinglan.webdavserver.webdavserverlib;

import android.app.Activity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;

public class AboutActivity extends Activity {
    public void postOnCreate(Bundle savedInstanceState) {
        TextView noteView = (TextView) findViewById(R.id.aboutTextView4);
        Linkify.addLinks(noteView, 2);
        TextView apacheLicense = (TextView) findViewById(R.id.aboutApacheLicense);
        apacheLicense.setText("http://www.apache.org/licenses/LICENSE-2.0");
        Linkify.addLinks(apacheLicense, 1);
    }
}
