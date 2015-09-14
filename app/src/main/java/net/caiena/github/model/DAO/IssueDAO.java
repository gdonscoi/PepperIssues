package net.caiena.github.model.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.caiena.github.model.bean.Issue;

public class IssueDAO extends BaseDAO<Issue> {

    private static IssueDAO dao;

    public static IssueDAO getInstance(Context ctx) {
        if (dao == null) {
            dao = new IssueDAO(ctx);
        }
        return dao;
    }

    private IssueDAO(Context ctx) {
        super();
        super.ctx = ctx;
    }

    public SQLiteDatabase getConnectionDataBase() throws Exception {
        return dao.getHelper().getWritableDatabase();
    }

}
