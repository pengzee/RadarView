package work.pengzhe.com.radarview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created on 2017/4/7 10:42
 *
 * @author PengZee
 */

public class RadarView extends View {

    int startColor;
    int endColor;
    int mRadarBgColor;
    int mRadarLineColor;
    int radarCircleCount;
    int mRadarRadius;

    Paint mRadarPaint;
    Paint mRadarBg;
    float degress;
    float rotateAngel;
    Matrix matrix;
    SweepGradient radarShader;
    boolean isScan;

    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;
    private static final int MSG_WHAT = 0x111;
    private static final int DELAY_TIME = 5;

    public RadarView(Context context) {
        super(context);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RadarView);
        startColor = ta.getColor(R.styleable.RadarView_startColor, startColor);
        endColor = ta.getColor(R.styleable.RadarView_endColor, endColor);
        mRadarBgColor = ta.getColor(R.styleable.RadarView_bgColor, mRadarBgColor);
        mRadarLineColor = ta.getColor(R.styleable.RadarView_lineColor, mRadarLineColor);
        radarCircleCount = ta.getColor(R.styleable.RadarView_circleCount, radarCircleCount);
        init();
        ta.recycle();
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        mRadarBg = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRadarBg.setColor(mRadarBgColor);
        mRadarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRadarPaint.setColor(mRadarLineColor);
        mRadarPaint.setStrokeWidth(2);
        mRadarPaint.setStyle(Paint.Style.STROKE);
        mRadarRadius = 300;
        radarShader = new SweepGradient(0, 0, startColor, endColor);
        matrix = new Matrix();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureSize(1, DEFAULT_WIDTH, widthMeasureSpec);
        int height = measureSize(0, DEFAULT_HEIGHT, heightMeasureSpec);
        int measureSize = Math.max(width, height);
        setMeasuredDimension(measureSize, measureSize);
    }


    /**
     * @param specType    1为宽 其他为高
     * @param contentSize 默认值
     * @param measureSpec
     * @return
     */
    private int measureSize(int specType, int contentSize, int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = Math.max(contentSize, specSize);
        } else {
            result = contentSize;
            if (specType == 1) {
                result += (getPaddingLeft() + getPaddingRight());
            } else {
                result += (getPaddingTop() + getPaddingBottom());
            }
        }


        return result;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mRadarRadius, mRadarRadius);
        mRadarBg.setShader(null);


        canvas.drawCircle(0, 0, mRadarRadius, mRadarBg);

        for (int i = 0; i <= radarCircleCount; i++) {
            canvas.drawCircle(0, 0, (float) (i * 1.0 / radarCircleCount * mRadarRadius), mRadarPaint);
        }
        canvas.drawLine(-mRadarRadius, 0, mRadarRadius, 0, mRadarPaint);
        canvas.drawLine(0, mRadarRadius, 0, -mRadarRadius, mRadarPaint);


        mRadarBg.setShader(radarShader);
        canvas.concat(matrix);
        canvas.drawCircle(0, 0, mRadarRadius, mRadarBg);

        canvas.rotate(degress);
        canvas.concat(matrix);
       // matrix.preRotate(rotateAngel, 0, 0);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            rotateAngel += 3;
            postInvalidate();

            matrix.reset();
            matrix.preRotate(rotateAngel, 0, 0);
            mHandler.sendEmptyMessageDelayed(MSG_WHAT, DELAY_TIME);
        }
    };

    public void scan() {
        if (!isScan) {
            isScan = true;
            mHandler.sendEmptyMessageDelayed(MSG_WHAT, DELAY_TIME);
        } else {
            isScan = false;
            mHandler.removeMessages(MSG_WHAT);
        }
    }


}
