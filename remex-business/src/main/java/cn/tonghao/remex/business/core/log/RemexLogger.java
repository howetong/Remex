package cn.tonghao.remex.business.core.log;

import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

public class RemexLogger implements org.slf4j.Logger {

    private org.slf4j.Logger logger;

    private RemexLogger(Class clazz) {
        logger = LoggerFactory.getLogger(clazz);
    }

    public static org.slf4j.Logger getLogger(Class clazz) {
        return new RemexLogger(clazz);
    }

    public String getLogIdMsg() {
        return "logTraceId[" + getUUID() + "]--";
    }

    private String getUUID() {
        return LogFilter.getUUID().get();
    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        logger.trace(getLogIdMsg() + msg);
    }

    @Override
    public void trace(String format, Object arg) {
        logger.trace(getLogIdMsg() + format, arg);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        logger.trace(getLogIdMsg() + format, arg1, arg2);
    }

    @Override
    public void trace(String format, Object... arguments) {
        logger.trace(getLogIdMsg() + format, arguments);
    }

    @Override
    public void trace(String msg, Throwable t) {
        logger.trace(getLogIdMsg() + msg, t);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return logger.isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String msg) {
        logger.trace(marker, getLogIdMsg() + msg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        logger.trace(marker, getLogIdMsg() + format, arg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        logger.trace(marker, getLogIdMsg() + format, arg1, arg2);
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        logger.trace(marker, getLogIdMsg() + format, argArray);
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        logger.trace(marker, getLogIdMsg() + msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        logger.debug(getLogIdMsg() + msg);
    }

    @Override
    public void debug(String format, Object arg) {
        logger.debug(getLogIdMsg() + format, arg);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        logger.debug(getLogIdMsg() + format, arg1, arg2);
    }

    @Override
    public void debug(String format, Object... arguments) {
        logger.debug(getLogIdMsg() + format, arguments);
    }

    @Override
    public void debug(String msg, Throwable t) {
        logger.debug(getLogIdMsg() + msg, t);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return logger.isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String msg) {
        logger.debug(marker, getLogIdMsg() + msg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        logger.debug(marker, getLogIdMsg() + format, arg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        logger.debug(marker, getLogIdMsg() + format, arg1, arg2);
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        logger.debug(marker, getLogIdMsg() + format, arguments);
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        logger.debug(marker, getLogIdMsg() + msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        logger.info(getLogIdMsg() + msg);
    }

    @Override
    public void info(String format, Object arg) {
        logger.info(getLogIdMsg() + format, arg);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        logger.info(getLogIdMsg() + format, arg1, arg2);
    }

    @Override
    public void info(String format, Object... arguments) {
        logger.info(getLogIdMsg() + format, arguments);
    }

    @Override
    public void info(String msg, Throwable t) {
        logger.info(getLogIdMsg() + msg, t);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return logger.isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String msg) {
        logger.info(marker, getLogIdMsg() + msg);
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        logger.info(marker, getLogIdMsg() + format, arg);
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        logger.info(marker, getLogIdMsg() + format, arg1, arg2);
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        logger.info(marker, getLogIdMsg() + format, arguments);
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        logger.info(marker, getLogIdMsg() + msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        logger.warn(getLogIdMsg() + msg);
    }

    @Override
    public void warn(String format, Object arg) {
        logger.warn(getLogIdMsg() + format, arg);
    }

    @Override
    public void warn(String format, Object... arguments) {
        logger.warn(getLogIdMsg() + format, arguments);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        logger.warn(getLogIdMsg() + format, arg1, arg2);
    }

    @Override
    public void warn(String msg, Throwable t) {
        logger.warn(getLogIdMsg() + msg, t);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return logger.isWarnEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String msg) {
        logger.warn(marker, getLogIdMsg() + msg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        logger.warn(marker, getLogIdMsg() + format, arg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        logger.warn(marker, getLogIdMsg() + format, arg1, arg2);
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        logger.warn(marker, getLogIdMsg() + format, arguments);
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        logger.warn(marker, getLogIdMsg() + msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        logger.error(getLogIdMsg() + msg);
    }

    @Override
    public void error(String format, Object arg) {
        logger.error(getLogIdMsg() + format, arg);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        logger.error(getLogIdMsg() + format, arg1, arg2);
    }

    @Override
    public void error(String format, Object... arguments) {
        logger.error(getLogIdMsg() + format, arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        logger.error(getLogIdMsg() + msg, t);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return logger.isErrorEnabled(marker);
    }

    @Override
    public void error(Marker marker, String msg) {
        logger.error(marker, getLogIdMsg() + msg);
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        logger.error(marker, getLogIdMsg() + format, arg);
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        logger.error(marker, getLogIdMsg() + format, arg1, arg2);
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        logger.error(marker, getLogIdMsg() + format, arguments);
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        logger.error(marker, getLogIdMsg() + msg, t);
    }

}
