package com.sheygam.java_19_17_04_18;

import android.os.Handler;
import android.os.Message;

public class Worker extends Thread {
    private Handler handler;

    public Worker(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        handler.sendEmptyMessage(MainActivity.WORK_START);
        Message msg;
        for (int i = 0; i < 10000; i++) {
            try {
                sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            if(isInterrupted()){
                break;
            }
            msg = handler.obtainMessage(MainActivity.WORK_UPDATE,i+1,-1);
            handler.sendMessage(msg);
        }

        handler.sendEmptyMessage(MainActivity.WORK_END);
    }
}
