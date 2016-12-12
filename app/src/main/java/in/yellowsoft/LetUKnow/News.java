package in.yellowsoft.LetUKnow;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Chinni on 30-07-2016.
 */
public class News implements Serializable {
    String id, title, image, data,type, link, is_urgent, now, insta_img, gallery_link = "", facebook_str, twitter_str, whatsapp_str, mail_str, video,mp4,m3u8, times,
            time, time_ar, time_fr, times2;
    Chanel chanels;
    ArrayList<String> img;


    News(JSONObject jsonObject) {
        img=new ArrayList<>();
        image = "";
        title = "";
        try {
            id = jsonObject.getString("id");
            title = jsonObject.getString("title");
            image = jsonObject.getString("image");
            data = jsonObject.getString("data");
            type = jsonObject.getString("type");
            link = jsonObject.getString("link");
            insta_img = jsonObject.getString("insta_img");
            is_urgent = jsonObject.getString("is_urgent");
            now = jsonObject.getString("now");
            JSONObject jsonObject2 = jsonObject.getJSONObject("chanel");
            chanels = new Chanel(jsonObject2);
            facebook_str = jsonObject.getString("facebook_str");
            twitter_str = jsonObject.getString("twitter_str");
            whatsapp_str = jsonObject.getString("whatsapp_str");
            mail_str = jsonObject.getString("mail_str");
            video = jsonObject.getString("video");
            mp4 = jsonObject.getString("mp4");
            m3u8 = jsonObject.getString("m3u8");
            times = jsonObject.getString("times");
            time = jsonObject.getString("time");
            time_ar = jsonObject.getString("time_ar");
            time_fr = jsonObject.getString("time_fr");
            times2 = jsonObject.getString("times2");
            if(jsonObject.getJSONArray("gallery").length()>0) {
                for (int i = 0; i < jsonObject.getJSONArray("gallery").length(); i++) {
                    img.add(jsonObject.getJSONArray("gallery").getString(i));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public String get_time(Context context) {
        if (Session.get_user_language(context).equals("ar"))
            return time_ar;
        else if (Session.get_user_language(context).equals("fr"))
            return time_fr;
        else
            return time;
    }

    public class Chanel implements Serializable {
        String ch_id, ch_title, ch_title_ar, ch_title_fr, ch_image;

        Chanel(JSONObject jsonObject1) {
            try {
                ch_id = jsonObject1.getString("id");
                ch_title = jsonObject1.getString("title");
                ch_title_ar = jsonObject1.getString("title_ar");
                ch_title_fr = jsonObject1.getString("title_fr");
                ch_image = jsonObject1.getString("image");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        public String get_ch_title(Context context) {
            if (Session.get_user_language(context).equals("ar"))
                return ch_title_ar;
            else if (Session.get_user_language(context).equals("fr"))
                return ch_title_fr;
            else
                return ch_title;
        }
    }


}