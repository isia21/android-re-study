package android.support.v4.f;

/* JADX INFO: loaded from: classes.dex */
public class f<E> implements Cloneable {
    private static final Object a = new Object();
    private boolean b;
    private long[] c;
    private Object[] d;
    private int e;

    public f() {
        this(10);
    }

    public f(int i) {
        Object[] objArr;
        this.b = false;
        if (i == 0) {
            this.c = c.b;
            objArr = c.c;
        } else {
            int iB = c.b(i);
            this.c = new long[iB];
            objArr = new Object[iB];
        }
        this.d = objArr;
        this.e = 0;
    }

    private void d() {
        int i = this.e;
        long[] jArr = this.c;
        Object[] objArr = this.d;
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            Object obj = objArr[i3];
            if (obj != a) {
                if (i3 != i2) {
                    jArr[i2] = jArr[i3];
                    objArr[i2] = obj;
                    objArr[i3] = null;
                }
                i2++;
            }
        }
        this.b = false;
        this.e = i2;
    }

    public long a(int i) {
        if (this.b) {
            d();
        }
        return this.c[i];
    }

    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public f<E> clone() {
        try {
            f<E> fVar = (f) super.clone();
            try {
                fVar.c = (long[]) this.c.clone();
                fVar.d = (Object[]) this.d.clone();
                return fVar;
            } catch (CloneNotSupportedException unused) {
                return fVar;
            }
        } catch (CloneNotSupportedException unused2) {
            return null;
        }
    }

    public E a(long j) {
        return a(j, null);
    }

    public E a(long j, E e) {
        int iA = c.a(this.c, this.e, j);
        return (iA < 0 || this.d[iA] == a) ? e : (E) this.d[iA];
    }

    public int b() {
        if (this.b) {
            d();
        }
        return this.e;
    }

    public E b(int i) {
        if (this.b) {
            d();
        }
        return (E) this.d[i];
    }

    public void b(long j) {
        int iA = c.a(this.c, this.e, j);
        if (iA < 0 || this.d[iA] == a) {
            return;
        }
        this.d[iA] = a;
        this.b = true;
    }

    public void b(long j, E e) {
        int iA = c.a(this.c, this.e, j);
        if (iA >= 0) {
            this.d[iA] = e;
            return;
        }
        int iA2 = iA ^ (-1);
        if (iA2 < this.e && this.d[iA2] == a) {
            this.c[iA2] = j;
            this.d[iA2] = e;
            return;
        }
        if (this.b && this.e >= this.c.length) {
            d();
            iA2 = c.a(this.c, this.e, j) ^ (-1);
        }
        if (this.e >= this.c.length) {
            int iB = c.b(this.e + 1);
            long[] jArr = new long[iB];
            Object[] objArr = new Object[iB];
            System.arraycopy(this.c, 0, jArr, 0, this.c.length);
            System.arraycopy(this.d, 0, objArr, 0, this.d.length);
            this.c = jArr;
            this.d = objArr;
        }
        if (this.e - iA2 != 0) {
            int i = iA2 + 1;
            System.arraycopy(this.c, iA2, this.c, i, this.e - iA2);
            System.arraycopy(this.d, iA2, this.d, i, this.e - iA2);
        }
        this.c[iA2] = j;
        this.d[iA2] = e;
        this.e++;
    }

    public void c() {
        int i = this.e;
        Object[] objArr = this.d;
        for (int i2 = 0; i2 < i; i2++) {
            objArr[i2] = null;
        }
        this.e = 0;
        this.b = false;
    }

    public String toString() {
        if (b() <= 0) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder(this.e * 28);
        sb.append('{');
        for (int i = 0; i < this.e; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(a(i));
            sb.append('=');
            E eB = b(i);
            if (eB != this) {
                sb.append(eB);
            } else {
                sb.append("(this Map)");
            }
        }
        sb.append('}');
        return sb.toString();
    }
}
