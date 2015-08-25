package net.caiena.github.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import net.caiena.github.R;
import net.caiena.github.Util.Constantes;
import net.caiena.github.Util.GenericRequest;
import net.caiena.github.adapter.AdapterRepos;
import net.caiena.github.adapter.SpacesItemDecoration;
import net.caiena.github.model.DAO.RepositoryDAO;
import net.caiena.github.model.bean.Repository;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {

    private RecyclerView listView;
    private Context context;
    private ArrayList<Repository> repositories;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repositories = new ArrayList<>();
        this.context = this;

        progressBar = (ProgressBar) findViewById(R.id.progressBarList);
        listView = (RecyclerView) findViewById(R.id.recycleViewList);
        listView.addItemDecoration(new SpacesItemDecoration(getResources()));
        listView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(mLayoutManager);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Repository> repositoriesDB = RepositoryDAO.getInstance(context).findAll();
//                    for(Repository repository:repositoriesDB)
//                        if(!repository.issues.isEmpty())
//                            repositories.add(repository);
                    repositories.addAll(repositoriesDB);
                } catch (Exception e) {
                    Log.i("ee","ee");
                }
                refreshAdapter();
            }
        }, 0);

    }

    @Override
    public void onBackPressed() {
    }

    private void refreshAdapter(){
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listView.setAdapter(new AdapterRepos(repositories, context));
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
