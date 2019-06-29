package com.example.bookreviewer;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class InformationActivity extends AppCompatActivity {
    ImageView backbutton;
    String url,author,title,price,review,img_url,siteName;
    Float star;
    int code;
    private ImageView imageView_img , imageView_review;
    private RatingBar RatingBar_star;
    private TextView textView_title, textView_author, textView_review, textView_price, textView_site, textView_pub, textView_date, goSite;
    String real_aladin, real_interpark, real_bandi, yes24_url;
    ImageView bookcart;


    private MySQLiteOpenHelper helper;
    String dbName = "st_file.db";
    int dbVersion = 1; // 데이터베이스 버전
    private SQLiteDatabase db;
    String tag = "SQLite"; // Log 에 사용할 tag
    protected void onCreate(Bundle savedInstanceState) {
        helper = new MySQLiteOpenHelper(
                this,  // 현재 화면의 제어권자
                dbName,// db 이름
                null,  // 커서팩토리-null : 표준커서가 사용됨
                dbVersion);       // 버전

        try {
//         // 데이터베이스 객체를 얻어오는 다른 간단한 방법
//         db = openOrCreateDatabase(dbName,  // 데이터베이스파일 이름
//                          Context.MODE_PRIVATE, // 파일 모드
//                          null);    // 커서 팩토리
//
//         String sql = "create table mytable(id integer primary key autoincrement, name text);";
//        db.execSQL(sql);

            db = helper.getWritableDatabase(); // 읽고 쓸수 있는 DB
            //db = helper.getReadableDatabase(); // 읽기 전용 DB select문
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.e(tag, "데이터베이스를 얻어올 수 없음");
            finish(); // 액티비티 종료
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_layout);
        backbutton = findViewById(R.id.backbutton);
        bookcart = findViewById(R.id.bookcart);
        backbutton.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        String sql = "insert into bookmark (title,image,author) values(제목,이미지,작가);" ;
        Intent intent = getIntent();
        url = intent.getExtras().getString("URL");
        author = intent.getExtras().getString("author");
        siteName = intent.getExtras().getString("siteName");
        img_url = intent.getExtras().getString("coverImage");
        star = intent.getExtras().getFloat("star");
        title = intent.getExtras().getString("title");
        price = intent.getExtras().getString("price");
        code = intent.getExtras().getInt("code");
        //Toast.makeText(InformationActivity.this,url,Toast.LENGTH_SHORT).show();
        imageView_img = findViewById(R.id.info_bookImage);
        textView_title = findViewById(R.id.info_title);
        textView_author = findViewById(R.id.info_author);
        RatingBar_star = findViewById(R.id.information_bookstar);
        textView_price = findViewById(R.id.info_price);
        textView_site = findViewById(R.id.info_site);
        goSite = findViewById(R.id.gotoBooksite);
        String review = "";
        String date = "";
        textView_pub = findViewById(R.id.info_Publisher);
        textView_date = findViewById(R.id.info_Date);
        textView_review = findViewById(R.id.info_bookcontext);
        imageView_review = findViewById(R.id.gotoReviewActivity);
        imageView_review.setOnClickListener(new ImageView.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent= new Intent(InformationActivity.this,reviewActivity.class);
                intent.putExtra("kyobo_url",url);
                intent.putExtra("yes24_url",yes24_url);
                intent.putExtra("aladin_url",real_aladin);
                intent.putExtra("interpark_url",real_interpark);
                intent.putExtra("bandi_url",real_bandi);
                intent.putExtra("siteName",siteName);

                startActivity(intent);
            }
        });

        try {
            textView_title.setText(title);
            textView_author.setText("작가 : " + author);
            RatingBar_star.setRating(star);
            textView_price.setText("가격 : " + price);
            textView_site.setText(siteName);
            GlideApp.with(imageView_img).load(img_url).into(imageView_img);

            if(siteName.equals("yes 24")){
                if(code == 1){
                    yes24_url = url.split("URL=")[1];
                }else {
                    yes24_url = url;
                }
                Document doc = Jsoup.connect(yes24_url).get();
                Elements elem = doc.select("div#yesWrap");
                textView_date.setText(elem.select("div[class=gd_infoTop] span[class=gd_pubArea] span[class=gd_date]").text());
                textView_pub.setText("출판사 : " + elem.select("div[class=gd_infoTop] span[class=gd_pubArea] span[class=gd_pub] a").text());
                Elements context = doc.select("div#infoset_introduce.gd_infoSet");
                //Log.e("콘텍스트",context.select("div[class=infoWrap_txt] div[class=infoWrap_txtInner]").text());
                String e = context.select("div[class=infoWrap_txt] div[class=infoWrap_txtInner]").text();
                String e_start = e.split("<b>")[1];
                String e_end = e_start.split("</b>")[0];
                String e_context[] = e_end.split("<br/>");
                String e_final = "";
                for(int i = 0; i < e_context.length; i++){
                    e_final += e_context[i];
                    e_final += "\n";
                }
                textView_review.setText(e_final);
                goSite.setOnClickListener(new TextView.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Uri uri = Uri.parse(yes24_url);
                        Intent it = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(it);
                    }
                });
            }
            else if(siteName.equals("교보문고")){
                Document doc = Jsoup.connect(url).get();
                Elements elem = doc.select("div#wrap");
                textView_date.setText(elem.select("div[class=author] span[class=date]").text());
                textView_pub.setText("출판사 : " + elem.select("div[class=author] span[title=출판사]").text());
                textView_review.setText(elem.select("div[class=box_detail_article] div[class=content]").text());
                goSite.setOnClickListener(new TextView.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Uri uri = Uri.parse(url);
                        Intent it = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(it);
                    }
                });
            }
            else if(siteName.equals("알라딘")){
                if(code == 1){
                String base_aladin = url.split("&u=")[1];
                Log.e("리얼알라딘",base_aladin);
                String splited_aladin_1 = base_aladin.split("%2F")[1];
                String splited_aladin_2 = base_aladin.split("%2F")[2];
                String splited_aladin_3 = splited_aladin_2.split("%3F")[0];
                String splited_aladin_4 = splited_aladin_2.split("%3F")[1];
                String splited_aladin_5 = splited_aladin_4.split("%3D")[0];
                String splited_aladin_6 = splited_aladin_4.split("%3D")[1];
                real_aladin = "https://www.aladin.co.kr/" + splited_aladin_1 +"/" + splited_aladin_3 + "?"
                        + splited_aladin_5 + "=" + splited_aladin_6;
                }else{
                    real_aladin = url;
                }
                Document doc = Jsoup.connect(real_aladin).get();
                Elements elem = doc.select("div#Ere_prod_allwrap");
                //textView_date.setText(elem.select("li[class=Ere_sub2_title]").text());
                String pub = elem.select("li[class=Ere_sub2_title]").text();
                String pub_s = pub.split("2")[1];
                textView_date.setText("2" + pub_s);
                textView_pub.setText("출판사 : " + elem.select("li[class=Ere_sub2_title] a[class=Ere_sub2_title]").next().next().text());
                textView_review.setText(elem.select("div[class=Ere_prod_middlewrap] div[class=Ere_prod_mconts_box]").next().next().next().select("div[class=Ere_prod_mconts_R]").text());
                Log.e("알라딘 리뷰",elem.select("div[class=Ere_prod_middlewrap] div[class=Ere_prod_mconts_box]").text());
                goSite.setOnClickListener(new TextView.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Uri uri = Uri.parse(real_aladin);
                        Intent it = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(it);
                    }
                });
            }
            else if(siteName.equals("interpark")){
                if(code == 1) {
                    real_interpark = url.split("url=")[1];
                    Log.e("리얼인터파크", real_interpark);
                    String real_real_interpark = real_interpark.split("http://")[1];
                }else{
                    real_interpark = url;
                }
                Document doc = Jsoup.connect(real_interpark).get();
                Elements elem_all = doc.select("div[class=incAll_wrap]");
                Elements elem_inf = elem_all.select("div[class=bookInfoBox] ul").select("li");
                //Element elem_inf_first = elem_inf.select("li").first();
                textView_pub.setText(elem_inf.next().select("li").first().text());
                textView_date.setText(elem_inf.next().next().select("li").first().text());
                textView_review.setText(elem_all.select("div[class=detail_txtContent] div[class=txtBox] p b").text());
                goSite.setOnClickListener(new TextView.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Uri uri = Uri.parse(real_interpark);
                        Intent it = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(it);
                    }
                });
            }
            else if(siteName.equals("BANDI N LUNIS")){
                if(code == 1) {
                    String base_bandi = url.split("url=%2F")[1];
                    String splited_bandi_1 = base_bandi.split("%2F")[0]; //front
                    String splited_bandi_2 = base_bandi.split("%2F")[1]; //product
                    String splited_bandi_3 = base_bandi.split("%2F")[2]; //detailProduct.do%3FprodId%3D4106561
                    String splited_bandi_4 = splited_bandi_3.split("%3F")[0]; //detailProduct.do
                    String splited_bandi_5 = splited_bandi_3.split("%3F")[1]; //prodId%3D4106561
                    String splited_bandi_6 = splited_bandi_5.split("%3D")[0]; //prodId
                    String splited_bandi_7 = splited_bandi_5.split("%3D")[1]; //4106561
                    real_bandi = "http://www.bandinlunis.com/" + splited_bandi_1 + "/" + splited_bandi_2 + "/" +
                            splited_bandi_4 + "?" + splited_bandi_6 + "=" + splited_bandi_7;
                }else{
                    real_bandi = url;
                }
                Document doc = Jsoup.connect(real_bandi).get();
                Elements elem = doc.select("div[class=wrap_contents]");
                textView_pub.setText("출판사 : " + elem.select("div[class=row_item] div[class=group_inside] ul li").next().select("a").text());
                textView_date.setText(elem.select("div[class=row_item] div[class=group_inside] ul li").next().next().text());
                textView_review.setText(elem.select("div[class=box_contents] div[class=group_txt] strong").text());
                goSite.setOnClickListener(new TextView.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Uri uri = Uri.parse(real_bandi);
                        Intent it = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(it);
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //new Description().execute();

        //북마크 구현
        bookcart.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mPostReference.child("bookname").push().setValue(url);
                Toast.makeText(InformationActivity.this, "bookcart에 추가", Toast.LENGTH_SHORT).show();
                Cursor c = db.rawQuery("select * from bookmark;",null);
                String sql = "insert into bookmark (title,image,author) values(\"" + title +"\""+ ",\"" + img_url + "\",\"" + author + "\");" ;
                db.execSQL(sql);
            }
        });
    }
//    private class Description extends AsyncTask<Void, Void, Void>{
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//
//        }
//    }
}