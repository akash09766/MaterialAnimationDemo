package avantari.com.materilaanimationtask.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

import avantari.com.materilaanimationtask.model.ChartData;
import avantari.com.materilaanimationtask.R;

/**
 * Created by Akash Wangalwar on 25-01-2017.
 */
public class ChartActivity extends AppCompatActivity implements View.OnClickListener {

    private Interpolator interpolator;
    private RelativeLayout mRevealView;
    private String TAG = ChartActivity.class.getSimpleName();
    private ImageView mageView;
    private BarChart chartlayout;
    private RelativeLayout mTitleView;
    private ImageView mBackBtn;
    private Animation animationScaleUp;
    private TextView mCurrentMsg;
    private Animation txtScaleUp;
    private TextView mCurrentvalue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chart_activity_layout);
        setIdsToViews();
        setListenerToViews();
        setupWindowAnimations();
    }

    private void setIdsToViews() {
        chartlayout = (BarChart) findViewById(R.id.graph_layout);
        mRevealView = (RelativeLayout) findViewById(R.id.graph_view_id);
        mTitleView = (RelativeLayout) findViewById(R.id.title_view_id);
        mCurrentMsg = (TextView) findViewById(R.id.current_msg_tv_id);
        mCurrentvalue = (TextView) findViewById(R.id.current_value_tv_id);
        mBackBtn = (ImageView) findViewById(R.id.back_btn_id);
        mageView = (ImageView) findViewById(R.id.shared_target);
        animationScaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_ip);
        txtScaleUp = AnimationUtils.loadAnimation(this, R.anim.bottom_up);
    }

    private void setListenerToViews() {
        mBackBtn.setOnClickListener(this);
    }

    private void setupWindowAnimations() {
        interpolator = AnimationUtils.loadInterpolator(this, android.R.interpolator.linear_out_slow_in);
        setupEnterAnimations();
        setupExitAnimations();
    }

    private void hideTarget() {
        mageView.setVisibility(View.INVISIBLE);
    }

    private void animateRevealShow(View viewRoot) {
        int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
        int finalRadius = Math.max(viewRoot.getWidth(), viewRoot.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, 0, finalRadius);
        viewRoot.setBackgroundColor(ContextCompat.getColor(this, R.color.orange));
        viewRoot.setVisibility(View.VISIBLE);
        anim.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        anim.setInterpolator(new AccelerateInterpolator());
        anim.start();
    }

    private void setupEnterAnimations() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.changebounds_with_arcmotion);
        getWindow().setSharedElementEnterTransition(transition);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                // Removing listener here is very important because shared element transition is executed again backwards on exit. If we don't remove the listener this code will be triggered again.
                transition.removeListener(this);
                hideTarget();
                animateRevealShow(mRevealView);
                mTitleView.setVisibility(View.VISIBLE);
                addDataToWtChart();
                mBackBtn.setVisibility(View.VISIBLE);
                mBackBtn.startAnimation(animationScaleUp);
                mCurrentMsg.setVisibility(View.VISIBLE);
                mCurrentMsg.startAnimation(txtScaleUp);

                startCount();
            }

            @Override
            public void onTransitionCancel(Transition transition) {
            }

            @Override
            public void onTransitionPause(Transition transition) {
            }

            @Override
            public void onTransitionResume(Transition transition) {
            }
        });
    }

    private void startCount() {

        final double all[] = {92.3, 95, 100, 110, 122, 135, 136, 145, 150, 152, 160, 161, 166, 170, 171, 174, 175, 179, 180.9};

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            int i = 0;

            @Override
            public void run() {
                if (all.length > i) {
                    mCurrentvalue.setText(String.valueOf(all[i]));
                    i++;
                    handler.postDelayed(this, 40);
                }
            }
        });
    }


    private void animateRevealHide(final View viewRoot) {
        int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
        int initialRadius = viewRoot.getWidth();

        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, initialRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                viewRoot.setVisibility(View.INVISIBLE);
            }
        });
        anim.setDuration(getResources().getInteger(R.integer.anim_duration_medium_));
        anim.start();
    }

    private void setupExitAnimations() {
        Fade returnTransition = new Fade();
        getWindow().setReturnTransition(returnTransition);
        returnTransition.setDuration(getResources().getInteger(R.integer.anim_duration_medium));
        returnTransition.setStartDelay(getResources().getInteger(R.integer.anim_duration_medium));
        returnTransition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                transition.removeListener(this);
//                mageView.setImageResource(R.drawable.bar);
//                animateButtonsOut();
                animateRevealHide(mRevealView);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
            }

            @Override
            public void onTransitionCancel(Transition transition) {
            }

            @Override
            public void onTransitionPause(Transition transition) {
            }

            @Override
            public void onTransitionResume(Transition transition) {
            }
        });
    }


    private void addDataToWtChart() {
        chartlayout.setVisibility(View.VISIBLE);
        chartlayout.getAxisRight().setEnabled(false);
        chartlayout.getDescription().setEnabled(false);
        chartlayout.setMaxVisibleValueCount(1);

        XAxis xAxis = chartlayout.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(true);
        xAxis.setGridColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
//        xAxis.setDrawLabels(false);

        YAxis leftAxis = chartlayout.getAxisLeft();
        leftAxis.setDrawAxisLine(true);
        leftAxis.setGridColor(Color.WHITE);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        ArrayList<BarEntry> valsComp2 = new ArrayList<BarEntry>();

        List<ChartData> data = new ArrayList<>();
        data.add(new ChartData("2", "10"));
        data.add(new ChartData("4", "50"));
        data.add(new ChartData("6", "110"));
        data.add(new ChartData("8", "90"));
        data.add(new ChartData("10", "60"));
        data.add(new ChartData("12", "180"));
        data.add(new ChartData("14", "110"));
        data.add(new ChartData("16", "10"));
        data.add(new ChartData("18", "50"));
        data.add(new ChartData("20", "110"));

        for (int i = 0; i < data.size(); i++) {
            ChartData info = data.get(i);
            BarEntry c2e1 = new BarEntry(Integer.valueOf(info.getxVal()), Integer.valueOf(info.getyVal())); // 0 == quarter 1
            valsComp2.add(c2e1);
        }

        BarDataSet barDataSet2 = new BarDataSet(valsComp2, "");
        barDataSet2.setHighlightEnabled(false);
        barDataSet2.setValueTextColor(Color.WHITE);
        barDataSet2.setHighlightEnabled(true);
//        barDataSet2.setColor(R.color.white);
        barDataSet2.setColor(getResources().getColor(R.color.white));

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet2);

        BarData data1 = new BarData(dataSets);
        chartlayout.setData(data1);

        chartlayout.invalidate();
        chartlayout.setDescription(null);
        chartlayout.setPinchZoom(false);
        chartlayout.setDrawValueAboveBar(true);
        chartlayout.animateXY(2000,2000);
    }

    @Override
    public void onClick(View view) {
        mCurrentMsg.setVisibility(View.INVISIBLE);
        finishAfterTransition();
    }

    @Override
    public void onBackPressed() {
        mCurrentMsg.setVisibility(View.INVISIBLE);
        finishAfterTransition();
    }
}