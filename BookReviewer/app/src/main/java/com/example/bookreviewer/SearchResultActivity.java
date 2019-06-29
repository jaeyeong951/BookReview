package com.example.bookreviewer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;


public class SearchResultActivity extends AppCompatActivity {
    //private SearchResultAdapter adapter;
    int killed = 0;
    private RecyclerView recyclerView;
    private ArrayList<itemObject_detail> list = new ArrayList<>();
    ImageView backbutton;
    String naver;
    String site_address_yes24;
    String site_address_aladin;
    String site_address_kyobo;
    String site_address_interpark;
    String site_address_bandi;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchresult_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_result);
        new Description().execute();
        Intent intent = getIntent();
        naver = intent.getExtras().getString("naverlink");
        backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class Description extends AsyncTask<Void, Void, Void> {

        //진행바표시
        private ProgressDialog progressDialog;
        //@InjectView(R.id.elastic_download_view)
        //ElasticDownloadView mElasticDownloadView;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mElasticDownloadView.startIntro();

            progressDialog = new ProgressDialog(SearchResultActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("정보를 수집하는 중입니다.");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc_naver = Jsoup.connect(naver).get();
                Elements site_address = doc_naver.select("li[class=lowest npay]");
                Log.e("naver",naver);
                //int size = site_address.size();
                int size = 0;
                while(size < 5){
                    String name = "";
                    if(site_address.select("div[class=npay_wrap] a[title=새창]").first() == null){
                        killed = 1;
                        return null;
                    }
                    else{
                        name = site_address.select("div[class=npay_wrap] a[title=새창]").first().text();
                    }
                    Log.e("이름",name);
                    if(name.equals("예스24")){
                        site_address_yes24 = site_address.select("div[class=npay_wrap] a").attr("href");
                        Log.e("예스24",site_address_yes24);
                        site_address = site_address.next();
                        size++;
                    }
                    else if(name.equals("인터넷 교보문고")){
                        site_address_kyobo = site_address.select("div[class=npay_wrap] a").attr("href");
                        Log.e("교보문고",site_address_kyobo);
                        site_address = site_address.next();
                        size++;
                    }
                    else if(name.equals("알라딘")){
                        site_address_aladin = site_address.select("div[class=npay_wrap] a").attr("href");
                        Log.e("교보문고",site_address_aladin);
                        site_address = site_address.next();
                        size++;
                    }
                    else if(name.equals("인터파크 도서")){
                        site_address_interpark = site_address.select("div[class=npay_wrap] a").attr("href");
                        Log.e("교보문고",site_address_interpark);
                        site_address = site_address.next();
                        size++;
                    }
                    else if(name.equals("반디앤루니스")){
                        site_address_bandi = site_address.select("div[class=npay_wrap] a").attr("href");
                        Log.e("교보문고",site_address_bandi);
                        site_address = site_address.next();
                        size++;
                    }
                    else{
                        site_address = site_address.next();
                    }
                }
                String real_yes24 = site_address_yes24.split("URL=")[1];
                //Log.e("리얼예스이십사",real_yes24);
                Document doc_yes24     = Jsoup.connect(real_yes24).get();
                //String html_yes24 = doc_yes24.html();
                //Log.e("예스24",html_yes24);
                //String site_adress_real_yes24 = doc_yes24.select("script[type=text/javascript]")
                Document doc_kyobo     = Jsoup.connect(site_address_kyobo).get();
                //String html_kyobo = doc_kyobo.html();
                //Log.e("kyobo html",html_kyobo);
                //Connection.Response response = Jsoup.connect(site_address_aladin).followRedirects(false).userAgent("userAgent").execute();
                //Log.e("URL_aladin",response.url().toString());
                //Log.e("Has Location",String.valueOf(response.hasHeader("location")));
                //Log.e("Location",response.header("Location"));
                //Connection.Response response_s = Jsoup.connect((response.header("location"))).followRedirects(false).userAgent("userAgent").execute();
                //Log.e("URL_aladin_s_header",String.valueOf(response_s.hasHeader("location")));
                //Log.e("Location",response_s.header("Location"));
                String base_aladin = site_address_aladin.split("&u=")[1];
                Log.e("리얼알라딘",base_aladin);
                String splited_aladin_1 = base_aladin.split("%2F")[1];
                String splited_aladin_2 = base_aladin.split("%2F")[2];
                String splited_aladin_3 = splited_aladin_2.split("%3F")[0];
                String splited_aladin_4 = splited_aladin_2.split("%3F")[1];
                String splited_aladin_5 = splited_aladin_4.split("%3D")[0];
                String splited_aladin_6 = splited_aladin_4.split("%3D")[1];
                String real_aladin = "https://www.aladin.co.kr/" + splited_aladin_1 +"/" + splited_aladin_3 + "?"
                        + splited_aladin_5 + "=" + splited_aladin_6;
                Log.e("리얼리얼알라딘",real_aladin);
                Document doc_aladin    = Jsoup.connect(real_aladin).get();


                //Connection.Response response_yes24 = Jsoup.connect(site_address_yes24).followRedirects(true).execute();
                //Log.e("URL_yes24",response_yes24.url().toString());
                //Log.e("status_yes24",String.valueOf(response_yes24.statusCode()));


                //HttpURLConnection con_yes = (HttpURLConnection) new URL(site_address_yes24).openConnection();
                //con_yes.setInstanceFollowRedirects(false);
                //con_yes.connect();
                //final int responseCode = con_yes.getResponseCode();
                //Log.e("리스폰스코드",Integer.toString(responseCode));
                //String realURL_yes = con_yes.getHeaderField(3).toString();
                //Log.e("예스이십사 유알엘",realURL_yes);

                /*HttpURLConnection con_aladin = (HttpURLConnection) new URL("https://www.aladin.co.kr/part/wgate.aspx?k=yX0iVru1r6MZd1dA4HlGejY2Ue8syl&sk=641696&u=%2Fshop%2Fwproduct").openConnection();
                con_aladin.setInstanceFollowRedirects(false);
                con_aladin.connect();
                final int responseCode_aladin = con_aladin.getResponseCode();
                Log.e("리스폰스코드",Integer.toString(responseCode_aladin));
                String realURL_aladin = con_aladin.getHeaderField(3).toString();
                Log.e("예스이십사 유알엘",realURL_aladin);*/

                //Document doc_interpark = Jsoup.connect(site_address_interpark).get();
                String real_interpark = site_address_interpark.split("url=")[1];
                Log.e("리얼인터파크",real_interpark);
                String real_real_interpark = real_interpark.split("http://")[1];
                Document doc_interpark = Jsoup.connect(real_interpark).get();

                //http://www.bandinlunis.com/front/partner.do?partner=101&url=%2Ffront%2Fproduct%2FdetailProduct.do%3FprodId%3D4106561
                Log.e("반디",site_address_bandi);
                String base_bandi = site_address_bandi.split("url=%2F")[1];
                String splited_bandi_1 = base_bandi.split("%2F")[0]; //front
                String splited_bandi_2 = base_bandi.split("%2F")[1]; //product
                String splited_bandi_3 = base_bandi.split("%2F")[2]; //detailProduct.do%3FprodId%3D4106561
                String splited_bandi_4 = splited_bandi_3.split("%3F")[0]; //detailProduct.do
                String splited_bandi_5 = splited_bandi_3.split("%3F")[1]; //prodId%3D4106561
                String splited_bandi_6 = splited_bandi_5.split("%3D")[0]; //prodId
                String splited_bandi_7 = splited_bandi_5.split("%3D")[1]; //4106561
                String real_bandi = "http://www.bandinlunis.com/" + splited_bandi_1 + "/" + splited_bandi_2 + "/" +
                        splited_bandi_4 + "?" + splited_bandi_6 + "=" + splited_bandi_7;
                Document doc_bandi     = Jsoup.connect(real_bandi).get();

                Elements yes24_forImage = doc_yes24.select("div#yesWrap").select("div#yDetailTopWrap");
                Elements yes24_forOther = doc_yes24./*select("div[id=yDetailTopWrap]").*/select("div[class=topColRgt]");
                Elements yes24 = doc_yes24.select("div#yDetailTopWrap.tp_book");
                String yes24_image = yes24.select("em[class=imgBdr] img").attr("src");
                //Log.e("예스24",yes24_image);
                String yes24_title = yes24_forOther.select("div[class=gd_infoTop] div[class=gd_titArea] h2[class=gd_name]").text();
                String yes24_author = yes24.select("span[class=gd_auth] a").first().text();
                Elements yes24_star_UP = yes24.select("span[id=spanGdRating]");
                String yes24_star = yes24_star_UP.select("em[class=yes_b]").text();
                if(yes24_star.isEmpty()){
                    yes24_star = "0";
                }
                //Log.e("예스24",yes24_star);
                String yes24_price = yes24.select("span[class=nor_price] em[class=yes_m]").text();
                String yes24_review = yes24.select("span[class=gd_reviewCount] a em").text();

                Elements kyobo = doc_kyobo.select("div[class=content_middle]");
                String kyobo_image = kyobo.select("div[class=cover] a img").attr("src");
                String kyobo_title = kyobo.select("h1[class=title] strong").text();
                String kyobo_author = kyobo.select("span[class=name] a[class=detail_author]").text();
                String kyobo_star = kyobo.select("div[class=review] a img").attr("alt");
                //kyobo는 if문 필요 e.g. 5점 만점에 5점 = 5
                if(kyobo_star.equals("5점 만점에 5점")){
                    kyobo_star = "10";
                }
                else if(kyobo_star.equals("5점 만점에 4점")){
                    kyobo_star = "8";
                }
                else if(kyobo_star.equals("5점 만점에 3점")){
                    kyobo_star = "6";
                }
                else if(kyobo_star.equals("5점 만점에 2점")){
                    kyobo_star = "4";
                }
                else if(kyobo_star.equals("5점 만점에 1점")){
                    kyobo_star = "2";
                }
                else if(kyobo_star.equals("5점 만점에 0점")){
                    kyobo_star = "0";
                }
                String kyobo_price = kyobo.select("span[class=sell_price] strong").text();
                String kyobo_review = kyobo.select("div[class=review] a").next().select("a[href=#review]").text();


                Elements aladin = doc_aladin.select("div#Ere_prod_allwrap");
                String aladin_image = aladin.select("div[class=c_front] img#CoverMainImage").attr("src");
                String aladin_title = aladin.select("div[class=Ere_prod_titlewrap] a[class=Ere_bo_title]").text();
                String aladin_author = aladin.select("div[class=Ere_prod_titlewrap] li[class=Ere_sub2_title] a").first().text();
                String aladin_star = aladin.select("div[class=info_list Ere_fs15 Ere_ht18] a[class=Ere_sub_pink Ere_fs16 Ere_str]").text();
                String aladin_price = aladin.select("div[class=Ritem Ere_ht11] span").text();
                String aladin_review = aladin.select("div[class=info_list Ere_fs15 Ere_ht18] span[class=Ere_PR10]").next().select("a").first().text();

                Elements interpark = doc_interpark.select("div[class=incAll_wrap]");
                String interpark_image = interpark.select("div[class=imgBox] img").attr("src");
                String interpark_title = interpark.select("div[class=titleWrap] p").text();
                //Log.e("인터파크 타이틀",interpark_title);
                String interpark_author = interpark.select("div[class=bookInfoBox] ul li").first().select("a").text();
                String interpark_star = interpark.select("div[class=expectGradeWrap] div[class=star_count]").text();
                //Log.e("인터파크 스타",interpark_star);
                String interpark_price = interpark.select("div[class=tabBox_1] p[class=tt_price] span[class=price]").text();
                String interpark_review = interpark.select("div[class=titleSet1] span[class=total_count]").text();

                Elements bandi = doc_bandi.select("div[class=wrap_contents]");
                String bandi_image = bandi.select("div[class=thumb_bookCover] img").attr("src");
                String bandi_title = bandi.select("div[class=group_title] h1").text();
                String bandi_author = bandi.select("div[class=group_inside] ul li").first().select("a").text();
                String bandi_star_base = bandi.select("div[class=vote] span[class=medium_ratings_num]").text();
                String bandi_star = bandi_star_base.split(" ")[0];
                if(bandi_star.equals("-")){
                    bandi_star = "0";
                }
                String bandi_price = bandi.select("div[class=tbl_right] span[class=sale_price]").text();
                String bandi_review = bandi.select("div[class=etc_info] span[class=ml10]").text();

                if(kyobo_title.length() >= 7){
                    kyobo_title = kyobo_title.substring(0,6);
                    kyobo_title = kyobo_title + "...";
                }
                if(yes24_title.length() >= 7){
                    yes24_title = yes24_title.substring(0,6);
                    yes24_title = yes24_title + "...";
                }
                if(aladin_title.length() >= 7){
                    aladin_title = aladin_title.substring(0,6);
                    aladin_title = aladin_title + "...";
                }
                if(interpark_title.length() >= 7){
                    interpark_title = interpark_title.substring(0,6);
                    interpark_title = interpark_title + "...";
                }
                if(bandi_title.length() >= 7){
                    bandi_title = bandi_title.substring(0,6);
                    bandi_title = bandi_title + "...";
                }

                list.add(new itemObject_detail(
                        kyobo_title,kyobo_image,"교보문고",kyobo_author,
                        Float.parseFloat(kyobo_star),kyobo_price+"원",kyobo_review,site_address_kyobo));
                list.add(new itemObject_detail(
                        yes24_title,yes24_image,"yes 24",yes24_author,
                        Float.parseFloat(yes24_star),yes24_price+"원","리뷰 "+yes24_review+"개",site_address_yes24));
                list.add(new itemObject_detail(
                        aladin_title,aladin_image,"알라딘",aladin_author,
                        Float.parseFloat(aladin_star),
                        aladin_price+"원",
                        aladin_review,site_address_aladin));
                list.add(new itemObject_detail(
                        interpark_title,interpark_image,"interpark",interpark_author,
                        Float.parseFloat(interpark_star),interpark_price+"원",interpark_review,site_address_interpark
                ));
                list.add(new itemObject_detail(
                        bandi_title,bandi_image,"BANDI N LUNIS",bandi_author,
                        Float.parseFloat(bandi_star),bandi_price,bandi_review,site_address_bandi
                ));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //어댑터와 연결
            SearchResultAdapter myAdapter = new SearchResultAdapter(list);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(myAdapter);
            //((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.HORIZONTAL);
            //mElasticDownloadView.success ();
            progressDialog.dismiss();
            if(killed == 1)
            {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"해당 도서는 판매중이지 않습니다", Toast.LENGTH_SHORT).show();
                killed = 0;
                finish();
            }
        }
    }
}