package net.caiena.github.model.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "user")
public class User implements IEntidade, Serializable {

    @DatabaseField(id = true)
    public int id;

    @DatabaseField
    public String login = "";

    @DatabaseField
    public String name= "";

    @DatabaseField
    public String email = "";

    @SerializedName("html_url")
    @DatabaseField
    public String html = "";

    @DatabaseField
    public String type = "";

    @DatabaseField(columnName = "avatar_url")
    @SerializedName("avatar_url")
    public String avatarUrl = "";

    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    private byte[] avatar;

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public Bitmap getAvatar() {
        if (this.avatar == null)
            return null;
        return BitmapFactory.decodeByteArray(this.avatar, 0, this.avatar.length);
    }

    public Bitmap getCircularAvatar() {
        Bitmap bitmapImage = getAvatar();
        if (bitmapImage == null)
            return null;

        Bitmap circleBitmap = Bitmap.createBitmap(bitmapImage.getWidth(), bitmapImage.getHeight(), Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmapImage, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(bitmapImage.getWidth() / 2, bitmapImage.getHeight() / 2, bitmapImage.getWidth() / 2, paint);

        return circleBitmap;
    }
}
