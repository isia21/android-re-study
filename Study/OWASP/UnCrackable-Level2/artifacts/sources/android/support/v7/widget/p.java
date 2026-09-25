package android.support.v7.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.support.v7.a.a;
import android.util.AttributeSet;
import android.widget.ImageView;

/* JADX INFO: loaded from: classes.dex */
public class p {
    private final ImageView a;
    private au b;
    private au c;
    private au d;

    public p(ImageView imageView) {
        this.a = imageView;
    }

    private boolean a(Drawable drawable) {
        if (this.d == null) {
            this.d = new au();
        }
        au auVar = this.d;
        auVar.a();
        ColorStateList colorStateListA = android.support.v4.widget.g.a(this.a);
        if (colorStateListA != null) {
            auVar.d = true;
            auVar.a = colorStateListA;
        }
        PorterDuff.Mode modeB = android.support.v4.widget.g.b(this.a);
        if (modeB != null) {
            auVar.c = true;
            auVar.b = modeB;
        }
        if (!auVar.d && !auVar.c) {
            return false;
        }
        l.a(drawable, auVar, this.a.getDrawableState());
        return true;
    }

    private boolean e() {
        int i = Build.VERSION.SDK_INT;
        return i > 21 ? this.b != null : i == 21;
    }

    public void a(int i) {
        if (i != 0) {
            Drawable drawableB = android.support.v7.b.a.b.b(this.a.getContext(), i);
            if (drawableB != null) {
                af.a(drawableB);
            }
            this.a.setImageDrawable(drawableB);
        } else {
            this.a.setImageDrawable(null);
        }
        d();
    }

    void a(ColorStateList colorStateList) {
        if (this.c == null) {
            this.c = new au();
        }
        this.c.a = colorStateList;
        this.c.d = true;
        d();
    }

    void a(PorterDuff.Mode mode) {
        if (this.c == null) {
            this.c = new au();
        }
        this.c.b = mode;
        this.c.c = true;
        d();
    }

    public void a(AttributeSet attributeSet, int i) {
        int iG;
        aw awVarA = aw.a(this.a.getContext(), attributeSet, a.j.AppCompatImageView, i, 0);
        try {
            Drawable drawable = this.a.getDrawable();
            if (drawable == null && (iG = awVarA.g(a.j.AppCompatImageView_srcCompat, -1)) != -1 && (drawable = android.support.v7.b.a.b.b(this.a.getContext(), iG)) != null) {
                this.a.setImageDrawable(drawable);
            }
            if (drawable != null) {
                af.a(drawable);
            }
            if (awVarA.g(a.j.AppCompatImageView_tint)) {
                android.support.v4.widget.g.a(this.a, awVarA.e(a.j.AppCompatImageView_tint));
            }
            if (awVarA.g(a.j.AppCompatImageView_tintMode)) {
                android.support.v4.widget.g.a(this.a, af.a(awVarA.a(a.j.AppCompatImageView_tintMode, -1), null));
            }
        } finally {
            awVarA.a();
        }
    }

    boolean a() {
        return Build.VERSION.SDK_INT < 21 || !(this.a.getBackground() instanceof RippleDrawable);
    }

    ColorStateList b() {
        if (this.c != null) {
            return this.c.a;
        }
        return null;
    }

    PorterDuff.Mode c() {
        if (this.c != null) {
            return this.c.b;
        }
        return null;
    }

    void d() {
        au auVar;
        Drawable drawable = this.a.getDrawable();
        if (drawable != null) {
            af.a(drawable);
        }
        if (drawable != null) {
            if (e() && a(drawable)) {
                return;
            }
            if (this.c != null) {
                auVar = this.c;
            } else if (this.b == null) {
                return;
            } else {
                auVar = this.b;
            }
            l.a(drawable, auVar, this.a.getDrawableState());
        }
    }
}
