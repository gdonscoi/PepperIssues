package net.caiena.github.model.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "issuelabel")
public class IssueLabel implements IEntidade, Serializable {

    public IssueLabel(Issue issue, Label label, Repository repository){
        this.id = repository.name.concat(label.name).concat(issue.id);
        this.issue = issue;
        this.label = label;
        this.repository = repository;
    }

    public IssueLabel(){}

    @DatabaseField(id = true)
    public String id;

    @DatabaseField(columnName = "issue_id", foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 1)
    public Issue issue;

    @DatabaseField(columnName = "label_id", foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 1)
    public Label label;

    @DatabaseField(columnName = "repository_id", foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 1)
    public Repository repository;

}
