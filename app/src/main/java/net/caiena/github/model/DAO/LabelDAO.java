package net.caiena.github.model.DAO;

import android.content.Context;

import net.caiena.github.model.bean.Label;

public class LabelDAO extends BaseDAO<Label> {

    private static LabelDAO dao;

    public static LabelDAO getInstance(Context ctx) {
        if (dao == null) {
            dao = new LabelDAO(ctx);
        }
        return dao;
    }

    private LabelDAO(Context ctx) {
        super();
        super.ctx = ctx;
    }

}
