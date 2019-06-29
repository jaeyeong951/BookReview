package com.example.bookreviewer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainAct_Adapter extends RecyclerView.Adapter<MainAct_Adapter.ViewHolder> {

    //데이터 배열 선언
    private ArrayList<ItemObject> mList;
    private Context context;
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView_img;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView_img = (ImageView) itemView.findViewById(R.id.recommend_image);
        }
    }

    //생성자
    public MainAct_Adapter(ArrayList<ItemObject> list) {
        this.mList = list;
    }

    @NonNull
    @Override
    public MainAct_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activitymain_image_viewholder, parent, false);
        this.context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAct_Adapter.ViewHolder holder, final int position) {


        GlideApp.with(holder.itemView).load(mList.get(position).getImg_url())
                .override(300,400)
                .into(holder.imageView_img);
        holder.imageView_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent= new Intent(context,InformationActivity.class);
                intent.putExtra("URL",mList.get(position).getUrl());
                intent.putExtra("siteName",mList.get(position).getSiteName());
                intent.putExtra("coverImage",mList.get(position).getImg_url());
                intent.putExtra("author",mList.get(position).getAuthor());
                intent.putExtra("title",mList.get(position).getTitle());
                intent.putExtra("star",mList.get(position).getStar());
                intent.putExtra("price",mList.get(position).getPrice());
                intent.putExtra("code",2);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}