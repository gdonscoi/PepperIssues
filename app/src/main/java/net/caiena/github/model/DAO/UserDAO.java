package net.caiena.github.model.DAO;

import android.content.Context;

import net.caiena.github.model.bean.User;

public class UserDAO extends BaseDAO<User> {

    private static UserDAO dao;

    public static UserDAO getInstance(Context ctx) {
        if (dao == null) {
            dao = new UserDAO(ctx);
        }
        return dao;
    }

    private UserDAO(Context ctx) {
        super();
        super.ctx = ctx;
    }

}
