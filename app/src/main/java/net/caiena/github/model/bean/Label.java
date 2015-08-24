package net.caiena.github.model.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "label")
public class Label implements IEntidade, Serializable {

    @DatabaseField(id = true)
    public String url;

    @DatabaseField(columnName = "repository_id", foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 1)
    public Repository repository;

    @DatabaseField
    public String name;

    @DatabaseField
    public String color;
}
