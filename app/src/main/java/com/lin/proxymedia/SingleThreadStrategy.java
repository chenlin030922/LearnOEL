package com.lin.proxymedia;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by linchen on 2018/3/16.
 * mail: linchen@sogou-inc.com
 */

public class SingleThreadStrategy implements ThreadStrategy {
    private ExecutorService mExecutors;

    public SingleThreadStrategy() {
        mExecutors = Executors.newSingleThreadExecutor();
    }

    @Override
    public void runThread(Runnable runnable) {
        mExecutors.submit(runnable);
    }
}
