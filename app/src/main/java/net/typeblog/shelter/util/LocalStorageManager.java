package net.typeblog.shelter.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocalStorageManager {
    public static final String PREF_IS_DEVICE_ADMIN = "is_device_admin";
    public static final String PREF_HAS_SETUP = "has_setup";
    public static final String PREF_AUTO_FREEZE_LIST_WORK_PROFILE = "auto_freeze_list_work_profile";

    private static final String LIST_DIVIDER = ",";

    private static LocalStorageManager sInstance = null;
    private SharedPreferences mPrefs = null;

    private LocalStorageManager(Context context) {
        mPrefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }

    // Should be called in Application class
    public static void initialize(Context context) {
        sInstance = new LocalStorageManager(context);
    }

    public static LocalStorageManager getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("LocalStorageManager must be initialized at start-up");
        }
        return sInstance;
    }

    // === Wrapper methods from SharedPreferences ===
    public boolean getBoolean(String pref) {
        return mPrefs.getBoolean(pref, false);
    }

    public void setBoolean(String pref, boolean value) {
        mPrefs.edit().putBoolean(pref, value).apply();
    }

    public String[] getStringList(String pref) {
        return mPrefs.getString(pref, "").split(LIST_DIVIDER);
    }

    public void setStringList(String pref, String[] list) {
        mPrefs.edit().putString(pref, String.join(LIST_DIVIDER, list)).apply();
    }

    public boolean stringListContains(String pref, String item) {
        return Arrays.asList(getStringList(pref)).indexOf(item) >= 0;
    }

    public void appendStringList(String pref, String newItem) {
        String str = mPrefs.getString(pref, null);
        if (str == null) {
            str = newItem;
        } else {
            str += LIST_DIVIDER + newItem;
        }
        mPrefs.edit().putString(pref, str).apply();
    }

    public void removeFromStringList(String pref, String item) {
        List<String> list = new ArrayList<>(Arrays.asList(getStringList(pref)));
        list.removeIf(item::equals);
        setStringList(pref, list.toArray(new String[]{}));
    }
}
