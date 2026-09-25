package android.support.v7.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.a.a;
import android.util.AttributeSet;
import android.view.View;

/* JADX INFO: loaded from: classes.dex */
class g {
    private final View a;
    private au d;
    private au e;
    private au f;
    private int c = -1;
    private final l b = l.a();

    g(View view) {
        this.a = view;
    }

    private boolean b(Drawable drawable) {
        if (this.f == null) {
            this.f = new au();
        }
        au auVar = this.f;
        auVar.a();
        ColorStateList colorStateListI = android.support.v4.g.p.i(this.a);
        if (colorStateListI != null) {
            auVar.d = true;
            auVar.a = colorStateListI;
        }
        PorterDuff.Mode modeJ = android.support.v4.g.p.j(this.a);
        if (modeJ != null) {
            auVar.c = true;
            auVar.b = modeJ;
        }
        if (!auVar.d && !auVar.c) {
            return false;
        }
        l.a(drawable, auVar, this.a.getDrawableState());
        return true;
    }

    private boolean d() {
        int i = Build.VERSION.SDK_INT;
        return i > 21 ? this.d != null : i == 21;
    }

    ColorStateList a() {
        if (this.e != null) {
            return this.e.a;
        }
        return null;
    }

    void a(int i) {
        this.c = i;
        b(this.b != null ? this.b.b(this.a.getContext(), i) : null);
        c();
    }

    void a(ColorStateList colorStateList) {
        if (this.e == null) {
            this.e = new au();
        }
        this.e.a = colorStateList;
        this.e.d = true;
        c();
    }

    void a(PorterDuff.Mode mode) {
        if (this.e == null) {
            this.e = new au();
        }
        this.e.b = mode;
        this.e.c = true;
        c();
    }

    void a(Drawable drawable) {
        this.c = -1;
        b((ColorStateList) null);
        c();
    }

    void a(AttributeSet attributeSet, int i) {
        aw awVarA = aw.a(this.a.getContext(), attributeSet, a.j.ViewBackgroundHelper, i, 0);
        try {
            if (awVarA.g(a.j.ViewBackgroundHelper_android_background)) {
                this.c = awVarA.g(a.j.ViewBackgroundHelper_android_background, -1);
                ColorStateList colorStateListB = this.b.b(this.a.getContext(), this.c);
                if (colorStateListB != null) {
                    b(colorStateListB);
                }
            }
            if (awVarA.g(a.j.ViewBackgroundHelper_backgroundTint)) {
                android.support.v4.g.p.a(this.a, awVarA.e(a.j.ViewBackgroundHelper_backgroundTint));
            }
            if (awVarA.g(a.j.ViewBackgroundHelper_backgroundTintMode)) {
                android.support.v4.g.p.a(this.a, af.a(awVarA.a(a.j.ViewBackgroundHelper_backgroundTintMode, -1), null));
            }
        } finally {
            awVarA.a();
        }
    }

    PorterDuff.Mode b() {
        if (this.e != null) {
            return this.e.b;
        }
        return null;
    }

    void b(ColorStateList colorStateList) {
        if (colorStateList != null) {
            if (this.d == null) {
                this.d = new au();
            }
            this.d.a = colorStateList;
            this.d.d = true;
        } else {
            this.d = null;
        }
        c();
    }

    void c() {
        au auVar;
        Drawable background = this.a.getBackground();
        if (background != null) {
            if (d() && b(background)) {
                return;
            }
            if (this.e != null) {
                auVar = this.e;
            } else if (this.d == null) {
                return;
            } else {
                auVar = this.d;
            }
            l.a(background, auVar, this.a.getDrawableState());
        }
    }
}
