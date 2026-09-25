package android.support.v4.f;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public final class b<E> implements Collection<E>, Set<E> {
    private static final int[] a = new int[0];
    private static final Object[] b = new Object[0];
    private static Object[] c;
    private static int d;
    private static Object[] e;
    private static int f;
    private int[] g;
    private Object[] h;
    private int i;
    private h<E, E> j;

    public b() {
        this(0);
    }

    public b(int i) {
        if (i == 0) {
            this.g = a;
            this.h = b;
        } else {
            d(i);
        }
        this.i = 0;
    }

    private int a() {
        int i = this.i;
        if (i == 0) {
            return -1;
        }
        int iA = c.a(this.g, i, 0);
        if (iA < 0 || this.h[iA] == null) {
            return iA;
        }
        int i2 = iA + 1;
        while (i2 < i && this.g[i2] == 0) {
            if (this.h[i2] == null) {
                return i2;
            }
            i2++;
        }
        for (int i3 = iA - 1; i3 >= 0 && this.g[i3] == 0; i3--) {
            if (this.h[i3] == null) {
                return i3;
            }
        }
        return i2 ^ (-1);
    }

    private int a(Object obj, int i) {
        int i2 = this.i;
        if (i2 == 0) {
            return -1;
        }
        int iA = c.a(this.g, i2, i);
        if (iA < 0 || obj.equals(this.h[iA])) {
            return iA;
        }
        int i3 = iA + 1;
        while (i3 < i2 && this.g[i3] == i) {
            if (obj.equals(this.h[i3])) {
                return i3;
            }
            i3++;
        }
        for (int i4 = iA - 1; i4 >= 0 && this.g[i4] == i; i4--) {
            if (obj.equals(this.h[i4])) {
                return i4;
            }
        }
        return i3 ^ (-1);
    }

    private static void a(int[] iArr, Object[] objArr, int i) {
        if (iArr.length == 8) {
            synchronized (b.class) {
                if (f < 10) {
                    objArr[0] = e;
                    objArr[1] = iArr;
                    for (int i2 = i - 1; i2 >= 2; i2--) {
                        objArr[i2] = null;
                    }
                    e = objArr;
                    f++;
                }
            }
            return;
        }
        if (iArr.length == 4) {
            synchronized (b.class) {
                if (d < 10) {
                    objArr[0] = c;
                    objArr[1] = iArr;
                    for (int i3 = i - 1; i3 >= 2; i3--) {
                        objArr[i3] = null;
                    }
                    c = objArr;
                    d++;
                }
            }
        }
    }

    private h<E, E> b() {
        if (this.j == null) {
            this.j = new h<E, E>() { // from class: android.support.v4.f.b.1
                @Override // android.support.v4.f.h
                protected int a() {
                    return b.this.i;
                }

                @Override // android.support.v4.f.h
                protected int a(Object obj) {
                    return b.this.a(obj);
                }

                @Override // android.support.v4.f.h
                protected Object a(int i, int i2) {
                    return b.this.h[i];
                }

                @Override // android.support.v4.f.h
                protected E a(int i, E e2) {
                    throw new UnsupportedOperationException("not a map");
                }

                @Override // android.support.v4.f.h
                protected void a(int i) {
                    b.this.c(i);
                }

                @Override // android.support.v4.f.h
                protected void a(E e2, E e3) {
                    b.this.add(e2);
                }

                @Override // android.support.v4.f.h
                protected int b(Object obj) {
                    return b.this.a(obj);
                }

                @Override // android.support.v4.f.h
                protected Map<E, E> b() {
                    throw new UnsupportedOperationException("not a map");
                }

                @Override // android.support.v4.f.h
                protected void c() {
                    b.this.clear();
                }
            };
        }
        return this.j;
    }

    private void d(int i) {
        if (i == 8) {
            synchronized (b.class) {
                if (e != null) {
                    Object[] objArr = e;
                    this.h = objArr;
                    e = (Object[]) objArr[0];
                    this.g = (int[]) objArr[1];
                    objArr[1] = null;
                    objArr[0] = null;
                    f--;
                    return;
                }
            }
        } else if (i == 4) {
            synchronized (b.class) {
                if (c != null) {
                    Object[] objArr2 = c;
                    this.h = objArr2;
                    c = (Object[]) objArr2[0];
                    this.g = (int[]) objArr2[1];
                    objArr2[1] = null;
                    objArr2[0] = null;
                    d--;
                    return;
                }
            }
        }
        this.g = new int[i];
        this.h = new Object[i];
    }

    public int a(Object obj) {
        return obj == null ? a() : a(obj, obj.hashCode());
    }

    public void a(int i) {
        if (this.g.length < i) {
            int[] iArr = this.g;
            Object[] objArr = this.h;
            d(i);
            if (this.i > 0) {
                System.arraycopy(iArr, 0, this.g, 0, this.i);
                System.arraycopy(objArr, 0, this.h, 0, this.i);
            }
            a(iArr, objArr, this.i);
        }
    }

    @Override // java.util.Collection, java.util.Set
    public boolean add(E e2) {
        int i;
        int iA;
        if (e2 == null) {
            iA = a();
            i = 0;
        } else {
            int iHashCode = e2.hashCode();
            i = iHashCode;
            iA = a(e2, iHashCode);
        }
        if (iA >= 0) {
            return false;
        }
        int i2 = iA ^ (-1);
        if (this.i >= this.g.length) {
            int i3 = 4;
            if (this.i >= 8) {
                i3 = (this.i >> 1) + this.i;
            } else if (this.i >= 4) {
                i3 = 8;
            }
            int[] iArr = this.g;
            Object[] objArr = this.h;
            d(i3);
            if (this.g.length > 0) {
                System.arraycopy(iArr, 0, this.g, 0, iArr.length);
                System.arraycopy(objArr, 0, this.h, 0, objArr.length);
            }
            a(iArr, objArr, this.i);
        }
        if (i2 < this.i) {
            int i4 = i2 + 1;
            System.arraycopy(this.g, i2, this.g, i4, this.i - i2);
            System.arraycopy(this.h, i2, this.h, i4, this.i - i2);
        }
        this.g[i2] = i;
        this.h[i2] = e2;
        this.i++;
        return true;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean addAll(Collection<? extends E> collection) {
        a(this.i + collection.size());
        Iterator<? extends E> it = collection.iterator();
        boolean zAdd = false;
        while (it.hasNext()) {
            zAdd |= add(it.next());
        }
        return zAdd;
    }

    public E b(int i) {
        return (E) this.h[i];
    }

    public E c(int i) {
        E e2 = (E) this.h[i];
        if (this.i <= 1) {
            a(this.g, this.h, this.i);
            this.g = a;
            this.h = b;
            this.i = 0;
        } else {
            int i2 = 8;
            if (this.g.length <= 8 || this.i >= this.g.length / 3) {
                this.i--;
                if (i < this.i) {
                    int i3 = i + 1;
                    System.arraycopy(this.g, i3, this.g, i, this.i - i);
                    System.arraycopy(this.h, i3, this.h, i, this.i - i);
                }
                this.h[this.i] = null;
            } else {
                if (this.i > 8) {
                    i2 = (this.i >> 1) + this.i;
                }
                int[] iArr = this.g;
                Object[] objArr = this.h;
                d(i2);
                this.i--;
                if (i > 0) {
                    System.arraycopy(iArr, 0, this.g, 0, i);
                    System.arraycopy(objArr, 0, this.h, 0, i);
                }
                if (i < this.i) {
                    int i4 = i + 1;
                    System.arraycopy(iArr, i4, this.g, i, this.i - i);
                    System.arraycopy(objArr, i4, this.h, i, this.i - i);
                }
            }
        }
        return e2;
    }

    @Override // java.util.Collection, java.util.Set
    public void clear() {
        if (this.i != 0) {
            a(this.g, this.h, this.i);
            this.g = a;
            this.h = b;
            this.i = 0;
        }
    }

    @Override // java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        return a(obj) >= 0;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean containsAll(Collection<?> collection) {
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            if (!contains(it.next())) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Set) {
            Set set = (Set) obj;
            if (size() != set.size()) {
                return false;
            }
            for (int i = 0; i < this.i; i++) {
                try {
                    if (!set.contains(b(i))) {
                        return false;
                    }
                } catch (ClassCastException | NullPointerException unused) {
                }
            }
            return true;
        }
        return false;
    }

    @Override // java.util.Collection, java.util.Set
    public int hashCode() {
        int[] iArr = this.g;
        int i = this.i;
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            i2 += iArr[i3];
        }
        return i2;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean isEmpty() {
        return this.i <= 0;
    }

    @Override // java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<E> iterator() {
        return b().e().iterator();
    }

    @Override // java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        int iA = a(obj);
        if (iA < 0) {
            return false;
        }
        c(iA);
        return true;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean removeAll(Collection<?> collection) {
        Iterator<?> it = collection.iterator();
        boolean zRemove = false;
        while (it.hasNext()) {
            zRemove |= remove(it.next());
        }
        return zRemove;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean retainAll(Collection<?> collection) {
        boolean z = false;
        for (int i = this.i - 1; i >= 0; i--) {
            if (!collection.contains(this.h[i])) {
                c(i);
                z = true;
            }
        }
        return z;
    }

    @Override // java.util.Collection, java.util.Set
    public int size() {
        return this.i;
    }

    @Override // java.util.Collection, java.util.Set
    public Object[] toArray() {
        Object[] objArr = new Object[this.i];
        System.arraycopy(this.h, 0, objArr, 0, this.i);
        return objArr;
    }

    @Override // java.util.Collection, java.util.Set
    public <T> T[] toArray(T[] tArr) {
        if (tArr.length < this.i) {
            tArr = (T[]) ((Object[]) Array.newInstance(tArr.getClass().getComponentType(), this.i));
        }
        System.arraycopy(this.h, 0, tArr, 0, this.i);
        if (tArr.length > this.i) {
            tArr[this.i] = null;
        }
        return tArr;
    }

    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder(this.i * 14);
        sb.append('{');
        for (int i = 0; i < this.i; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            E eB = b(i);
            if (eB != this) {
                sb.append(eB);
            } else {
                sb.append("(this Set)");
            }
        }
        sb.append('}');
        return sb.toString();
    }
}
