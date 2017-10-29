package org.h2.util;


import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by computer on 2017/10/29.
 */
public class LogUtil {
    private static volatile LogUtil instance;
    private static ExecutorService pool = Executors.newFixedThreadPool(5);
    private static Lock lock = new ReentrantLock();

    public void print(Class cla, Object... objects) {
        LogWorker logWorker = new LogWorker();
        String className = cla.getName();
        String time = DateFormatUtils.format(new Date(), "dd-MMM-yyyy HH:mm:ss:SSS");
        String context = "";
        for (Object obj : objects) {
            context = context + "--" + obj.toString();
        }
        String log = className + "___" + time + " : " + context;
        logWorker.setLog(log);
        LogRunnable logRunnable = new LogRunnable(logWorker);
        pool.execute(logRunnable);
    }

    public static synchronized LogUtil get() {
        if (null == instance) {
            synchronized (LogUtil.class) {
                if (instance == null) {
                    instance = new LogUtil();
                    PrintStream out;
                    try {
                        out = new PrintStream("D://log.txt");
                        System.setOut(out);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return instance;
    }

    class LogRunnable implements Runnable {
        private LogWorker logWorker;

        public LogRunnable(LogWorker logWorker) {
            this.logWorker = logWorker;
        }

        @Override
        public void run() {
            lock.lock();
            try {
                logWorker.print();
            } catch (Exception e) {
            } finally {
                lock.unlock();
            }
        }
    }

    class LogWorker {
        private String log;

        public void print() {
            System.out.println(log);
        }

        public String getLog() {
            return log;
        }

        public void setLog(String log) {
            this.log = log;
        }
    }
}
