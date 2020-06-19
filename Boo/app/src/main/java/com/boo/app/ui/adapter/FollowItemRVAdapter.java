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
import com.boo.app.model.FollowItem;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FollowItemRVAdapter extends RecyclerView.Adapter<FollowItemRVAdapter.FollowItemViewHolder> {

    Context ctx;
    List<FollowItem> followList;

    Transformation circle = new RoundedTransformationBuilder()
            .oval(true)
            .build();

    public static class FollowItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.recent_activity_profile_img)
        ImageView profileImg;
        @Bind(R.id.user_name)
        TextView userName;
        @Bind(R.id.follow_btn)
        TextView followingButton;

        Context ctx;
        boolean isFollow = false;

        public FollowItemViewHolder(View itemView, Context ctx) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.ctx = ctx;
        }

        @OnClick(R.id.follow_btn)
        public void onFollowingButtonClick() {
            if (isFollow) {
                followingButton.setSelected(false);
                followingButton.setText(R.string.follow);
                followingButton.setTextColor(ContextCompat.getColor(ctx, R.color.colorAccent));
                isFollow = false;
            } else {
                followingButton.setSelected(true);
                followingButton.setText(R.string.following);
                followingButton.setTextColor(ContextCompat.getColor(ctx, android.R.color.white));
                isFollow = true;
            }
        }
    }

    public FollowItemRVAdapter(Context ctx, List<FollowItem> followList) {
        this.ctx = ctx;
        this.followList = followList;
    }

    @Override
    public FollowItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.follow_item, parent, false);
        return new FollowItemViewHolder(view, ctx);
    }

    @Override
    public void onBindViewHolder(FollowItemViewHolder holder, int position) {
        final FollowItem follow = followList.get(position);
        Picasso.with(ctx).load(ApiRequest.getMediaUrl(follow.getProfileUrl())).placeholder(R.drawable.ic_no_avatar).fit().centerCrop().transform(circle).into(holder.profileImg);
        holder.userName.setText(follow.getUserName());
    }

    @Override
    public int getItemCount() {
        return followList.size();
    }
}
