package com.example.springBootWithRealcode.exception;

public class CacheExpiredException extends RuntimeException {

    private String cacheExpired;

    public CacheExpiredException(String cache_expired, String s) {
        super(s);
        this.cacheExpired = cacheExpired;

    }
}
