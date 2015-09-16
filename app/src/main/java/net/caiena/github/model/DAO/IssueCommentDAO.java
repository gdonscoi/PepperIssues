package net.caiena.github.model.DAO;

import android.content.Context;

import net.caiena.github.model.bean.IssueComment;

public class IssueCommentDAO extends BaseDAO<IssueComment> {

    private static IssueCommentDAO dao;

    public static IssueCommentDAO getInstance(Context ctx) {
        if (dao == null) {
            dao = new IssueCommentDAO(ctx);
        }
        return dao;
    }

    private IssueCommentDAO(Context ctx) {
        super();
        super.ctx = ctx;
    }

}
