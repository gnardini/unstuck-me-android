package ar.com.wolox.unstuckme.factory;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.HashMap;

import ar.com.wolox.unstuckme.R;

public class FontFactory {

    private static final String FONTS = "fonts/";
    private static final String TTF = ".ttf";

    private static HashMap<String, Typeface> sFonts = new HashMap<String, Typeface>();

    public static Typeface getFont(Context context, String fontName) {
        fontName = FONTS + fontName + TTF;
        Typeface font = sFonts.get(fontName);
        if (font != null) return font;
        font = Typeface.createFromAsset(context.getAssets(), fontName);
        sFonts.put(fontName, font);
        return font;
    }

    public static void setTypeface(TextView textView, AttributeSet attrs) {
        TypedArray typedArray = textView.getContext()
                .obtainStyledAttributes(attrs, R.styleable.FontTextView);
        if (typedArray == null) return;
        String customFont = typedArray.getString(R.styleable.FontTextView_font);
        if (customFont == null) {
            customFont = textView.getContext().getString(R.string.font_open_sans_regular);
        }
        setTypeface(textView, customFont);
        typedArray.recycle();
    }

    public static void setTypeface(TextView textView, String font) {
        Typeface tf;
        try {
            tf = FontFactory.getFont(textView.getContext(), font);
        } catch (Exception e) {
            return;
        }
        textView.setTypeface(tf);
    }
}