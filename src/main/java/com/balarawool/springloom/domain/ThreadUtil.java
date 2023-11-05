package com.balarawool.springloom.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadUtil {

    static Logger logger = LoggerFactory.getLogger(ThreadUtil.class);

    public static void logAndWait(String task) {
        long delay = (long)(Math.random() * 10);
        logger.info("Thread: " + getThreadName() + " Performing task: " + task + "() will take " + delay + " seconds");
        try {
            Thread.sleep(delay * 1_000);
            logger.info("Done task: " + task + "()");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getThreadName() {
        var th = Thread.currentThread();
        return th.isVirtual() ? th.toString() : th.getName();
    }
}
