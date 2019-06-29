package com.example.bookreviewer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class reviewActivity extends AppCompatActivity {
    private ArrayList<reviewObject> list = new ArrayList<>();
    RecyclerView recyclerView;
    ScrollView childScroll;
    String site = "";
    String siteName;
    Intent intent;
    ImageView backbutton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_layout);
        new Description().execute();

        recyclerView = findViewById(R.id.recycler_review);
        childScroll = findViewById(R.id.childScroll);
        intent = getIntent();
        siteName = intent.getExtras().getString("siteName");
        Log.e("사이트",siteName);
        backbutton = findViewById(R.id.backbutton);

        backbutton.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

private class Description extends AsyncTask<Void, Void, Void> {

    private ProgressDialog progressDialog;

    @Override
    protected void onPreExecute(){
        super.onPreExecute();

        progressDialog = new ProgressDialog(reviewActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("정보를 수집하는 중입니다.");
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params){
        try {
            if(siteName.equals("yes 24")){
                site = intent.getExtras().getString("yes24_url");
                Document doc = Jsoup.connect(site).get();
                Log.e("사이트 yes24",site);
                Elements mElementData = doc.select("div[class=gd_dContLft] div#infoset_reivew").select("div[class=reviewInfoGrp]");
                for (Element elem : mElementData) {
                    String title = elem.select("div[class=reviewInfoBot crop] div[class=review_cont]").text();
                    Log.e("내용",title);
                    String name = elem.select("div[class=review_etc] em[class=txt_id] a").text();
                    String Date = elem.select("div[class=review_etc] em[class=txt_data]").text();
                    String rate_s = elem.select("div[class=review_etc]").select("span[class=rating rating_4 bgGD]").first().text();
                    if(rate_s.equals("평점5점")){
                        rate_s = "10";
                    }
                    else if(rate_s.equals("평점4점")){
                        rate_s = "8";
                    }
                    else if(rate_s.equals("평점3점")){
                        rate_s = "6";
                    }
                    else if(rate_s.equals("평점2점")){
                        rate_s = "4";
                    }
                    else if(rate_s.equals("평점1점")){
                        rate_s = "2";
                    }
                    else{
                        rate_s = "0";
                    }

                    list.add(new reviewObject(Date, name, title, Float.parseFloat(rate_s)));
                }
            }
            else if(siteName.equals("교보문고")){
                site = intent.getExtras().getString("kyobo_url");
                Document doc = Jsoup.connect(site).get();
                Log.e("사이트 kyobo",site);
                Elements mElementData = doc.select("div[class=content_middle] ul[class=list_detail_booklog]").select("li");
                for(Element elem : mElementData){
                    String title = elem.select("div[class=content]").text();
                    Log.e("교보 내용",title);
                    String name = elem.select("div[class=title] span[class=info] a").text();
                    String date_s = elem.select("div[class=title] span[class=info]").text();
                    Log.e("교보 디테일",date_s);
                    //String[] date = date_s.split("|");
                    //date_s = date[1];
                    date_s = date_s.split("\\|")[1];
                    Log.e("교보 디테일",date_s);
                    String rate = elem.select("div[class=title] span[class=info] img").attr("alt");
                    if(rate.equals("5점 만점에 5점")){
                        rate = "10";
                    }
                    else if(rate.equals("5점 만점에 4점")){
                        rate = "8";
                    }
                    else if(rate.equals("5점 만점에 3점")){
                        rate = "6";
                    }
                    else if(rate.equals("5점 만점에 2점")){
                        rate = "4";
                    }
                    else if(rate.equals("5점 만점에 1점")){
                        rate = "2";
                    }
                    else if(rate.equals("5점 만점에 0점")){
                        rate = "0";
                    }
                    list.add(new reviewObject(date_s, name, title, Float.parseFloat(rate)));
                }
            }
            else if(siteName.equals("알라딘")){

            }
            else if(siteName.equals("interpark")){

            }
            else if(siteName.equals("BANDI N LUNIS")){

            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result){
        //어댑터와 연결
        reviewAdapter myAdapter = new reviewAdapter(list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myAdapter);

        progressDialog.dismiss();

    }
}
}

