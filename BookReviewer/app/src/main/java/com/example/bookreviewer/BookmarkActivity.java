package com.example.bookreviewer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {
    //    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
//    private DatabaseReference mReference = mDatabase.getReference();
//    private ChildEventListener mChild;
    private BookmarkAdapter adapter = new BookmarkAdapter();
    int id;
    String title;
    String image;
    String author;
    private MySQLiteOpenHelper helper;
    String dbName = "st_file.db";
    int dbVersion = 1; // 데이터베이스 버전
    private SQLiteDatabase db;
    String tag = "SQLite"; // Log 에 사용할 tag
    ImageView backbutton;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmark_layout);
        backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        mReference = mDatabase.getReference("child 이름"); // 변경값을 확인할 child 이름
//
//        mReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
//
//                    // child 내에 있는 데이터만큼 반복합니다.
//                    String msg2 = messageData.getValue().toString();
//                    Array.add(msg2);
//                    adapter.add(msg2);
//
//                }
//                adapter.notifyDataSetChanged();
//                listView.setSelection(adapter.getCount() - 1);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        helper = new MySQLiteOpenHelper(
                this,  // 현재 화면의 제어권자
                dbName,// db 이름
                null,  // 커서팩토리-null : 표준커서가 사용됨
                dbVersion);       // 버전

        try {
            db = helper.getWritableDatabase(); // 읽고 쓸수 있는 DB
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.e(tag, "데이터베이스를 얻어올 수 없음");
            finish(); // 액티비티 종료
        }

        init();
        //getData();
        initDatabase();
    }

    Cursor c;

    private void initDatabase() {

        c = db.rawQuery("select * from bookmark;", null);
        while (c.moveToNext()) {
            Bookmark data = new Bookmark();
            id = c.getInt(0);
            title = c.getString(1);
            image = c.getString(2);
            author = c.getString(3);
            data.setTitle(title);
            data.setAuthor(author);
            data.setImage(image);

            Log.e(tag, "id: " + id + "title:" + title + " image:" + image + " author:" + author);
            adapter.addItem(data);
        }
        adapter.notifyDataSetChanged();
    }

    private void init() {
        RecyclerView recyclerView = findViewById(R.id.bookmark_recycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new BookmarkAdapter();
        recyclerView.setAdapter(adapter);
    }
}
//    private void getData() {
//        // 임의의 데이터입니다.
//        List<String> listTitle = Arrays.asList("국화", "사막", "수국", "해파리", "코알라", "등대", "펭귄", "튤립",
//                "국화", "사막", "수국", "해파리", "코알라", "등대", "펭귄", "튤립");
//        List<String> listContent = Arrays.asList(
//
//        );
//        List<Integer> listResId = Arrays.asList(
//
//        );
//        for (int i = 0; i < listTitle.size(); i++) {
//            // 각 List의 값들을 data 객체에 set 해줍니다.
//            BookData data = new BookData();
//            data.setTitle(listTitle.get(i));
//            data.setAuthor(listContent.get(i));
//            data.setResId(listResId.get(i));
//
//            // 각 값이 들어간 data를 adapter에 추가합니다.
//            adapter.addItem(data);
//        }
//        adapter.notifyDataSetChanged();
//    }
////        mDatabase = FirebaseDatabase.getInstance();
////
////        mReference = mDatabase.getReference("log");
////        mReference.child("log").setValue("check");
////
////        mChild = new ChildEventListener() {
////
////            @Override
////            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
////
////            }
////
////            @Override
////            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
////
////            }
////
////            @Override
////            public void onChildRemoved(DataSnapshot dataSnapshot) {
////
////            }
////
////            @Override
////            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
////            }
////
////            @Override
////            public void onCancelled(DatabaseError databaseError) {
////
////            }
////        };
////        mReference.addChildEventListener(mChild);
////    }
////
////    @Override
////    protected void onDestroy() {
////        super.onDestroy();
////        mReference.removeEventListener(mChild);
////    }
//}
