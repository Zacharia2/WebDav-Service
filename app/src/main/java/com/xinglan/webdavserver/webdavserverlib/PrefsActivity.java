package com.xinglan.webdavserver.webdavserverlib;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import com.xinglan.webdavserver.FileDialog;
import com.xinglan.webdavserver.utilities.FileUtil;

/* loaded from: classes.dex */
public class PrefsActivity extends PreferenceActivity {
    public static final int PREFERENCE_ACTIVITY = 0;
    public static final int PREF_RESULT_NONE = 1;
    public static final int PREF_RESULT_RESET = 2;
    private Preference.OnPreferenceChangeListener onPasswordEnabledChange = new Preference.OnPreferenceChangeListener() { 
        @Override // android.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            try {
                PrefsActivity.this.setEnableDisablePassword(preference, Boolean.valueOf(newValue.toString()).booleanValue());
                return true;
            } catch (Exception e) {
                return true;
            }
        }
    };
    private Preference.OnPreferenceChangeListener onHomeDirChange = new Preference.OnPreferenceChangeListener() { 
        @Override // android.preference.Preference.OnPreferenceChangeListener
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            try {
                Preference homeDirPref = PrefsActivity.this.findPreference(Prefs.PREF_CUSTOMFOLDER);
                homeDirPref.setEnabled(Prefs.isHomeDirCustomDir(WebdavserverApp.getAppContext(), Integer.valueOf(newValue.toString()).intValue()));
                return true;
            } catch (Exception e) {
                return true;
            }
        }
    };

    @Override // android.preference.PreferenceActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        String[] entries;
        String[] entryValues;
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        ListPreference homeDirPref = (ListPreference) findPreference(Prefs.PREF_HOMEDIR);
        if (FileUtil.GetSecondaryPrivateDirectory(WebdavserverApp.getAppContext()) != null) {
            entries = getResources().getStringArray(R.array.home_dir_api19);
            entryValues = getResources().getStringArray(R.array.home_dir_values_api19);
        } else if (Build.VERSION.SDK_INT >= 8) {
            entries = getResources().getStringArray(R.array.home_dir_api8);
            entryValues = getResources().getStringArray(R.array.home_dir_values_api8);
        } else {
            entries = getResources().getStringArray(R.array.home_dir);
            entryValues = getResources().getStringArray(R.array.home_dir_values);
        }
        homeDirPref.setEntries(entries);
        homeDirPref.setEntryValues(entryValues);
        homeDirPref.setOnPreferenceChangeListener(this.onHomeDirChange);
        ListPreference lockPref = (ListPreference) findPreference(Prefs.PREF_LOCK);
        if (Build.VERSION.SDK_INT >= 12) {
            lockPref.setEntries(R.array.lock_api12);
            lockPref.setEntryValues(R.array.lock_values_api12);
        } else {
            lockPref.setEntries(R.array.lock);
            lockPref.setEntryValues(R.array.lock_values);
        }
        Preference customPref = findPreference(Prefs.PREF_RESETPREFS);
        customPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { 
            @Override // android.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference pref) {
                Intent resultIntent = new Intent();
                PrefsActivity.this.setResult(2, resultIntent);
                PrefsActivity.this.finish();
                return false;
            }
        });
        setEnableDisablePassword(findPreference(Prefs.PREF_PASSWORD), Prefs.getPasswordEnabled(WebdavserverApp.getAppContext()));
        setPreferenceChangeListener(Prefs.PREF_PASSWORD, this.onPasswordEnabledChange);
        Preference customFolderPref = findPreference(Prefs.PREF_CUSTOMFOLDER);
        customFolderPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { 
            @Override // android.preference.Preference.OnPreferenceClickListener
            public boolean onPreferenceClick(Preference pref) {
                Intent intent = new Intent(PrefsActivity.this.getBaseContext(), (Class<?>) FileDialog.class);
                intent.putExtra(FileDialog.START_PATH, Prefs.getCustomFolder(WebdavserverApp.getAppContext()));
                intent.putExtra(FileDialog.CAN_SELECT_DIR, true);
                intent.putExtra(FileDialog.SHOW_FOLDERS_ONLY, true);
                PrefsActivity.this.startActivityForResult(intent, 1);
                return false;
            }
        });
        String currentCustomFolder = Prefs.getCustomFolder(WebdavserverApp.getAppContext());
        customFolderPref.setSummary(currentCustomFolder);
        customFolderPref.setEnabled(Prefs.isHomeDirCustomDir(WebdavserverApp.getAppContext()));
    }

    private void setPreferenceChangeListener(String key, Preference.OnPreferenceChangeListener listener) {
        Preference preference = findPreference(key);
        preference.setOnPreferenceChangeListener(this.onPasswordEnabledChange);
    }

    public void setEnableDisablePassword(Preference preference, boolean enabled) {
        Preference userPref = findPreference(Prefs.PREF_USERNAME);
        Preference passPref = findPreference(Prefs.PREF_USERPASS);
        userPref.setEnabled(enabled);
        passPref.setEnabled(enabled);
    }

    @Override // android.preference.PreferenceActivity, android.app.Activity
    public synchronized void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == -1) {
            String filePath = data.getStringExtra(FileDialog.RESULT_PATH);
            Prefs.setCustomFolder(WebdavserverApp.getAppContext(), filePath);
            Preference customFolderPref = findPreference(Prefs.PREF_CUSTOMFOLDER);
            customFolderPref.setSummary(filePath);
        }
    }
}
