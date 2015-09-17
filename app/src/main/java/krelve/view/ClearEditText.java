package krelve.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;


/**
 * Created by wwjun.wang on 2015/9/16.
 */
public class ClearEditText extends EditText {
    private Drawable mRightDrawable;
    private Drawable[] drawables;
    private Animation mAnimation;

    public ClearEditText(Context context) {
        super(context);
        init();
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        // 获得left、top、right、bottom设置的drawable
        drawables = this.getCompoundDrawables();

        // 获取在布局文件中设置的android:drawableRight
        mRightDrawable = drawables[2];

        setOnFocusChangeListener(new FocusChangeListenerImpl());
        addTextChangedListener(new TextWatcherImpl());
        isClearable(false);
    }

    /**
     * getWidth():得到控件的宽度
     * event.getX():抬起时的坐标
     * getTotalPaddingRight(): right drawable 左边缘至控件右边缘的距离
     * getPaddingRight(): right drawable 右边缘至控件右边缘的距离
     * getWidth() - getTotalPaddingRight(): 控件左边到right drawable 左边缘的区域
     * getWidth() - getPaddingRight(): 控件左边到right drawable右边缘的区域
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if ((event.getX() > (getWidth() - getTotalPaddingRight()))
                        && (event.getX() < (getWidth() - getPaddingRight()))) {
                    if (mAnimation != null) {
                        mAnimation.start();
                    } else {
                        setText("");
                        isClearable(false);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private class FocusChangeListenerImpl implements OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                isClearable(getText().toString().length() != 0);
            } else {
                isClearable(false);
            }
        }
    }

    private class TextWatcherImpl implements TextWatcher {
        @Override
        public void afterTextChanged(Editable s) {
            isClearable(getText().toString().length() != 0);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

    }

    protected void isClearable(boolean isVisible) {
        // 设置控件 left, top, right, bottom 的drawable
        setCompoundDrawables(drawables[0],
                drawables[1], isVisible ? mRightDrawable : null,
                drawables[3]);
    }


    public void setAnimation(Animation animation) {
        this.mAnimation = animation;
        this.mAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setText("");
                isClearable(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void clearAnim() {
        this.mAnimation = null;
    }

}
