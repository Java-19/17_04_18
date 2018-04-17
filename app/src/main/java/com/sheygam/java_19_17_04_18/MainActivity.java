package com.sheygam.java_19_17_04_18;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int WORK_START = 0x01;
    public static final int WORK_END = 0x02;
    public static final int WORK_UPDATE = 0x03;

    private Button startBtn, stopBtn;
    private ProgressBar myProgress;
    private Handler handler;
    private TextView countTxt;
    private WorkerTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startBtn = findViewById(R.id.start_btn);
        stopBtn = findViewById(R.id.stop_btn);
        myProgress = findViewById(R.id.my_progress);
        countTxt = findViewById(R.id.count_txt);
        startBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        handler = new Handler(callback);
//        handler = new Handler();
    }

    private Handler.Callback callback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case WORK_START:
                    myProgress.setVisibility(View.VISIBLE);
                    startBtn.setEnabled(false);
                    break;
                case WORK_END:
                    myProgress.setVisibility(View.INVISIBLE);
                    startBtn.setEnabled(true);
                    countTxt.setText("");
                    break;
                case WORK_UPDATE:
                    countTxt.setText(String.valueOf(msg.arg1));
                    break;
            }
            return true;
        }
    };

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.start_btn){
//          Worker worker = new Worker(handler);
//          worker.start();
//          worker.interrupt();
//            startWork();

            task = new WorkerTask();
            task.execute(10);

        }else if(v.getId() == R.id.stop_btn){
            task.cancel(true);
        }
    }

    public void startSecondWork(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void startWork(){

        new Thread(new Runnable() {
            int i = 0;
            @Override
            public void run() {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        myProgress.setVisibility(View.VISIBLE);
                        startBtn.setEnabled(false);
                    }
                });

                for (; i < 10; i++) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            countTxt.setText(String.valueOf(i+1));
                        }
                    });
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        myProgress.setVisibility(View.INVISIBLE);
                        startBtn.setEnabled(true);
                        countTxt.setText("");
                    }
                });
            }
        }).start();
    }


    class WorkerTask extends AsyncTask<Integer,Integer,String>{

        @Override
        protected void onPreExecute() {
            myProgress.setVisibility(View.VISIBLE);
            startBtn.setEnabled(false);
            stopBtn.setEnabled(true);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            countTxt.setText(String.valueOf(values[0]));
            myProgress.setProgress(values[0]+1);
        }

        @Override
        protected String doInBackground(Integer... arr) {

            for (int i = 0; i < arr[0]; i++) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i,10,15);
                Log.d("MY_TAG", "doInBackground: " + i);

                if(isCancelled()){
                    return "Was stopped";
                }
            }
            return "All done!";
        }

        @Override
        protected void onPostExecute(String s) {
            countTxt.setText(s);
            myProgress.setVisibility(View.INVISIBLE);
            startBtn.setEnabled(true);
            stopBtn.setEnabled(false);
        }

        @Override
        protected void onCancelled(String s) {
            countTxt.setText(s);
            myProgress.setVisibility(View.INVISIBLE);
            startBtn.setEnabled(true);
            stopBtn.setEnabled(false);
        }
    }
}
