package net.caiena.github;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import java.util.HashMap;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    public static RequestQueue requestQueue;
    public static String accessToken;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        context = this;
        requestQueue = Volley.newRequestQueue(this);


        CookieManager.getInstance().setAcceptCookie(true);
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                String accessCodeFragment = "code=";

                // We hijack the GET request to extract the OAuth parameters

                if (url.contains(accessCodeFragment)) {
                    String accessCode = url.substring(url.indexOf(accessCodeFragment));
                    HashMap<String, String> params = new HashMap<>();
                    params.put("client_id", "75138c0316d81f593c13");
                    params.put("client_secret", "ab8412ede76a1e19d7a23e8f7992f7705207e2b6");
                    params.put("code", accessCode.substring(5));
                    params.put("Content-Type", "application/json");
                    params.put("Accept", "application/json");

                    GenericRequest<JsonObject> a = new GenericRequest<>(Request.Method.POST, "https://github.com/login/oauth/access_token", JsonObject.class, params, new Response.Listener<JsonObject>() {
                        @Override
                        public void onResponse(JsonObject response) {
                            Log.i("Response", response.toString());
                            accessToken = response.get("access_token").toString().replaceAll("\"", "");
                            Intent i = new Intent(context, MainActivity.class);
                            context.startActivity(i);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("Response", error.getMessage());
                        }
                    }, params);
                    requestQueue.add(a);

                }

            }

        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        String url = "https://github.com/login/oauth/authorize?client_id=75138c0316d81f593c13&scope=repo";
        webView.loadUrl(url);
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
