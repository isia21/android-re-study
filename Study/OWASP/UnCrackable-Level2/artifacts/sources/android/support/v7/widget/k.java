package android.support.v7.widget;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.a.a;
import android.util.AttributeSet;
import android.widget.CompoundButton;

/* JADX INFO: loaded from: classes.dex */
class k {
    private final CompoundButton a;
    private ColorStateList b = null;
    private PorterDuff.Mode c = null;
    private boolean d = false;
    private boolean e = false;
    private boolean f;

    k(CompoundButton compoundButton) {
        this.a = compoundButton;
    }

    int a(int i) {
        Drawable drawableA;
        return (Build.VERSION.SDK_INT >= 17 || (drawableA = android.support.v4.widget.c.a(this.a)) == null) ? i : i + drawableA.getIntrinsicWidth();
    }

    ColorStateList a() {
        return this.b;
    }

    void a(ColorStateList colorStateList) {
        this.b = colorStateList;
        this.d = true;
        d();
    }

    void a(PorterDuff.Mode mode) {
        this.c = mode;
        this.e = true;
        d();
    }

    void a(AttributeSet attributeSet, int i) {
        int resourceId;
        TypedArray typedArrayObtainStyledAttributes = this.a.getContext().obtainStyledAttributes(attributeSet, a.j.CompoundButton, i, 0);
        try {
            if (typedArrayObtainStyledAttributes.hasValue(a.j.CompoundButton_android_button) && (resourceId = typedArrayObtainStyledAttributes.getResourceId(a.j.CompoundButton_android_button, 0)) != 0) {
                this.a.setButtonDrawable(android.support.v7.b.a.b.b(this.a.getContext(), resourceId));
            }
            if (typedArrayObtainStyledAttributes.hasValue(a.j.CompoundButton_buttonTint)) {
                android.support.v4.widget.c.a(this.a, typedArrayObtainStyledAttributes.getColorStateList(a.j.CompoundButton_buttonTint));
            }
            if (typedArrayObtainStyledAttributes.hasValue(a.j.CompoundButton_buttonTintMode)) {
                android.support.v4.widget.c.a(this.a, af.a(typedArrayObtainStyledAttributes.getInt(a.j.CompoundButton_buttonTintMode, -1), null));
            }
        } finally {
            typedArrayObtainStyledAttributes.recycle();
        }
    }

    PorterDuff.Mode b() {
        return this.c;
    }

    void c() {
        if (this.f) {
            this.f = false;
        } else {
            this.f = true;
            d();
        }
    }

    void d() {
        Drawable drawableA = android.support.v4.widget.c.a(this.a);
        if (drawableA != null) {
            if (this.d || this.e) {
                Drawable drawableMutate = android.support.v4.b.a.a.f(drawableA).mutate();
                if (this.d) {
                    android.support.v4.b.a.a.a(drawableMutate, this.b);
                }
                if (this.e) {
                    android.support.v4.b.a.a.a(drawableMutate, this.c);
                }
                if (drawableMutate.isStateful()) {
                    drawableMutate.setState(this.a.getDrawableState());
                }
                this.a.setButtonDrawable(drawableMutate);
            }
        }
    }
}
