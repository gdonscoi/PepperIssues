package net.caiena.github.model.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.stmt.QueryBuilder;

import net.caiena.github.model.bean.Issue;
import net.caiena.github.model.bean.IssueComment;
import net.caiena.github.model.bean.IssueLabel;
import net.caiena.github.model.bean.Label;
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

    public void deleteCascade(Repository repository) throws Exception {

        QueryBuilder<IssueLabel, Object> issueLabelQueryBuilder = IssueLabelDAO.getInstance(ctx).getConnection().queryBuilder();
        issueLabelQueryBuilder.where().eq("repository_id" , repository.id);
        List<IssueLabel> issueLabelList = issueLabelQueryBuilder.query();
        for(IssueLabel issueLabel :issueLabelList)
            IssueLabelDAO.getInstance(ctx).delete(issueLabel);

        QueryBuilder<Label, Object> labelQueryBuilder = LabelDAO.getInstance(ctx).getConnection().queryBuilder();
        labelQueryBuilder.where().eq("repository_id" , repository.id);
        List<Label> labelList = labelQueryBuilder.query();
        for(Label label :labelList)
            LabelDAO.getInstance(ctx).delete(label);

        QueryBuilder<Issue, Object> issueQueryBuilder = IssueDAO.getInstance(ctx).getConnection().queryBuilder();
        issueQueryBuilder.where().eq("repository_id" , repository.id);

        QueryBuilder<IssueComment, Object> issueCommentQueryBuilder = IssueCommentDAO.getInstance(ctx).getConnection().queryBuilder();
        issueCommentQueryBuilder.join(issueQueryBuilder);
        List<IssueComment> issueCommentList = issueCommentQueryBuilder.query();
        for(IssueComment issueComment :issueCommentList)
            IssueCommentDAO.getInstance(ctx).delete(issueComment);

        List<Issue> issueList = issueQueryBuilder.query();
        for(Issue issue :issueList)
            IssueDAO.getInstance(ctx).delete(issue);

        dao.delete(repository);

    }

}
