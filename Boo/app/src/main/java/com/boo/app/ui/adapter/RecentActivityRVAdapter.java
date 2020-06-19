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
import com.boo.app.model.RecentActivityItem;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecentActivityRVAdapter extends RecyclerView.Adapter<RecentActivityRVAdapter.RecentActivityViewHolder> {

    Context ctx;
    List<RecentActivityItem> recentActivity;

    Transformation circle = new RoundedTransformationBuilder()
            .oval(true)
            .build();

    public static class RecentActivityViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.recent_activity_profile_img)
        ImageView profileImg;
        @Bind(R.id.user_name)
        TextView userName;
        @Bind(R.id.recent_activity_name)
        TextView activityName;
//        @Bind(R.id.recent_activity_subject)
//        TextView activitySubject;
        @Bind(R.id.recent_activity_time)
        TextView activityTime;

        public RecentActivityViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public RecentActivityRVAdapter(Context ctx, List<RecentActivityItem> recentActivity) {
        this.ctx = ctx;
        this.recentActivity = recentActivity;
    }

    @Override
    public RecentActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_activity_item, parent, false);
        return new RecentActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecentActivityViewHolder holder, int position) {
        RecentActivityItem activity = recentActivity.get(position);
        Picasso.with(ctx).load(ApiRequest.getMediaUrl(activity.getProfileImgId())).placeholder(R.drawable.ic_no_avatar).fit().centerCrop().transform(circle).into(holder.profileImg);
        holder.userName.setText(activity.getUserName());
        String type1 = activity.getUserName() + "is following you.";
        String type2 = activity.getUserName() + "booed your post.";
        String type3 = activity.getUserName() + "commented on your post.";
        if(activity.getRecentActivityType() == 1){
            holder.activityName.setText(type1);
        }else if(activity.getRecentActivityType() == 2){
            holder.activityName.setText(type2);
        }else{
            holder.activityName.setText(type3);
        }
        holder.activityTime.setText(activity.getRecentActivityTime());

    }

    @Override
    public int getItemCount() {
        return recentActivity.size();
    }

}
