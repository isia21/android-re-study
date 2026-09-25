package android.support.v7.widget;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.DataSetObservable;
import android.os.AsyncTask;
import android.text.TextUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
class e extends DataSetObservable {
    static final String a = "e";
    private static final Object e = new Object();
    private static final Map<String, e> f = new HashMap();
    final Context b;
    final String c;
    boolean d;
    private final Object g;
    private final List<a> h;
    private final List<c> i;
    private Intent j;
    private b k;
    private int l;
    private boolean m;
    private boolean n;
    private boolean o;
    private d p;

    public static final class a implements Comparable<a> {
        public final ResolveInfo a;
        public float b;

        public a(ResolveInfo resolveInfo) {
            this.a = resolveInfo;
        }

        @Override // java.lang.Comparable
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compareTo(a aVar) {
            return Float.floatToIntBits(aVar.b) - Float.floatToIntBits(this.b);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && Float.floatToIntBits(this.b) == Float.floatToIntBits(((a) obj).b);
        }

        public int hashCode() {
            return Float.floatToIntBits(this.b) + 31;
        }

        public String toString() {
            return "[resolveInfo:" + this.a.toString() + "; weight:" + new BigDecimal(this.b) + "]";
        }
    }

    public interface b {
        void a(Intent intent, List<a> list, List<c> list2);
    }

    public static final class c {
        public final ComponentName a;
        public final long b;
        public final float c;

        public c(ComponentName componentName, long j, float f) {
            this.a = componentName;
            this.b = j;
            this.c = f;
        }

        public c(String str, long j, float f) {
            this(ComponentName.unflattenFromString(str), j, f);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            c cVar = (c) obj;
            if (this.a == null) {
                if (cVar.a != null) {
                    return false;
                }
            } else if (!this.a.equals(cVar.a)) {
                return false;
            }
            return this.b == cVar.b && Float.floatToIntBits(this.c) == Float.floatToIntBits(cVar.c);
        }

        public int hashCode() {
            return (((((this.a == null ? 0 : this.a.hashCode()) + 31) * 31) + ((int) (this.b ^ (this.b >>> 32)))) * 31) + Float.floatToIntBits(this.c);
        }

        public String toString() {
            return "[; activity:" + this.a + "; time:" + this.b + "; weight:" + new BigDecimal(this.c) + "]";
        }
    }

    public interface d {
        boolean a(e eVar, Intent intent);
    }

    /* JADX INFO: renamed from: android.support.v7.widget.e$e, reason: collision with other inner class name */
    private final class AsyncTaskC0016e extends AsyncTask<Object, Void, Void> {
        AsyncTaskC0016e() {
        }

        /* JADX WARN: Removed duplicated region for block: B:46:0x0071 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        @Override // android.os.AsyncTask
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public java.lang.Void doInBackground(java.lang.Object... r12) {
            /*
                Method dump skipped, instruction units count: 256
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.e.AsyncTaskC0016e.doInBackground(java.lang.Object[]):java.lang.Void");
        }
    }

    private boolean a(c cVar) {
        boolean zAdd = this.i.add(cVar);
        if (zAdd) {
            this.n = true;
            h();
            c();
            e();
            notifyChanged();
        }
        return zAdd;
    }

    private void c() {
        if (!this.m) {
            throw new IllegalStateException("No preceding call to #readHistoricalData");
        }
        if (this.n) {
            this.n = false;
            if (TextUtils.isEmpty(this.c)) {
                return;
            }
            new AsyncTaskC0016e().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new ArrayList(this.i), this.c);
        }
    }

    private void d() {
        boolean zF = f() | g();
        h();
        if (zF) {
            e();
            notifyChanged();
        }
    }

    private boolean e() {
        if (this.k == null || this.j == null || this.h.isEmpty() || this.i.isEmpty()) {
            return false;
        }
        this.k.a(this.j, this.h, Collections.unmodifiableList(this.i));
        return true;
    }

    private boolean f() {
        if (!this.o || this.j == null) {
            return false;
        }
        this.o = false;
        this.h.clear();
        List<ResolveInfo> listQueryIntentActivities = this.b.getPackageManager().queryIntentActivities(this.j, 0);
        int size = listQueryIntentActivities.size();
        for (int i = 0; i < size; i++) {
            this.h.add(new a(listQueryIntentActivities.get(i)));
        }
        return true;
    }

    private boolean g() {
        if (!this.d || !this.n || TextUtils.isEmpty(this.c)) {
            return false;
        }
        this.d = false;
        this.m = true;
        i();
        return true;
    }

    private void h() {
        int size = this.i.size() - this.l;
        if (size <= 0) {
            return;
        }
        this.n = true;
        for (int i = 0; i < size; i++) {
            this.i.remove(0);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0036, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:?, code lost:
    
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void i() {
        /*
            r9 = this;
            android.content.Context r0 = r9.b     // Catch: java.io.FileNotFoundException -> Lc4
            java.lang.String r1 = r9.c     // Catch: java.io.FileNotFoundException -> Lc4
            java.io.FileInputStream r0 = r0.openFileInput(r1)     // Catch: java.io.FileNotFoundException -> Lc4
            org.xmlpull.v1.XmlPullParser r1 = android.util.Xml.newPullParser()     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84 org.xmlpull.v1.XmlPullParserException -> La0
            java.lang.String r2 = "UTF-8"
            r1.setInput(r0, r2)     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84 org.xmlpull.v1.XmlPullParserException -> La0
            r2 = 0
        L12:
            r3 = 1
            if (r2 == r3) goto L1d
            r4 = 2
            if (r2 == r4) goto L1d
            int r2 = r1.next()     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84 org.xmlpull.v1.XmlPullParserException -> La0
            goto L12
        L1d:
            java.lang.String r2 = "historical-records"
            java.lang.String r4 = r1.getName()     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84 org.xmlpull.v1.XmlPullParserException -> La0
            boolean r2 = r2.equals(r4)     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84 org.xmlpull.v1.XmlPullParserException -> La0
            if (r2 == 0) goto L7a
            java.util.List<android.support.v7.widget.e$c> r2 = r9.i     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84 org.xmlpull.v1.XmlPullParserException -> La0
            r2.clear()     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84 org.xmlpull.v1.XmlPullParserException -> La0
        L2e:
            int r4 = r1.next()     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84 org.xmlpull.v1.XmlPullParserException -> La0
            if (r4 != r3) goto L3b
            if (r0 == 0) goto Lbd
        L36:
            r0.close()     // Catch: java.io.IOException -> Lbd
            goto Lbd
        L3b:
            r5 = 3
            if (r4 == r5) goto L2e
            r5 = 4
            if (r4 != r5) goto L42
            goto L2e
        L42:
            java.lang.String r4 = r1.getName()     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84 org.xmlpull.v1.XmlPullParserException -> La0
            java.lang.String r5 = "historical-record"
            boolean r4 = r5.equals(r4)     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84 org.xmlpull.v1.XmlPullParserException -> La0
            if (r4 == 0) goto L72
            java.lang.String r4 = "activity"
            r5 = 0
            java.lang.String r4 = r1.getAttributeValue(r5, r4)     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84 org.xmlpull.v1.XmlPullParserException -> La0
            java.lang.String r6 = "time"
            java.lang.String r6 = r1.getAttributeValue(r5, r6)     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84 org.xmlpull.v1.XmlPullParserException -> La0
            long r6 = java.lang.Long.parseLong(r6)     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84 org.xmlpull.v1.XmlPullParserException -> La0
            java.lang.String r8 = "weight"
            java.lang.String r5 = r1.getAttributeValue(r5, r8)     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84 org.xmlpull.v1.XmlPullParserException -> La0
            float r5 = java.lang.Float.parseFloat(r5)     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84 org.xmlpull.v1.XmlPullParserException -> La0
            android.support.v7.widget.e$c r8 = new android.support.v7.widget.e$c     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84 org.xmlpull.v1.XmlPullParserException -> La0
            r8.<init>(r4, r6, r5)     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84 org.xmlpull.v1.XmlPullParserException -> La0
            r2.add(r8)     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84 org.xmlpull.v1.XmlPullParserException -> La0
            goto L2e
        L72:
            org.xmlpull.v1.XmlPullParserException r1 = new org.xmlpull.v1.XmlPullParserException     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84 org.xmlpull.v1.XmlPullParserException -> La0
            java.lang.String r2 = "Share records file not well-formed."
            r1.<init>(r2)     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84 org.xmlpull.v1.XmlPullParserException -> La0
            throw r1     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84 org.xmlpull.v1.XmlPullParserException -> La0
        L7a:
            org.xmlpull.v1.XmlPullParserException r1 = new org.xmlpull.v1.XmlPullParserException     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84 org.xmlpull.v1.XmlPullParserException -> La0
            java.lang.String r2 = "Share records file does not start with historical-records tag."
            r1.<init>(r2)     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84 org.xmlpull.v1.XmlPullParserException -> La0
            throw r1     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84 org.xmlpull.v1.XmlPullParserException -> La0
        L82:
            r1 = move-exception
            goto Lbe
        L84:
            r1 = move-exception
            java.lang.String r2 = android.support.v7.widget.e.a     // Catch: java.lang.Throwable -> L82
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L82
            r3.<init>()     // Catch: java.lang.Throwable -> L82
            java.lang.String r4 = "Error reading historical recrod file: "
            r3.append(r4)     // Catch: java.lang.Throwable -> L82
            java.lang.String r4 = r9.c     // Catch: java.lang.Throwable -> L82
            r3.append(r4)     // Catch: java.lang.Throwable -> L82
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Throwable -> L82
            android.util.Log.e(r2, r3, r1)     // Catch: java.lang.Throwable -> L82
            if (r0 == 0) goto Lbd
            goto L36
        La0:
            r1 = move-exception
            java.lang.String r2 = android.support.v7.widget.e.a     // Catch: java.lang.Throwable -> L82
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L82
            r3.<init>()     // Catch: java.lang.Throwable -> L82
            java.lang.String r4 = "Error reading historical recrod file: "
            r3.append(r4)     // Catch: java.lang.Throwable -> L82
            java.lang.String r4 = r9.c     // Catch: java.lang.Throwable -> L82
            r3.append(r4)     // Catch: java.lang.Throwable -> L82
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Throwable -> L82
            android.util.Log.e(r2, r3, r1)     // Catch: java.lang.Throwable -> L82
            if (r0 == 0) goto Lbd
            goto L36
        Lbd:
            return
        Lbe:
            if (r0 == 0) goto Lc3
            r0.close()     // Catch: java.io.IOException -> Lc3
        Lc3:
            throw r1
        Lc4:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.e.i():void");
    }

    public int a() {
        int size;
        synchronized (this.g) {
            d();
            size = this.h.size();
        }
        return size;
    }

    public int a(ResolveInfo resolveInfo) {
        synchronized (this.g) {
            d();
            List<a> list = this.h;
            int size = list.size();
            for (int i = 0; i < size; i++) {
                if (list.get(i).a == resolveInfo) {
                    return i;
                }
            }
            return -1;
        }
    }

    public ResolveInfo a(int i) {
        ResolveInfo resolveInfo;
        synchronized (this.g) {
            d();
            resolveInfo = this.h.get(i).a;
        }
        return resolveInfo;
    }

    public Intent b(int i) {
        synchronized (this.g) {
            if (this.j == null) {
                return null;
            }
            d();
            a aVar = this.h.get(i);
            ComponentName componentName = new ComponentName(aVar.a.activityInfo.packageName, aVar.a.activityInfo.name);
            Intent intent = new Intent(this.j);
            intent.setComponent(componentName);
            if (this.p != null) {
                if (this.p.a(this, new Intent(intent))) {
                    return null;
                }
            }
            a(new c(componentName, System.currentTimeMillis(), 1.0f));
            return intent;
        }
    }

    public ResolveInfo b() {
        synchronized (this.g) {
            d();
            if (this.h.isEmpty()) {
                return null;
            }
            return this.h.get(0).a;
        }
    }

    public void c(int i) {
        synchronized (this.g) {
            d();
            a aVar = this.h.get(i);
            a aVar2 = this.h.get(0);
            a(new c(new ComponentName(aVar.a.activityInfo.packageName, aVar.a.activityInfo.name), System.currentTimeMillis(), aVar2 != null ? (aVar2.b - aVar.b) + 5.0f : 1.0f));
        }
    }
}
