package net.caiena.github.model.DAO;

import android.content.Context;

import net.caiena.github.model.bean.IssueLabel;

public class IssueLabelDAO extends BaseDAO<IssueLabel> {

    private static IssueLabelDAO dao;

    public static IssueLabelDAO getInstance(Context ctx) {
        if (dao == null) {
            dao = new IssueLabelDAO(ctx);
        }
        return dao;
    }

    private IssueLabelDAO(Context ctx) {
        super();
        super.ctx = ctx;
    }

}
