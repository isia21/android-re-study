package android.support.v4.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public final class aa implements Iterable<Intent> {
    private static final c a;
    private final ArrayList<Intent> b = new ArrayList<>();
    private final Context c;

    public interface a {
        Intent a_();
    }

    static class b extends c {
        b() {
        }
    }

    static class c {
        c() {
        }
    }

    static {
        a = Build.VERSION.SDK_INT >= 16 ? new b() : new c();
    }

    private aa(Context context) {
        this.c = context;
    }

    public static aa a(Context context) {
        return new aa(context);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public aa a(Activity activity) {
        Intent intentA_ = activity instanceof a ? ((a) activity).a_() : null;
        if (intentA_ == null) {
            intentA_ = v.a(activity);
        }
        if (intentA_ != null) {
            ComponentName component = intentA_.getComponent();
            if (component == null) {
                component = intentA_.resolveActivity(this.c.getPackageManager());
            }
            a(component);
            a(intentA_);
        }
        return this;
    }

    public aa a(ComponentName componentName) {
        int size = this.b.size();
        try {
            Context context = this.c;
            while (true) {
                Intent intentA = v.a(context, componentName);
                if (intentA == null) {
                    return this;
                }
                this.b.add(size, intentA);
                context = this.c;
                componentName = intentA.getComponent();
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("TaskStackBuilder", "Bad ComponentName while traversing activity parent metadata");
            throw new IllegalArgumentException(e);
        }
    }

    public aa a(Intent intent) {
        this.b.add(intent);
        return this;
    }

    public void a() {
        a((Bundle) null);
    }

    public void a(Bundle bundle) {
        if (this.b.isEmpty()) {
            throw new IllegalStateException("No intents added to TaskStackBuilder; cannot startActivities");
        }
        Intent[] intentArr = (Intent[]) this.b.toArray(new Intent[this.b.size()]);
        intentArr[0] = new Intent(intentArr[0]).addFlags(268484608);
        if (android.support.v4.a.a.a(this.c, intentArr, bundle)) {
            return;
        }
        Intent intent = new Intent(intentArr[intentArr.length - 1]);
        intent.addFlags(268435456);
        this.c.startActivity(intent);
    }

    @Override // java.lang.Iterable
    @Deprecated
    public Iterator<Intent> iterator() {
        return this.b.iterator();
    }
}
