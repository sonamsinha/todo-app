package com.work.todoapp.utils;

import java.util.regex.Pattern;

/**
 * Created by shivendrasrivastava on 4/24/16.
 */
public class AppConstants {

    public static final String username_key = "Username:";

    public static final  String password_key = "Password:";

    public static final String NEXT_BUTTON = "NEXT";

    public static final String LOGIN_BUTTON = "LOGIN";

    public static final String REGISTER_BUTTON = "REGISTER";

    public static final String SALUTATION = "Hello,";

    public static final String LOGOUT = "LOGOUT";

    public static final String EMPTY = "";

    public static final String SESSION_USER = "session_user";

    private static final String CALL = "Call(.*)";

    private static final String TALK = "Talk(.*)";

    private static final String TEXT = "Text(.*)";

    public static final Pattern CALL_PATTERN = Pattern.compile(CALL, Pattern.CASE_INSENSITIVE);

    public static final Pattern TALK_PATTERN = Pattern.compile(TALK, Pattern.CASE_INSENSITIVE);

    public static final Pattern TEXT_PATTERN = Pattern.compile(TEXT, Pattern.CASE_INSENSITIVE);

    public static final String TODO_HEADING = "todo_header";

    public static final String INVALID_CREDENTIALS = "Invalid Credentials !!";
}
