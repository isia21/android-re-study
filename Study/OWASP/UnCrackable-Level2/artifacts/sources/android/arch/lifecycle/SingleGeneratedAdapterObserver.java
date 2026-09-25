package android.arch.lifecycle;

import android.arch.lifecycle.c;

/* JADX INFO: loaded from: classes.dex */
public class SingleGeneratedAdapterObserver implements GenericLifecycleObserver {
    private final b a;

    SingleGeneratedAdapterObserver(b bVar) {
        this.a = bVar;
    }

    @Override // android.arch.lifecycle.GenericLifecycleObserver
    public void a(e eVar, c.a aVar) {
        this.a.a(eVar, aVar, false, null);
        this.a.a(eVar, aVar, true, null);
    }
}
