package net.caiena.github.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;

import net.caiena.github.Util.Constantes;
import net.caiena.github.Util.GenericRequest;
import net.caiena.github.R;

import java.util.HashMap;

public class WebViewActivity extends BaseActivity {

    private String accessCode = "";
    private final String accessCodeFragment = "code=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        final WebView webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {


                if (url.contains(accessCodeFragment) && accessCode.equals("")) {
                    accessCode = url.substring(url.indexOf(accessCodeFragment));
                    HashMap<String, String> params = new HashMap<>();
                    params.put("client_id", Constantes.CLIENT_ID);
                    params.put("client_secret", Constantes.CLIENT_SECRET);
                    params.put("code", accessCode.substring(5));
                    params.put("Content-Type", "application/json");
                    params.put("Accept", "application/json");

                    GenericRequest<JsonObject> a = new GenericRequest<>(Request.Method.POST, Constantes.URL_GITHUB.concat(Constantes.URL_ACESS_TOKEN), JsonObject.class, params, new Response.Listener<JsonObject>() {
                        @Override
                        public void onResponse(JsonObject response) {
                            Log.i("Response", response.toString());
                            String accessToken = response.get("access_token").toString().replaceAll("\"", "");

                            Intent requestIntent = new Intent();
                            requestIntent.putExtra("access_token", accessToken);

                            setResult(accessToken.trim().equals("") ? RESULT_CANCELED : RESULT_OK, requestIntent);
                            finish();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("Response", error.getMessage());
                        }
                    }, params);
                    requestQueue.add(a);

                    webView.setVisibility(View.GONE);
                }

            }

        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(Constantes.URL_GITHUB.concat(Constantes.URL_AUTH));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_web_view, menu);
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
