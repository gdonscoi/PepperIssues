package net.caiena.github.model.bean;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "milestone")
public class Milestone implements IEntidade, Serializable {

    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField
    public String url;

    @DatabaseField
    public String number;

    @DatabaseField
    public String state;

    @DatabaseField
    public String title;

    @DatabaseField
    public String description;

    @DatabaseField(columnName = "repository_id", foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 1)
    public Repository repository;

    @DatabaseField(columnName = "open_issues")
    @SerializedName("open_issues")
    public int openIssues;

    @DatabaseField( columnName = "closed_issues")
    @SerializedName("closed_issues")
    public int closedIssues;

}
