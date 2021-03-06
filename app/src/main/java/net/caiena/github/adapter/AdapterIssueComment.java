package net.caiena.github.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.caiena.github.R;
import net.caiena.github.model.bean.Issue;
import net.caiena.github.model.bean.IssueComment;

import java.util.ArrayList;

import in.uncod.android.bypass.Bypass;

public class AdapterIssueComment extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<IssueComment> comments;
    private Issue issue;
    private Context context;
    private Bypass bypass;
    public static final int CONTENT_ISSUE_TYPE = 0;
    public static final int CONTENT_COMMENT_TYPE = 1;

    public static class ViewHolderIssue extends RecyclerView.ViewHolder implements View.OnClickListener {
        public RelativeLayout container;
        public TextView titleIssue;
        public TextView bodyIssue;
        public TextView ownerIssue;

        public ViewHolderIssue(View v) {
            super(v);
            container = (RelativeLayout) v.findViewById(R.id.container_issue_detail_title);
            titleIssue = (TextView) container.findViewById(R.id.text_title_issue);
            bodyIssue = (TextView) container.findViewById(R.id.text_body_issue);
            ownerIssue = (TextView) container.findViewById(R.id.text_owner_issue);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public static class ViewHolderComment extends RecyclerView.ViewHolder implements View.OnClickListener {
        public RelativeLayout container;
        public TextView bodyComment;
        public TextView ownerComment;

        public ViewHolderComment(View v) {
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

    public AdapterIssueComment(ArrayList<IssueComment> comments, Issue issue, Context context) {
        this.context = context;
        this.comments = comments;
        this.issue = issue;
        this.bypass= new Bypass(context);
    }

    @Override
    public int getItemViewType(int position) {
        return position == CONTENT_ISSUE_TYPE ? CONTENT_ISSUE_TYPE : CONTENT_COMMENT_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View v;
        if (viewType == CONTENT_ISSUE_TYPE) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_issue_detail_list, parent, false);
            return new ViewHolderIssue(v);
        }

        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_issue_comment_list, parent, false);
        return new ViewHolderComment(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderComment) {
            ((ViewHolderComment) holder).bodyComment.setText(bypass.markdownToSpannable(comments.get(position - 1).body));
            ((ViewHolderComment) holder).bodyComment.setMovementMethod(LinkMovementMethod.getInstance());
            ((ViewHolderComment) holder).ownerComment.setText(comments.get(position - 1).ownerComment);
            return;
        }

        ((ViewHolderIssue) holder).titleIssue.setText(issue.title);
        ((ViewHolderIssue) holder).bodyIssue.setText(bypass.markdownToSpannable(issue.body));
        ((ViewHolderIssue) holder).bodyIssue.setMovementMethod(LinkMovementMethod.getInstance());
        ((ViewHolderIssue) holder).ownerIssue.setText(issue.ownerLogin);

    }


    @Override
    public int getItemCount() {
        return (comments.size() + 1);
    }

}
