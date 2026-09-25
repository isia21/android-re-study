package android.support.v4.g;

import android.os.Build;
import android.support.a.a;
import android.view.ViewGroup;

/* JADX INFO: loaded from: classes.dex */
public final class q {
    static final c a;

    static class a extends c {
        a() {
        }
    }

    static class b extends a {
        b() {
        }

        @Override // android.support.v4.g.q.c
        public boolean a(ViewGroup viewGroup) {
            return viewGroup.isTransitionGroup();
        }
    }

    static class c {
        c() {
        }

        public boolean a(ViewGroup viewGroup) {
            Boolean bool = (Boolean) viewGroup.getTag(a.C0002a.tag_transition_group);
            return ((bool == null || !bool.booleanValue()) && viewGroup.getBackground() == null && p.e(viewGroup) == null) ? false : true;
        }
    }

    static {
        a = Build.VERSION.SDK_INT >= 21 ? new b() : Build.VERSION.SDK_INT >= 18 ? new a() : new c();
    }

    public static boolean a(ViewGroup viewGroup) {
        return a.a(viewGroup);
    }
}
