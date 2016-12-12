package in.yellowsoft.LetUKnow;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Chinni on 30-07-2016.
 */
public class Categories implements Serializable {
    String id,title,title_ar,title_fr,image;
    ArrayList<Chanel> chanels;
    Categories(JSONObject jsonObject){
        chanels=new ArrayList<>();
        try {
            id=jsonObject.getString("id");
            title=jsonObject.getString("title");
            title_ar=jsonObject.getString("title_ar");
            title_fr=jsonObject.getString("title_fr");
            image=jsonObject.getString("image");
            for(int i=0;i<jsonObject.getJSONArray("chanels").length();i++){

                JSONObject jsonObject2 =jsonObject.getJSONArray("chanels").getJSONObject(i);
                Chanel chanel=new Chanel(jsonObject2);
                chanels.add(chanel);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    public String get_title(Context context) {
        if (Session.get_user_language(context).equals("ar"))
            return title_ar;
        else if (Session.get_user_language(context).equals("fr"))
            return title_fr;
        else
            return title;
    }
    public class Chanel implements Serializable{
        String ch_id,ch_title,ch_title_ar,ch_title_fr,ch_image,cover_image,count;
        Chanel(JSONObject jsonObject1){
            try {
                ch_id=jsonObject1.getString("id");
                ch_title=jsonObject1.getString("title");
                ch_title_ar=jsonObject1.getString("title_ar");
                ch_title_fr=jsonObject1.getString("title_fr");
                ch_image=jsonObject1.getString("image");
                cover_image=jsonObject1.getString("cover_image");
                count = jsonObject1.getString("count");
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
