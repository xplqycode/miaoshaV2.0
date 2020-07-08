package com.pxu.redis.constants;

public class ExpireTimeConstant {

    public static final int ONE_SECOND_EXPIRETIME = 1;

    public static final int TEN_SECOND_EXPIRETIME = 10;

    public static final int DAY_EXPIRETIME = 3600 * 24;

    public static final int HALF_HOUR_EXPIRETIME = 1800;

    public static final int TEN_MINUTES_EXPIRETIME = 600;

    public static final int FIVE_MINUTE_EXPIRETIME = 60 * 5;

    public static final int ONE_HOUR_EXPIRETIME = 3600;

    public static final int ONE_DAY_EXPIRETIME = 24 * 3600;

    public static final int ONE_MINUTE_EXPIRETIME = 60;

    public static final int TWO_MINUTE_EXPIRETIME = 120;

    public static final int HALF_MINUTE_EXPIRETIME = 30;

    public static final int ONE_MONTH_EXPIRETIME = 30 * ONE_DAY_EXPIRETIME;

    public static final int CACHE_DEFAULT_EXPIRE_SECONDS = ONE_MINUTE_EXPIRETIME * 5;

}
