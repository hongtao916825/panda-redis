package com.panda.redis.base.common;

import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 日志工具类，使用静态方法打印日志  无需每个类中定义日志对象
 * Logback对每个Logger对象做了缓存，每次调用LoggerFactory.getLogger(String name)时如果已存在则从缓存中获取不会生成新的对象;
 * 同时也不会有对象的创建与销毁造成的性能损失
 * @author zhoujumbo
 * @datetime 2019-08-01
 */
public class LogUtil {

    public static void error(String msg) {
        LoggerFactory.getLogger(getClassName()).error(msg);
    }

    public static void error(String msg, Object... obj) {
        LoggerFactory.getLogger(getClassName()).error(msg, obj);
    }

    public static void warn(String msg) {
        LoggerFactory.getLogger(getClassName()).warn(msg);
    }

    public static void warn(String msg, Object... obj) {
        LoggerFactory.getLogger(getClassName()).warn(msg, obj);
    }

    public static void info(String msg) {
        LoggerFactory.getLogger(getClassName()).info(msg);
    }

    public static void info(String msg, Object... obj) {
        LoggerFactory.getLogger(getClassName()).info(msg, obj);
    }

    public static void debug(String msg) {
        LoggerFactory.getLogger(getClassName()).debug(msg);
    }

    public static void debug(String msg, Object... obj) {
        LoggerFactory.getLogger(getClassName()).debug(msg, obj);
    }

    public static boolean isTraceEnabled() {
        return LoggerFactory.getLogger(getClassName()).isTraceEnabled();
    }

    public static boolean isTraceEnabled(Marker val) {
        return LoggerFactory.getLogger(getClassName()).isTraceEnabled(val);
    }

    public static boolean isDebugEnabled() {
        return LoggerFactory.getLogger(getClassName()).isDebugEnabled();
    }

    public static boolean isDebugEnabled(Marker val) {
        return LoggerFactory.getLogger(getClassName()).isDebugEnabled(val);
    }

    public static boolean isInfoEnabled() {
        return LoggerFactory.getLogger(getClassName()).isInfoEnabled();
    }

    public static boolean isInfoEnabled(Marker val) {
        return LoggerFactory.getLogger(getClassName()).isInfoEnabled(val);
    }

    public static boolean isWarnEnabled() {
        return LoggerFactory.getLogger(getClassName()).isWarnEnabled();
    }

    public static boolean isWarnEnabled(Marker val) {
        return LoggerFactory.getLogger(getClassName()).isWarnEnabled(val);
    }

    public static boolean isErrorEnabled() {
        return LoggerFactory.getLogger(getClassName()).isErrorEnabled();
    }

    public static boolean isErrorEnabled(Marker val) {
        return LoggerFactory.getLogger(getClassName()).isErrorEnabled(val);
    }

    /**
     * 获取调用 error,info,debug静态类的类名
     * @return
     */
    private static String getClassName() {
        return new SecurityManager() {
            public String getClassName() {
                return getClassContext()[3].getName();
            }
        }.getClassName();
    }

    public static String printStackTraceToString(Throwable t){
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw,true));
        return sw.getBuffer().toString();
    }
}

