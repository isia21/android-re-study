package android.support.v7.view.menu;

import android.content.Context;
import android.view.MenuItem;
import android.view.SubMenu;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
abstract class c<T> extends d<T> {
    final Context a;
    private Map<android.support.v4.c.a.b, MenuItem> c;
    private Map<android.support.v4.c.a.c, SubMenu> d;

    c(Context context, T t) {
        super(t);
        this.a = context;
    }

    final MenuItem a(MenuItem menuItem) {
        if (!(menuItem instanceof android.support.v4.c.a.b)) {
            return menuItem;
        }
        android.support.v4.c.a.b bVar = (android.support.v4.c.a.b) menuItem;
        if (this.c == null) {
            this.c = new android.support.v4.f.a();
        }
        MenuItem menuItem2 = this.c.get(menuItem);
        if (menuItem2 != null) {
            return menuItem2;
        }
        MenuItem menuItemA = q.a(this.a, bVar);
        this.c.put(bVar, menuItemA);
        return menuItemA;
    }

    final SubMenu a(SubMenu subMenu) {
        if (!(subMenu instanceof android.support.v4.c.a.c)) {
            return subMenu;
        }
        android.support.v4.c.a.c cVar = (android.support.v4.c.a.c) subMenu;
        if (this.d == null) {
            this.d = new android.support.v4.f.a();
        }
        SubMenu subMenu2 = this.d.get(cVar);
        if (subMenu2 != null) {
            return subMenu2;
        }
        SubMenu subMenuA = q.a(this.a, cVar);
        this.d.put(cVar, subMenuA);
        return subMenuA;
    }

    final void a() {
        if (this.c != null) {
            this.c.clear();
        }
        if (this.d != null) {
            this.d.clear();
        }
    }

    final void a(int i) {
        if (this.c == null) {
            return;
        }
        Iterator<android.support.v4.c.a.b> it = this.c.keySet().iterator();
        while (it.hasNext()) {
            if (i == it.next().getGroupId()) {
                it.remove();
            }
        }
    }

    final void b(int i) {
        if (this.c == null) {
            return;
        }
        Iterator<android.support.v4.c.a.b> it = this.c.keySet().iterator();
        while (it.hasNext()) {
            if (i == it.next().getItemId()) {
                it.remove();
                return;
            }
        }
    }
}
