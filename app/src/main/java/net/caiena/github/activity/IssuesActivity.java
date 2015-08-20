package net.caiena.github.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import net.caiena.github.R;
import net.caiena.github.Util.Constantes;
import net.caiena.github.Util.GenericRequest;
import net.caiena.github.adapter.AdapterIssues;
import net.caiena.github.adapter.SpacesItemDecoration;
import net.caiena.github.model.bean.Issue;
import net.caiena.github.model.bean.Milestone;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class IssuesActivity extends BaseActivity {

    private Context context;
    private RecyclerView listView;
    private ArrayList<Issue> issues;
    private String nameRepository;
    private String loginOwner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues);

        this.context = this;
        Bundle extras = getIntent().getExtras();
        nameRepository = extras.getString("repository");
        loginOwner = extras.getString("owner");
        listView = (RecyclerView) findViewById(R.id.recycleViewListIssues);
        listView.addItemDecoration(new SpacesItemDecoration(getResources()));
        listView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(mLayoutManager);

        issues = new ArrayList<>();

        final Type issueType = new TypeToken<List<Issue>>() {}.getType();

        final Type milestoneType = new TypeToken<List<Milestone>>() {}.getType();
        GenericRequest<ArrayList<Milestone>> requestMilestone = new GenericRequest<>(Constantes.URL_API_REPOS
                .concat(loginOwner != null ? loginOwner : "")
                .concat("/")
                .concat(nameRepository != null ? nameRepository : "")
                .concat(Constantes.URL_API_MILESTONE)
                .concat(getAcessToken()), milestoneType, new Response.Listener<ArrayList<Milestone>>() {
            @Override
            public void onResponse(ArrayList<Milestone> response) {
                for(Milestone milestone : response)
                    if(milestone.title.toLowerCase().equals("backlog") || milestone.title.toLowerCase().equals("product backlog") ) {

                        GenericRequest<ArrayList<Issue>> requestIssue = new GenericRequest<>(Constantes.URL_API_REPOS
                                .concat(loginOwner != null ? loginOwner : "")
                                .concat("/")
                                .concat(nameRepository != null ? nameRepository : "")
                                .concat(Constantes.URL_API_ISSUES)
                                .concat(getAcessToken()
                                        .concat("&milestone=")
                                        .concat(String.valueOf(milestone.number))
                                        .concat("&labels=feature")), issueType, new Response.Listener<ArrayList<Issue>>() {
                            @Override
                            public void onResponse(ArrayList<Issue> response) {
                                issues.addAll(response);
                                listView.setAdapter(new AdapterIssues(issues, context));
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                listView.setAdapter(new AdapterIssues(issues, context));
                                Log.i("Response", error.getMessage());
                            }
                        }, params, true);

                        requestQueue.add(requestIssue);
                        break;
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Response", error.getMessage());
            }
        }, params, true);

        requestQueue.add(requestMilestone);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_issues, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
