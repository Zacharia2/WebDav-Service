/*
 * Copyright (C) 2011 Patrik 乲erfeldt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xinglan.webdavserver;

import com.xinglan.webdavserver.viewflow.TitleFlowIndicator;
import com.xinglan.webdavserver.viewflow.ViewFlow;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;

public class TitleViewFlowExample extends Activity {

    private ViewFlow viewFlow;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_title);
        setContentView(R.layout.circle_layout);

        viewFlow = findViewById(R.id.view_flow);
        AndroidVersionAdapter adapter = new AndroidVersionAdapter(this);
        viewFlow.setAdapter(adapter,0);
        TitleFlowIndicator indicator = findViewById(R.id.view_flow_indic);
        indicator.setTitleProvider(adapter);
        viewFlow.setFlowIndicator(indicator);

    }

    /* If your min SDK version is < 8 you need to trigger the onConfigurationChanged in ViewFlow manually, like this */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        viewFlow.onConfigurationChanged(newConfig);
    }

}