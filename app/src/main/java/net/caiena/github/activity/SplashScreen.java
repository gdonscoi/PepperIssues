package net.caiena.github.activity;

import android.content.Intent;
import android.os.Bundle;

import net.caiena.github.R;

public class SplashScreen extends BaseActivity {

    static public final int WEBVIEW_ACTIVITY_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (!getAcessToken().trim().equals("")) {
            Intent intentWebView = new Intent(SplashScreen.this,MainActivity.class);
//            Intent intentWebView = new Intent(SplashScreen.this, UpdateActivity.class);
            startActivity(intentWebView);
            return;
        }

        Intent intentWebView = new Intent(SplashScreen.this, WebViewActivity.class);
        startActivityForResult(intentWebView, WEBVIEW_ACTIVITY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == WEBVIEW_ACTIVITY_REQUEST) {
            if (resultCode == RESULT_OK) {
                setAcessToken(data.getStringExtra("access_token"));
                Intent intentWebView = new Intent(SplashScreen.this, UpdateActivity.class);
                startActivity(intentWebView);
            } else if (resultCode == RESULT_CANCELED) {
                Intent intentWebView = new Intent(SplashScreen.this, WebViewActivity.class);
                startActivityForResult(intentWebView, WEBVIEW_ACTIVITY_REQUEST);
            }
        }
    }

    @Override
    public void onBackPressed() {
    }
}
