package com.lin.proxymedia;

/**
 * Created by linchen on 2018/3/16.
 * mail: linchen@sogou-inc.com
 */

public interface ThreadStrategy {
    void runThread(Runnable runnable);
}
