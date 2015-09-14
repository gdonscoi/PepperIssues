package net.caiena.github.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.caiena.github.R;
import net.caiena.github.adapter.AdapterRepos;
import net.caiena.github.adapter.SpacesItemDecoration;
import net.caiena.github.model.DAO.RepositoryDAO;
import net.caiena.github.model.bean.Repository;

import java.util.ArrayList;


public class RepositoriesActivity extends BaseActivity {

    private RecyclerView listView;
    private TextView naoPossuiRepositorio;
    private Context context;
    private ArrayList<Repository> repositories;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repositories = new ArrayList<>();
        this.context = this;
        setTitle("Repositórios");

        naoPossuiRepositorio = (TextView) findViewById(R.id.text_nao_possui_repositorio);
        progressBar = (ProgressBar) findViewById(R.id.progressBarList);
        listView = (RecyclerView) findViewById(R.id.recycleViewList);
        listView.addItemDecoration(new SpacesItemDecoration(getResources()));
        listView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(mLayoutManager);

        new Thread(new Runnable() {
            public void run() {
                try {
                    repositories.addAll(RepositoryDAO.getInstance(context).getRepositoriesWithIssues());
                } catch (Exception e) {
                    repositories = new ArrayList<>();
                }
                refreshAdapter();
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.refresh) {
            Intent intentWebView = new Intent(RepositoriesActivity.this, UpdateActivity.class);
            intentWebView.putExtra("update" , true);
            startActivity(intentWebView);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
    }

    private void refreshAdapter() {
        RepositoriesActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (repositories.isEmpty())
                    naoPossuiRepositorio.setVisibility(View.VISIBLE);
                else
                    listView.setAdapter(new AdapterRepos(repositories, context));
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
