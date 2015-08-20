package net.caiena.github.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Repository implements IEntidade{

    public String name;

    @SerializedName("full_name")
    public String nameFull;

    public int id;

    @SerializedName("private")
    public boolean isPrivate;

    public String description;

    @SerializedName("has_issues")
    public boolean hasIssues;

    @SerializedName("open_issues_count")
    public int countOpenIssues;

    public String url;

    public User owner;

    public ArrayList<Issue> issues;

}
