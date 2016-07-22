package fr.pnksmr.archillect;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

public class MainActivity extends AppCompatActivity {

    private static final int INTERVAL = 60000;

    private ImageView mContentView;
    private Twitter mTwitter;
    private Handler mTimerHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mContentView = (ImageView)findViewById(R.id.content);

        setupTwitter();
        getTweets();
    }

    @SuppressLint("InlinedApi")
    @Override
    protected void onResume() {
        super.onResume();

        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void setupTwitter() {
        TwitterFactory tf = new TwitterFactory();
        mTwitter = tf.getInstance();
    }

    private void getTweets() {
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
                Picasso.with(MainActivity.this).load(task.getResult()).into(mContentView);
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);

        mTimerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getTweets();
            }
        }, INTERVAL);
    }

}
