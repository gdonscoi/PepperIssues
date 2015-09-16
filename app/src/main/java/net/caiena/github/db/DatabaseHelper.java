package net.caiena.github.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import net.caiena.github.model.bean.IssueComment;
import net.caiena.github.model.bean.IEntidade;
import net.caiena.github.model.bean.Issue;
import net.caiena.github.model.bean.IssueLabel;
import net.caiena.github.model.bean.Label;
import net.caiena.github.model.bean.Repository;
import net.caiena.github.model.bean.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    //nome do banco
    private static final String DATABASE_FILE_NAME = "pepperissues";

    // versao do banco - qualquer alteracao de banco incremente no valor
    private static final int DATABASE_VERSION = 4;

    private Map<Class, Dao<IEntidade, Object>> daos = new HashMap<>();

    public DatabaseHelper(Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {

            Log.i(DatabaseHelper.class.getName(), "onCreate Database");

            Log.i(DatabaseHelper.class.getName(), "Create table Issue");
            TableUtils.createTable(connectionSource, Issue.class);
            Log.i(DatabaseHelper.class.getName(), "Create table Label");
            TableUtils.createTable(connectionSource, Label.class);
            Log.i(DatabaseHelper.class.getName(), "Create table Repository");
            TableUtils.createTable(connectionSource, Repository.class);
            Log.i(DatabaseHelper.class.getName(), "Create table User");
            TableUtils.createTable(connectionSource, User.class);
            Log.i(DatabaseHelper.class.getName(), "Create table IssueLabel");
            TableUtils.createTable(connectionSource, IssueLabel.class);
            Log.i(DatabaseHelper.class.getName(), "Create table Comment");
            TableUtils.createTable(connectionSource, IssueComment.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade Database");

            Log.i(DatabaseHelper.class.getName(), "Update table Issue");
            TableUtils.dropTable(connectionSource, Issue.class, true);
            Log.i(DatabaseHelper.class.getName(), "Update table Label");
            TableUtils.dropTable(connectionSource, Label.class, true);
            Log.i(DatabaseHelper.class.getName(), "Update table Repository");
            TableUtils.dropTable(connectionSource, Repository.class, true);
            Log.i(DatabaseHelper.class.getName(), "Update table User");
            TableUtils.dropTable(connectionSource, User.class, true);
            Log.i(DatabaseHelper.class.getName(), "Update table IssueLabel");
            TableUtils.dropTable(connectionSource, IssueLabel.class, true);
            Log.i(DatabaseHelper.class.getName(), "Update table Comment");
            TableUtils.dropTable(connectionSource, IssueComment.class, true);

            onCreate(db, connectionSource);
        } catch (android.database.SQLException | SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "exception during onUpgrade", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    public <T> Dao<T, Object> getDAO(Class<T> entidadeClass) {
        Dao<T, Object> dao;
        if (daos.get(entidadeClass) == null) {
            try {
                dao = getDao(entidadeClass);
            } catch (SQLException e) {
                Log.e(DatabaseHelper.class.getName(), "exception during getDAO", e);
                throw new RuntimeException(e);
            }
            daos.put(entidadeClass, (Dao<IEntidade, Object>) dao);
        }
        return (Dao<T, Object>) daos.get(entidadeClass);
    }

}
