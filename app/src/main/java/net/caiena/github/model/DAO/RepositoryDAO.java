package net.caiena.github.model.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.stmt.QueryBuilder;

import net.caiena.github.model.bean.IssueLabel;
import net.caiena.github.model.bean.Repository;

import java.util.List;

public class RepositoryDAO extends BaseDAO<Repository> {

    private static RepositoryDAO dao;

    public static RepositoryDAO getInstance(Context ctx) {
        if (dao == null) {
            dao = new RepositoryDAO(ctx);
        }
        return dao;
    }

    private RepositoryDAO(Context ctx) {
        super();
        super.ctx = ctx;
    }

    public SQLiteDatabase getConnectionDataBase() throws Exception {
        return dao.getHelper().getWritableDatabase();
    }

    public List<Repository> getRepositoriesWithIssues() throws Exception {

        QueryBuilder<IssueLabel, Object> issueLabelQueryBuilder = IssueLabelDAO.getInstance(ctx).getConnection().queryBuilder();
        QueryBuilder<Repository, Object> repositoryQueryBuilder = dao.getConnection().queryBuilder().distinct();
        return repositoryQueryBuilder.join(issueLabelQueryBuilder).query();
    }

}
