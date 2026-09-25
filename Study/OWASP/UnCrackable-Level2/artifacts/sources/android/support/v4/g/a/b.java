package android.support.v4.g.a;

import android.os.Build;
import android.os.Bundle;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class b {
    private final Object a;

    static class a extends AccessibilityNodeProvider {
        final b a;

        a(b bVar) {
            this.a = bVar;
        }

        @Override // android.view.accessibility.AccessibilityNodeProvider
        public AccessibilityNodeInfo createAccessibilityNodeInfo(int i) {
            android.support.v4.g.a.a aVarA = this.a.a(i);
            if (aVarA == null) {
                return null;
            }
            return aVarA.a();
        }

        @Override // android.view.accessibility.AccessibilityNodeProvider
        public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(String str, int i) {
            List<android.support.v4.g.a.a> listA = this.a.a(str, i);
            if (listA == null) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            int size = listA.size();
            for (int i2 = 0; i2 < size; i2++) {
                arrayList.add(listA.get(i2).a());
            }
            return arrayList;
        }

        @Override // android.view.accessibility.AccessibilityNodeProvider
        public boolean performAction(int i, int i2, Bundle bundle) {
            return this.a.a(i, i2, bundle);
        }
    }

    /* JADX INFO: renamed from: android.support.v4.g.a.b$b, reason: collision with other inner class name */
    static class C0007b extends a {
        C0007b(b bVar) {
            super(bVar);
        }

        @Override // android.view.accessibility.AccessibilityNodeProvider
        public AccessibilityNodeInfo findFocus(int i) {
            android.support.v4.g.a.a aVarB = this.a.b(i);
            if (aVarB == null) {
                return null;
            }
            return aVarB.a();
        }
    }

    public b() {
        this.a = Build.VERSION.SDK_INT >= 19 ? new C0007b(this) : Build.VERSION.SDK_INT >= 16 ? new a(this) : null;
    }

    public b(Object obj) {
        this.a = obj;
    }

    public android.support.v4.g.a.a a(int i) {
        return null;
    }

    public Object a() {
        return this.a;
    }

    public List<android.support.v4.g.a.a> a(String str, int i) {
        return null;
    }

    public boolean a(int i, int i2, Bundle bundle) {
        return false;
    }

    public android.support.v4.g.a.a b(int i) {
        return null;
    }
}
