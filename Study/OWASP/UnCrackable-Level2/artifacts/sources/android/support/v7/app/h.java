package android.support.v7.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.f;
import android.support.v7.view.f;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.view.Window;

/* JADX INFO: loaded from: classes.dex */
class h extends j {
    private int t;
    private boolean u;
    private boolean v;
    private b w;

    class a extends f.a {
        a(Window.Callback callback) {
            super(callback);
        }

        final ActionMode a(ActionMode.Callback callback) {
            f.a aVar = new f.a(h.this.a, callback);
            android.support.v7.view.b bVarB = h.this.b(aVar);
            if (bVarB != null) {
                return aVar.b(bVarB);
            }
            return null;
        }

        @Override // android.support.v7.view.i, android.view.Window.Callback
        public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
            return h.this.o() ? a(callback) : super.onWindowStartingActionMode(callback);
        }
    }

    final class b {
        private o b;
        private boolean c;
        private BroadcastReceiver d;
        private IntentFilter e;

        b(o oVar) {
            this.b = oVar;
            this.c = oVar.a();
        }

        final int a() {
            this.c = this.b.a();
            return this.c ? 2 : 1;
        }

        final void b() {
            boolean zA = this.b.a();
            if (zA != this.c) {
                this.c = zA;
                h.this.i();
            }
        }

        final void c() {
            d();
            if (this.d == null) {
                this.d = new BroadcastReceiver() { // from class: android.support.v7.app.h.b.1
                    @Override // android.content.BroadcastReceiver
                    public void onReceive(Context context, Intent intent) {
                        b.this.b();
                    }
                };
            }
            if (this.e == null) {
                this.e = new IntentFilter();
                this.e.addAction("android.intent.action.TIME_SET");
                this.e.addAction("android.intent.action.TIMEZONE_CHANGED");
                this.e.addAction("android.intent.action.TIME_TICK");
            }
            h.this.a.registerReceiver(this.d, this.e);
        }

        final void d() {
            if (this.d != null) {
                h.this.a.unregisterReceiver(this.d);
                this.d = null;
            }
        }
    }

    h(Context context, Window window, d dVar) {
        super(context, window, dVar);
        this.t = -100;
        this.v = true;
    }

    private boolean h(int i) {
        Resources resources = this.a.getResources();
        Configuration configuration = resources.getConfiguration();
        int i2 = configuration.uiMode & 48;
        int i3 = i == 2 ? 32 : 16;
        if (i2 == i3) {
            return false;
        }
        if (y()) {
            ((Activity) this.a).recreate();
            return true;
        }
        Configuration configuration2 = new Configuration(configuration);
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        configuration2.uiMode = i3 | (configuration2.uiMode & (-49));
        resources.updateConfiguration(configuration2, displayMetrics);
        if (Build.VERSION.SDK_INT >= 26) {
            return true;
        }
        l.a(resources);
        return true;
    }

    private int w() {
        return this.t != -100 ? this.t : j();
    }

    private void x() {
        if (this.w == null) {
            this.w = new b(o.a(this.a));
        }
    }

    private boolean y() {
        if (!this.u || !(this.a instanceof Activity)) {
            return false;
        }
        try {
            return (this.a.getPackageManager().getActivityInfo(new ComponentName(this.a, this.a.getClass()), 0).configChanges & 512) == 0;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("AppCompatDelegate", "Exception while getting ActivityInfo", e);
            return true;
        }
    }

    @Override // android.support.v7.app.j
    View a(View view, String str, Context context, AttributeSet attributeSet) {
        return null;
    }

    @Override // android.support.v7.app.f
    Window.Callback a(Window.Callback callback) {
        return new a(callback);
    }

    @Override // android.support.v7.app.j, android.support.v7.app.e
    public void a(Bundle bundle) {
        super.a(bundle);
        if (bundle == null || this.t != -100) {
            return;
        }
        this.t = bundle.getInt("appcompat:local_night_mode", -100);
    }

    @Override // android.support.v7.app.f, android.support.v7.app.e
    public void c() {
        super.c();
        i();
    }

    @Override // android.support.v7.app.f, android.support.v7.app.e
    public void c(Bundle bundle) {
        super.c(bundle);
        if (this.t != -100) {
            bundle.putInt("appcompat:local_night_mode", this.t);
        }
    }

    int d(int i) {
        if (i == -100) {
            return -1;
        }
        if (i != 0) {
            return i;
        }
        x();
        return this.w.a();
    }

    @Override // android.support.v7.app.j, android.support.v7.app.f, android.support.v7.app.e
    public void d() {
        super.d();
        if (this.w != null) {
            this.w.d();
        }
    }

    @Override // android.support.v7.app.j, android.support.v7.app.f, android.support.v7.app.e
    public void g() {
        super.g();
        if (this.w != null) {
            this.w.d();
        }
    }

    @Override // android.support.v7.app.f, android.support.v7.app.e
    public boolean i() {
        int iW = w();
        int iD = d(iW);
        boolean zH = iD != -1 ? h(iD) : false;
        if (iW == 0) {
            x();
            this.w.c();
        }
        this.u = true;
        return zH;
    }

    @Override // android.support.v7.app.f
    public boolean o() {
        return this.v;
    }
}
