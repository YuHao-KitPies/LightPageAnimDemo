package xyz.kitpies.lightpageanim;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.drawable.RotateDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    AlphaReflectionImageView mText;
    RotateImageView mIcon;
    boolean isAnim = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIcon = (RotateImageView) findViewById(R.id.rotateImageView);

        mText = (AlphaReflectionImageView) findViewById(R.id.alphaReflectionImageView);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnim();
            }
        });
    }


    private void startAnim(){
        if(!isAnim) {
            final float yDensity = getResources().getDisplayMetrics().density;
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnimator.setDuration(2000);
            valueAnimator.setRepeatCount(0);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    animation.getAnimatedValue();
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mText.getLayoutParams();
                    float value = (Float) animation.getAnimatedValue();
                    lp.bottomMargin = (int) (value * 200 * yDensity);
                    mText.setLayoutParams(lp);
                    mText.setAlpha(value);
                    mIcon.setCurrentRotateDegree(360 * 3 * value);//转三圈
                }
            });
            isAnim = true;
            valueAnimator.start();
            valueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    Toast.makeText(MainActivity.this, R.string.hint_text, Toast.LENGTH_SHORT).show();
                    isAnim = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }

}
