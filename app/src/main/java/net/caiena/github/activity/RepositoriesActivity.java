package net.caiena.github.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.caiena.github.R;
import net.caiena.github.Util.UpdateController;
import net.caiena.github.adapter.AdapterRepos;
import net.caiena.github.adapter.SpacesItemDecoration;
import net.caiena.github.model.DAO.RepositoryDAO;
import net.caiena.github.model.DAO.UserDAO;
import net.caiena.github.model.bean.Repository;

import java.util.ArrayList;


public class RepositoriesActivity extends BaseActivity {

    private RecyclerView listView;
    private TextView naoPossuiRepositorio;
    private Context context;
    private ArrayList<Repository> repositories;
//    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityTransitions();
        setContentView(R.layout.activity_repositories);

        repositories = new ArrayList<>();
        this.context = this;
//        setTitle("Repositórios");

        supportPostponeEnterTransition();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Repositórios");
//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                collapsingToolbarLayout.setBackground(new BitmapDrawable(getResources(), UserDAO.getInstance(this).findAll().get(0).getAvatar()));
//            }else{
//                collapsingToolbarLayout.setBackgroundDrawable(new BitmapDrawable(getResources(), UserDAO.getInstance(this).findAll().get(0).getAvatar()));
//            }
//        } catch (Exception ignored) {}
//        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#ffffff"));
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#ffffff"));
//        collapsingToolbarLayout.setContentScrimColor(Color.parseColor("#696969"));
        collapsingToolbarLayout.setStatusBarScrimColor(Color.parseColor("#ffffff"));

        naoPossuiRepositorio = (TextView) findViewById(R.id.text_nao_possui_repositorio);
//        progressBar = (ProgressBar) findViewById(R.id.progressBarList);
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

    private void initActivityTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        Drawable drawable = menu.findItem(R.id.refreshRepositories).getIcon();

        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, Color.parseColor("#ffffff"));
        menu.findItem(R.id.refreshRepositories).setIcon(drawable);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.refreshRepositories) {
            Intent intentWebView = new Intent(RepositoriesActivity.this, DownloadActivity.class);
            intentWebView.putExtra("update", true);
            intentWebView.putExtra("typeUpdate", UpdateController.TYPE_UPDATE_REPOSITORIES);
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
//                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
