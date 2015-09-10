package com.doubletuan.sns.config;

/**
 * Application constants.
 */
public final class Constants {

    public static final String POST_IMAGE_RESOURCE_PATH = "http://192.168.0.120:8080/assets/posts/";

	private Constants() {
    }

    // Spring profile for development, production and "fast", see http://jhipster.github.io/profiles.html
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    public static final String SPRING_PROFILE_FAST = "fast";
    // Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
    public static final String SPRING_PROFILE_CLOUD = "cloud";
    // Spring profile used when deploying to Heroku
    public static final String SPRING_PROFILE_HEROKU = "heroku";

    public static final String SYSTEM_ACCOUNT = "system";
    
    public static final String POST_IMAGE_PATH = "/Users/jerry/Desktop/api/04-代码/dt-sns/src/main/webapp/assets/posts/";
    
    public static final String USER_UPLOADED_FILE_RELATIVE_PATH = "user_uploaded_file_relative_path";
    public static final String SYSTEM_ROOT_HTTP_PATH = "system_root_http_path";
    public static final String USER_UPLOADED_FILE_ROOT_PATH = "user_uploaded_file_root_path";

}
