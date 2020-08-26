package com.example.newsx;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsSourceDetailsActivity extends AppCompatActivity {


    private TextView nameTv,descriptionTv,countryTv,categoryTv,languageTv;
    private RecyclerView newsRv;
    private ArrayList<ModeNewsSourceDetails> sourceDetailsArrayList;
    private AdapterNewsSourceDetails adapterNewsSourceDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_source_details);

        //actionbar and title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("Latest News");
        //add back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        nameTv=findViewById(R.id.nameTv);
        descriptionTv=findViewById(R.id.descriptionTv);
        countryTv=findViewById(R.id.countryTv);
        categoryTv=findViewById(R.id.categoryTv);
        languageTv=findViewById(R.id.languageTv);
        newsRv=findViewById(R.id.newsRv);

        //get data from intent (that we passed from adapter)
        Intent intent=getIntent();
        String id=intent.getStringExtra("id");
        String name=intent.getStringExtra("name");
        String  description=intent.getStringExtra("description");
        String country=intent.getStringExtra("country");
        String category=intent.getStringExtra("category");
        String language=intent.getStringExtra("language");


        actionBar.setTitle(name);//set title/name of news source we selected

        nameTv.setText(name);
        descriptionTv.setText(description);
        countryTv.setText("Country: "+country);
        categoryTv.setText("Category "+category);
        languageTv.setText("Language "+language);

        loadNewsData(id);
    }

    private void loadNewsData(String id) {
        //init list
        sourceDetailsArrayList =new ArrayList<>();

        //url
        String url="https://newsapi.org/v2/top-headlines?sources="+ id +"&apiKey="+Contants.API_KEY;

        //progress bar
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Loading News");
        progressDialog.show();

        //request data
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //we got the response
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    //we need to get array out of that object
                    JSONArray jsonArray=jsonObject.getJSONArray("articles");

                    //get all daa from that array using loop
                    for(int j=0;j<jsonArray.length();j++){

                        //each array element is a jsonObject
                        JSONObject jsonObjectNew=jsonArray.getJSONObject(j);
                        //get actual data from that json object,make sure to use same name as in response
                        String title=jsonObjectNew.getString("title");
                        String description=jsonObjectNew.getString("description");
                        String url=jsonObjectNew.getString("url");
                        String urlToImage=jsonObjectNew.getString("urlToImage");
                        String publishedAt=jsonObjectNew.getString("publishedAt");
                        String content=jsonObjectNew.getString("content");

                        //we need to convert date format
                        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        SimpleDateFormat dateFormat1=new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        String formattedDate ="";
                        try {
                            //try to format date time
                            Date date=dateFormat.parse(publishedAt);
                            formattedDate=dateFormat1.format(date);
                        }
                        catch(Exception e){
                            formattedDate=publishedAt;
                        }
                        //add data to new instance of model
                        ModeNewsSourceDetails model=new ModeNewsSourceDetails(""+title,""+description,""+url,""+urlToImage,""+formattedDate,""+content);

                        //add that model to our list
                        sourceDetailsArrayList.add(model);
                    }

                    //dismiss dialog
                    progressDialog.dismiss();

                    //setup adapter,add to that adapter
                    adapterNewsSourceDetails=new AdapterNewsSourceDetails(NewsSourceDetailsActivity.this,sourceDetailsArrayList);
                    //set adapter
                    newsRv.setAdapter(adapterNewsSourceDetails);
                } catch (Exception e) {
                    //exception while formatting data
                    progressDialog.dismiss();
                    Toast.makeText(NewsSourceDetailsActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //falied getting response dismiss progress,show error message
                progressDialog.dismiss();
                Toast.makeText(NewsSourceDetailsActivity.this,""+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        //add request to volley queue
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}