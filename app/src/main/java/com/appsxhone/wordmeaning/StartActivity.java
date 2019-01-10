package com.appsxhone.wordmeaning;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class StartActivity extends Activity {

    private ImageButton playButton, settingButton, rateButton, changeButton, leaderboardButton;
    ImageView splash;
    DonutProgress donutProgress;
    private TextView scoreMainActivity, hintButtonMainActivity;
    Animation animBlink, animSlideUp, animMoveFromRight, animMoveFromLeft, animBounceFromLeft,animBounceFromRight;

    FragmentManager fragmentManager = getFragmentManager();
    private UserPreferences userPreferences;
    int score = 0, hint;
    SoundPlayer soundPlayer;

    RelativeLayout relativeLayout;

    private int[] LEAVES = {R.mipmap.a_01, R.mipmap.b_01, R.mipmap.c_01, R.mipmap.d_01, R.mipmap.e_01,
            R.mipmap.f_01, R.mipmap.g_01, R.mipmap.h_01, R.mipmap.i_01, R.mipmap.j_01, R.mipmap.k_01,
            R.mipmap.l_01, R.mipmap.m_01, R.mipmap.n_01, R.mipmap.o_01, R.mipmap.p_01, R.mipmap.q_01,
            R.mipmap.r_01, R.mipmap.s_01, R.mipmap.t_01, R.mipmap.u_01, R.mipmap.v_01, R.mipmap.w_01,
            R.mipmap.x_01, R.mipmap.y_01, R.mipmap.z_01};

    private Rect mDisplaySize = new Rect();

    private RelativeLayout mRootLayout;
    private ArrayList<View> mAllImageViews = new ArrayList<>();

    private float mScale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayoutStartActivity);

        AppRater.app_launched(this);

        soundPlayer = new SoundPlayer(getApplicationContext());

        playButton = (ImageButton) findViewById(R.id.btnPlay);
        settingButton = (ImageButton) findViewById(R.id.btnSettings);
        rateButton = (ImageButton) findViewById(R.id.btnRate);
        changeButton = (ImageButton) findViewById(R.id.btnChallenge);
        leaderboardButton = (ImageButton) findViewById(R.id.btnLeaderboard);
        splash = (ImageView) findViewById(R.id.splash);

        userPreferences = UserPreferences.getInstance(getApplicationContext());

        donutProgress = (DonutProgress) findViewById(R.id.txtProgress);
        donutProgress.setMax(100);
        donutProgress.setProgress((100 * userPreferences.getProgress()) / 1725);

        scoreMainActivity = (TextView) findViewById(R.id.txtScore);
        score = userPreferences.getScore();
        scoreMainActivity.setText(String.valueOf(score));
        scoreMainActivity.setTypeface(Typeface.createFromAsset(getAssets(), "ARCENA.ttf"));

        hintButtonMainActivity = (TextView) findViewById(R.id.btnHint);
        hint = userPreferences.getHint();
        hintButtonMainActivity.setText(String.valueOf(hint));
        hintButtonMainActivity.setTypeface(Typeface.createFromAsset(getAssets(), "ARCENA.ttf"));

        animation();

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LevelActivity.class));
                finish();
            }
        });

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SettingsDialogFragment settingsFragment = new SettingsDialogFragment();
                settingsFragment.show(fragmentManager, "Settings");
            }
        });

        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.appsxhone.wordmeaning")));
            }
        });

        Display display = getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getRectSize(mDisplaySize);
        }

        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        mScale = metrics.density;

        mRootLayout = (RelativeLayout) findViewById(R.id.main_layout);

        new Timer().schedule(new ExeTimerTask(), 0, 5000);

    }

    @Override
    protected void onPause() {
        super.onPause();
        soundPlayer.pauseSound(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        soundPlayer.resumeSound(getApplicationContext());
        userPreferences = UserPreferences.getInstance(getApplicationContext());

        scoreMainActivity = (TextView) findViewById(R.id.txtScore);
        int score = userPreferences.getScore();
        scoreMainActivity.setText(String.valueOf(score));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPlayer.stopSound(getApplicationContext());
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int viewId = new Random().nextInt(LEAVES.length);
            Drawable d = ContextCompat.getDrawable(getApplicationContext(), LEAVES[viewId]);
            LayoutInflater inflate = LayoutInflater.from(StartActivity.this);
            ImageView imageView = (ImageView) inflate.inflate(R.layout.ani_image_view, null);
            imageView.setImageDrawable(d);
            mRootLayout.addView(imageView);

            mAllImageViews.add(imageView);

            RelativeLayout.LayoutParams animationLayout = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
            animationLayout.setMargins(0, (int) (-100 * mScale), 0, 0);
            animationLayout.width = (int) (60 * mScale);
            animationLayout.height = (int) (60 * mScale);

            startAnimation(imageView);
        }
    };

    public void startAnimation(final ImageView aniView) {

        aniView.setPivotX(aniView.getWidth() / 2);
        aniView.setPivotY(aniView.getHeight() / 2);

        long delay = new Random().nextInt(Constants.MAX_DELAY);

        final ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(Constants.ANIM_DURATION);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setStartDelay(delay);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            int angle = 50 + (int) (Math.random() * 101);
            int movex = new Random().nextInt(mDisplaySize.right);

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = ((Float) (animation.getAnimatedValue())).floatValue();

                aniView.setRotation(angle * value);
                aniView.setTranslationX((movex - 40) * value);
                aniView.setTranslationY((mDisplaySize.bottom + (150 * mScale)) * value);
            }
        });

        animator.start();
    }

    private class ExeTimerTask extends TimerTask {
        @Override
        public void run() {
            // we don't really use the message 'what' but we have to specify something.
            mHandler.sendEmptyMessage(Constants.EMPTY_MESSAGE_WHAT);
        }
    }


    @Override
    public void onBackPressed() {
        Snackbar snackbar = Snackbar
                .make(relativeLayout, "Do you want to exit?", Snackbar.LENGTH_LONG)
                .setAction("Yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
        // Changing message text color
        snackbar.setActionTextColor(Color.GREEN);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.CYAN);
        sbView.setBackgroundColor(Color.RED);
        snackbar.show();
    }

    public void animation() {
        animBlink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_up);
        animMoveFromRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_from_right);
        animMoveFromLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_from_left);
        animBounceFromLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce_from_left);
        animBounceFromRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce_from_right);


        playButton.startAnimation(animMoveFromLeft);
        changeButton.startAnimation(animMoveFromRight);
        settingButton.startAnimation(animSlideUp);
        rateButton.startAnimation(animSlideUp);
        leaderboardButton.startAnimation(animSlideUp);
        splash.startAnimation(animBlink);

        scoreMainActivity.startAnimation(animMoveFromLeft);
        hintButtonMainActivity.startAnimation(animMoveFromRight);
    }
}