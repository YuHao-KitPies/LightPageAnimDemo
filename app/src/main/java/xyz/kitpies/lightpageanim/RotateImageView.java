/*
 * Copyright (C) 2017 KitPies YuHao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.kitpies.lightpageanim;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 旋转ImageView.
 */

public class RotateImageView extends View {

    private static final String TAG = RotateImageView.class.getSimpleName();
    private Drawable mDrawable = null;
    /** 主物体透明系数 */
    private float mAlpha = 1f;
    private float mCurrentRotateDegree = 0f;
    private Paint mPaint = new Paint();

    public RotateImageView(Context context) {
        super(context);
    }

    public RotateImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RotateImageView);
        mDrawable = a.getDrawable(R.styleable.RotateImageView_pic_src);
        mAlpha = a.getFloat(R.styleable.RotateImageView_main_alpha, 1f);
        mAlpha = mAlpha<0?0f:mAlpha>1?1:mAlpha;
        mCurrentRotateDegree = a.getFloat(R.styleable.RotateImageView_init_rotate_step_degree, 0f);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(mDrawable==null){
            setMeasuredDimension(0, 0);
        }else{

            int dwMSP = MeasureSpec.makeMeasureSpec(mDrawable.getIntrinsicWidth()+getPaddingLeft()+getPaddingRight(), MeasureSpec.EXACTLY);
            int dhMSP = MeasureSpec.makeMeasureSpec(mDrawable.getIntrinsicHeight()+getPaddingTop()+getPaddingBottom(), MeasureSpec.EXACTLY);

            setMeasuredDimension(dwMSP, dhMSP);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mDrawable!=null){
            int w = mDrawable.getIntrinsicWidth();
            int h = mDrawable.getIntrinsicHeight();
            int pl = getPaddingLeft();
            int pr = getPaddingRight();
            int pt = getPaddingTop();
            int pb = getBottom();

            mDrawable.setBounds(pl,pt,pl+w, pt+h);
            mDrawable.setAlpha((int) (mAlpha*255));
            canvas.rotate(mCurrentRotateDegree,pl+w/2,pt+h/2);
            mDrawable.draw(canvas);
        }
    }

    public void setCurrentRotateDegree(float currentRotateDegree) {
        mCurrentRotateDegree = currentRotateDegree;
        invalidate();
    }

    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
        invalidate();
    }

    public void setAlpha(float alpha) {
        mAlpha = alpha<0?0f:alpha>1?1:alpha;
        invalidate();
    }

    public float getAlpha() {
        return mAlpha;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public float getCurrentRotateDegree() {
        return mCurrentRotateDegree;
    }
}
