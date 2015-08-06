package ar.com.wolox.unstuckme.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class FixedSwipeRefreshLayout extends SwipeRefreshLayout {

    private boolean mHandleTouch = true;

    public FixedSwipeRefreshLayout(Context context) {
        super(context);
    }

    public FixedSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mHandleTouch = false;
                break;
            default:
                if (mHandleTouch) {
                    return super.onTouchEvent(ev);
                }
                mHandleTouch = onInterceptTouchEvent(ev);
                switch (action) {
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mHandleTouch = true;
                        break;
                }
                break;
        }
        return true;
    }
}