package net.caiena.github.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;

import net.caiena.github.R;
import net.caiena.github.Util.UpdateController;
import net.caiena.github.adapter.IssueDataProvider;
import net.caiena.github.adapter.IssueDraggableAdapter;
import net.caiena.github.model.DAO.IssueLabelDAO;
import net.caiena.github.model.bean.Issue;
import net.caiena.github.model.bean.IssueLabel;
import net.caiena.github.model.bean.Label;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class IssuesActivity extends BaseActivity {

    private Context context;
    private RecyclerView listView;
    private TextView naoPossui;
    private ArrayList<Issue> issues;
    private int idRepository;
    private ProgressBar progressBar;
    private IssueDraggableAdapter myItemAdapter;

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
        issues = new ArrayList<>();
        listView = (RecyclerView) findViewById(R.id.recycleViewListIssues);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(animator);
        if (supportsViewElevation()) {
            // Lollipop or later has native drop shadow feature. ItemShadowDecorator is not required.
        } else {
            listView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) ContextCompat.getDrawable(this, R.drawable.material_shadow_z1)));
        }
        listView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(this, R.drawable.list_divider), true));

        new Thread(new Runnable() {
            public void run() {
                List<IssueLabel> issueLabels;
                try {
                    issueLabels = IssueLabelDAO.getInstance(context).findByParam("repository_id", idRepository);
                } catch (Exception e) {
                    issueLabels = Collections.emptyList();
                }
                HashMap<String, ArrayList<Label>> hashMapIssues = new HashMap<>();
                for (IssueLabel issueLabel : issueLabels) {
                    if (!hashMapIssues.containsKey(issueLabel.issue.id)) {
                        hashMapIssues.put(issueLabel.issue.id, new ArrayList<Label>());
                        hashMapIssues.get(issueLabel.issue.id).add(issueLabel.label);
                        continue;
                    }
                    hashMapIssues.get(issueLabel.issue.id).add(issueLabel.label);
                }
                for (IssueLabel issueLabel : issueLabels) {
                    if (hashMapIssues.containsKey(issueLabel.issue.id)) {
                        Issue issue = issueLabel.issue;
                        issue.labels.addAll(hashMapIssues.get(issue.id));
                        issues.add(issue);
                        hashMapIssues.remove(issue.id);
                    }
                }
                if (!issues.isEmpty())
                    Collections.sort(issues);
                refreshAdapter();
            }
        }).start();
    }

    private void refreshAdapter() {
        IssuesActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (issues.isEmpty())
                    naoPossui.setVisibility(View.VISIBLE);
                else {
                    RecyclerViewDragDropManager mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
                    mRecyclerViewDragDropManager.setDraggingItemShadowDrawable(
                            (NinePatchDrawable) ContextCompat.getDrawable(context, R.drawable.material_shadow_z3));

                    IssueDataProvider issueDataProvider = new IssueDataProvider(issues);
                    myItemAdapter = new IssueDraggableAdapter(issueDataProvider, context);
                    RecyclerView.Adapter mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(myItemAdapter);

                    listView.setAdapter(mWrappedAdapter);
                    mRecyclerViewDragDropManager.attachRecyclerView(listView);
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_issues, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.refreshIssues) {
            Intent intentWebView = new Intent(IssuesActivity.this, DownloadActivity.class);
            intentWebView.putExtra("update" , true);
            intentWebView.putExtra("typeUpdate", UpdateController.TYPE_UPDATE_ISSUES);
            intentWebView.putExtra("idRepository", idRepository);
            startActivity(intentWebView);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private boolean supportsViewElevation() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }

    @Override
    protected void onPause() {
        myItemAdapter.saveOrderList();
        super.onPause();
    }
}
