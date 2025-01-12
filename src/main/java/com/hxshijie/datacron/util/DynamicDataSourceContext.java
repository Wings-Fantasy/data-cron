package com.hxshijie.datacron.util;

public class DynamicDataSourceContext {

    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static void setDataSource() {
        setDataSource("master");
    }

    public static void setDataSource(String dataSource) {
        CONTEXT.set(dataSource);
    }

    public static String getDataSource() {
        return CONTEXT.get();
    }
}
