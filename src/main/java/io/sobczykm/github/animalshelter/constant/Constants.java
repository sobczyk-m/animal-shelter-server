package io.sobczykm.github.animalshelter.constant;

public class Constants {
    public static final String[] PUBLIC_URLS = {"/employee/login/**"};
    public static final String[] PUBLIC_ROUTES = {"/employee/login"};
    public static final int PASSWORD_STRENGTH = 12;
    public static final String AUTHORITIES = "authorities";
    public static final long ACCESS_TOKEN_EXPIRATION_TIME = 1_800_000L;
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String EMPTY = "";
}
