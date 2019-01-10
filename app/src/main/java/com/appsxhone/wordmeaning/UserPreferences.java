package com.appsxhone.wordmeaning;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Sameer on 23-Feb-16.
 */
public class UserPreferences {
    private static final String USER_SCORE = "USER_SCORE", USER_HINT = "USER_HINT", USER_PROGRESS = "USER_PROGRESS";
    private static int progress = 0;

    private static SharedPreferences mPrefs;

    private static UserPreferences instance;

    private UserPreferences(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static UserPreferences getInstance(Context context) {
        if (instance == null)
            instance = new UserPreferences(context);
        return instance;
    }

    public static void updateScore(int userScore) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(USER_SCORE, userScore);
        editor.commit();
    }

    public static void updateHint(int hint) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(USER_HINT, hint);
        editor.commit();
    }

    public static void updateProgress() {
        SharedPreferences.Editor editor = mPrefs.edit();
        progress = mPrefs.getInt(USER_PROGRESS,0);
        editor.putInt(USER_PROGRESS,progress+1);
        editor.commit();
    }

    public static int getScore() {
        return mPrefs.getInt(USER_SCORE, 0);
    }

    public static int getHint() {
        return mPrefs.getInt(USER_HINT, 10);
    }

    public static int getProgress() {
        return mPrefs.getInt(USER_PROGRESS,0);
    }
}
