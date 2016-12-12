package in.yellowsoft.LetUKnow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by HP on 8/18/2016.
 */
public class Session {
    static String SERVER_URL = "http://clients.yellowsoft.in/LetUKnow/api/";
    static String user_key = "radio_user";
    static String lan_key = "radio_lan";
    static String words_key = "danden_words";

    public static   void forceRTLIfSupported(Activity activity)
    {
        SharedPreferences sharedPref;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        Log.e("lan", sharedPref.getString(lan_key, "-1"));

        if (sharedPref.getString(lan_key, "-1").equals("en")) {
            Resources res = activity.getResources();
            // Change locale settings in the app.
            DisplayMetrics dm = res.getDisplayMetrics();
            android.content.res.Configuration conf = res.getConfiguration();
            conf.locale = new Locale("en".toLowerCase());
            res.updateConfiguration(conf, dm);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                activity.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
        }

        else if(sharedPref.getString(lan_key, "-1").equals("ar")){
            Resources res = activity.getResources();
            // Change locale settings in the app.
            DisplayMetrics dm = res.getDisplayMetrics();
            android.content.res.Configuration conf = res.getConfiguration();
            conf.locale = new Locale("ar".toLowerCase());
            res.updateConfiguration(conf, dm);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                activity.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }

        else {
            Resources res = activity.getResources();
            // Change locale settings in the app.
            DisplayMetrics dm = res.getDisplayMetrics();
            android.content.res.Configuration conf = res.getConfiguration();
            conf.locale = new Locale("ar".toLowerCase());
            res.updateConfiguration(conf, dm);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                activity.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
        }

    }
    static SharedPreferences sharedPreferences;
    public static void set_user_language(Context context,String user_id){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(lan_key,user_id);
        editor.commit();
    }
    public static String get_user_language1(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(lan_key,"ar");
    }
    public static String get_user_language(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(lan_key,"ar");
    }

    public static String get_append(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(sharedPreferences==null)
            return "";
        if(sharedPreferences.getString(lan_key,"en").equals("ar"))
        {
            return "_ar";
        }
        else if(sharedPreferences.getString(lan_key,"en").equals("fr"))
        {
            return "_fr";
        }
        return "";
    }

    public static String get_user_id(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(user_key,"-1");
    }
    public static  void set_user_id(Context context,String user_id){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(user_key,user_id);
        editor.commit();
    }

    public static JSONObject get_user_language_words(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(sharedPreferences.getString(words_key,"-1"));
            jsonObject = jsonObject.getJSONObject(get_user_language(context));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    public static String getword(Context context,String word)
    {

        JSONObject words = get_user_language_words(context);

        try {
            return words.getString(word);
        } catch (JSONException e) {
            e.printStackTrace();
            return word;
        }
    }

    public static void set_user_language_words(Context context,String user_id){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(words_key,user_id);
        editor.commit();
    }
    public static String get_gcmid(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("gcm_id", "-1");
    }

    public static void set_gcmid(Context context, String gcm_id) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("gcm_id",gcm_id);
        editor.commit();
    }
    public static void set_minimizetime(Context context){
        Date date = new Date(System.currentTimeMillis());
        long millis = date.getTime();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("timer_key", date.getTime()).apply();
        editor.commit();
    }

    public static void get_minimizetime(Context context){

        Date resume = new Date(System.currentTimeMillis());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Date pause = new Date(sharedPreferences.getLong("timer_key", 0));

        if(sharedPreferences.getLong("timer_key",0)!=0) {

            long resume_time = System.currentTimeMillis();
            long pause_time = sharedPreferences.getLong("timer_key", 0);
            long diff = resume_time-pause_time;
            Log.e("time_diff",String.valueOf(diff));

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("timer_key", 0).apply();

            long twentyfiveminutes = 1*1000*60*2;

            if(diff>twentyfiveminutes){

                Intent i = context.getPackageManager()
                        .getLaunchIntentForPackage( context.getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }

        }

    }
    public static String getDeviceId(Context context){
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();

        return deviceId;
    }
    public static void sendRegistrationToServer(Context context,String token) {
        String status,chanels,d_id;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(sharedPreferences.getBoolean("notify",false))
            status="true";
        else
            status="false";
        DatabaseHandler databaseHandler;
        databaseHandler=new DatabaseHandler(context);
        chanels=databaseHandler.all_selected_channels_new("0");
        // TODO: Implement this method to send token to your app server.
        // http://clients.yellowsoft.in/naqsh/admin/send_notice.php?action=list
        String url = Session.SERVER_URL+"android-token-register.php?";
        try {
            url = url + "device_token="+token+"&status="+status+"&chanels="+chanels+"&device_id="+getDeviceId(context);
            Log.e("urlllllll",url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.e("response",jsonArray.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("error",volleyError.toString());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);

    }
}
