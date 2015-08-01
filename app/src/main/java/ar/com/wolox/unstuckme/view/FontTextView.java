package ar.com.wolox.unstuckme.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import ar.com.wolox.unstuckme.factory.FontFactory;

public class FontTextView extends TextView {

    public FontTextView(Context context) {
        super(context);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(attrs);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(attrs);
    }

    public void setCustomFont(AttributeSet atts) {
        FontFactory.setTypeface(this, atts);
    }

    public void setCustomFont(String font) {
        FontFactory.setTypeface(this, font);
    }
}