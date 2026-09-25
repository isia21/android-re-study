package sg.vantagepoint.a;

import android.content.Context;

/* JADX INFO: renamed from: sg.vantagepoint.a.a */
/* JADX INFO: loaded from: classes.dex */
public class Utils_Debug {
    /* JADX INFO: renamed from: a */
    public static boolean IsDebugRun(Context context) {
        return (context.getApplicationContext().getApplicationInfo().flags & 2) != 0;
    }
}
