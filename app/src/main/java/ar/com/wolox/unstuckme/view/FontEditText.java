package ar.com.wolox.unstuckme.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import ar.com.wolox.unstuckme.factory.FontFactory;

public class FontEditText extends EditText {

    public FontEditText(Context context) {
        super(context);
    }

    public FontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public FontEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    public void setCustomFont(Context context, AttributeSet atts) {
        FontFactory.setTypeface(this, atts);
    }

    public void setCustomFont(Context context, String font) {
        FontFactory.setTypeface(this, font);
    }
}