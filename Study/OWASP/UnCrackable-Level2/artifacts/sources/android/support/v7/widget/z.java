package android.support.v7.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.a.a.b;
import android.support.v7.a.a;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.widget.TextView;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes.dex */
class z {
    final TextView a;
    private au b;
    private au c;
    private au d;
    private au e;
    private final ac f;
    private int g = 0;
    private Typeface h;
    private boolean i;

    z(TextView textView) {
        this.a = textView;
        this.f = new ac(this.a);
    }

    protected static au a(Context context, l lVar, int i) {
        ColorStateList colorStateListB = lVar.b(context, i);
        if (colorStateListB == null) {
            return null;
        }
        au auVar = new au();
        auVar.d = true;
        auVar.a = colorStateListB;
        return auVar;
    }

    static z a(TextView textView) {
        return Build.VERSION.SDK_INT >= 17 ? new aa(textView) : new z(textView);
    }

    private void a(Context context, aw awVar) {
        String strD;
        Typeface typeface;
        this.g = awVar.a(a.j.TextAppearance_android_textStyle, this.g);
        boolean z = true;
        if (awVar.g(a.j.TextAppearance_android_fontFamily) || awVar.g(a.j.TextAppearance_fontFamily)) {
            this.h = null;
            int i = awVar.g(a.j.TextAppearance_fontFamily) ? a.j.TextAppearance_fontFamily : a.j.TextAppearance_android_fontFamily;
            if (!context.isRestricted()) {
                final WeakReference weakReference = new WeakReference(this.a);
                try {
                    this.h = awVar.a(i, this.g, new b.a() { // from class: android.support.v7.widget.z.1
                        @Override // android.support.v4.a.a.b.a
                        public void a(int i2) {
                        }

                        @Override // android.support.v4.a.a.b.a
                        public void a(Typeface typeface2) {
                            z.this.a((WeakReference<TextView>) weakReference, typeface2);
                        }
                    });
                    if (this.h != null) {
                        z = false;
                    }
                    this.i = z;
                } catch (Resources.NotFoundException | UnsupportedOperationException unused) {
                }
            }
            if (this.h != null || (strD = awVar.d(i)) == null) {
                return;
            }
            this.h = Typeface.create(strD, this.g);
            return;
        }
        if (awVar.g(a.j.TextAppearance_android_typeface)) {
            this.i = false;
            switch (awVar.a(a.j.TextAppearance_android_typeface, 1)) {
                case 1:
                    typeface = Typeface.SANS_SERIF;
                    break;
                case 2:
                    typeface = Typeface.SERIF;
                    break;
                case 3:
                    typeface = Typeface.MONOSPACE;
                    break;
                default:
                    return;
            }
            this.h = typeface;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(WeakReference<TextView> weakReference, Typeface typeface) {
        if (this.i) {
            this.h = typeface;
            TextView textView = weakReference.get();
            if (textView != null) {
                textView.setTypeface(typeface, this.g);
            }
        }
    }

    private void b(int i, float f) {
        this.f.a(i, f);
    }

    void a() {
        if (this.b == null && this.c == null && this.d == null && this.e == null) {
            return;
        }
        Drawable[] compoundDrawables = this.a.getCompoundDrawables();
        a(compoundDrawables[0], this.b);
        a(compoundDrawables[1], this.c);
        a(compoundDrawables[2], this.d);
        a(compoundDrawables[3], this.e);
    }

    void a(int i) {
        this.f.a(i);
    }

    void a(int i, float f) {
        if (android.support.v4.widget.b.a || c()) {
            return;
        }
        b(i, f);
    }

    void a(int i, int i2, int i3, int i4) {
        this.f.a(i, i2, i3, i4);
    }

    void a(Context context, int i) {
        ColorStateList colorStateListE;
        aw awVarA = aw.a(context, i, a.j.TextAppearance);
        if (awVarA.g(a.j.TextAppearance_textAllCaps)) {
            a(awVarA.a(a.j.TextAppearance_textAllCaps, false));
        }
        if (Build.VERSION.SDK_INT < 23 && awVarA.g(a.j.TextAppearance_android_textColor) && (colorStateListE = awVarA.e(a.j.TextAppearance_android_textColor)) != null) {
            this.a.setTextColor(colorStateListE);
        }
        a(context, awVarA);
        awVarA.a();
        if (this.h != null) {
            this.a.setTypeface(this.h, this.g);
        }
    }

    final void a(Drawable drawable, au auVar) {
        if (drawable == null || auVar == null) {
            return;
        }
        l.a(drawable, auVar, this.a.getDrawableState());
    }

    @SuppressLint({"NewApi"})
    void a(AttributeSet attributeSet, int i) {
        ColorStateList colorStateListE;
        ColorStateList colorStateListE2;
        boolean z;
        boolean zA;
        Context context = this.a.getContext();
        l lVarA = l.a();
        aw awVarA = aw.a(context, attributeSet, a.j.AppCompatTextHelper, i, 0);
        int iG = awVarA.g(a.j.AppCompatTextHelper_android_textAppearance, -1);
        if (awVarA.g(a.j.AppCompatTextHelper_android_drawableLeft)) {
            this.b = a(context, lVarA, awVarA.g(a.j.AppCompatTextHelper_android_drawableLeft, 0));
        }
        if (awVarA.g(a.j.AppCompatTextHelper_android_drawableTop)) {
            this.c = a(context, lVarA, awVarA.g(a.j.AppCompatTextHelper_android_drawableTop, 0));
        }
        if (awVarA.g(a.j.AppCompatTextHelper_android_drawableRight)) {
            this.d = a(context, lVarA, awVarA.g(a.j.AppCompatTextHelper_android_drawableRight, 0));
        }
        if (awVarA.g(a.j.AppCompatTextHelper_android_drawableBottom)) {
            this.e = a(context, lVarA, awVarA.g(a.j.AppCompatTextHelper_android_drawableBottom, 0));
        }
        awVarA.a();
        boolean z2 = this.a.getTransformationMethod() instanceof PasswordTransformationMethod;
        boolean z3 = true;
        if (iG != -1) {
            aw awVarA2 = aw.a(context, iG, a.j.TextAppearance);
            if (z2 || !awVarA2.g(a.j.TextAppearance_textAllCaps)) {
                z = false;
                zA = false;
            } else {
                zA = awVarA2.a(a.j.TextAppearance_textAllCaps, false);
                z = true;
            }
            a(context, awVarA2);
            if (Build.VERSION.SDK_INT < 23) {
                ColorStateList colorStateListE3 = awVarA2.g(a.j.TextAppearance_android_textColor) ? awVarA2.e(a.j.TextAppearance_android_textColor) : null;
                colorStateListE2 = awVarA2.g(a.j.TextAppearance_android_textColorHint) ? awVarA2.e(a.j.TextAppearance_android_textColorHint) : null;
                ColorStateList colorStateList = colorStateListE3;
                colorStateListE = awVarA2.g(a.j.TextAppearance_android_textColorLink) ? awVarA2.e(a.j.TextAppearance_android_textColorLink) : null;
                colorStateListE = colorStateList;
            } else {
                colorStateListE = null;
                colorStateListE2 = null;
            }
            awVarA2.a();
        } else {
            colorStateListE = null;
            colorStateListE2 = null;
            z = false;
            zA = false;
        }
        aw awVarA3 = aw.a(context, attributeSet, a.j.TextAppearance, i, 0);
        if (z2 || !awVarA3.g(a.j.TextAppearance_textAllCaps)) {
            z3 = z;
        } else {
            zA = awVarA3.a(a.j.TextAppearance_textAllCaps, false);
        }
        if (Build.VERSION.SDK_INT < 23) {
            if (awVarA3.g(a.j.TextAppearance_android_textColor)) {
                colorStateListE = awVarA3.e(a.j.TextAppearance_android_textColor);
            }
            if (awVarA3.g(a.j.TextAppearance_android_textColorHint)) {
                colorStateListE2 = awVarA3.e(a.j.TextAppearance_android_textColorHint);
            }
            if (awVarA3.g(a.j.TextAppearance_android_textColorLink)) {
                colorStateListE = awVarA3.e(a.j.TextAppearance_android_textColorLink);
            }
        }
        a(context, awVarA3);
        awVarA3.a();
        if (colorStateListE != null) {
            this.a.setTextColor(colorStateListE);
        }
        if (colorStateListE2 != null) {
            this.a.setHintTextColor(colorStateListE2);
        }
        if (colorStateListE != null) {
            this.a.setLinkTextColor(colorStateListE);
        }
        if (!z2 && z3) {
            a(zA);
        }
        if (this.h != null) {
            this.a.setTypeface(this.h, this.g);
        }
        this.f.a(attributeSet, i);
        if (!android.support.v4.widget.b.a || this.f.a() == 0) {
            return;
        }
        int[] iArrE = this.f.e();
        if (iArrE.length > 0) {
            if (this.a.getAutoSizeStepGranularity() != -1.0f) {
                this.a.setAutoSizeTextTypeUniformWithConfiguration(this.f.c(), this.f.d(), this.f.b(), 0);
            } else {
                this.a.setAutoSizeTextTypeUniformWithPresetSizes(iArrE, 0);
            }
        }
    }

    void a(boolean z) {
        this.a.setAllCaps(z);
    }

    void a(boolean z, int i, int i2, int i3, int i4) {
        if (android.support.v4.widget.b.a) {
            return;
        }
        b();
    }

    void a(int[] iArr, int i) {
        this.f.a(iArr, i);
    }

    void b() {
        this.f.f();
    }

    boolean c() {
        return this.f.g();
    }

    int d() {
        return this.f.a();
    }

    int e() {
        return this.f.b();
    }

    int f() {
        return this.f.c();
    }

    int g() {
        return this.f.d();
    }

    int[] h() {
        return this.f.e();
    }
}
