package net.caiena.github.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import net.caiena.github.R;
import net.caiena.github.Util.Constantes;

import java.util.HashMap;

public class BaseActivity extends AppCompatActivity {

    private SharedPreferences prefUsuario;
    public static RequestQueue requestQueue;
    public static HashMap<String, String> params;
    public String access_token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefUsuario = getSharedPreferences(Constantes.SHAREDPREFERENCE_AUTH, 0);
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(this);
        if(params == null){
            params = new HashMap<>();
            params.put("Accept", "application/vnd.github.v3.full+json");
            params.put("charset", "utf-8");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
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

    public String getAcessToken() {
        if (access_token.equals(""))
            access_token = prefUsuario.getString("access_token", "");
        return access_token;
    }

    public void setAcessToken(String acessToken) {
        SharedPreferences.Editor editor = prefUsuario.edit();
        editor.putString("access_token", acessToken);
        editor.apply();
        access_token = acessToken;
    }

    public boolean isFirstDownload() {
        return prefUsuario.getBoolean("first_download", true);
    }

    public void setFirstDownload(boolean controlUpdate) {
        SharedPreferences.Editor editor = prefUsuario.edit();
        editor.putBoolean("first_download", controlUpdate);
        editor.apply();
    }

}
