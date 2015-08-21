package net.caiena.github.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import net.caiena.github.R;
import net.caiena.github.Util.ActivityProgressUpdatable;
import net.caiena.github.Util.UpdateController;

public class UpdateActivity extends BaseActivity implements ActivityProgressUpdatable {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);
        progressBar.setProgress(0);

        UpdateController updateController = new UpdateController();
        updateController.setCallback(this);
        updateController.doUpdate(getAcessToken());

    }

    @Override
    public void updateProgressBar(final int progress) {
        UpdateActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressBar != null)
                    progressBar.incrementProgressBy(progress);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update, menu);
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
