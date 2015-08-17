package net.caiena.github.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
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
import net.caiena.github.adapter.AdapterRepos;
import net.caiena.github.adapter.SpacesItemDecoration;
import net.caiena.github.model.Repository;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IssuesActivity extends BaseActivity {

    private Context context;
    private RecyclerView listView;
    HashMap<String,String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues);

        this.context = this;
        listView = (RecyclerView) findViewById(R.id.recycleViewList);
        listView.addItemDecoration(new SpacesItemDecoration(getResources()));
        listView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(mLayoutManager);

        Type repositoryType = new TypeToken<List<Repository>>() {
        }.getType();
        GenericRequest<ArrayList<Repository>> aa = new GenericRequest<>(Constantes.URL_API_REPOSITORIES, repositoryType, new Response.Listener<ArrayList<Repository>>() {
            @Override
            public void onResponse(ArrayList<Repository> response) {
//                repositories.addAll(response);
//                listView.setAdapter(new AdapterRepos(repositories, context));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Response", error.getMessage());
            }
        }, params, true);

        params = new HashMap<>();
        params.put("Accept", "application/vnd.github.v3.full+json");

        requestQueue.add(aa);
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
