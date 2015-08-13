package net.caiena.github;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private HashMap<String, String> params;
    private JsonObject user;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        button = (Button) findViewById(R.id.buttonA);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenericRequest<JsonArray> aaa = new GenericRequest<>(Request.Method.GET, "https://api.github.com/repos/caiena/sgc-issues/issues?access_token=" + WebViewActivity.accessToken, JsonArray.class, params, new Response.Listener<JsonArray>() {
                    @Override
                    public void onResponse(JsonArray response) {
                        Log.i("Response", response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Response", error.getMessage());
                    }
                }, params);
                WebViewActivity.requestQueue.add(aaa);
            }
        });
        params = new HashMap<>();
        params.put("Accept", "application/vnd.github.v3.full+json");
        GenericRequest<JsonObject> a = new GenericRequest<>("https://api.github.com/user?access_token=" + WebViewActivity.accessToken, JsonObject.class, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                user = response;
                Log.i("Response", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Response", error.getMessage());
            }
        }, params);
        WebViewActivity.requestQueue.add(a);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onBackPressed() {
        params.put("note", "admin script");
        params.put("scopes", "repo");
        GenericRequest<JsonArray> aa = new GenericRequest<>(Request.Method.GET, "https://api.github.com/authorizations", JsonArray.class, params, new Response.Listener<JsonArray>() {
            @Override
            public void onResponse(JsonArray response) {
                Log.i("Response", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Response", error.getMessage());
            }
        }, params);
        WebViewActivity.requestQueue.add(aa);
    }
}
