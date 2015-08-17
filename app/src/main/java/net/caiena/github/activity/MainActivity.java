package net.caiena.github.activity;

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

import net.caiena.github.Util.Constantes;
import net.caiena.github.Util.GenericRequest;
import net.caiena.github.R;

import java.util.HashMap;


public class MainActivity extends BaseActivity {

    private HashMap<String, String> params;
    private JsonObject user;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        GenericRequest<JsonArray> aaa = new GenericRequest<>(Request.Method.GET, Constantes.URL_GITHUB_API.concat(Constantes.URL_API_ISSUES.concat(Constantes.URL_PARAM_ACESS_TOKEN.concat(access_token))), JsonArray.class, params, new Response.Listener<JsonArray>() {
//            @Override
//            public void onResponse(JsonArray response) {
//                Log.i("Response", response.toString());
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.i("Response", error.getMessage());
//            }
//        }, params);
//        requestQueue.add(aaa);
        final GenericRequest<JsonObject> aa = new GenericRequest<>("https://api.github.com/user/repos?access_token=456e50131d62d238b7be0e044a2c70249fe2210d", JsonObject.class, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                Log.i("Response", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Response", error.getMessage());
            }
        }, params);


        params = new HashMap<>();
        params.put("Accept", "application/vnd.github.v3.full+json");
        params.put("Authorization", getAcessToken());
        GenericRequest<JsonObject> a = new GenericRequest<>(Constantes.URL_GITHUB_API.concat(Constantes.URL_API_AUTORIZATION_USER.concat(getAcessToken())), JsonObject.class, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                Log.i("Response", response.toString());
                requestQueue.add(aa);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Response", error.getMessage());
            }
        }, params);
        requestQueue.add(aa);

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
//        params.put("note", "admin script");
//        params.put("scopes", "repo");
//        GenericRequest<JsonArray> aa = new GenericRequest<>(Request.Method.GET, "https://api.github.com/authorizations", JsonArray.class, params, new Response.Listener<JsonArray>() {
//            @Override
//            public void onResponse(JsonArray response) {
//                Log.i("Response", response.toString());
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.i("Response", error.getMessage());
//            }
//        }, params);
//        requestQueue.add(aa);
    }
}
