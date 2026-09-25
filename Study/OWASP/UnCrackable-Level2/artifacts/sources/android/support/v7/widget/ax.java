package android.support.v7.widget;

import android.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.a.a;
import android.support.v7.view.menu.h;
import android.support.v7.view.menu.o;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/* JADX INFO: loaded from: classes.dex */
public class ax implements ae {
    Toolbar a;
    CharSequence b;
    Window.Callback c;
    boolean d;
    private int e;
    private View f;
    private View g;
    private Drawable h;
    private Drawable i;
    private Drawable j;
    private boolean k;
    private CharSequence l;
    private CharSequence m;
    private d n;
    private int o;
    private int p;
    private Drawable q;

    public ax(Toolbar toolbar, boolean z) {
        this(toolbar, z, a.h.abc_action_bar_up_description, a.e.abc_ic_ab_back_material);
    }

    public ax(Toolbar toolbar, boolean z, int i, int i2) {
        this.o = 0;
        this.p = 0;
        this.a = toolbar;
        this.b = toolbar.getTitle();
        this.l = toolbar.getSubtitle();
        this.k = this.b != null;
        this.j = toolbar.getNavigationIcon();
        aw awVarA = aw.a(toolbar.getContext(), null, a.j.ActionBar, a.C0011a.actionBarStyle, 0);
        this.q = awVarA.a(a.j.ActionBar_homeAsUpIndicator);
        if (z) {
            CharSequence charSequenceC = awVarA.c(a.j.ActionBar_title);
            if (!TextUtils.isEmpty(charSequenceC)) {
                b(charSequenceC);
            }
            CharSequence charSequenceC2 = awVarA.c(a.j.ActionBar_subtitle);
            if (!TextUtils.isEmpty(charSequenceC2)) {
                c(charSequenceC2);
            }
            Drawable drawableA = awVarA.a(a.j.ActionBar_logo);
            if (drawableA != null) {
                b(drawableA);
            }
            Drawable drawableA2 = awVarA.a(a.j.ActionBar_icon);
            if (drawableA2 != null) {
                a(drawableA2);
            }
            if (this.j == null && this.q != null) {
                c(this.q);
            }
            c(awVarA.a(a.j.ActionBar_displayOptions, 0));
            int iG = awVarA.g(a.j.ActionBar_customNavigationLayout, 0);
            if (iG != 0) {
                a(LayoutInflater.from(this.a.getContext()).inflate(iG, (ViewGroup) this.a, false));
                c(this.e | 16);
            }
            int iF = awVarA.f(a.j.ActionBar_height, 0);
            if (iF > 0) {
                ViewGroup.LayoutParams layoutParams = this.a.getLayoutParams();
                layoutParams.height = iF;
                this.a.setLayoutParams(layoutParams);
            }
            int iD = awVarA.d(a.j.ActionBar_contentInsetStart, -1);
            int iD2 = awVarA.d(a.j.ActionBar_contentInsetEnd, -1);
            if (iD >= 0 || iD2 >= 0) {
                this.a.a(Math.max(iD, 0), Math.max(iD2, 0));
            }
            int iG2 = awVarA.g(a.j.ActionBar_titleTextStyle, 0);
            if (iG2 != 0) {
                this.a.a(this.a.getContext(), iG2);
            }
            int iG3 = awVarA.g(a.j.ActionBar_subtitleTextStyle, 0);
            if (iG3 != 0) {
                this.a.b(this.a.getContext(), iG3);
            }
            int iG4 = awVarA.g(a.j.ActionBar_popupTheme, 0);
            if (iG4 != 0) {
                this.a.setPopupTheme(iG4);
            }
        } else {
            this.e = r();
        }
        awVarA.a();
        e(i);
        this.m = this.a.getNavigationContentDescription();
        this.a.setNavigationOnClickListener(new View.OnClickListener() { // from class: android.support.v7.widget.ax.1
            final android.support.v7.view.menu.a a;

            {
                this.a = new android.support.v7.view.menu.a(ax.this.a.getContext(), 0, R.id.home, 0, 0, ax.this.b);
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (ax.this.c == null || !ax.this.d) {
                    return;
                }
                ax.this.c.onMenuItemSelected(0, this.a);
            }
        });
    }

    private void e(CharSequence charSequence) {
        this.b = charSequence;
        if ((this.e & 8) != 0) {
            this.a.setTitle(charSequence);
        }
    }

    private int r() {
        if (this.a.getNavigationIcon() == null) {
            return 11;
        }
        this.q = this.a.getNavigationIcon();
        return 15;
    }

    private void s() {
        this.a.setLogo((this.e & 2) != 0 ? ((this.e & 1) == 0 || this.i == null) ? this.h : this.i : null);
    }

    private void t() {
        Toolbar toolbar;
        Drawable drawable;
        if ((this.e & 4) != 0) {
            toolbar = this.a;
            drawable = this.j != null ? this.j : this.q;
        } else {
            toolbar = this.a;
            drawable = null;
        }
        toolbar.setNavigationIcon(drawable);
    }

    private void u() {
        if ((this.e & 4) != 0) {
            if (TextUtils.isEmpty(this.m)) {
                this.a.setNavigationContentDescription(this.p);
            } else {
                this.a.setNavigationContentDescription(this.m);
            }
        }
    }

    @Override // android.support.v7.widget.ae
    public android.support.v4.g.s a(final int i, long j) {
        return android.support.v4.g.p.d(this.a).a(i == 0 ? 1.0f : 0.0f).a(j).a(new android.support.v4.g.u() { // from class: android.support.v7.widget.ax.2
            private boolean c = false;

            @Override // android.support.v4.g.u, android.support.v4.g.t
            public void a(View view) {
                ax.this.a.setVisibility(0);
            }

            @Override // android.support.v4.g.u, android.support.v4.g.t
            public void b(View view) {
                if (this.c) {
                    return;
                }
                ax.this.a.setVisibility(i);
            }

            @Override // android.support.v4.g.u, android.support.v4.g.t
            public void c(View view) {
                this.c = true;
            }
        });
    }

    @Override // android.support.v7.widget.ae
    public ViewGroup a() {
        return this.a;
    }

    @Override // android.support.v7.widget.ae
    public void a(int i) {
        a(i != 0 ? android.support.v7.b.a.b.b(b(), i) : null);
    }

    @Override // android.support.v7.widget.ae
    public void a(Drawable drawable) {
        this.h = drawable;
        s();
    }

    @Override // android.support.v7.widget.ae
    public void a(o.a aVar, h.a aVar2) {
        this.a.a(aVar, aVar2);
    }

    @Override // android.support.v7.widget.ae
    public void a(ap apVar) {
        if (this.f != null && this.f.getParent() == this.a) {
            this.a.removeView(this.f);
        }
        this.f = apVar;
        if (apVar == null || this.o != 2) {
            return;
        }
        this.a.addView(this.f, 0);
        Toolbar.b bVar = (Toolbar.b) this.f.getLayoutParams();
        bVar.width = -2;
        bVar.height = -2;
        bVar.a = 8388691;
        apVar.setAllowCollapse(true);
    }

    @Override // android.support.v7.widget.ae
    public void a(Menu menu, o.a aVar) {
        if (this.n == null) {
            this.n = new d(this.a.getContext());
            this.n.a(a.f.action_menu_presenter);
        }
        this.n.a(aVar);
        this.a.a((android.support.v7.view.menu.h) menu, this.n);
    }

    public void a(View view) {
        if (this.g != null && (this.e & 16) != 0) {
            this.a.removeView(this.g);
        }
        this.g = view;
        if (view == null || (this.e & 16) == 0) {
            return;
        }
        this.a.addView(this.g);
    }

    @Override // android.support.v7.widget.ae
    public void a(Window.Callback callback) {
        this.c = callback;
    }

    @Override // android.support.v7.widget.ae
    public void a(CharSequence charSequence) {
        if (this.k) {
            return;
        }
        e(charSequence);
    }

    @Override // android.support.v7.widget.ae
    public void a(boolean z) {
        this.a.setCollapsible(z);
    }

    @Override // android.support.v7.widget.ae
    public Context b() {
        return this.a.getContext();
    }

    @Override // android.support.v7.widget.ae
    public void b(int i) {
        b(i != 0 ? android.support.v7.b.a.b.b(b(), i) : null);
    }

    public void b(Drawable drawable) {
        this.i = drawable;
        s();
    }

    public void b(CharSequence charSequence) {
        this.k = true;
        e(charSequence);
    }

    @Override // android.support.v7.widget.ae
    public void b(boolean z) {
    }

    @Override // android.support.v7.widget.ae
    public void c(int i) {
        CharSequence charSequence;
        Toolbar toolbar;
        int i2 = this.e ^ i;
        this.e = i;
        if (i2 != 0) {
            if ((i2 & 4) != 0) {
                if ((i & 4) != 0) {
                    u();
                }
                t();
            }
            if ((i2 & 3) != 0) {
                s();
            }
            if ((i2 & 8) != 0) {
                if ((i & 8) != 0) {
                    this.a.setTitle(this.b);
                    toolbar = this.a;
                    charSequence = this.l;
                } else {
                    charSequence = null;
                    this.a.setTitle((CharSequence) null);
                    toolbar = this.a;
                }
                toolbar.setSubtitle(charSequence);
            }
            if ((i2 & 16) == 0 || this.g == null) {
                return;
            }
            if ((i & 16) != 0) {
                this.a.addView(this.g);
            } else {
                this.a.removeView(this.g);
            }
        }
    }

    public void c(Drawable drawable) {
        this.j = drawable;
        t();
    }

    public void c(CharSequence charSequence) {
        this.l = charSequence;
        if ((this.e & 8) != 0) {
            this.a.setSubtitle(charSequence);
        }
    }

    @Override // android.support.v7.widget.ae
    public boolean c() {
        return this.a.g();
    }

    @Override // android.support.v7.widget.ae
    public void d() {
        this.a.h();
    }

    @Override // android.support.v7.widget.ae
    public void d(int i) {
        this.a.setVisibility(i);
    }

    public void d(CharSequence charSequence) {
        this.m = charSequence;
        u();
    }

    @Override // android.support.v7.widget.ae
    public CharSequence e() {
        return this.a.getTitle();
    }

    public void e(int i) {
        if (i == this.p) {
            return;
        }
        this.p = i;
        if (TextUtils.isEmpty(this.a.getNavigationContentDescription())) {
            f(this.p);
        }
    }

    @Override // android.support.v7.widget.ae
    public void f() {
        Log.i("ToolbarWidgetWrapper", "Progress display unsupported");
    }

    public void f(int i) {
        d(i == 0 ? null : b().getString(i));
    }

    @Override // android.support.v7.widget.ae
    public void g() {
        Log.i("ToolbarWidgetWrapper", "Progress display unsupported");
    }

    @Override // android.support.v7.widget.ae
    public boolean h() {
        return this.a.a();
    }

    @Override // android.support.v7.widget.ae
    public boolean i() {
        return this.a.b();
    }

    @Override // android.support.v7.widget.ae
    public boolean j() {
        return this.a.c();
    }

    @Override // android.support.v7.widget.ae
    public boolean k() {
        return this.a.d();
    }

    @Override // android.support.v7.widget.ae
    public boolean l() {
        return this.a.e();
    }

    @Override // android.support.v7.widget.ae
    public void m() {
        this.d = true;
    }

    @Override // android.support.v7.widget.ae
    public void n() {
        this.a.f();
    }

    @Override // android.support.v7.widget.ae
    public int o() {
        return this.e;
    }

    @Override // android.support.v7.widget.ae
    public int p() {
        return this.o;
    }

    @Override // android.support.v7.widget.ae
    public Menu q() {
        return this.a.getMenu();
    }
}
