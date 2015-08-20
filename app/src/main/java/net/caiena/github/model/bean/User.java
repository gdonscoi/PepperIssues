package net.caiena.github.model.bean;

import com.google.gson.annotations.SerializedName;

public class User implements IEntidade{

    public String login;

    public String name;

    public String email;

    public int id;

    public String type;

    @SerializedName("avatar_url")
    public String avatarUrl;
}
