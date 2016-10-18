package com.liang530.views.textView;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.liang530.utils.BaseAppUtil;

/**
 * Created by hongliang on 16-6-3.
 * 固定宽度的单行TextView,为了能让文字完整放下,自动缩放字体大小
 *
 */
public class FontSizeFitTextView extends TextView {
    private Float maxTextSize;
    private Paint mTestPaint;
    private int mWidth;
    private float defaultMaxTextSize;
    public FontSizeFitTextView(Context context) {
        this(context,null);
    }

    public FontSizeFitTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FontSizeFitTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialise();
    }
    private void initialise() {
        mTestPaint = new Paint();
        mTestPaint.set(this.getPaint());
        defaultMaxTextSize= BaseAppUtil.sp2px(this.getContext(),12);
    }

    public Float getMaxTextSize() {
        return maxTextSize;
    }

    public void setMaxTextSize(Float maxTextSize) {
        this.maxTextSize = maxTextSize;
        refitText(this.getText().toString(),mWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int height = getMeasuredHeight();
        mWidth=parentWidth;
        refitText(this.getText().toString(), parentWidth);
        this.setMeasuredDimension(parentWidth, height);
    }

    /**
     * 文字改变时改变重新计算字体大小
     * @param text
     * @param start
     * @param before
     * @param after
     */
    @Override
    protected void onTextChanged(final CharSequence text, final int start, final int before, final int after) {
        mWidth= this.getWidth();
        refitText(text.toString(), this.getWidth());
    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
        if (w != oldw) {//宽度改变时重新计算字体大小
            mWidth= w;
            refitText(this.getText().toString(), w);
        }
    }
    private void refitText(String text, int textWidth)
    {
        if (textWidth <= 0)
            return;
        int targetWidth = textWidth - this.getPaddingLeft() - this.getPaddingRight();
        float hi = maxTextSize==null?defaultMaxTextSize:maxTextSize;
        float lo = 2;
        final float threshold = 0.5f; // How close we have to be
        mTestPaint.set(this.getPaint());
        //二分查找最优值,精度控制
        while((hi - lo) > threshold) {
            float size = (hi+lo)/2;
            mTestPaint.setTextSize(size);
            if(mTestPaint.measureText(text) >= targetWidth){
                hi = size;
            }else{
                lo = size;
            }
        }
        // Use lo so that we undershoot rather than overshoot
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, lo);
    }
}
