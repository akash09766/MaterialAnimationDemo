package avantari.com.materilaanimationtask.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import avantari.com.materilaanimationtask.R;
import avantari.com.materilaanimationtask.custom.TransitionHelper;
import me.itangqi.waveloadingview.WaveLoadingView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton mGraphBtn;
    private TextView mCurrentMsg;
    private Animation txtScaleUp;
    private TextView mCurrentValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setIdsToViews();
        setListenerToViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCurrentMsg.setVisibility(View.VISIBLE);
        mCurrentMsg.startAnimation(txtScaleUp);
        final double all[] =  {92.3,95,100,110,122,135,136,145,150,152,160,161,166,170,171,174,175,179,180.9};

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            int i = 18;
            @Override
            public void run() {
                if(0 <= i){
                    mCurrentValue.setText(String.valueOf(all[i]));
                    i--;
                    handler.postDelayed(this, 40);
                }
            }
        });
    }


    private void setIdsToViews() {

        mCurrentMsg = (TextView) findViewById(R.id.current_msg_tv_id_);
        mCurrentValue = (TextView) findViewById(R.id.current_value_tv_id);
        WaveLoadingView mWaveLoadingView = (WaveLoadingView) findViewById(R.id.waveLoadingView);
        mGraphBtn = (FloatingActionButton) findViewById(R.id.graph_btn_id);
        txtScaleUp = AnimationUtils.loadAnimation(this, R.anim.bottom_up);
    }

    private void setListenerToViews() {
        mGraphBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        transitionToActivity(ChartActivity.class, mGraphBtn, R.string.transition_reveal1);
    }

    private void transitionToActivity(Class target, View viewHolder, int transitionName) {
        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(this, false,
                new Pair<>(viewHolder, getString(transitionName)));
        startActivity(target, pairs);
    }

    private void startActivity(Class target, Pair<View, String>[] pairs) {
        Intent i = new Intent(this, target);
        ActivityOptionsCompat transitionActivityOptions =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairs);
        startActivity(i, transitionActivityOptions.toBundle());
    }
}
