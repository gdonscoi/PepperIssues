package net.caiena.github.model.bean;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import net.caiena.github.Util.AbstractDataProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@DatabaseTable(tableName = "issue")
public class Issue extends AbstractDataProvider.Data implements IEntidade, Serializable, Comparable {

    @DatabaseField(id = true)
    public String id;

    @DatabaseField(columnName = "repository_id", foreign = true, foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 1, columnDefinition = "integer references repository(id) on delete cascade")
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

    @DatabaseField
    public int comments;

    @Expose
    @ForeignCollectionField(eager = true, maxEagerForeignCollectionLevel = 1)
    public Collection<IssueComment> commentList = new ArrayList<>();

    public User user;

    @Expose
    @DatabaseField
    public String ownerLogin;

    @Expose
    @DatabaseField
    public int position;

    @Override
    public long getId() {
        return number;
    }

    @Override
    public Object getObject() {
        return this;
    }

    @Override
    public boolean isSectionHeader() {
        return false;
    }

    @Override
    public int getViewType() {
        return 0;
    }

    @Override
    public int getSwipeReactionType() {
        return 0;
    }

    @Override
    public String getText() {
        return title;
    }

    @Override
    public void setPinnedToSwipeLeft(boolean pinned) {

    }

    @Override
    public boolean isPinnedToSwipeLeft() {
        return false;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return ((Integer) this.position).compareTo(((Issue) o).position);
    }

    @Override
    public boolean equals(Object object) {
        return object != null && object instanceof Issue && ((Issue) object).id.equals(this.id);
    }
}
