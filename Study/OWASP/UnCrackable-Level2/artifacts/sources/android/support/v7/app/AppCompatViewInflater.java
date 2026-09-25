package android.support.v7.app;

import android.R;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v7.a.a;
import android.support.v7.widget.ab;
import android.support.v7.widget.at;
import android.support.v7.widget.q;
import android.support.v7.widget.r;
import android.support.v7.widget.u;
import android.support.v7.widget.v;
import android.support.v7.widget.w;
import android.support.v7.widget.y;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class AppCompatViewInflater {
    private static final Class<?>[] a = {Context.class, AttributeSet.class};
    private static final int[] b = {R.attr.onClick};
    private static final String[] c = {"android.widget.", "android.view.", "android.webkit."};
    private static final Map<String, Constructor<? extends View>> d = new android.support.v4.f.a();
    private final Object[] e = new Object[2];

    private static class a implements View.OnClickListener {
        private final View a;
        private final String b;
        private Method c;
        private Context d;

        public a(View view, String str) {
            this.a = view;
            this.b = str;
        }

        private void a(Context context, String str) {
            String str2;
            Method method;
            while (context != null) {
                try {
                    if (!context.isRestricted() && (method = context.getClass().getMethod(this.b, View.class)) != null) {
                        this.c = method;
                        this.d = context;
                        return;
                    }
                } catch (NoSuchMethodException unused) {
                }
                context = context instanceof ContextWrapper ? ((ContextWrapper) context).getBaseContext() : null;
            }
            int id = this.a.getId();
            if (id == -1) {
                str2 = "";
            } else {
                str2 = " with id '" + this.a.getContext().getResources().getResourceEntryName(id) + "'";
            }
            throw new IllegalStateException("Could not find method " + this.b + "(View) in a parent or ancestor Context for android:onClick attribute defined on view " + this.a.getClass() + str2);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (this.c == null) {
                a(this.a.getContext(), this.b);
            }
            try {
                this.c.invoke(this.d, view);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Could not execute non-public method for android:onClick", e);
            } catch (InvocationTargetException e2) {
                throw new IllegalStateException("Could not execute method for android:onClick", e2);
            }
        }
    }

    private static Context a(Context context, AttributeSet attributeSet, boolean z, boolean z2) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, a.j.View, 0, 0);
        int resourceId = z ? typedArrayObtainStyledAttributes.getResourceId(a.j.View_android_theme, 0) : 0;
        if (z2 && resourceId == 0 && (resourceId = typedArrayObtainStyledAttributes.getResourceId(a.j.View_theme, 0)) != 0) {
            Log.i("AppCompatViewInflater", "app:theme is now deprecated. Please move to using android:theme instead.");
        }
        typedArrayObtainStyledAttributes.recycle();
        return resourceId != 0 ? ((context instanceof android.support.v7.view.d) && ((android.support.v7.view.d) context).a() == resourceId) ? context : new android.support.v7.view.d(context, resourceId) : context;
    }

    private View a(Context context, String str, String str2) {
        String str3;
        Constructor<? extends View> constructor = d.get(str);
        if (constructor == null) {
            try {
                ClassLoader classLoader = context.getClassLoader();
                if (str2 != null) {
                    str3 = str2 + str;
                } else {
                    str3 = str;
                }
                constructor = classLoader.loadClass(str3).asSubclass(View.class).getConstructor(a);
                d.put(str, constructor);
            } catch (Exception unused) {
                return null;
            }
        }
        constructor.setAccessible(true);
        return constructor.newInstance(this.e);
    }

    private void a(View view, AttributeSet attributeSet) {
        Context context = view.getContext();
        if (context instanceof ContextWrapper) {
            if (Build.VERSION.SDK_INT < 15 || android.support.v4.g.p.n(view)) {
                TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, b);
                String string = typedArrayObtainStyledAttributes.getString(0);
                if (string != null) {
                    view.setOnClickListener(new a(view, string));
                }
                typedArrayObtainStyledAttributes.recycle();
            }
        }
    }

    private void a(View view, String str) {
        if (view != null) {
            return;
        }
        throw new IllegalStateException(getClass().getName() + " asked to inflate view for <" + str + ">, but returned null");
    }

    private View b(Context context, String str, AttributeSet attributeSet) {
        if (str.equals("view")) {
            str = attributeSet.getAttributeValue(null, "class");
        }
        try {
            this.e[0] = context;
            this.e[1] = attributeSet;
            if (-1 != str.indexOf(46)) {
                return a(context, str, (String) null);
            }
            for (int i = 0; i < c.length; i++) {
                View viewA = a(context, str, c[i]);
                if (viewA != null) {
                    return viewA;
                }
            }
            return null;
        } catch (Exception unused) {
            return null;
        } finally {
            this.e[0] = null;
            this.e[1] = null;
        }
    }

    protected ab a(Context context, AttributeSet attributeSet) {
        return new ab(context, attributeSet);
    }

    protected View a(Context context, String str, AttributeSet attributeSet) {
        return null;
    }

    final View a(View view, String str, Context context, AttributeSet attributeSet, boolean z, boolean z2, boolean z3, boolean z4) {
        Context context2;
        View viewA;
        context2 = (!z || view == null) ? context : view.getContext();
        if (z2 || z3) {
            context2 = a(context2, attributeSet, z2, z3);
        }
        if (z4) {
            context2 = at.a(context2);
        }
        switch (str) {
            case "TextView":
                viewA = a(context2, attributeSet);
                a(viewA, str);
                break;
            case "ImageView":
                viewA = b(context2, attributeSet);
                a(viewA, str);
                break;
            case "Button":
                viewA = c(context2, attributeSet);
                a(viewA, str);
                break;
            case "EditText":
                viewA = d(context2, attributeSet);
                a(viewA, str);
                break;
            case "Spinner":
                viewA = e(context2, attributeSet);
                a(viewA, str);
                break;
            case "ImageButton":
                viewA = f(context2, attributeSet);
                a(viewA, str);
                break;
            case "CheckBox":
                viewA = g(context2, attributeSet);
                a(viewA, str);
                break;
            case "RadioButton":
                viewA = h(context2, attributeSet);
                a(viewA, str);
                break;
            case "CheckedTextView":
                viewA = i(context2, attributeSet);
                a(viewA, str);
                break;
            case "AutoCompleteTextView":
                viewA = j(context2, attributeSet);
                a(viewA, str);
                break;
            case "MultiAutoCompleteTextView":
                viewA = k(context2, attributeSet);
                a(viewA, str);
                break;
            case "RatingBar":
                viewA = l(context2, attributeSet);
                a(viewA, str);
                break;
            case "SeekBar":
                viewA = m(context2, attributeSet);
                a(viewA, str);
                break;
            default:
                viewA = a(context2, str, attributeSet);
                break;
        }
        if (viewA == null && context != context2) {
            viewA = b(context2, str, attributeSet);
        }
        if (viewA != null) {
            a(viewA, attributeSet);
        }
        return viewA;
    }

    protected q b(Context context, AttributeSet attributeSet) {
        return new q(context, attributeSet);
    }

    protected android.support.v7.widget.h c(Context context, AttributeSet attributeSet) {
        return new android.support.v7.widget.h(context, attributeSet);
    }

    protected android.support.v7.widget.m d(Context context, AttributeSet attributeSet) {
        return new android.support.v7.widget.m(context, attributeSet);
    }

    protected y e(Context context, AttributeSet attributeSet) {
        return new y(context, attributeSet);
    }

    protected android.support.v7.widget.o f(Context context, AttributeSet attributeSet) {
        return new android.support.v7.widget.o(context, attributeSet);
    }

    protected android.support.v7.widget.i g(Context context, AttributeSet attributeSet) {
        return new android.support.v7.widget.i(context, attributeSet);
    }

    protected u h(Context context, AttributeSet attributeSet) {
        return new u(context, attributeSet);
    }

    protected android.support.v7.widget.j i(Context context, AttributeSet attributeSet) {
        return new android.support.v7.widget.j(context, attributeSet);
    }

    protected android.support.v7.widget.f j(Context context, AttributeSet attributeSet) {
        return new android.support.v7.widget.f(context, attributeSet);
    }

    protected r k(Context context, AttributeSet attributeSet) {
        return new r(context, attributeSet);
    }

    protected v l(Context context, AttributeSet attributeSet) {
        return new v(context, attributeSet);
    }

    protected w m(Context context, AttributeSet attributeSet) {
        return new w(context, attributeSet);
    }
}
