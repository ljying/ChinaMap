package com.example.nemo.mapdemo.map;

import android.content.Context;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.nemo.mapdemo.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:  svg管理
 * @author nemo
 * @version 2.0
 * @since 16/4/12
 */
class MapSVGManager {

    private static final String TAG = "ChinaMapView";

    private Context mContext;

    private static final Object Lock = new Object();

    private volatile List<ProvincePath> mProvincePathList;

    private RectF mTotalRect;

    private static MapSVGManager instance;

    private Handler mMainHandler;

    private MapSVGManager(Context context) {
        mContext = context.getApplicationContext();
        mMainHandler = new Handler(Looper.getMainLooper());
        init();
    }


    public static MapSVGManager getInstance(Context context) {
        if (instance == null) {
            synchronized (MapSVGManager.class) {
                if (instance == null) {
                    instance = new MapSVGManager(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    void getProvincePathListAsync(final Callback callback) {
        if (mProvincePathList == null) {
            new Thread(() -> {
                try {
                    synchronized (Lock) {
                        if (mProvincePathList == null) {
                            Lock.wait();
                        }
                    }
                    mMainHandler.post(() -> callback.onResult(mProvincePathList, mTotalRect));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }).start();
        } else {
            mMainHandler.post(() -> callback.onResult(mProvincePathList, mTotalRect));
        }

    }

    private void init() {
        new Thread(() -> {
            try {
                if (mProvincePathList == null) {
                    long startTime = System.currentTimeMillis();
                    if (mProvincePathList == null) {
                        List<ProvincePath> list = new ArrayList<>();
                        InputStream inputStream = mContext.getResources().openRawResource(R.raw.china);
                        XmlPullParser parser = XmlPullParserFactory.newInstance()
                                .newPullParser();
                        parser.setInput(inputStream, "utf-8");
                        int eventType;

                        float left = -1;
                        float right = -1;
                        float top = -1;
                        float bottom = -1;
                        while ((eventType = parser.getEventType()) !=
                                XmlPullParser.END_DOCUMENT) {
                            if (eventType == XmlPullParser.START_TAG) {
                                String name = parser.getName();
                                if ("path".equals(name)) {
                                    String id = parser.getAttributeValue(null, "id");
                                    String title = parser.getAttributeValue(null, "title");
                                    String pathData = parser.getAttributeValue(null, "d");
                                    ProvincePath provincePath = new ProvincePath(Integer.valueOf(id), title, pathData);
                                    Path path = provincePath.getPath();

                                    RectF rect = new RectF();
                                    path.computeBounds(rect, true);

                                    left = left == -1 ? rect.left : Math.min(left, rect.left);
                                    right = right == -1 ? rect.right : Math.max(right, rect.right);
                                    top = top == -1 ? rect.top : Math.min(top, rect.top);
                                    bottom = bottom == -1 ? rect.bottom : Math.max(bottom, rect.bottom);
                                    list.add(provincePath);
                                }
                            }
                            parser.next();
                        }
                        mTotalRect = new RectF(left, top, right, bottom);
                        mProvincePathList = list;
                    }
                    Log.i(TAG, "初始化结束->" + (System.currentTimeMillis() - startTime));


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            synchronized (Lock) {
                Lock.notifyAll();
            }
        }).start();
    }

    interface Callback {

        void onResult(List<ProvincePath> provincePathList, RectF size);

    }


}
