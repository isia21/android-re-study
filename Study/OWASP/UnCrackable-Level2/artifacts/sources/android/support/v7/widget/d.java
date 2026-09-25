package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.g.c;
import android.support.v7.a.a;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.view.menu.o;
import android.support.v7.view.menu.p;
import android.support.v7.widget.ActionMenuView;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
class d extends android.support.v7.view.menu.b implements c.a {
    private b A;
    C0015d g;
    e h;
    a i;
    c j;
    final f k;
    int l;
    private Drawable m;
    private boolean n;
    private boolean o;
    private boolean p;
    private int q;
    private int r;
    private int s;
    private boolean t;
    private boolean u;
    private boolean v;
    private boolean w;
    private int x;
    private final SparseBooleanArray y;
    private View z;

    private class a extends android.support.v7.view.menu.n {
        public a(Context context, android.support.v7.view.menu.u uVar, View view) {
            super(context, uVar, view, false, a.C0011a.actionOverflowMenuStyle);
            if (!((android.support.v7.view.menu.j) uVar.getItem()).j()) {
                a(d.this.g == null ? (View) d.this.f : d.this.g);
            }
            a(d.this.k);
        }

        @Override // android.support.v7.view.menu.n
        protected void e() {
            d.this.i = null;
            d.this.l = 0;
            super.e();
        }
    }

    private class b extends ActionMenuItemView.b {
        b() {
        }

        @Override // android.support.v7.view.menu.ActionMenuItemView.b
        public android.support.v7.view.menu.s a() {
            if (d.this.i != null) {
                return d.this.i.b();
            }
            return null;
        }
    }

    private class c implements Runnable {
        private e b;

        public c(e eVar) {
            this.b = eVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (d.this.c != null) {
                d.this.c.f();
            }
            View view = (View) d.this.f;
            if (view != null && view.getWindowToken() != null && this.b.c()) {
                d.this.h = this.b;
            }
            d.this.j = null;
        }
    }

    /* JADX INFO: renamed from: android.support.v7.widget.d$d, reason: collision with other inner class name */
    private class C0015d extends q implements ActionMenuView.a {
        private final float[] b;

        public C0015d(Context context) {
            super(context, null, a.C0011a.actionOverflowButtonStyle);
            this.b = new float[2];
            setClickable(true);
            setFocusable(true);
            setVisibility(0);
            setEnabled(true);
            ay.a(this, getContentDescription());
            setOnTouchListener(new ai(this) { // from class: android.support.v7.widget.d.d.1
                @Override // android.support.v7.widget.ai
                public android.support.v7.view.menu.s a() {
                    if (d.this.h == null) {
                        return null;
                    }
                    return d.this.h.b();
                }

                @Override // android.support.v7.widget.ai
                public boolean b() {
                    d.this.d();
                    return true;
                }

                @Override // android.support.v7.widget.ai
                public boolean c() {
                    if (d.this.j != null) {
                        return false;
                    }
                    d.this.e();
                    return true;
                }
            });
        }

        @Override // android.support.v7.widget.ActionMenuView.a
        public boolean c() {
            return false;
        }

        @Override // android.support.v7.widget.ActionMenuView.a
        public boolean d() {
            return false;
        }

        @Override // android.view.View
        public boolean performClick() {
            if (super.performClick()) {
                return true;
            }
            playSoundEffect(0);
            d.this.d();
            return true;
        }

        @Override // android.widget.ImageView
        protected boolean setFrame(int i, int i2, int i3, int i4) {
            boolean frame = super.setFrame(i, i2, i3, i4);
            Drawable drawable = getDrawable();
            Drawable background = getBackground();
            if (drawable != null && background != null) {
                int width = getWidth();
                int height = getHeight();
                int iMax = Math.max(width, height) / 2;
                int paddingLeft = (width + (getPaddingLeft() - getPaddingRight())) / 2;
                int paddingTop = (height + (getPaddingTop() - getPaddingBottom())) / 2;
                android.support.v4.b.a.a.a(background, paddingLeft - iMax, paddingTop - iMax, paddingLeft + iMax, paddingTop + iMax);
            }
            return frame;
        }
    }

    private class e extends android.support.v7.view.menu.n {
        public e(Context context, android.support.v7.view.menu.h hVar, View view, boolean z) {
            super(context, hVar, view, z, a.C0011a.actionOverflowMenuStyle);
            a(8388613);
            a(d.this.k);
        }

        @Override // android.support.v7.view.menu.n
        protected void e() {
            if (d.this.c != null) {
                d.this.c.close();
            }
            d.this.h = null;
            super.e();
        }
    }

    private class f implements o.a {
        f() {
        }

        @Override // android.support.v7.view.menu.o.a
        public void a(android.support.v7.view.menu.h hVar, boolean z) {
            if (hVar instanceof android.support.v7.view.menu.u) {
                hVar.p().a(false);
            }
            o.a aVarA = d.this.a();
            if (aVarA != null) {
                aVarA.a(hVar, z);
            }
        }

        @Override // android.support.v7.view.menu.o.a
        public boolean a(android.support.v7.view.menu.h hVar) {
            if (hVar == null) {
                return false;
            }
            d.this.l = ((android.support.v7.view.menu.u) hVar).getItem().getItemId();
            o.a aVarA = d.this.a();
            if (aVarA != null) {
                return aVarA.a(hVar);
            }
            return false;
        }
    }

    public d(Context context) {
        super(context, a.g.abc_action_menu_layout, a.g.abc_action_menu_item_layout);
        this.y = new SparseBooleanArray();
        this.k = new f();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private View a(MenuItem menuItem) {
        ViewGroup viewGroup = (ViewGroup) this.f;
        if (viewGroup == null) {
            return null;
        }
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            if ((childAt instanceof p.a) && ((p.a) childAt).getItemData() == menuItem) {
                return childAt;
            }
        }
        return null;
    }

    @Override // android.support.v7.view.menu.b
    public android.support.v7.view.menu.p a(ViewGroup viewGroup) {
        android.support.v7.view.menu.p pVar = this.f;
        android.support.v7.view.menu.p pVarA = super.a(viewGroup);
        if (pVar != pVarA) {
            ((ActionMenuView) pVarA).setPresenter(this);
        }
        return pVarA;
    }

    @Override // android.support.v7.view.menu.b
    public View a(android.support.v7.view.menu.j jVar, View view, ViewGroup viewGroup) {
        View actionView = jVar.getActionView();
        if (actionView == null || jVar.n()) {
            actionView = super.a(jVar, view, viewGroup);
        }
        actionView.setVisibility(jVar.isActionViewExpanded() ? 8 : 0);
        ActionMenuView actionMenuView = (ActionMenuView) viewGroup;
        ViewGroup.LayoutParams layoutParams = actionView.getLayoutParams();
        if (!actionMenuView.checkLayoutParams(layoutParams)) {
            actionView.setLayoutParams(actionMenuView.generateLayoutParams(layoutParams));
        }
        return actionView;
    }

    @Override // android.support.v7.view.menu.b, android.support.v7.view.menu.o
    public void a(Context context, android.support.v7.view.menu.h hVar) {
        super.a(context, hVar);
        Resources resources = context.getResources();
        android.support.v7.view.a aVarA = android.support.v7.view.a.a(context);
        if (!this.p) {
            this.o = aVarA.b();
        }
        if (!this.v) {
            this.q = aVarA.c();
        }
        if (!this.t) {
            this.s = aVarA.a();
        }
        int measuredWidth = this.q;
        if (this.o) {
            if (this.g == null) {
                this.g = new C0015d(this.a);
                if (this.n) {
                    this.g.setImageDrawable(this.m);
                    this.m = null;
                    this.n = false;
                }
                int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
                this.g.measure(iMakeMeasureSpec, iMakeMeasureSpec);
            }
            measuredWidth -= this.g.getMeasuredWidth();
        } else {
            this.g = null;
        }
        this.r = measuredWidth;
        this.x = (int) (resources.getDisplayMetrics().density * 56.0f);
        this.z = null;
    }

    public void a(Configuration configuration) {
        if (!this.t) {
            this.s = android.support.v7.view.a.a(this.b).a();
        }
        if (this.c != null) {
            this.c.b(true);
        }
    }

    public void a(Drawable drawable) {
        if (this.g != null) {
            this.g.setImageDrawable(drawable);
        } else {
            this.n = true;
            this.m = drawable;
        }
    }

    @Override // android.support.v7.view.menu.b, android.support.v7.view.menu.o
    public void a(android.support.v7.view.menu.h hVar, boolean z) {
        f();
        super.a(hVar, z);
    }

    @Override // android.support.v7.view.menu.b
    public void a(android.support.v7.view.menu.j jVar, p.a aVar) {
        aVar.a(jVar, 0);
        ActionMenuItemView actionMenuItemView = (ActionMenuItemView) aVar;
        actionMenuItemView.setItemInvoker((ActionMenuView) this.f);
        if (this.A == null) {
            this.A = new b();
        }
        actionMenuItemView.setPopupCallback(this.A);
    }

    public void a(ActionMenuView actionMenuView) {
        this.f = actionMenuView;
        actionMenuView.a(this.c);
    }

    @Override // android.support.v4.g.c.a
    public void a(boolean z) {
        if (z) {
            super.a((android.support.v7.view.menu.u) null);
        } else if (this.c != null) {
            this.c.a(false);
        }
    }

    @Override // android.support.v7.view.menu.b
    public boolean a(int i, android.support.v7.view.menu.j jVar) {
        return jVar.j();
    }

    @Override // android.support.v7.view.menu.b, android.support.v7.view.menu.o
    public boolean a(android.support.v7.view.menu.u uVar) {
        boolean z = false;
        if (!uVar.hasVisibleItems()) {
            return false;
        }
        android.support.v7.view.menu.u uVar2 = uVar;
        while (uVar2.s() != this.c) {
            uVar2 = (android.support.v7.view.menu.u) uVar2.s();
        }
        View viewA = a(uVar2.getItem());
        if (viewA == null) {
            return false;
        }
        this.l = uVar.getItem().getItemId();
        int size = uVar.size();
        int i = 0;
        while (true) {
            if (i >= size) {
                break;
            }
            MenuItem item = uVar.getItem(i);
            if (item.isVisible() && item.getIcon() != null) {
                z = true;
                break;
            }
            i++;
        }
        this.i = new a(this.b, uVar, viewA);
        this.i.a(z);
        this.i.a();
        super.a(uVar);
        return true;
    }

    @Override // android.support.v7.view.menu.b
    public boolean a(ViewGroup viewGroup, int i) {
        if (viewGroup.getChildAt(i) == this.g) {
            return false;
        }
        return super.a(viewGroup, i);
    }

    @Override // android.support.v7.view.menu.b, android.support.v7.view.menu.o
    public void b(boolean z) {
        super.b(z);
        ((View) this.f).requestLayout();
        boolean z2 = false;
        if (this.c != null) {
            ArrayList<android.support.v7.view.menu.j> arrayListK = this.c.k();
            int size = arrayListK.size();
            for (int i = 0; i < size; i++) {
                android.support.v4.g.c cVarA = arrayListK.get(i).a();
                if (cVarA != null) {
                    cVarA.a(this);
                }
            }
        }
        ArrayList<android.support.v7.view.menu.j> arrayListL = this.c != null ? this.c.l() : null;
        if (this.o && arrayListL != null) {
            int size2 = arrayListL.size();
            if (size2 == 1) {
                z2 = !arrayListL.get(0).isActionViewExpanded();
            } else if (size2 > 0) {
                z2 = true;
            }
        }
        if (z2) {
            if (this.g == null) {
                this.g = new C0015d(this.a);
            }
            ViewGroup viewGroup = (ViewGroup) this.g.getParent();
            if (viewGroup != this.f) {
                if (viewGroup != null) {
                    viewGroup.removeView(this.g);
                }
                ActionMenuView actionMenuView = (ActionMenuView) this.f;
                actionMenuView.addView(this.g, actionMenuView.c());
            }
        } else if (this.g != null && this.g.getParent() == this.f) {
            ((ViewGroup) this.f).removeView(this.g);
        }
        ((ActionMenuView) this.f).setOverflowReserved(this.o);
    }

    @Override // android.support.v7.view.menu.b, android.support.v7.view.menu.o
    public boolean b() {
        ArrayList<android.support.v7.view.menu.j> arrayListI;
        int size;
        int iA;
        int i;
        int i2;
        boolean z;
        d dVar = this;
        int i3 = 0;
        if (dVar.c != null) {
            arrayListI = dVar.c.i();
            size = arrayListI.size();
        } else {
            arrayListI = null;
            size = 0;
        }
        int i4 = dVar.s;
        int i5 = dVar.r;
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        ViewGroup viewGroup = (ViewGroup) dVar.f;
        int i6 = i4;
        int i7 = 0;
        boolean z2 = false;
        int i8 = 0;
        for (int i9 = 0; i9 < size; i9++) {
            android.support.v7.view.menu.j jVar = arrayListI.get(i9);
            if (jVar.l()) {
                i7++;
            } else if (jVar.k()) {
                i8++;
            } else {
                z2 = true;
            }
            if (dVar.w && jVar.isActionViewExpanded()) {
                i6 = 0;
            }
        }
        if (dVar.o && (z2 || i8 + i7 > i6)) {
            i6--;
        }
        int i10 = i6 - i7;
        SparseBooleanArray sparseBooleanArray = dVar.y;
        sparseBooleanArray.clear();
        if (dVar.u) {
            iA = i5 / dVar.x;
            i = ((i5 % dVar.x) / iA) + dVar.x;
        } else {
            iA = 0;
            i = 0;
        }
        int i11 = i5;
        int i12 = 0;
        int i13 = 0;
        while (i12 < size) {
            android.support.v7.view.menu.j jVar2 = arrayListI.get(i12);
            if (jVar2.l()) {
                View viewA = dVar.a(jVar2, dVar.z, viewGroup);
                if (dVar.z == null) {
                    dVar.z = viewA;
                }
                if (dVar.u) {
                    iA -= ActionMenuView.a(viewA, i, iA, iMakeMeasureSpec, i3);
                } else {
                    viewA.measure(iMakeMeasureSpec, iMakeMeasureSpec);
                }
                int measuredWidth = viewA.getMeasuredWidth();
                i11 -= measuredWidth;
                if (i13 != 0) {
                    measuredWidth = i13;
                }
                int groupId = jVar2.getGroupId();
                if (groupId != 0) {
                    z = true;
                    sparseBooleanArray.put(groupId, true);
                } else {
                    z = true;
                }
                jVar2.d(z);
                i2 = size;
                i13 = measuredWidth;
            } else if (jVar2.k()) {
                int groupId2 = jVar2.getGroupId();
                boolean z3 = sparseBooleanArray.get(groupId2);
                boolean z4 = (i10 > 0 || z3) && i11 > 0 && (!dVar.u || iA > 0);
                if (z4) {
                    boolean z5 = z4;
                    View viewA2 = dVar.a(jVar2, dVar.z, viewGroup);
                    i2 = size;
                    if (dVar.z == null) {
                        dVar.z = viewA2;
                    }
                    if (dVar.u) {
                        int iA2 = ActionMenuView.a(viewA2, i, iA, iMakeMeasureSpec, 0);
                        iA -= iA2;
                        if (iA2 == 0) {
                            z5 = false;
                        }
                    } else {
                        viewA2.measure(iMakeMeasureSpec, iMakeMeasureSpec);
                    }
                    int measuredWidth2 = viewA2.getMeasuredWidth();
                    i11 -= measuredWidth2;
                    if (i13 == 0) {
                        i13 = measuredWidth2;
                    }
                    z4 = z5 & (!dVar.u ? i11 + i13 <= 0 : i11 < 0);
                } else {
                    i2 = size;
                }
                if (z4 && groupId2 != 0) {
                    sparseBooleanArray.put(groupId2, true);
                } else if (z3) {
                    sparseBooleanArray.put(groupId2, false);
                    for (int i14 = 0; i14 < i12; i14++) {
                        android.support.v7.view.menu.j jVar3 = arrayListI.get(i14);
                        if (jVar3.getGroupId() == groupId2) {
                            if (jVar3.j()) {
                                i10++;
                            }
                            jVar3.d(false);
                        }
                    }
                }
                if (z4) {
                    i10--;
                }
                jVar2.d(z4);
            } else {
                i2 = size;
                jVar2.d(false);
                i12++;
                size = i2;
                dVar = this;
                i3 = 0;
            }
            i12++;
            size = i2;
            dVar = this;
            i3 = 0;
        }
        return true;
    }

    public Drawable c() {
        if (this.g != null) {
            return this.g.getDrawable();
        }
        if (this.n) {
            return this.m;
        }
        return null;
    }

    public void c(boolean z) {
        this.o = z;
        this.p = true;
    }

    public void d(boolean z) {
        this.w = z;
    }

    public boolean d() {
        if (!this.o || h() || this.c == null || this.f == null || this.j != null || this.c.l().isEmpty()) {
            return false;
        }
        this.j = new c(new e(this.b, this.c, this.g, true));
        ((View) this.f).post(this.j);
        super.a((android.support.v7.view.menu.u) null);
        return true;
    }

    public boolean e() {
        if (this.j != null && this.f != null) {
            ((View) this.f).removeCallbacks(this.j);
            this.j = null;
            return true;
        }
        e eVar = this.h;
        if (eVar == null) {
            return false;
        }
        eVar.d();
        return true;
    }

    public boolean f() {
        return e() | g();
    }

    public boolean g() {
        if (this.i == null) {
            return false;
        }
        this.i.d();
        return true;
    }

    public boolean h() {
        return this.h != null && this.h.f();
    }

    public boolean i() {
        return this.j != null || h();
    }
}
