package net.caiena.github.model.bean;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@DatabaseTable(tableName = "repository")
public class Repository implements IEntidade , Serializable {

    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField
    public String name;

    @DatabaseField(columnName = "full_name")
    @SerializedName("full_name")
    public String nameFull;

    @DatabaseField(columnName = "private")
    @SerializedName("private")
    public boolean isPrivate;

    @DatabaseField
    public String description;

    @DatabaseField(columnName = "has_issues")
    @SerializedName("has_issues")
    public boolean hasIssues;

    @DatabaseField(columnName = "open_issues_count")
    @SerializedName("open_issues_count")
    public int countOpenIssues;

    @DatabaseField
    public String url;

    @DatabaseField(columnName = "user_id", foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 1)
    public User owner;

    @ForeignCollectionField(eager = true, maxEagerForeignCollectionLevel = 1)
    public Collection<Issue> issues = new ArrayList<>();

}
