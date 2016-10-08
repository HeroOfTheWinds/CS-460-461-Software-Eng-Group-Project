package com.unity3d.player;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/* renamed from: com.unity3d.player.s */
public final class C0838s extends Dialog implements TextWatcher, OnClickListener {
    private static int f215c;
    private static int f216d;
    private Context f217a;
    private UnityPlayer f218b;

    /* renamed from: com.unity3d.player.s.1 */
    final class C08351 implements OnFocusChangeListener {
        final /* synthetic */ C0838s f212a;

        C08351(C0838s c0838s) {
            this.f212a = c0838s;
        }

        public final void onFocusChange(View view, boolean z) {
            if (z) {
                this.f212a.getWindow().setSoftInputMode(5);
            }
        }
    }

    /* renamed from: com.unity3d.player.s.2 */
    final class C08362 extends EditText {
        final /* synthetic */ C0838s f213a;

        C08362(C0838s c0838s, Context context) {
            this.f213a = c0838s;
            super(context);
        }

        public final boolean onKeyPreIme(int i, KeyEvent keyEvent) {
            if (i != 4) {
                return i != 84 ? super.onKeyPreIme(i, keyEvent) : true;
            } else {
                this.f213a.m180a(this.f213a.m176a(), true);
                return true;
            }
        }

        public final void onWindowFocusChanged(boolean z) {
            super.onWindowFocusChanged(z);
            if (z) {
                ((InputMethodManager) this.f213a.f217a.getSystemService("input_method")).showSoftInput(this, 0);
            }
        }
    }

    /* renamed from: com.unity3d.player.s.3 */
    final class C08373 implements OnEditorActionListener {
        final /* synthetic */ C0838s f214a;

        C08373(C0838s c0838s) {
            this.f214a = c0838s;
        }

        public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == 6) {
                this.f214a.m180a(this.f214a.m176a(), false);
            }
            return false;
        }
    }

    static {
        f215c = 1627389952;
        f216d = -1;
    }

    public C0838s(Context context, UnityPlayer unityPlayer, String str, int i, boolean z, boolean z2, boolean z3, String str2) {
        super(context);
        this.f217a = null;
        this.f218b = null;
        this.f217a = context;
        this.f218b = unityPlayer;
        getWindow().setGravity(80);
        getWindow().requestFeature(1);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        setContentView(createSoftInputView());
        getWindow().setLayout(-1, -2);
        getWindow().clearFlags(2);
        EditText editText = (EditText) findViewById(1057292289);
        Button button = (Button) findViewById(1057292290);
        m178a(editText, str, i, z, z2, z3, str2);
        button.setOnClickListener(this);
        editText.setOnFocusChangeListener(new C08351(this));
    }

    private static int m175a(int i, boolean z, boolean z2, boolean z3) {
        int i2 = 0;
        int i3 = z ? AccessibilityNodeInfoCompat.ACTION_PASTE : 0;
        int i4 = z2 ? AccessibilityNodeInfoCompat.ACTION_SET_SELECTION : 0;
        if (z3) {
            i2 = AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS;
        }
        i2 |= i4 | i3;
        return (i < 0 || i > 7) ? i2 : i2 | new int[]{1, 16385, 12290, 17, 2, 3, 97, 33}[i];
    }

    private String m176a() {
        EditText editText = (EditText) findViewById(1057292289);
        return editText == null ? null : editText.getText().toString().trim();
    }

    private void m178a(EditText editText, String str, int i, boolean z, boolean z2, boolean z3, String str2) {
        editText.setImeOptions(6);
        editText.setText(str);
        editText.setHint(str2);
        editText.setHintTextColor(f215c);
        editText.setInputType(C0838s.m175a(i, z, z2, z3));
        editText.addTextChangedListener(this);
        editText.setClickable(true);
        if (!z2) {
            editText.selectAll();
        }
    }

    private void m180a(String str, boolean z) {
        Selection.removeSelection(((EditText) findViewById(1057292289)).getEditableText());
        this.f218b.reportSoftInputStr(str, 1, z);
    }

    public final void m182a(String str) {
        EditText editText = (EditText) findViewById(1057292289);
        if (editText != null) {
            editText.setText(str);
            editText.setSelection(str.length());
        }
    }

    public final void afterTextChanged(Editable editable) {
        this.f218b.reportSoftInputStr(editable.toString(), 0, false);
    }

    public final void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    protected final View createSoftInputView() {
        View relativeLayout = new RelativeLayout(this.f217a);
        relativeLayout.setLayoutParams(new LayoutParams(-1, -1));
        relativeLayout.setBackgroundColor(f216d);
        View c08362 = new C08362(this, this.f217a);
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(15);
        layoutParams.addRule(0, 1057292290);
        c08362.setLayoutParams(layoutParams);
        c08362.setId(1057292289);
        relativeLayout.addView(c08362);
        c08362 = new Button(this.f217a);
        c08362.setText(this.f217a.getResources().getIdentifier("ok", "string", "android"));
        layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(15);
        layoutParams.addRule(11);
        c08362.setLayoutParams(layoutParams);
        c08362.setId(1057292290);
        c08362.setBackgroundColor(0);
        relativeLayout.addView(c08362);
        ((EditText) relativeLayout.findViewById(1057292289)).setOnEditorActionListener(new C08373(this));
        relativeLayout.setPadding(16, 16, 16, 16);
        return relativeLayout;
    }

    public final void onBackPressed() {
        m180a(m176a(), true);
    }

    public final void onClick(View view) {
        m180a(m176a(), false);
    }

    public final void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }
}
