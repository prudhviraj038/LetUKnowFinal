package in.yellowsoft.LetUKnow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by HP on 7/26/2016.
 */
public class SubCategories extends Fragment {

    GridView listView;
    Categories categories;
    SubCategoryAdapter categoryAdapter ;
    FragmentTouchListner mCallBack;


    public interface FragmentTouchListner{
        public  void back();
        public void subcatselected(Categories.Chanel chanel,String parent,String parent_name);
        public void setselected(String index);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallBack = (MainActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement LogoutUser");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.subcatfragment, container, false);
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        View view = getView();
        mCallBack.setselected("4");

        categories = (Categories)getArguments().getSerializable("categorie");
        Log.e("ch_tit", String.valueOf(categories.title));
        Log.e("ch_count", String.valueOf(categories.chanels.size()));
        in.yellowsoft.LetUKnow.MyTextView label = (in.yellowsoft.LetUKnow.MyTextView) view.findViewById(R.id.label);
        label.setText(Html.fromHtml(categories.title));
        categoryAdapter = new SubCategoryAdapter(getActivity(),categories);

                customs_in_header = (LinearLayout) view.findViewById(R.id.custom_header_layout);
                listView=(GridView)view.findViewById(R.id.listView);
                listView.setAdapter(categoryAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                      //  mCallBack.subcatselected(categories.chanels.get(position),categories.id,categories.title);
                    }
                });
                ArrayList<String> tab_names = new ArrayList<>();
                tab_names.add("New Sources");
                tab_names.add("Edit Sources");
                customs_in_header.setVisibility(View.GONE);

        ImageView back_btn = (ImageView) view.findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.back();
            }
        });


        }

    ArrayList<in.yellowsoft.LetUKnow.MyTextView> custom_names;
    LinearLayout customs_in_header;

    public void display_custom(final ArrayList<String> jsonArray) {

        custom_names = new ArrayList<>();
        customs_in_header.removeAllViewsInLayout();
        customs_in_header.setVisibility(View.VISIBLE);
        for (int i=0;i<jsonArray.size();i++){
            final  in.yellowsoft.LetUKnow.MyTextView temp = new in.yellowsoft.LetUKnow.MyTextView(getActivity());
            try {
                temp.setText(jsonArray.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
            temp.setLayoutParams(params);
            temp.setSingleLine(true);
            temp.setGravity(Gravity.CENTER);

            temp.setTextColor(getActivity().getResources().getColor(R.color.aa_app_blue));
            temp.setBackgroundResource(R.drawable.border_empty_appcolor);
            final int finalI = i;
            final int finalI1 = i;
            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int j = 0; j < jsonArray.size(); j++) {
                        custom_names.get(j).setTextColor(getActivity().getResources().getColor(R.color.aa_app_blue));
                        custom_names.get(j).setBackgroundResource(R.drawable.border_empty_appcolor);
                    }

                    temp.setBackgroundResource(R.drawable.border_full_appcolor);
                    temp.setTextColor(Color.parseColor("white"));
                   // customizeFagment.custom_item_selected(finalI1);


                }
            });

            custom_names.add(temp);
            customs_in_header.addView(temp);
        }
        custom_names.get(0).setBackgroundResource(R.drawable.border_full_appcolor);
        custom_names.get(0).setTextColor(Color.parseColor("white"));
    }

    private void get_categories(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("please_wait");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String url = Session.SERVER_URL+"categories.php";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.e("response",jsonArray.toString());
                if(progressDialog!=null)
                    progressDialog.dismiss();

                for(int i=0;i<jsonArray.length();i++){

                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        //categories.add(new Categories(jsonObject));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                categoryAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(progressDialog!=null)
                    progressDialog.dismiss();
                Log.e("error",volleyError.toString());

            }
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

    }



}
