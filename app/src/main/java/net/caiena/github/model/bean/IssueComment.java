package net.caiena.github.model.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "comment")
public class IssueComment implements Serializable, IEntidade {

    @DatabaseField(id = true)
    public String id;

    @DatabaseField(columnName = "issue_id", foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 1)
    public Issue issue;

    public User user;

    @DatabaseField
    public String ownerComment = "";

    @DatabaseField
    public String body = "";
}
