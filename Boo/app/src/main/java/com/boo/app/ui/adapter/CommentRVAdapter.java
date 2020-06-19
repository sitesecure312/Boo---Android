package com.boo.app.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boo.app.R;
import com.boo.app.api.ApiRequest;
import com.boo.app.model.Comment;
import com.boo.app.ui.utils.DateFormat;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class CommentRVAdapter extends RecyclerView.Adapter<CommentRVAdapter.CommentViewHolder> {

    Context ctx;
    List<Comment> comments;
    Transformation circle = new RoundedTransformationBuilder()
            .oval(true)
            .build();


    public CommentRVAdapter(Context ctx, List<Comment> comments) {
        this.ctx = ctx;
        this.comments = comments;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        Picasso.with(ctx).load(ApiRequest.getMediaUrl(comment.getUserPhoto())).placeholder(R.drawable.ic_no_avatar).fit().centerCrop().transform(circle).into(holder.profileImg);
        holder.userName.setText(comment.getUserName());
        if(comment.getCommentDate() < 60){
            holder.commentTime.setText("Less than 1 min ago");
        }else if(comment.getCommentDate()/60 < 60){
            if(comment.getCommentDate()/60 < 2)
                holder.commentTime.setText("1 min ago");
            else
                holder.commentTime.setText(String.valueOf(comment.getCommentDate()/60) + " mins ago");
        }else if(comment.getCommentDate()/3600 < 24){
            if(comment.getCommentDate()/3600 < 2)
                holder.commentTime.setText("1 hr ago");
            else
                holder.commentTime.setText(String.valueOf(comment.getCommentDate()/3600) + " hrs ago");
        }else if(comment.getCommentDate()/86400 < 30){
            if(comment.getCommentDate()/86400 < 2)
                holder.commentTime.setText("1 day ago");
            else
                holder.commentTime.setText(String.valueOf(comment.getCommentDate()/86400) + " days ago");
        }else{
            if(comment.getCommentDate()/2592000 < 2)
                holder.commentTime.setText("1 month ago");
            else
                holder.commentTime.setText(String.valueOf(comment.getCommentDate()/2592000) + " months ago");
        }

        holder.commentField.setText(comment.getCommentContent());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.comment_profile_img)
        ImageView profileImg;
        @Bind(R.id.user_name)
        TextView userName;
        @Bind(R.id.comment_time)
        TextView commentTime;
        @Bind(R.id.comment)
        TextView commentField;


        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
