package com.brzhang.chainmap.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Typeface;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


import com.brzhang.chainmap.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:  mapview
 *
 * @author nemo
 * @version 2.0
 * @since 16/4/12
 */
public class ChinaMapView extends View {

    private static final String TAG = "ChinaMapView";

    private Paint paint;

    private int miniWidth;
    private int miniHeight;
    private int provinceTextSize;
    private int provinceMargin;
    private int numberMargin;
    private int bottomPadding;

    private float scale = 1;

    private RectF mapSize;

    private ProvinceItem selectedItem;


    /*原始省份路径*/
    private List<ProvinceItem> provincePathInfoItems;

    /*使用来着色的信息*/
    private Collection mColoredDataList;


    public void setmColorStrategy(IColorStrategy mColorStrategy) {
        this.mColorStrategy = mColorStrategy;
    }

    private IColorStrategy mColorStrategy;

    /*标尺信息*/
    private Bitmap mBitmapRule;

    private GestureDetectorCompat gestureDetectorCompat;

    public ChinaMapView(Context context) {
        super(context);
        init(null, 0);
    }

    public ChinaMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ChinaMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    /**
     * 初始化加载地图数据并设置相关手势监听
     *
     * @param attrs    属性
     * @param defStyle 默认属性
     */
    private void init(AttributeSet attrs, int defStyle) {
        paint = new Paint();
        paint.setAntiAlias(true);
        miniWidth = getContext().getResources().getDimensionPixelSize(R.dimen.map_min_width);
        miniHeight = getContext().getResources().getDimensionPixelSize(R.dimen.map_min_height);
        provinceTextSize = getResources().getDimensionPixelSize(R.dimen.map_province_text_size);
        provinceMargin = getResources().getDimensionPixelSize(R.dimen.map_province_margin);
        numberMargin = getResources().getDimensionPixelSize(R.dimen.map_number_margin);

        gestureDetectorCompat = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDown(MotionEvent e) {
                float x = e.getX();
                float y = e.getY();
                handlerTouch((int) x, (int) y);
                return true;
            }
//
//            @Override
//            public boolean onSingleTapUp(MotionEvent e) {
//                float x = e.getX();
//                float y = e.getY();
//                return handlerTouch((int) x, (int) y);
//            }

//            @Override
//            public void onShowPress(MotionEvent e) {
//                float x = e.getX();
//                float y = e.getY();
//                handlerTouch((int) x, (int) y);
//            }
        });
        mBitmapRule = BitmapFactory.decodeResource(getResources(), R.drawable.scale_rule);

        bottomPadding = getResources().getDimensionPixelSize(R.dimen.map_bottom_padding);

        if (!isInEditMode()) {
            //获取地图svg封装信息
            MapSVGManager.getInstance(getContext()).getProvincePathListAsync(new MapSVGManager.Callback() {
                @Override
                public void onResult(List<ProvincePath> provincePathList, RectF size) {
                    List<ProvinceItem> list = new ArrayList<>();
                    for (ProvincePath provincePath : provincePathList) {
                        ProvinceItem item = new ProvinceItem();
                        item.setPath(provincePath.getPath());
                        item.setProvinceCode(provincePath.getCode());
                        item.setProvinceName(provincePath.getName());
                        list.add(item);
                    }
                    if (mColoredDataList != null && mColorStrategy != null) {
                        mColorStrategy.setMapColor(list, mColoredDataList);
                    }
                    mapSize = size;
                    provincePathInfoItems = list;

                    //刷新布局
                    requestLayout();
                    postInvalidate();
                }
            });
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int viewWidth = width;
        int viewHeight = height;

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                viewWidth = width > miniWidth ? width : miniWidth;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                viewWidth = miniWidth;
                break;
        }

        int computeHeight;
        if (mapSize != null) {
            double mapWidth = mapSize.width();
            double mapHeight = mapSize.height();
            scale = (float) (viewWidth / mapWidth);
            computeHeight = (int) (mapHeight * viewWidth / mapWidth);
        } else {
            computeHeight = (miniHeight * viewWidth / miniWidth);
        }

        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                viewHeight = height;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                viewHeight = miniHeight > computeHeight ? miniHeight : computeHeight;
                break;
        }

        if (mapSize != null) {
            double mapWidth = mapSize.width();
            scale = (float) (viewWidth / mapWidth);
        }

        setMeasuredDimension(MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(viewHeight + bottomPadding, MeasureSpec.EXACTLY));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetectorCompat.onTouchEvent(event);
    }

    /**
     * 处理手势触摸
     *
     * @param x 当前x
     * @param y 当前y
     * @return 是否触摸到区域部分
     */
    private boolean handlerTouch(int x, int y) {
        ProvinceItem provinceItem = null;
        final List<ProvinceItem> list = provincePathInfoItems;
        if (list == null) {
            return false;
        }
        for (ProvinceItem temp : list) {
            if (temp.isTouched((int) (x / scale), (int) (y / scale))) {
                provinceItem = temp;
                break;
            }
        }

        if (provinceItem != null && !provinceItem.equals(selectedItem)) {
            selectedItem = provinceItem;
            postInvalidate();
        }
        return provinceItem != null;
    }

    /**
     * 设置显示数据
     *
     * @param list 加载数据集合
     */
    public <T> void setData(Collection<T> list) {
        if (provincePathInfoItems != null) {
            //重新设置绘制区域信息
            if (mColorStrategy != null) {
                mColorStrategy.setMapColor(provincePathInfoItems, list);
            }
            postInvalidate();
        }
        mColoredDataList = list;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final List<ProvinceItem> list = provincePathInfoItems;
        if (list != null) {

            int width = getWidth();
            int height = getHeight();
            canvas.save();
            canvas.scale(scale, scale);
            for (ProvinceItem item : list) {
                if (!item.equals(selectedItem)) {
                    item.drawItem(canvas, paint, false);
                }
            }
            if (selectedItem != null) {
                selectedItem.drawItem(canvas, paint, true);
            }

            canvas.restore();

            if (selectedItem != null) {

                paint.setTypeface(Typeface.DEFAULT);
                paint.setColor(0xFF333333);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.clearShadowLayer();
                paint.setTextSize(provinceTextSize);

                if (mColorStrategy instanceof ValueColorStrategy) {
                     /*隐藏不绘制身份多少人*/
//                    String provinceName = selectedItem.getProvinceName();
//                    canvas.drawText(provinceName, width / 2, provinceMargin, paint);
//                    int number = selectedItem.getPersonNumber();
//                    canvas.drawText(number + "人", width / 2, provinceMargin + provinceTextSize + numberMargin, paint);
                }
            }
            /*隐藏标尺*/
//            canvas.drawBitmap(mBitmapRule, 0, height - mBitmapRule.getHeight(), paint);
        }
    }

    /**
     * 地图绘制省份区域信息
     */
    static class ProvinceItem {
        /**
         * 区域路径
         */
        private Path path;

        /**
         * 区域背景色，默认白色
         */
        private int drawColor = Color.WHITE;

        /**
         * 区域省份名称
         */
        private String provinceName;

        /**
         * 区域省份编码
         */
        private int provinceCode;

        /**
         * 区域省份人数
         */
        private int personNumber;

        /**
         * 区域绘制方法
         *
         * @param canvas     画布
         * @param paint      画笔
         * @param isSelected 是否选中
         */
        void drawItem(Canvas canvas, Paint paint, boolean isSelected) {

            //选中时绘制阴影描边效果
            if (isSelected) {
                paint.setStrokeWidth(2);
                paint.setColor(Color.BLACK);
                paint.setStyle(Paint.Style.FILL);
                paint.setShadowLayer(8, 0, 0, 0xFFFFFFFF);
                canvas.drawPath(path, paint);

                paint.clearShadowLayer();
                paint.setColor(drawColor);
                paint.setStyle(Paint.Style.FILL);
                paint.setStrokeWidth(2);
                canvas.drawPath(path, paint);

            } else {
                //非选中时，绘制描边效果
                paint.clearShadowLayer();
                paint.setStrokeWidth(1);
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(drawColor);
                canvas.drawPath(path, paint);

                paint.setStyle(Paint.Style.STROKE);
                int strokeColor = 0xFFD0E8F4;
                paint.setColor(strokeColor);
                canvas.drawPath(path, paint);
            }
        }

        /**
         * 判断该区域是否处于touch状态
         *
         * @param x 当前x
         * @param y 当前y
         * @return 是否处于touch状态
         */
        boolean isTouched(int x, int y) {
            RectF r = new RectF();
            path.computeBounds(r, true);

            Region region = new Region();
            region.setPath(path, new Region((int) r.left, (int) r.top, (int) r.right, (int) r.bottom));
            return region.contains(x, y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ProvinceItem item = (ProvinceItem) o;

            return provinceCode == item.provinceCode;

        }

        @Override
        public int hashCode() {
            return provinceCode;
        }

        public Path getPath() {
            return path;
        }

        void setPath(Path path) {
            this.path = path;
        }

        public int getDrawColor() {
            return drawColor;
        }

        void setDrawColor(int drawColor) {
            this.drawColor = drawColor;
        }

        String getProvinceName() {
            return provinceName;
        }

        void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        int getProvinceCode() {
            return provinceCode;
        }

        void setProvinceCode(int provinceCode) {
            this.provinceCode = provinceCode;
        }

        int getPersonNumber() {
            return personNumber;
        }

        void setPersonNumber(int personNumber) {
            this.personNumber = personNumber;
        }
    }
}
