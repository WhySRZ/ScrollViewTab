package test.srz.com.testbar;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import test.srz.com.testbar.bean.TextBean;
import test.srz.com.testbar.holder.TextHolder;
import test.srz.com.testbar.view.ObservableScrollView;
import test.srz.com.testbar.view.MyStepsView;


public class MainActivity
        extends AppCompatActivity
        implements ObservableScrollView.ScrollViewListener

{
    private MultiTypeAdapter mTypeAdapter;
    private Items            mItems;
    private MultiTypeAdapter mSecondTypeAdapter;
    private Items            mSecondItems;
    private static final String TAG = "MainActivity";
    private int mY = -1;
    private FrameLayout          wrapperFl;
    private MyStepsView          mStepsView;
    private ObservableScrollView scrollView;
    private LinearLayout         containerLl;
    private boolean firstAlreadyInflated = true;
    private ViewGroup firstFloorVg;
    private ViewGroup secondFloorVg;
    private ViewGroup thirdFloorVg;
    private ViewGroup fourthFloorVg;
    private int secondFloorVgPositionDistance;//第二层滑动至顶部的距离
    private int thirdFloorVgPositionDistance;
    private int fourthFloorVgPositionDistance;
    private int currentPosition = 0;
    private boolean tabInterceptTouchEventTag = true;//标志位，用来区分是点击了tab还是手动滑动scrollview
    RecyclerView mRecyclerView;
    RecyclerView mSecondRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListeners();
    }

    private void initViews() {
        wrapperFl = (FrameLayout) findViewById(R.id.wrapperFl);
        mStepsView = (MyStepsView) findViewById(R.id.step_view);
        scrollView = (ObservableScrollView) findViewById(R.id.scrollView);
        containerLl = (LinearLayout) findViewById(R.id.containerLl);
        /*for (int i = 0; i < 4; i++) {
            tabLayout.addTab(tabLayout.newTab().setText("tab" + (i + 1)));
        }*/
        mStepsView.setStepsCount(4);

        firstFloorVg = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.item_floor_first, null);
        mRecyclerView = firstFloorVg.findViewById(R.id.recycler_view);
        initFirstView();
        secondFloorVg = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.item_floor_second, null);
        mSecondRecyclerView = secondFloorVg.findViewById(R.id.second_recycle);
        initSecondView();
        thirdFloorVg = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.item_floor_third, null);
        fourthFloorVg = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.item_floor_fourth, null);

        containerLl.addView(firstFloorVg);
        containerLl.addView(secondFloorVg);
        containerLl.addView(thirdFloorVg);
        containerLl.addView(fourthFloorVg);

        MeasureHeight(firstFloorVg);
        MeasureHeight(secondFloorVg);
        MeasureHeight(thirdFloorVg);
        Log.d(TAG, "2第一层测量的高度为：" + secondFloorVgPositionDistance);
        Log.d(TAG, "2第二层测量的高度为：" + thirdFloorVgPositionDistance);
        Log.d(TAG, "2第三层测量的高度为：" + fourthFloorVgPositionDistance);

    }

    private void initSecondView() {
        mSecondTypeAdapter = new MultiTypeAdapter();
        mSecondTypeAdapter.register(TextBean.class,new TextHolder(MainActivity.this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        mSecondRecyclerView.setLayoutManager(layoutManager);
        mSecondRecyclerView.setHasFixedSize(true);
        mSecondRecyclerView.setNestedScrollingEnabled(false);
        mSecondRecyclerView.setAdapter(mSecondTypeAdapter);
        mSecondItems = new Items();

        for (int i = 0; i < 20; i++) {

            TextBean bean = new TextBean("第二层" +i);
            mSecondItems.add(bean);

        }
        mSecondTypeAdapter.setItems(mSecondItems);
        mSecondTypeAdapter.notifyDataSetChanged();

    }

    private void initFirstView() {
        mTypeAdapter = new MultiTypeAdapter();
        mTypeAdapter.register(TextBean.class, new TextHolder(MainActivity.this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(mTypeAdapter);
        mItems = new Items();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 50; i++){
                    TextBean bean = new TextBean("数据" + i);
                    mItems.add(bean);
                }
                mTypeAdapter.setItems(mItems);
                mTypeAdapter.notifyDataSetChanged();

               /* firstFloorVg.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        firstFloorVg.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        int height = firstFloorVg.getHeight();
                        int[] firstFloorVgPosition = new int[2];
                        firstFloorVg.getLocationOnScreen(firstFloorVgPosition);
                        Log.d(TAG, "第一层测量的高度为：" + height + "屏距离" + firstFloorVgPosition[1]);
                    }
                });*/
               MeasureHeight(firstFloorVg);
               MeasureHeight(secondFloorVg);
               MeasureHeight(thirdFloorVg);
                Log.d(TAG, "第一层测量的高度为：" + secondFloorVgPositionDistance);
                Log.d(TAG, "第二层测量的高度为：" + thirdFloorVgPositionDistance);
                Log.d(TAG, "第三层测量的高度为：" + fourthFloorVgPositionDistance);
               //MeasureHeight(fourthFloorVg);




            }
        },1000);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

       // Log.d("firstAlreadyInflated", " firstAlreadyInflated" + firstAlreadyInflated);

        if (firstAlreadyInflated) {//获取各层离screen顶部的位置以及计算滑动值相应顶部所需要的距离

            //getMeasure();
        }
    }

    /*private void getMeasure() {

        firstAlreadyInflated = false;
        int[] firstFloorVgPosition = new int[2];
        int[] secondFloorVgPosition = new int[2];
        int[] thirdFloorVgPosition = new int[2];
        int[] fourthFloorVgPosition = new int[2];
        firstFloorVg.getLocationOnScreen(firstFloorVgPosition);
        secondFloorVg.getLocationOnScreen(secondFloorVgPosition);
        thirdFloorVg.getLocationOnScreen(thirdFloorVgPosition);
        fourthFloorVg.getLocationOnScreen(fourthFloorVgPosition);
        int firstFloorVgPositionAnchor = firstFloorVgPosition[1];
        int secondFloorVgPositionAnchor = secondFloorVgPosition[1];
        int thirdFloorVgPositionAnchor = thirdFloorVgPosition[1];
        int fourthFloorVgPositionAnchor = fourthFloorVgPosition[1];

        Log.d(TAG, "第一层距离屏幕的距离是：" + firstFloorVgPosition[1]);
        Log.d(TAG, "第二层距离屏幕的距离是：" + secondFloorVgPosition[1]);
        Log.d(TAG, "第三层距离屏幕的距离是：" + thirdFloorVgPosition[1]);
        Log.d(TAG, "第四层距离屏幕的距离是：" + fourthFloorVgPosition[1]);

        secondFloorVgPositionDistance = secondFloorVgPositionAnchor - firstFloorVgPositionAnchor;
        thirdFloorVgPositionDistance = thirdFloorVgPositionAnchor - firstFloorVgPositionAnchor;
        fourthFloorVgPositionDistance = fourthFloorVgPositionAnchor - firstFloorVgPositionAnchor;
    }*/

    private void initListeners() {
        wrapperFl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG,"wrapperFl onTouch");
                tabInterceptTouchEventTag = true;//让tab来处理滑动
                return false;
            }
        });
        /*tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentPosition = tab.getPosition();
                if(!tabInterceptTouchEventTag){//手动滑动页面时则不再次处理滑动
                    return;
                }
                scrollView.computeScroll();
                switch (currentPosition) {
                    case 0:
                        scrollView.smoothScrollTo(0, 0);
                        break;
                    case 1:
                        scrollView.smoothScrollTo(0, secondFloorVgPositionDistance);
                        break;
                    case 2:
                        scrollView.smoothScrollTo(0, thirdFloorVgPositionDistance);
                        break;
                    case 3:
                        scrollView.smoothScrollTo(0, fourthFloorVgPositionDistance);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/

        mStepsView.setAddViewSelectListener(new MyStepsView.addViewSelectListener() {
            @Override
            public void viewSelect(View view) {
                currentPosition = view.getId();

                Log.d("currentPosition", " currentPosition;" + currentPosition);
                if(!tabInterceptTouchEventTag){//手动滑动页面时则不再次处理滑动
                    return;
                }
                scrollView.computeScroll();
                switch (currentPosition) {
                    case 0:
                        scrollView.smoothScrollTo(0, 0);
                        break;
                    case 1:
                        scrollView.smoothScrollTo(0, secondFloorVgPositionDistance);
                        break;
                    case 2:
                        scrollView.smoothScrollTo(0, thirdFloorVgPositionDistance);
                        break;
                    case 3:
                        scrollView.smoothScrollTo(0, fourthFloorVgPositionDistance);
                        break;
                    default:
                        break;
                }
            }
        });
        scrollView.setScrollViewListener(this);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "scrollView onTouch");
                tabInterceptTouchEventTag = false;//让scrollview处理滑动
                return false;
            }
        });
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (tabInterceptTouchEventTag) {//让tab来处理滑动
            return;
        }
        if (mY == y){
            return;
        }else {
            mY = y;
        }
        Log.d(TAG, "当前scrollView的位置——>" + y);
        if (y < secondFloorVgPositionDistance) {
            if (currentPosition != 0) {
                scrollView.computeScroll();
                mStepsView.setCurrentStep(0);
                Log.d("mStepView", " mStepsView.setCurrentStep(0);");
            }
        } else if (y < thirdFloorVgPositionDistance) {
            if (currentPosition != 1) {
                scrollView.computeScroll();
                mStepsView.setCurrentStep(1);
                Log.d("mStepView", " mStepsView.setCurrentStep(1);");
            }
        } else if (y < fourthFloorVgPositionDistance) {
            if (currentPosition != 2) {
                scrollView.computeScroll();
                mStepsView.setCurrentStep(2);
                Log.d("mStepView", " mStepsView.setCurrentStep(2);");
            }
        } else {
            if (currentPosition != 3) {
                scrollView.computeScroll();
                mStepsView.setCurrentStep(3);
                Log.d("mStepView", " mStepsView.setCurrentStep(3);");
            }
        }
    }


    public void MeasureHeight(final ViewGroup viewGroup){


        viewGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int height = viewGroup.getHeight();
                int [] four = new int[2];
                viewGroup.getLocationOnScreen(four);

                if (viewGroup == firstFloorVg){
                    secondFloorVgPositionDistance = height;
                }else if (viewGroup == secondFloorVg){
                    thirdFloorVgPositionDistance = height + secondFloorVgPositionDistance;
                }else if (viewGroup == thirdFloorVg){
                    fourthFloorVgPositionDistance = height + thirdFloorVgPositionDistance;
                }
                Log.d(TAG, "第二层测量的高度为：" + height + " 屏幕距离" + four[1]);
            }
        });
    }
}
