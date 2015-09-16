package net.caiena.github.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.caiena.github.R;
import net.caiena.github.Util.ActivityUpdatable;
import net.caiena.github.Util.DownloadController;
import net.caiena.github.Util.UpdateController;

public class DownloadActivity extends BaseActivity implements ActivityUpdatable {

    private ProgressBar progressBar;
    private TextView titleText;
    private ImageView avatar;
    private TextView textName;
    private TextView textHtml;
    private Animation fadeInAnimation;
    private Context context;
    private boolean flagUpdate;
    private int typeUpdate;
    private int idRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        context = this;

        Bundle extras = getIntent().getExtras();
        if(extras == null)
            extras = new Bundle();

        flagUpdate = extras.getBoolean("update",false);
        typeUpdate = extras.getInt("typeUpdate");
        idRepository = extras.getInt("idRepository" ,0);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);

        titleText = (TextView) findViewById(R.id.text_title_update);
        titleText.setText(context.getString(R.string.tile_text_update));
        titleText.setTextColor(Color.parseColor("#696969"));

        avatar = (ImageView) findViewById(R.id.avatar);
        avatar.setVisibility(View.GONE);
        avatar.setOnClickListener(null);

        textName = (TextView) findViewById(R.id.text_name);
        textName.setVisibility(View.GONE);

        textHtml = (TextView) findViewById(R.id.text_html);
        textHtml.setVisibility(View.GONE);

        fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        if(flagUpdate) {
            UpdateController updateController = new UpdateController(getAcessToken(), progressBar, this,typeUpdate);
            updateController.setCallback(this);
            updateController.execute(idRepository);
            return;
        }

        DownloadController downloadController = new DownloadController(getAcessToken(), progressBar, this);
        downloadController.setCallback(this);
        downloadController.execute();

    }

    @Override
    public void updateProgressBar(final int progress, final int max) {
        DownloadActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressBar != null) {
                    progressBar.setMax(max);
                    progressBar.setIndeterminate(false);
                    progressBar.incrementProgressBy(progress);
                }
            }
        });
    }

    @Override
    public void updateInfoActivity(final Bitmap circleBitmap, final String name, final String html) {
        DownloadActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (circleBitmap != null) {
                    avatar.setImageBitmap(circleBitmap);
                    avatar.setVisibility(View.VISIBLE);
                    avatar.startAnimation(fadeInAnimation);
                }
                if (!name.equals("")) {
                    textName.setText(name);
                    textName.setVisibility(View.VISIBLE);
                    textName.startAnimation(fadeInAnimation);
                }
                if (!html.equals("")) {
                    textHtml.setText(html);
                    textHtml.setVisibility(View.VISIBLE);
                    textHtml.startAnimation(fadeInAnimation);
                }
            }
        });
    }

    @Override
    public void updateError() {
        titleText.setText("Erro ao sincronizar");
        titleText.setTextColor(Color.parseColor("#c80900"));
        titleText.startAnimation(fadeInAnimation);

        Drawable imageButtonRefresh = ContextCompat.getDrawable(context, R.drawable.refresh_button);
        avatar.setImageDrawable(imageButtonRefresh);
        avatar.startAnimation(fadeInAnimation);

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if(flagUpdate) {
                    getIntent().putExtra("typeUpdate" ,typeUpdate);
                    getIntent().putExtra("idRepository", idRepository);
                }
                startActivity(getIntent());
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
