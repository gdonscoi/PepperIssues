package net.caiena.github.model.DAO;

import android.content.Context;

import net.caiena.github.model.bean.Repository;

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

}
