package com.mahinse.fortunetellerview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mahinse on 2/28/2018.
 */

public class FortuneTellerView extends View implements PullListener {
    private static final String TAG = "FortuneTellerView";

    private static final int DRAWABLE_SIZE = 50;
    private static final int TEXT_PADDING = 5;

    private static final int DESIRED_WIDTH = ViewGroup.LayoutParams.MATCH_PARENT;
    private static final int DESIRED_HEIGHT = ViewGroup.LayoutParams.MATCH_PARENT;

    private static final int DEFAULT_TOP_COLOR = Color.parseColor("#907ADC");
    private static final int DEFAULT_LEFT_COLOR = Color.parseColor("#FF3F34");
    private static final int DEFAULT_RIGHT_COLOR = Color.parseColor("#1164B4");
    private static final int DEFAULT_BOTTOM_COLOR = Color.parseColor("#EEEE00");

    private static final int DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF");
    private static final int DEFAULT_TEXT_SIZE = 24;

    private int topColor;
    private int leftColor;
    private int rightColor;
    private int bottomColor;

    private int textColor;
    private int textSize;
    private int textPadding;

    private int drawableSize;

    private Paint paintPortion;
    private Paint paintText;

    private String choiceTop;
    private String choiceLeft;
    private String choiceRight;
    private String choiceBottom;

    private Drawable choiceImageTop;
    private Drawable choiceImageLeft;
    private Drawable choiceImageRight;
    private Drawable choiceImageBottom;

    private Path pathTop;
    private Path pathLeft;
    private Path pathRight;
    private Path pathBottom;

    private RectF bounds;
    private PointF center;

    private float topPullValue;
    private float leftPullValue;
    private float rightPullValue;
    private float bottomPullValue;

    private PullAnimator topValueAnimator;
    private PullAnimator leftValueAnimator;
    private PullAnimator rightValueAnimator;
    private PullAnimator bottomValueAnimator;

    private FortuneTellerListener fortuneTellerListener;

    private GestureDetector gestureDetector;

    public FortuneTellerView(Context context) {
        super(context);
        init(null);
    }

    public FortuneTellerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FortuneTellerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public FortuneTellerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(final AttributeSet attrs) {
        Log.d(TAG, "init");
        initStyle(attrs);

        bounds = new RectF();
        center = new PointF();

        topPullValue = 1f;
        leftPullValue = 1f;
        rightPullValue = 1f;
        bottomPullValue = 1f;

        paintPortion = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintPortion.setStyle(Paint.Style.FILL);
        setLayerType(LAYER_TYPE_SOFTWARE, paintPortion);

        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setStyle(Paint.Style.FILL);
        paintText.setColor(textColor);
        paintText.setTextSize(textSize);

        pathTop = new Path();
        pathTop.setFillType(Path.FillType.EVEN_ODD);

        pathLeft = new Path();
        pathLeft.setFillType(Path.FillType.EVEN_ODD);

        pathRight = new Path();
        pathRight.setFillType(Path.FillType.EVEN_ODD);

        pathBottom = new Path();
        pathBottom.setFillType(Path.FillType.EVEN_ODD);

        drawableSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DRAWABLE_SIZE, getResources().getDisplayMetrics());
        textPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, TEXT_PADDING, getResources().getDisplayMetrics());

        topValueAnimator = new PullAnimator(this, Direction.UP);
        leftValueAnimator = new PullAnimator(this, Direction.LEFT);
        rightValueAnimator = new PullAnimator(this, Direction.RIGHT);
        bottomValueAnimator = new PullAnimator(this, Direction.DOWN);
        gestureDetector = new GestureDetector(getContext(), swipeGestureListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(gestureDetector != null) {
            gestureDetector.onTouchEvent(event);
        }
        return true;
    }

    private void initStyle(final AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.FortuneTellerView);
            topColor = typedArray.getColor(R.styleable.FortuneTellerView_ftv_top_color, DEFAULT_TOP_COLOR);
            leftColor = typedArray.getColor(R.styleable.FortuneTellerView_ftv_left_color, DEFAULT_LEFT_COLOR);
            rightColor = typedArray.getColor(R.styleable.FortuneTellerView_ftv_right_color, DEFAULT_RIGHT_COLOR);
            bottomColor = typedArray.getColor(R.styleable.FortuneTellerView_ftv_bottom_color, DEFAULT_BOTTOM_COLOR);

            textColor = typedArray.getColor(R.styleable.FortuneTellerView_ftv_text_color, DEFAULT_TEXT_COLOR);
            textSize = (int) typedArray.getDimension(R.styleable.FortuneTellerView_ftv_text_size, DEFAULT_TEXT_SIZE);

            choiceTop = typedArray.getString(R.styleable.FortuneTellerView_ftv_top_choice);
            choiceLeft = typedArray.getString(R.styleable.FortuneTellerView_ftv_left_choice);
            choiceRight = typedArray.getString(R.styleable.FortuneTellerView_ftv_right_choice);
            choiceBottom = typedArray.getString(R.styleable.FortuneTellerView_ftv_bottom_choice);

            choiceImageTop = getResources().getDrawable(typedArray.getResourceId(R.styleable.FortuneTellerView_ftv_top_choice_image, 0));
            choiceImageLeft = getResources().getDrawable(typedArray.getResourceId(R.styleable.FortuneTellerView_ftv_left_choice_image, 0));
            choiceImageRight = getResources().getDrawable(typedArray.getResourceId(R.styleable.FortuneTellerView_ftv_right_choice_image, 0));
            choiceImageBottom = getResources().getDrawable(typedArray.getResourceId(R.styleable.FortuneTellerView_ftv_bottom_choice_image, 0));
            typedArray.recycle();
        } else {
            topColor = DEFAULT_TOP_COLOR;
            leftColor = DEFAULT_LEFT_COLOR;
            rightColor = DEFAULT_RIGHT_COLOR;
            bottomColor = DEFAULT_BOTTOM_COLOR;
            textColor = DEFAULT_TEXT_COLOR;
        }
    }

    public void setChoices(final String top, final String left, final String right, final String bottom) {
        Log.d(TAG, "setChoices");
        this.choiceTop = top;
        this.choiceLeft = left;
        this.choiceRight = right;
        this.choiceBottom = bottom;
    }

    public void setChoiceImages(final int top, final int left, final int right, final int bottom) {
        Log.d(TAG, "setChoiceImages");
        this.choiceImageTop = top != 0 ? getResources().getDrawable(top) : null;
        this.choiceImageLeft = left != 0 ? getResources().getDrawable(left) : null;
        this.choiceImageRight = right != 0 ? getResources().getDrawable(right) : null;
        this.choiceImageBottom = bottom != 0 ? getResources().getDrawable(bottom) : null;
    }

    public void setTextColor(int textColor) {
        Log.d(TAG, "setTextColor");
        this.textColor = textColor;
    }

    public void setTextSize(int textSize) {
        Log.d(TAG, "setTextSize");
        textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, getResources().getDisplayMetrics());
        paintText.setTextSize(textSize);
    }

    public void setBackgroundColors(final int top, final int left, final int right, final int bottom) {
        Log.d(TAG, "setBackgroundColors");
        this.topColor = top;
        this.leftColor = left;
        this.rightColor = right;
        this.bottomColor = bottom;
    }

    public void setFortuneTellerListener(FortuneTellerListener fortuneTellerListener) {
        Log.d(TAG, "setFortuneTellerListener");
        this.fortuneTellerListener = fortuneTellerListener;
    }

    private SwipeGestureListener swipeGestureListener = new SwipeGestureListener() {
        @Override
        public boolean onSwipe(Direction direction) {
            topPullValue = 1f;
            leftPullValue = 1f;
            rightPullValue = 1f;
            bottomPullValue = 1f;

            if(direction == Direction.UP) {
                bottomValueAnimator.startPullAnimation();
            } else if(direction == Direction.LEFT) {
                rightValueAnimator.startPullAnimation();
            } else if(direction == Direction.RIGHT) {
                leftValueAnimator.startPullAnimation();
            } else if(direction == Direction.DOWN) {
                topValueAnimator.startPullAnimation();
            }
            return false;
        }
    };

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure");

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int measuredWidth;
        if (widthMode == MeasureSpec.EXACTLY) {
            measuredWidth = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            measuredWidth = Math.min(DESIRED_WIDTH, widthSize);
        } else {
            measuredWidth = DESIRED_WIDTH;
        }

        int measuredHeight;
        if (heightMode == MeasureSpec.EXACTLY) {
            measuredHeight = heightSize;
        } else if (heightMode  == MeasureSpec.AT_MOST) {
            measuredHeight = Math.min(DESIRED_HEIGHT, heightSize);
        } else {
            measuredHeight = DESIRED_HEIGHT;
        }

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);

        int width = getWidth();
        int height = getHeight();

        pathTop.reset();
        pathTop.moveTo(0.5f * width, 0.5f * height);
        pathTop.rLineTo(-0.5f * topPullValue * width, -0.5f * topPullValue * height);
        pathTop.rLineTo(2f * 0.5f * topPullValue * width, 0);
        pathTop.close();
        paintPortion.setColor(topColor);
        canvas.drawPath(pathTop, paintPortion);

        if(!TextUtils.isEmpty(choiceTop)) {
            if(choiceImageTop != null) {
                drawTextAndImage(canvas, topPullValue, pathTop, choiceTop, choiceImageTop);
            } else {
                drawText(canvas, topPullValue, pathTop, choiceTop);
            }
        }

        pathLeft.reset();
        pathLeft.moveTo(0.5f * width, 0.5f * height);
        pathLeft.rLineTo(-0.5f * leftPullValue * width, -0.5f * leftPullValue * height);
        pathLeft.rLineTo(0, 2f * 0.5f * leftPullValue * height);
        pathLeft.close();
        paintPortion.setColor(leftColor);
        canvas.drawPath(pathLeft, paintPortion);

        if(!TextUtils.isEmpty(choiceLeft)) {
            if(choiceImageLeft != null) {
                drawTextAndImage(canvas, leftPullValue, pathLeft, choiceLeft, choiceImageLeft);
            } else {
                drawText(canvas, leftPullValue, pathLeft, choiceLeft);
            }
        }

        pathRight.reset();
        pathRight.moveTo(0.5f * width, 0.5f * height);
        pathRight.rLineTo(0.5f * rightPullValue * width, -0.5f * rightPullValue * height);
        pathRight.rLineTo(0, 2f * 0.5f * rightPullValue * height);
        pathRight.close();
        paintPortion.setColor(rightColor);
        canvas.drawPath(pathRight, paintPortion);

        if(!TextUtils.isEmpty(choiceRight)) {
            if(choiceImageRight != null) {
                drawTextAndImage(canvas, rightPullValue, pathRight, choiceRight, choiceImageRight);
            } else {
                drawText(canvas, rightPullValue, pathRight, choiceRight);
            }
        }

        pathBottom.reset();
        pathBottom.moveTo(0.5f * width, 0.5f * height);
        pathBottom.rLineTo(-0.5f * bottomPullValue * width, 0.5f * bottomPullValue * height);
        pathBottom.rLineTo(2f * 0.5f * bottomPullValue * width, 0);
        pathBottom.close();
        paintPortion.setColor(bottomColor);
        canvas.drawPath(pathBottom, paintPortion);

        if(!TextUtils.isEmpty(choiceBottom)) {
            if(choiceImageBottom != null) {
                drawTextAndImage(canvas, bottomPullValue, pathBottom, choiceBottom, choiceImageBottom);
            } else {
                drawText(canvas, bottomPullValue, pathBottom, choiceBottom);
            }
        }
    }

    private void drawText(final Canvas canvas, final float pullValue, final Path path, final String text) {
        path.computeBounds(bounds, false);
        center.set((bounds.left + bounds.right) / 2, (bounds.top + bounds.bottom) / 2);
        paintText.setTextSize(textSize * pullValue);
        float textHeight = paintText.ascent() - paintText.descent();
        float textWidth = paintText.measureText(text);
        canvas.drawText(text, center.x  - (textWidth / 2), center.y - (textHeight/ 2), paintText);
    }

    private void drawTextAndImage(final Canvas canvas, final float pullValue, final Path path, final String text, final Drawable drawable) {
        path.computeBounds(bounds, false);
        center.set((bounds.left + bounds.right) / 2, (bounds.top + bounds.bottom) / 2);

        int left = (int) (center.x - ((pullValue * drawableSize) / 2));
        int top = (int) (center.y - (pullValue * drawableSize));
        int right = (int) (center.x + ((pullValue * drawableSize) / 2));
        int bottom = (int) (top + (pullValue * drawableSize));
        drawable.setBounds(left, top, right, bottom);
        drawable.draw(canvas);

        paintText.setTextSize(textSize * pullValue);
        float textHeight = paintText.ascent() - paintText.descent();
        float textWidth = paintText.measureText(text);
        canvas.drawText(text, center.x  - (textWidth / 2), center.y - (textHeight/ 2) + textPadding, paintText);
    }

    @Override
    public void onNewPullValue(final Direction direction, float value) {
        if(direction == Direction.UP) {
            topPullValue = value;
        } else if (direction == Direction.LEFT) {
            leftPullValue = value;
        } else if (direction == Direction.RIGHT) {
            rightPullValue = value;
        } else if (direction == Direction.DOWN) {
            bottomPullValue = value;
        }
        invalidate();
    }

    @Override
    public void onFinishedAnimation(final Direction direction) {
        Log.d(TAG, "onFinishedAnimation: direction=" + direction);
        if(fortuneTellerListener != null) {
            fortuneTellerListener.onOptionChosen(direction);
        }
    }
}
