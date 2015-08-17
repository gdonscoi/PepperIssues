package net.caiena.github.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import net.caiena.github.R;
import net.caiena.github.Util.Constantes;

public class SplashScreen extends BaseActivity {

    static public final int WEBVIEW_ACTIVITY_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if(!getAcessToken().trim().equals("")){
            Intent intentWebView = new Intent(SplashScreen.this,MainActivity.class);
            startActivity(intentWebView);
            return;
        }

        Intent intentWebView = new Intent(SplashScreen.this,WebViewActivity.class);
        startActivityForResult(intentWebView, WEBVIEW_ACTIVITY_REQUEST);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == WEBVIEW_ACTIVITY_REQUEST) {
            if (resultCode == RESULT_OK) {
                setAcessToken(data.getStringExtra("access_token"));
                Intent intentWebView = new Intent(SplashScreen.this,MainActivity.class);
                startActivity(intentWebView);
            } else if(resultCode == RESULT_CANCELED) {
                Intent intentWebView = new Intent(SplashScreen.this, WebViewActivity.class);
                startActivityForResult(intentWebView, WEBVIEW_ACTIVITY_REQUEST);
            }
        }
    }
}