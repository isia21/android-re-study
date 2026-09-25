package android.arch.lifecycle;

import android.arch.lifecycle.a;
import android.arch.lifecycle.c;

/* JADX INFO: loaded from: classes.dex */
class ReflectiveGenericLifecycleObserver implements GenericLifecycleObserver {
    private final Object a;
    private final a.C0001a b;

    ReflectiveGenericLifecycleObserver(Object obj) {
        this.a = obj;
        this.b = a.a.b(this.a.getClass());
    }

    @Override // android.arch.lifecycle.GenericLifecycleObserver
    public void a(e eVar, c.a aVar) {
        this.b.a(eVar, aVar, this.a);
    }
}
