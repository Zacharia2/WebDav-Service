package com.xinglan.webdavserver.utilities;

import java.io.DataOutputStream;

public class Root {
    public static boolean SuAvailable() {
        try {
            Runtime.getRuntime().exec("su");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean RequestRoot() {
        try {
            Process p = Runtime.getRuntime().exec("su");
            Thread.sleep(10000L);
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            os.writeBytes("echo \"Do I have root temp?\" >/system/temporary.txt\n");
            os.writeBytes("exit\n");
            os.flush();
            try {
                p.waitFor();
                int exitCode = p.exitValue();
                if (exitCode != 255) {
                    return true;
                }
                return false;
            } catch (InterruptedException e) {
                return false;
            }
        } catch (Exception e3) {
            return false;
        }
    }
}
