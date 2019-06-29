package com.example.bookreviewer;

import android.content.Context;
import android.content.Intent;
import android.media.Rating;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class reviewAdapter extends RecyclerView.Adapter<reviewAdapter.ViewHolder> {

    //데이터 배열 선언
    private ArrayList<reviewObject> mList;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView reviewContext;
        private TextView reviewerName;
        private TextView reviewDate;
        private RatingBar reviewStar;


        public ViewHolder(View itemView) {
            super(itemView);

            reviewContext = itemView.findViewById(R.id.textView);
            reviewerName = itemView.findViewById(R.id.reviewer_name);
            reviewDate = itemView.findViewById(R.id.reveiwer_date);
            reviewStar = itemView.findViewById(R.id.reviewer_bookstar);
        }
    }

    //생성자
    public reviewAdapter(ArrayList<reviewObject> list) {
        this.mList = list;
    }

    @NonNull
    @Override
    public reviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_viewholder, parent, false);
        this.context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull reviewAdapter.ViewHolder holder, final int position) {

        holder.reviewContext.setText(String.valueOf(mList.get(position).getReview()));
        holder.reviewerName.setText(String.valueOf(mList.get(position).getName()));
        holder.reviewStar.setRating(Float.valueOf(mList.get(position).getRating()));
        holder.reviewDate.setText(String.valueOf(mList.get(position).getDate()));
//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent= new Intent(context,InformationActivity.class);
//                intent.putExtra("URL",mList.get(position).getUrl());
//                intent.putExtra("siteName",mList.get(position).getSite_name());
//                intent.putExtra("coverImage",mList.get(position).getImg_url());
//                intent.putExtra("author",mList.get(position).getAuthor());
//                intent.putExtra("title",mList.get(position).getTitle());
//                intent.putExtra("star",mList.get(position).getStar());
//                intent.putExtra("price",mList.get(position).getPrice());
//
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}