package xyz.kitpies.lightpageanim;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 可以设置透明度的带有倒影的ImageView.
 */

public class AlphaReflectionImageView extends View {

    private static final String TAG = AlphaReflectionImageView.class.getSimpleName();
    /** 反光系数 */
    private final float REFLECTION_COEFFICIENT = 0.5f;
    private Drawable mDrawable = null;
    /** 反光间隙 */
    private float mReflectionGap = 0f;
    /** 主物体透明系数 */
    private float mAlpha = 1f;
    private Paint mPaint = new Paint();
    private Bitmap mTb;
    private int[] mColors;

    public AlphaReflectionImageView(Context context) {
        super(context);
    }

    public AlphaReflectionImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AlphaReflectionImageView);
        mDrawable = a.getDrawable(R.styleable.AlphaReflectionImageView_pic_src);
        mReflectionGap = a.getDimension(R.styleable.AlphaReflectionImageView_reflection_gap, 0f);
        mAlpha = a.getFloat(R.styleable.AlphaReflectionImageView_main_alpha, 1f);
        mAlpha = mAlpha<0?0f:mAlpha>1?1:mAlpha;
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(mDrawable==null){
            setMeasuredDimension(0, 0);
        }else{

            int dwMSP = MeasureSpec.makeMeasureSpec(mDrawable.getIntrinsicWidth()+getPaddingLeft()+getPaddingRight(), MeasureSpec.EXACTLY);
            int dhMSP = MeasureSpec.makeMeasureSpec((int) (mDrawable.getIntrinsicHeight()*2+mReflectionGap)+getPaddingTop()+getPaddingBottom(), MeasureSpec.EXACTLY);

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
            mDrawable.draw(canvas);

            Bitmap src = Bitmap.createBitmap(((BitmapDrawable)mDrawable).getBitmap());
            if(mTb==null||mTb.getWidth()!=src.getWidth()||mTb.getHeight()!=src.getHeight()) {
                mTb = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
                mColors = new int[mTb.getWidth() * mTb.getHeight()];
            }
            src.getPixels(mColors,0,mTb.getWidth(),0,0,mTb.getWidth(),mTb.getHeight());
            for(int i = 0;i<mTb.getWidth();i++){
                for(int j = 0;j<mTb.getHeight();j++){
                    if((mColors[j*mTb.getWidth()+i]&0xff000000)!=0){
                        mColors[j*mTb.getWidth()+i]=(mColors[j*mTb.getWidth()+i]&0x00ffffff)|(((int)(mAlpha*0xff*REFLECTION_COEFFICIENT*(j/(float)mTb.getHeight()))<<24));
                    }
                }
            }
            mTb.setPixels(mColors,0,mTb.getWidth(),0,0,mTb.getWidth(),mTb.getHeight());
            canvas.scale(1f,-1f,pl+w/2,pt+h+mReflectionGap/2);
            canvas.drawBitmap(mTb, new Rect(0,0,mTb.getWidth(),mTb.getHeight()),new RectF(pl,pt,pl+w, pt+h),mPaint);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if(mTb!=null&&!mTb.isRecycled()){
            mTb.recycle();
        }
    }

    public void setAlpha(float alpha) {
        mAlpha = alpha<0?0f:alpha>1?1:alpha;
        invalidate();
    }

    public void setReflectionGap(float reflectionGap) {
        mReflectionGap = reflectionGap<0?0:reflectionGap;
        invalidate();
    }

    public float getAlpha() {
        return mAlpha;
    }

    public float getReflectionGap() {
        return mReflectionGap;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
        invalidate();
    }
}
