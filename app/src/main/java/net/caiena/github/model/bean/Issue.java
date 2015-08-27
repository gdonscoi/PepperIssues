package net.caiena.github.model.bean;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import org.w3c.dom.Comment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@DatabaseTable(tableName = "issue")
public class Issue implements IEntidade, Serializable {

    @DatabaseField(id = true)
    public String id;

    @DatabaseField(columnName = "repository_id", foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 1)
    public Repository repository;

    @DatabaseField
    public String title;

    @DatabaseField
    public String state;

    @DatabaseField
    public String body;

    public ArrayList<Label> labels = new ArrayList<>();

    @DatabaseField
    public String nameMilestone;

    @DatabaseField
    public int number;

    @Expose
    @ForeignCollectionField(eager = true, maxEagerForeignCollectionLevel = 1)
    public Collection<Comment> comments = new ArrayList<>();

}
