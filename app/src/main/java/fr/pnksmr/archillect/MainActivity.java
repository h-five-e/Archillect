package fr.pnksmr.archillect;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import io.github.sporklibrary.Spork;
import io.github.sporklibrary.annotations.BindClick;
import io.github.sporklibrary.annotations.BindLayout;
import io.github.sporklibrary.annotations.BindView;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

@BindLayout(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    private static final int UPDATE_INTERVAL = 60000;
    private static final int HIDE_UI_DELAY = 3000;
    private static final int FADE_DURATION = 1000;

    @BindView(R.id.content)
    private View mContentView;

    @BindView(R.id.imageview_placeholder)
    private ImageView mImageViewPlaceholder;

    @BindView(R.id.imageview)
    private ImageView mImageView;

    @BindView(R.id.settings_fab)
    private View mSettingsFab;

    private Twitter mTwitter;

    private Handler mHandler = new Handler();

    private String mCurrentUrl;

    private Runnable mGetTweets = new Runnable() {
        @Override
        public void run() {
            getTweets();
        }
    };

    private Runnable mHideUi = new Runnable() {
        @Override
        public void run() {
            hideUI();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Spork.bind(this);

        setupTwitter();
    }

    @Override
    protected void onResume() {
        super.onResume();

        hideUI();
        getTweets();
    }

    @Override
    protected void onPause() {
        mHandler.removeCallbacks(mGetTweets);
        super.onPause();
    }

    @SuppressLint("InlinedApi")
    private void hideUI() {
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        mSettingsFab.setVisibility(View.INVISIBLE);
    }

    private void setupTwitter() {
        TwitterFactory tf = new TwitterFactory();
        mTwitter = tf.getInstance();
    }

    private void getTweets() {
        mHandler.removeCallbacks(mGetTweets);

        Task.callInBackground(new Callable<ResponseList<Status>>() {
            @Override
            public ResponseList<Status> call() throws Exception {
                return mTwitter.getUserTimeline("archillect", new Paging(1, 1));
            }
        }).onSuccess(new Continuation<ResponseList<Status>, String>() {
            @Override
            public String then(Task<ResponseList<Status>> task) throws Exception {
                if (task.getResult().size() > 0 && task.getResult().get(0).getMediaEntities().length > 0) {
                    return task.getResult().get(0).getMediaEntities()[0].getMediaURLHttps();
                }
                return null;
            }
        }).onSuccess(new Continuation<String, Void>() {
            @Override
            public Void then(Task<String> task) throws Exception {

                String url = task.getResult();

                if (url == null || url.equals(mCurrentUrl)) {
                    return null;
                }

                mCurrentUrl = url;

                mImageViewPlaceholder.setImageDrawable(mImageView.getDrawable());
                mImageView.setAlpha(0f);

                Picasso
                        .with(MainActivity.this)
                        .load(task.getResult())
                        .noFade()
                        .into(mImageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                mImageView.animate().alpha(1f).setDuration(FADE_DURATION);
                            }

                            @Override
                            public void onError() {

                            }
                        });
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);

        mHandler.postDelayed(mGetTweets, UPDATE_INTERVAL);
    }

    @BindClick(R.id.content)
    private void onContentClick() {
        mSettingsFab.setVisibility(View.VISIBLE);
        mHandler.removeCallbacks(mHideUi);
        mHandler.postDelayed(mHideUi, HIDE_UI_DELAY);
    }

    @BindClick(R.id.settings_fab)
    private void onSettingsClick() {
        startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
    }

}
