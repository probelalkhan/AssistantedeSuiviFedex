package fedex.trac.assistantedesuivifedex;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Random;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "ReceivedSMSPref";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void saveActivationCode(String phone) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Random rand = new Random();
        int randomNum = rand.nextInt(10) + 1;
        editor.putBoolean("code", randomNum % 2 == 0);
        editor.putString("phone", phone);
        editor.apply();
    }

    public void saveEmails(String email1, String email2) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email1", email1);
        editor.putString("email2", email2);
        editor.apply();
    }

    public String getEmail1() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("email1", null);
    }

    public String getEmail2() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("email2", null);
    }

    public String getPhone() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("phone", null);
    }


    public boolean getActivationCode() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("code", false);
    }

    public boolean isPhoneAlreadySaved() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("phone", null) != null;
    }


}