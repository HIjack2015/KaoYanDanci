package cn.jk.kaoyandanci.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * <pre>
 *     author : jiakang
 *     e-mail : 1079153785@qq.com
 *     time   : 2018/07/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ShadowImageView extends android.support.v7.widget.AppCompatImageView {

    public ShadowImageView(Context context) {
        super(context);
    }

    public ShadowImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setShadowWhenPress(null);
    }

    public ShadowImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setShadowWhenPress(null);
    }

    /**
     * @param outRect 传入为空 则范围为本view.
     *                不为空则为传入的view.
     */
    public void setShadowWhenPress(final Rect outRect) {
        setOnTouchListener(new OnTouchListener() {
            private Rect rect;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setColorFilter(Color.argb(100, 0, 0, 0));
                    if (outRect != null) {
                        rect = outRect;
                    } else {
                        rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                    }

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    setColorFilter(Color.argb(0, 0, 0, 0));
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                        setColorFilter(Color.argb(0, 0, 0, 0));
                    }
                }
                return false;
            }
        });
    }
}
