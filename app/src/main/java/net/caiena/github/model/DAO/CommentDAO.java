package net.caiena.github.model.DAO;

import android.content.Context;

import net.caiena.github.model.bean.IssueComment;

public class CommentDAO extends BaseDAO<IssueComment> {

    private static CommentDAO dao;

    public static CommentDAO getInstance(Context ctx) {
        if (dao == null) {
            dao = new CommentDAO(ctx);
        }
        return dao;
    }

    private CommentDAO(Context ctx) {
        super();
        super.ctx = ctx;
    }

}
