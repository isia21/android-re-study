package android.support.v7.app;

import android.app.UiModeManager;
import android.content.Context;
import android.support.v7.app.h;
import android.view.ActionMode;
import android.view.Window;

/* JADX INFO: loaded from: classes.dex */
class i extends h {
    private final UiModeManager t;

    class a extends h.a {
        a(Window.Callback callback) {
            super(callback);
        }

        @Override // android.support.v7.app.h.a, android.support.v7.view.i, android.view.Window.Callback
        public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
            return null;
        }

        @Override // android.support.v7.view.i, android.view.Window.Callback
        public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int i) {
            return (i.this.o() && i == 0) ? a(callback) : super.onWindowStartingActionMode(callback, i);
        }
    }

    i(Context context, Window window, d dVar) {
        super(context, window, dVar);
        this.t = (UiModeManager) context.getSystemService("uimode");
    }

    @Override // android.support.v7.app.h, android.support.v7.app.f
    Window.Callback a(Window.Callback callback) {
        return new a(callback);
    }

    @Override // android.support.v7.app.h
    int d(int i) {
        if (i == 0 && this.t.getNightMode() == 0) {
            return -1;
        }
        return super.d(i);
    }
}
