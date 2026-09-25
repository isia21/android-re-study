package android.support.v4.app;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
final class o implements Parcelable {
    public static final Parcelable.Creator<o> CREATOR = new Parcelable.Creator<o>() { // from class: android.support.v4.app.o.1
        @Override // android.os.Parcelable.Creator
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public o createFromParcel(Parcel parcel) {
            return new o(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public o[] newArray(int i) {
            return new o[i];
        }
    };
    p[] a;
    int[] b;
    d[] c;
    int d;
    int e;

    public o() {
        this.d = -1;
    }

    public o(Parcel parcel) {
        this.d = -1;
        this.a = (p[]) parcel.createTypedArray(p.CREATOR);
        this.b = parcel.createIntArray();
        this.c = (d[]) parcel.createTypedArray(d.CREATOR);
        this.d = parcel.readInt();
        this.e = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedArray(this.a, i);
        parcel.writeIntArray(this.b);
        parcel.writeTypedArray(this.c, i);
        parcel.writeInt(this.d);
        parcel.writeInt(this.e);
    }
}
