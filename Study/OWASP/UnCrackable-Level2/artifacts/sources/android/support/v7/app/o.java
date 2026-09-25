package android.support.v7.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import java.util.Calendar;

/* JADX INFO: loaded from: classes.dex */
class o {
    private static o a;
    private final Context b;
    private final LocationManager c;
    private final a d = new a();

    private static class a {
        boolean a;
        long b;
        long c;
        long d;
        long e;
        long f;

        a() {
        }
    }

    o(Context context, LocationManager locationManager) {
        this.b = context;
        this.c = locationManager;
    }

    private Location a(String str) {
        try {
            if (this.c.isProviderEnabled(str)) {
                return this.c.getLastKnownLocation(str);
            }
            return null;
        } catch (Exception e) {
            Log.d("TwilightManager", "Failed to get last known location", e);
            return null;
        }
    }

    static o a(Context context) {
        if (a == null) {
            Context applicationContext = context.getApplicationContext();
            a = new o(applicationContext, (LocationManager) applicationContext.getSystemService("location"));
        }
        return a;
    }

    private void a(Location location) {
        long j;
        a aVar = this.d;
        long jCurrentTimeMillis = System.currentTimeMillis();
        n nVarA = n.a();
        nVarA.a(jCurrentTimeMillis - 86400000, location.getLatitude(), location.getLongitude());
        long j2 = nVarA.a;
        nVarA.a(jCurrentTimeMillis, location.getLatitude(), location.getLongitude());
        boolean z = nVarA.c == 1;
        long j3 = nVarA.b;
        long j4 = nVarA.a;
        boolean z2 = z;
        nVarA.a(86400000 + jCurrentTimeMillis, location.getLatitude(), location.getLongitude());
        long j5 = nVarA.b;
        if (j3 == -1 || j4 == -1) {
            j = 43200000 + jCurrentTimeMillis;
        } else {
            j = (jCurrentTimeMillis > j4 ? 0 + j5 : jCurrentTimeMillis > j3 ? 0 + j4 : 0 + j3) + 60000;
        }
        aVar.a = z2;
        aVar.b = j2;
        aVar.c = j3;
        aVar.d = j4;
        aVar.e = j5;
        aVar.f = j;
    }

    @SuppressLint({"MissingPermission"})
    private Location b() {
        Location locationA = android.support.v4.a.c.a(this.b, "android.permission.ACCESS_COARSE_LOCATION") == 0 ? a("network") : null;
        Location locationA2 = android.support.v4.a.c.a(this.b, "android.permission.ACCESS_FINE_LOCATION") == 0 ? a("gps") : null;
        return (locationA2 == null || locationA == null) ? locationA2 != null ? locationA2 : locationA : locationA2.getTime() > locationA.getTime() ? locationA2 : locationA;
    }

    private boolean c() {
        return this.d.f > System.currentTimeMillis();
    }

    boolean a() {
        a aVar = this.d;
        if (c()) {
            return aVar.a;
        }
        Location locationB = b();
        if (locationB != null) {
            a(locationB);
            return aVar.a;
        }
        Log.i("TwilightManager", "Could not get last known location. This is probably because the app does not have any location permissions. Falling back to hardcoded sunrise/sunset values.");
        int i = Calendar.getInstance().get(11);
        return i < 6 || i >= 22;
    }
}
