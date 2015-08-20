package net.caiena.github.model.bean;

import com.google.gson.annotations.SerializedName;

public class Milestone implements IEntidade{

    public String url;

    public int id;

    public String number;

    public String state;

    public String title;

    public String description;

    @SerializedName("open_issues")
    public int openIssues;

    @SerializedName("closed_issues")
    public int closedIssues;

}
