package net.caiena.github.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.caiena.github.R;
import net.caiena.github.model.bean.Comment;

import java.util.ArrayList;

public class AdapterIssueComment extends RecyclerView.Adapter<AdapterIssueComment.ViewHolder> {

    private ArrayList<Comment> comments;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public RelativeLayout container;
        public TextView bodyComment;
        public TextView ownerComment;

        public ViewHolder(View v) {
            super(v);
            container = (RelativeLayout) v.findViewById(R.id.container_issue_comment);
            bodyComment = (TextView) container.findViewById(R.id.text_body_issue_comment);
            ownerComment = (TextView) container.findViewById(R.id.text_owner_issue_comment);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public AdapterIssueComment(ArrayList<Comment> comments, Context context) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public AdapterIssueComment.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_issue_comment_list, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bodyComment.setText(comments.get(position).body);
        holder.ownerComment.setText(comments.get(position).ownerComment);
    }


    @Override
    public int getItemCount() {
        return comments.size();
    }

}
