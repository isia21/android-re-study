package sg.vantagepoint.a;

import android.os.Build;
import java.io.File;

/* JADX INFO: renamed from: sg.vantagepoint.a.c */
/* JADX INFO: loaded from: classes.dex */
public class Utils_Root {
    /* JADX INFO: renamed from: a */
    public static boolean IsCanAccessSU() {
        for (String str : System.getenv("PATH").split(":")) {
            if (new File(str, "su").exists()) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: renamed from: b */
    public static boolean IsBuildTagsContain_TestKeys() {
        String str = Build.TAGS;
        return str != null && str.contains("test-keys");
    }

    /* JADX INFO: renamed from: c */
    public static boolean IsCanFindRootJunk() {
        for (String str : new String[]{"/system/app/Superuser.apk", "/system/xbin/daemonsu", "/system/etc/init.d/99SuperSUDaemon", "/system/bin/.ext/.su", "/system/etc/.has_su_daemon", "/system/etc/.installed_su_daemon", "/dev/com.koushikdutta.superuser.daemon/"}) {
            if (new File(str).exists()) {
                return true;
            }
        }
        return false;
    }
}
