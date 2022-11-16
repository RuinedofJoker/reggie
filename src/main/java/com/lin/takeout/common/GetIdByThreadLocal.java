package com.lin.takeout.common;

public class GetIdByThreadLocal {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal();

    public static void getThreadLocal(long id) {
        threadLocal.set(id);
    }

    public static long getId() {
        return threadLocal.get();
    }
}
