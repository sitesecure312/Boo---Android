package com.boo.app.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boo.app.R;
import com.boo.app.api.ApiRequest;
import com.boo.app.model.FeedItem;
import com.boo.app.ui.activity.CommentActivity;
import com.boo.app.ui.activity.FullPhotoActivity;
import com.boo.app.ui.activity.SharePostActivity;
import com.boo.app.ui.utils.DateFormat;
import com.boo.app.ui.utils.PicassoUtils;
import com.boo.app.ui.utils.TintUtils;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedRVAdapter extends RecyclerView.Adapter<FeedRVAdapter.FeedViewHolder> {

    public Context ctx;
    public static String photoUrl;
    private List<FeedItem> items;
    WeakReference<ActionListener> listener;
    Transformation circle = new RoundedTransformationBuilder()
            .oval(true)
            .build();

    public interface ActionListener {
        void onComment(FeedItem post);

        void onBoo(FeedItem post);

        void onUser(FeedItem post);

        void onShare(FeedItem post);
    }

    public FeedRVAdapter(Context ctx, List<FeedItem> items, ActionListener listener) {
        this.ctx = ctx;
        this.items = items;
        this.listener = new WeakReference<>(listener);
    }


    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_list_item, parent, false);
        return new FeedViewHolder(view, ctx);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        final FeedItem item = items.get(position);
        Picasso.with(ctx).load(ApiRequest.getMediaUrl(item.getUserPhoto())).placeholder(R.drawable.ic_no_avatar).fit().centerCrop().transform(circle).into(holder.profileImg);
        holder.userName.setText(item.getUserName());
        if(item.getTimeDiff() < 60){
            holder.postTime.setText("Less than 1 min ago");
        }else if(item.getTimeDiff()/60 < 60){
            if(item.getTimeDiff()/60 < 2)
                holder.postTime.setText("1 min ago");
            else
                holder.postTime.setText(String.valueOf(item.getTimeDiff()/60) + " mins ago");
        }else if(item.getTimeDiff()/3600 < 24){
            if(item.getTimeDiff()/3600 < 2)
                holder.postTime.setText("1 hr ago");
            else
                holder.postTime.setText(String.valueOf(item.getTimeDiff()/3600) + " hrs ago");
        }else if(item.getTimeDiff()/86400 < 30){
            if(item.getTimeDiff()/86400 < 2)
                holder.postTime.setText("1 day ago");
            else
                holder.postTime.setText(String.valueOf(item.getTimeDiff()/86400) + " days ago");
        }else{
            if(item.getTimeDiff()/2592000 < 2)
                holder.postTime.setText("1 month ago");
            else
                holder.postTime.setText(String.valueOf(item.getTimeDiff()/2592000) + " months ago");
        }
//        holder.postTime.setText(DateFormat.getTimeExpired(item.getPostdate()));
        if (!TextUtils.isEmpty(item.getPhotoUrl())) {
            holder.postImg.setVisibility(View.VISIBLE);
            Picasso.with(ctx).load(ApiRequest.getMediaUrl(item.getPhotoUrl())).into(holder.postImg);
        } else {
            holder.postImg.setVisibility(View.GONE);
        }
        holder.postImg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                photoUrl = item.getPhotoUrl();
                ctx.startActivity(new Intent(ctx, FullPhotoActivity.class));
            }
        });
        holder.postContent.setText(item.getPostContent());
        holder.booTooButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.get().onBoo(item);
            }
        });
        holder.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.get().onComment(item);
            }
        });
        if(item.getBooedCount()>0){
            holder.mTxtBoo.setText(ctx.getString(R.string.boo_too_count,item.getBooedCount()));
        }
        else {
            holder.mTxtBoo.setText(R.string.boo_too);
        }
        if(item.getCommentsCount()>0){
            holder.txtComment.setText(ctx.getString(R.string.comment_count,item.getCommentsCount()));
        }
        else {
            holder.txtComment.setText(R.string.comment);
        }
        holder.booTooButton.setSelected(item.isBooedByMe());
        holder.blockUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.get().onUser(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class FeedViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.block_user)
        View blockUser;
        @Bind(R.id.user_name)
        TextView userName;
        @Bind(R.id.post_time)
        TextView postTime;
        @Bind(R.id.post_img)
        ImageView postImg;
        @Bind(R.id.post_text)
        TextView postContent;
        @Bind(R.id.profile_image)
        ImageView profileImg;

        @Bind(R.id.ic_boo_too)
        ImageView icBooToo;
        @Bind(R.id.ic_comment)
        ImageView icComment;
        @Bind(R.id.ic_share)
        ImageView icShare;
        @Bind(R.id.txtBoo)
        TextView mTxtBoo;
        @Bind(R.id.txtComment)
        TextView txtComment;
        @Bind(R.id.boo_too_btn)
        LinearLayout booTooButton;
        @Bind(R.id.comment_btn)
        LinearLayout commentButton;
        @Bind(R.id.share_btn)
        LinearLayout shareButton;


        public FeedViewHolder(View itemView, Context ctx) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            TintUtils.setPressedTint(ctx, icBooToo, R.color.colorBottomMenuItem, R.color.colorPrimary);
            TintUtils.setSelectedTint(ctx, icBooToo, R.color.colorBottomMenuItem, R.color.colorPrimary);
            TintUtils.setPressedTint(ctx, icComment, R.color.colorBottomMenuItem, R.color.colorPrimary);
            TintUtils.setPressedTint(ctx, icShare, R.color.colorBottomMenuItem, R.color.colorPrimary);
        }
    }


}
