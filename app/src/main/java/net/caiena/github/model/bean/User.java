package net.caiena.github.model.bean;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "user")
public class User implements IEntidade, Serializable {

    @DatabaseField(id = true)
    public int id;

    @DatabaseField
    public String login;

    @DatabaseField
    public String name;

    @DatabaseField
    public String email;

    @DatabaseField
    public String type;

    @DatabaseField(columnName = "avatar_url")
    @SerializedName("avatar_url")
    public String avatarUrl;
}
