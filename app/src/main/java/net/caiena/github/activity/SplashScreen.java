package net.caiena.github.activity;

import android.content.Intent;
import android.os.Bundle;

import net.caiena.github.R;
import net.caiena.github.Util.UpdateController;

public class SplashScreen extends BaseActivity {

    static public final int WEBVIEW_ACTIVITY_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (getAcessToken().trim().equals("")) {
            Intent intentWebView = new Intent(SplashScreen.this, WebViewActivity.class);
            startActivityForResult(intentWebView, WEBVIEW_ACTIVITY_REQUEST);
            return;
        }

        Intent intent;
        if (isFirstDownload()) {
            intent = new Intent(SplashScreen.this, DownloadActivity.class);
            intent.putExtra("update", false);
            startActivity(intent);
            return;
        }

        intent = new Intent(SplashScreen.this, RepositoriesActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == WEBVIEW_ACTIVITY_REQUEST) {
            if (resultCode == RESULT_OK) {
                setAcessToken(data.getStringExtra("access_token"));
                Intent intentWebView = new Intent(SplashScreen.this, DownloadActivity.class);
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
