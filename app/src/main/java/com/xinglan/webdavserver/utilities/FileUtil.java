package com.xinglan.webdavserver.utilities;

import android.content.Context;
import android.os.Build;
import java.io.File;

public class FileUtil {
    public static String GetSecondaryPrivateDirectory(Context context) {
        File[] externalDirs;
        if (Build.VERSION.SDK_INT < 19 || (externalDirs = context.getExternalFilesDirs(null)) == null) {
            return null;
        }
        if (externalDirs.length >= 1 && externalDirs[0] != null) {
            try {
                if (!externalDirs[0].exists()) {
                    externalDirs[0].mkdirs();
                }
            } catch (Exception e) {
            }
        }
        if (externalDirs.length < 2 || externalDirs[1] == null) {
            return null;
        }
        try {
            if (!externalDirs[1].exists()) {
                externalDirs[1].mkdirs();
            }
        } catch (Exception e2) {
        }
        return externalDirs[1].getPath();
    }
}
