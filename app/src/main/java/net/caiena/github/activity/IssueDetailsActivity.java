package net.caiena.github.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.caiena.github.R;
import net.caiena.github.adapter.AdapterIssueComment;
import net.caiena.github.adapter.SpacesItemDecoration;
import net.caiena.github.model.DAO.CommentDAO;
import net.caiena.github.model.DAO.RepositoryDAO;
import net.caiena.github.model.bean.Comment;

import java.util.ArrayList;

public class IssueDetailsActivity extends BaseActivity {

    private RecyclerView listView;
    private Context context;
    private ArrayList<Comment> comments;
    private ProgressBar progressBar;
    private int idIssue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_details);

        context = this;

        Bundle extras = getIntent().getExtras();
        idIssue = extras.getInt("idIssue");
        progressBar = (ProgressBar) findViewById(R.id.progressBarListComment);
        listView = (RecyclerView) findViewById(R.id.recycleViewListComment);
        listView.addItemDecoration(new SpacesItemDecoration(getResources()));
        listView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(mLayoutManager);

        new Thread(new Runnable() {
            public void run() {
                try {
                    comments.addAll(CommentDAO.getInstance(context).findByParam("issue_id" , idIssue));
                } catch (Exception e) {
                    comments = new ArrayList<>();
                }
                refreshAdapter();
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
    }

    private void refreshAdapter() {
        IssueDetailsActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!comments.isEmpty())
                    listView.setAdapter(new AdapterIssueComment(comments, context));
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
