package soexample.umeng.com.xinjian;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import soexample.umeng.com.xinjian.guilei.MyCircleProgress;

public class MainActivity extends AppCompatActivity {
    private MyCircleProgress progressView;
    private TextView mT1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mT1 = findViewById(R.id.text111dsadsadsadsadsa);
        progressView =findViewById(R.id.MyCircleProgress);
        initAnmint();
        new ProgressAnimation().execute();
    }

    private void initAnmint() {
        //做缩放动画
        ObjectAnimator scale = ObjectAnimator.ofFloat(mT1, "scaleX", new float[]{0f,0.5f,1f,3f,1f});
        //做透明动画
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mT1, "alpha", new float[]{0.2f, 0.4f, 0.6f, 0.8f, 1.0f});
        //做旋转动画
        ObjectAnimator rotationY = ObjectAnimator.ofFloat(mT1, "rotationX", new float[]{0f,90f, 160f, 270f, 360f});
        AnimatorSet set = new AnimatorSet();
        set.setDuration(5000);
        set.playTogether(scale,alpha,rotationY);
        set.start();
    }

    class ProgressAnimation extends AsyncTask<Void, Integer, Void> {
        int i1=0;
        @Override
        protected Void doInBackground(Void... params) {
            //进度值不断的变化
            for (int i = 0; i <= progressView.getMax(); i++) {
                //Log.e("Lixiaoliang", "wwwww"+i);
                try {
                    i1=i;
                    publishProgress(i);
                    Thread.sleep(50);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (i1==100){
                                startActivity(new Intent(MainActivity.this,TwoActivity.class));
                                finish();
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //更新进度值
            progressView.setProgress(values[0]);
            progressView.invalidate();

            super.onProgressUpdate(values);
        }
    }
}

