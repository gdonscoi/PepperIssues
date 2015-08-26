package net.caiena.github.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.caiena.github.R;
import net.caiena.github.adapter.AdapterIssues;
import net.caiena.github.adapter.SpacesItemDecoration;
import net.caiena.github.model.DAO.IssueLabelDAO;
import net.caiena.github.model.bean.Issue;
import net.caiena.github.model.bean.IssueLabel;
import net.caiena.github.model.bean.Label;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IssuesActivity extends BaseActivity {

    private Context context;
    private RecyclerView listView;
    private TextView naoPossui;
    private ArrayList<Issue> issues;
    private int idRepository;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues);

        this.context = this;
        Bundle extras = getIntent().getExtras();
        idRepository = extras.getInt("idRepository");

        setTitle(extras.getString("nameRepository"));

        naoPossui = (TextView) findViewById(R.id.text_nao_possui_issue);
        progressBar = (ProgressBar) findViewById(R.id.progressBarListIssue);
        listView = (RecyclerView) findViewById(R.id.recycleViewListIssues);
        listView.addItemDecoration(new SpacesItemDecoration(getResources()));
        listView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(mLayoutManager);

        issues = new ArrayList<>();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<IssueLabel> issueLabels;
                try {
                    issueLabels = IssueLabelDAO.getInstance(context).findByParam("repository_id", idRepository);
                } catch (Exception e) {
                    issueLabels = Collections.emptyList();
                }
                HashMap<String,ArrayList<Label>> hashMapIssues = new HashMap<>();
                    for (IssueLabel issueLabel : issueLabels) {
                        if(!hashMapIssues.containsKey(issueLabel.issue.id)) {
                            hashMapIssues.put(issueLabel.issue.id, new ArrayList<Label>());
                            hashMapIssues.get(issueLabel.issue.id).add(issueLabel.label);
                            continue;
                        }
                        hashMapIssues.get(issueLabel.issue.id).add(issueLabel.label);
                    }
                    for (IssueLabel issueLabel : issueLabels) {
                        if (hashMapIssues.containsKey(issueLabel.issue.id)){
                            Issue issue = issueLabel.issue;
                            issue.labels.addAll(hashMapIssues.get(issue.id));
                            issues.add(issue);
                            hashMapIssues.remove(issue.id);
                        }
                    }
                refreshAdapter();
            }
        }, 1000);
    }

    private void refreshAdapter() {
        IssuesActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(issues.isEmpty())
                    naoPossui.setVisibility(View.VISIBLE);
                else
                    listView.setAdapter(new AdapterIssues(issues, context));
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}
