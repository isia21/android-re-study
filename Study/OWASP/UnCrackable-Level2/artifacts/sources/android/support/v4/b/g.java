package android.support.v4.b;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.CancellationSignal;
import android.support.v4.a.a.a;
import android.support.v4.b.c;
import android.support.v4.e.b;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
class g implements c.a {

    private interface a<T> {
        boolean a(T t);

        int b(T t);
    }

    g() {
    }

    private a.c a(a.b bVar, int i) {
        return (a.c) a(bVar.a(), i, new a<a.c>() { // from class: android.support.v4.b.g.2
            @Override // android.support.v4.b.g.a
            /* JADX INFO: renamed from: a, reason: avoid collision after fix types in other method and merged with bridge method [inline-methods] */
            public int b(a.c cVar) {
                return cVar.b();
            }

            @Override // android.support.v4.b.g.a
            /* JADX INFO: renamed from: b, reason: avoid collision after fix types in other method and merged with bridge method [inline-methods] */
            public boolean a(a.c cVar) {
                return cVar.c();
            }
        });
    }

    private static <T> T a(T[] tArr, int i, a<T> aVar) {
        int i2 = (i & 1) == 0 ? 400 : 700;
        boolean z = (i & 2) != 0;
        T t = null;
        int i3 = Integer.MAX_VALUE;
        for (T t2 : tArr) {
            int iAbs = (Math.abs(aVar.b(t2) - i2) * 2) + (aVar.a(t2) == z ? 0 : 1);
            if (t == null || i3 > iAbs) {
                t = t2;
                i3 = iAbs;
            }
        }
        return t;
    }

    @Override // android.support.v4.b.c.a
    public Typeface a(Context context, Resources resources, int i, String str, int i2) {
        File fileA = h.a(context);
        if (fileA == null) {
            return null;
        }
        try {
            if (h.a(fileA, resources, i)) {
                return Typeface.createFromFile(fileA.getPath());
            }
            return null;
        } catch (RuntimeException unused) {
            return null;
        } finally {
            fileA.delete();
        }
    }

    @Override // android.support.v4.b.c.a
    public Typeface a(Context context, CancellationSignal cancellationSignal, b.C0006b[] c0006bArr, int i) throws Throwable {
        InputStream inputStreamOpenInputStream;
        InputStream inputStream = null;
        if (c0006bArr.length < 1) {
            return null;
        }
        try {
            inputStreamOpenInputStream = context.getContentResolver().openInputStream(a(c0006bArr, i).a());
            try {
                Typeface typefaceA = a(context, inputStreamOpenInputStream);
                h.a(inputStreamOpenInputStream);
                return typefaceA;
            } catch (IOException unused) {
                h.a(inputStreamOpenInputStream);
                return null;
            } catch (Throwable th) {
                th = th;
                inputStream = inputStreamOpenInputStream;
                h.a(inputStream);
                throw th;
            }
        } catch (IOException unused2) {
            inputStreamOpenInputStream = null;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    @Override // android.support.v4.b.c.a
    public Typeface a(Context context, a.b bVar, Resources resources, int i) {
        a.c cVarA = a(bVar, i);
        if (cVarA == null) {
            return null;
        }
        return c.a(context, resources, cVarA.d(), cVarA.a(), i);
    }

    protected Typeface a(Context context, InputStream inputStream) {
        File fileA = h.a(context);
        if (fileA == null) {
            return null;
        }
        try {
            if (h.a(fileA, inputStream)) {
                return Typeface.createFromFile(fileA.getPath());
            }
            return null;
        } catch (RuntimeException unused) {
            return null;
        } finally {
            fileA.delete();
        }
    }

    protected b.C0006b a(b.C0006b[] c0006bArr, int i) {
        return (b.C0006b) a(c0006bArr, i, new a<b.C0006b>() { // from class: android.support.v4.b.g.1
            @Override // android.support.v4.b.g.a
            /* JADX INFO: renamed from: a, reason: avoid collision after fix types in other method and merged with bridge method [inline-methods] */
            public int b(b.C0006b c0006b) {
                return c0006b.c();
            }

            @Override // android.support.v4.b.g.a
            /* JADX INFO: renamed from: b, reason: avoid collision after fix types in other method and merged with bridge method [inline-methods] */
            public boolean a(b.C0006b c0006b) {
                return c0006b.d();
            }
        });
    }
}
