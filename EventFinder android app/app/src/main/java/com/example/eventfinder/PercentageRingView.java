package com.example.eventfinder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class PercentageRingView extends View {

    private Paint circlePaint;
    private Paint ringPaint;
    private Paint textPaint;
    private RectF rectF;
    private int percentage;

    public PercentageRingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.BLACK);
        circlePaint.setStyle(Paint.Style.FILL);

        ringPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ringPaint.setColor(Color.RED);
        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setStrokeWidth(10f);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(50f);

        rectF = new RectF();
        percentage = 0;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectF.set(0 + ringPaint.getStrokeWidth() / 2, 0 + ringPaint.getStrokeWidth() / 2,
                w - ringPaint.getStrokeWidth() / 2, h - ringPaint.getStrokeWidth() / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - ringPaint.getStrokeWidth() / 2, circlePaint);
        canvas.drawArc(rectF, -90, 360 * percentage / 100f, false, ringPaint);

        String text = String.valueOf(percentage);
        float textWidth = textPaint.measureText(text);
        float textHeight = (textPaint.descent() + textPaint.ascent()) / 2;
        canvas.drawText(text, getWidth() / 2 - textWidth / 2, getHeight() / 2 - textHeight, textPaint);
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
        invalidate();
    }
}
