package com.szip.smartdream.Controller;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.szip.smartdream.Bean.HttpBean.ClockData;
import com.szip.smartdream.MyApplication;
import com.szip.smartdream.R;
import com.szip.smartdream.Util.DateUtil;
import com.szip.smartdream.View.PulldownUpdateView;

import static android.media.AudioManager.FLAG_PLAY_SOUND;
import static android.media.AudioManager.STREAM_MUSIC;

public class ClockRunningActivity extends BaseActivity {

    private MediaPlayer mediaPlayer;
    private int volume;
    private AudioManager am;
    private PowerManager pm;
    private PowerManager.WakeLock mWakelock;
    private PulldownUpdateView updateView;
    private TextView timeTv;
    private TextView typeTv;
    private int pos;
    private int playTimes = 0;

    private MyApplication app;

    private PowrBroadcast broadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_clock_running);
        app = (MyApplication) getApplicationContext();
        Intent intent = getIntent();
        pos = intent.getIntExtra("pos",0);
        initView();
        initBroad();
        //初始化音乐播放

        am=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        volume = am.getStreamVolume(STREAM_MUSIC);//保存手机原来的音量
        am.setStreamVolume (STREAM_MUSIC, am.getStreamMaxVolume(STREAM_MUSIC), FLAG_PLAY_SOUND);//设置系统音乐最大
        if (pos!=-1) {
            mediaPlayer = MediaPlayer.create(this, R.raw.dang_ring);

        } else{
            mediaPlayer = MediaPlayer.create(this, R.raw.bugu);
            ((TextView)findViewById(R.id.tipTv)).setText(getString(R.string.cencelClock1));
        }
        mediaPlayer.start();
        mediaPlayer.setVolume(1f,1f);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (pos!=-1){
                    if (playTimes!=15){
                        mediaPlayer.start();
                        playTimes++;
                    }else {
                        playTimes = 0;
                        mediaPlayer.stop();
                        am.setStreamVolume (STREAM_MUSIC, volume, FLAG_PLAY_SOUND);//播放完毕，设置回之前的音量
                        finish();
                    }
                }else {
                    mediaPlayer.stop();
                    am.setStreamVolume (STREAM_MUSIC, volume, FLAG_PLAY_SOUND);//播放完毕，设置回之前的音量
                }


            }
        });
    }

    private void initBroad() {
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        broadcast = new PowrBroadcast();
        registerReceiver(broadcast,mIntentFilter);
    }

    @SuppressLint("InvalidWakeLockTag")
    private void initView() {
        //配置锁屏的时候亮屏
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.SCREEN_DIM_WAKE_LOCK, "SimpleTimer");

        //动画
        ImageView iv = findViewById(R.id.cancelIv);
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(iv,"scaleX",1.2f,0.8f);
        anim1.setRepeatCount(-1);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(iv,"scaleY",1.2f,0.8f);
        anim2.setRepeatCount(-1);
        AnimatorSet set = new AnimatorSet();
        set.play(anim1).with(anim2);
        set.setDuration(1000);
        set.start();

        //可滑动的控件
        updateView = findViewById(R.id.pullView);
        updateView.setListener(new PulldownUpdateView.PulldownListener() {
            @Override
            public void updateNow() {
                mediaPlayer.stop();
                am.setStreamVolume (STREAM_MUSIC, volume, FLAG_PLAY_SOUND);//播放完毕，设置回之前的音量
                finish();
            }
        });


        typeTv = findViewById(R.id.typeTv);
        if (pos!=-1){
            timeTv = findViewById(R.id.timeTv);
            timeTv.setText(DateUtil.getTimeNow());
            ClockData clockData = app.getClockList().get(pos);
            if (clockData.getType() == 1){
                typeTv.setText(clockData.getRemark()+" "+getString(R.string.nurseTime));
            }else if (clockData.getType() == 2){
                typeTv.setText(getString(R.string.awakeTime));
            }else {
                typeTv.setText(getString(R.string.sleepTime));
            }
        }else
            typeTv.setText(getString(R.string.wakeupNow));

    }

    @Override
    protected void onResume() {
        super.onResume();
        mWakelock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWakelock.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        am.setStreamVolume (STREAM_MUSIC, volume, FLAG_PLAY_SOUND);//播放完毕，设置回之前的音量
    }

    public class PowrBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            PowerManager pm =(PowerManager)context.getSystemService(Context.POWER_SERVICE);
            if(!pm.isScreenOn()){
                mediaPlayer.stop();
                am.setStreamVolume (STREAM_MUSIC, volume, FLAG_PLAY_SOUND);//播放完毕，设置回之前的音量
                ClockRunningActivity.this.finish();
            }
        }

    }



}
