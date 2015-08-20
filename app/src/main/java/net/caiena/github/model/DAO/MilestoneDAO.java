package net.caiena.github.model.DAO;

import android.content.Context;

import net.caiena.github.model.bean.Milestone;

public class MilestoneDAO extends BaseDAO<Milestone> {

    private static MilestoneDAO dao;

    public static MilestoneDAO getInstance(Context ctx) {
        if (dao == null) {
            dao = new MilestoneDAO(ctx);
        }
        return dao;
    }

    private MilestoneDAO(Context ctx) {
        super();
        super.ctx = ctx;
    }

}
