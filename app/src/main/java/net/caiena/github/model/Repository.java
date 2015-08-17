package net.caiena.github.model;

import com.google.gson.annotations.SerializedName;

public class Repository {

    public String name;

    @SerializedName("full_name")
    public String nameFull;

    public String id;

    @SerializedName("private")
    public boolean isPrivate;

    public String description;

    @SerializedName("has_issues")
    public boolean hasIssues;

    public String url;

}
