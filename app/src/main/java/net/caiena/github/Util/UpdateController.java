package net.caiena.github.Util;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import net.caiena.github.GitHubController;
import net.caiena.github.activity.MainActivity;
import net.caiena.github.activity.UpdateActivity;
import net.caiena.github.model.DAO.CommentDAO;
import net.caiena.github.model.DAO.IssueDAO;
import net.caiena.github.model.DAO.IssueLabelDAO;
import net.caiena.github.model.DAO.LabelDAO;
import net.caiena.github.model.DAO.RepositoryDAO;
import net.caiena.github.model.DAO.UserDAO;
import net.caiena.github.model.bean.Comment;
import net.caiena.github.model.bean.Issue;
import net.caiena.github.model.bean.IssueLabel;
import net.caiena.github.model.bean.Label;
import net.caiena.github.model.bean.Milestone;
import net.caiena.github.model.bean.Repository;
import net.caiena.github.model.bean.User;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateController extends AsyncTask<String, Integer, Boolean> {

    private String accessToken;
    private ProgressBar progressBar;
    private ActivityUpdatable callbackProgressBarActivity;
    private Context context;
    private User user;
    public static final int TYPE_UPDATE_PROGRESS_BAR = 1;
    public static final int TYPE_UPDATE_AVATAR = 2;

    public UpdateController(String accessToken, ProgressBar progressBar, Context context) {
        this.accessToken = accessToken;
        this.progressBar = progressBar;
        this.context = context;
    }

    public void setCallback(ActivityUpdatable callback) {
        this.callbackProgressBarActivity = callback;
    }

    @Override
    protected void onPreExecute() {
        progressBar.setProgress(0);
    }

    @Override
    protected Boolean doInBackground(String... params) {
        GitHubController gitHubController = new GitHubController(accessToken);
        SQLiteDatabase db = null;
        try {
            user = gitHubController.getUser();
            user.setAvatar(gitHubController.getAvatarUser(user.avatarUrl));
            publishProgress(TYPE_UPDATE_AVATAR);

            ArrayList<Repository> repositories = gitHubController.getRepositories();
            ArrayList<Issue> issues = new ArrayList<>();
            ArrayList<Comment> comments = new ArrayList<>();
            ArrayList<IssueLabel> issueLabels = new ArrayList<>();
            int sizeRepositories = repositories.size();

            HashMap<String, Label> labelHashMap = new HashMap<>();

            for (Repository repository : repositories) {
                repository.ownerLogin = repository.owner.login;
                ArrayList<Milestone> milestones = gitHubController.getMilestones(repository.ownerLogin, repository.name);

                for (Milestone milestone : milestones) {
                    if (milestone.title.toLowerCase().equals("backlog") || milestone.title.toLowerCase().equals("product backlog")) {
                        IssueLabel issueLabel;
                        ArrayList<Issue> issuesGitHub = new ArrayList<>();
                        issuesGitHub.addAll(gitHubController.getIssues(repository.ownerLogin, repository.name, milestone.number));
                        for (Issue issue : issuesGitHub) {
                            issue.repository = repository;
                            issue.nameMilestone = milestone.title;

                            ArrayList<Comment> commentsIssue = gitHubController.getComments(repository.ownerLogin, repository.name,issue.number);
                            for(Comment commentIndex :commentsIssue) {
                                commentIndex.issue = issue;
                                commentIndex.ownerComment = commentIndex.user.login;
                                comments.add(commentIndex);
                            }

                            for (Label label : issue.labels) {
                                issueLabel = new IssueLabel(issue, label, repository);
                                label.repository = repository;
                                labelHashMap.put(label.url, label);
                                issueLabels.add(issueLabel);
                            }
                        }
                        issues.addAll(issuesGitHub);
                    }
                }

                publishProgress(TYPE_UPDATE_PROGRESS_BAR, 1, sizeRepositories);
            }
            db = RepositoryDAO.getInstance(context).getConnectionDataBase();
            db.beginTransaction();

            UserDAO.getInstance(context).createOrUpdate(user);
            RepositoryDAO.getInstance(context).createOrUpdate(repositories);
            IssueDAO.getInstance(context).createOrUpdate(issues);
            LabelDAO.getInstance(context).createOrUpdate(new ArrayList<>(labelHashMap.values()));
            CommentDAO.getInstance(context).createOrUpdate(comments);
            IssueLabelDAO.getInstance(context).createOrUpdate(issueLabels);

            db.setTransactionSuccessful();


        } catch (Exception e) {
            return null;
        } catch (Throwable throwable) {
            return null;
        } finally {
            if (db != null) {
                db.endTransaction();
            }
        }
        return true;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (values[0] == TYPE_UPDATE_PROGRESS_BAR) {
            callbackProgressBarActivity.updateProgressBar(values[1], values[2]);
        } else if (values[0] == TYPE_UPDATE_AVATAR) {
            callbackProgressBarActivity.updateInfoActivity(user.getCircularAvatar(), user.name, user.html);
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result == null) {
            callbackProgressBarActivity.updateError();
            return;
        }
        Intent intentWebView = new Intent(context, MainActivity.class);
        context.startActivity(intentWebView);
        ((UpdateActivity) context).setControlUpdate(true);
        ((UpdateActivity) context).finish();
    }

}