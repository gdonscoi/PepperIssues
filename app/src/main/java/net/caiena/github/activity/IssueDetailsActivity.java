package net.caiena.github.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import net.caiena.github.R;
import net.caiena.github.adapter.AdapterIssueComment;
import net.caiena.github.model.bean.IssueComment;
import net.caiena.github.model.bean.Issue;

import java.util.ArrayList;

public class IssueDetailsActivity extends BaseActivity {

    private RecyclerView listView;
    private Context context;
    private ArrayList<IssueComment> comments;
    private ProgressBar progressBar;
    private Issue issue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_details);

        context = this;

        Bundle extras = getIntent().getExtras();
        issue = (Issue) extras.get("issue");
        setTitle("#" + issue.number);
        comments = new ArrayList<>();

        progressBar = (ProgressBar) findViewById(R.id.progressBarListComment);
        listView = (RecyclerView) findViewById(R.id.recycleViewListComment);
        listView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(mLayoutManager);

        new Thread(new Runnable() {
            public void run() {
                try {
//                    comments.addAll(CommentDAO.getInstance(context).findByParam("issue_id" , idIssue));
                    comments.addAll(new ArrayList<>(issue.commentList));
                } catch (Exception ignored) {

                }
                refreshAdapter();
            }
        }).start();
    }

    private void refreshAdapter() {
        IssueDetailsActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listView.setAdapter(new AdapterIssueComment(comments, issue, context));
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
