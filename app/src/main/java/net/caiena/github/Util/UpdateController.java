package net.caiena.github.Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import net.caiena.github.GitHubController;
import net.caiena.github.model.DAO.IssueDAO;
import net.caiena.github.model.DAO.IssueLabelDAO;
import net.caiena.github.model.DAO.LabelDAO;
import net.caiena.github.model.DAO.RepositoryDAO;
import net.caiena.github.model.bean.Issue;
import net.caiena.github.model.bean.IssueLabel;
import net.caiena.github.model.bean.Label;
import net.caiena.github.model.bean.Milestone;
import net.caiena.github.model.bean.Repository;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateController extends AsyncTask<String, Integer, Boolean> {

    private String accessToken;
    private ProgressBar progressBar;
    private ActivityProgressUpdatable callbackProgressBarActivity;
    private Context context;

    public UpdateController(String accessToken, ProgressBar progressBar, Context context) {
        this.accessToken = accessToken;
        this.progressBar = progressBar;
        this.context = context;
    }

    public void setCallback(ActivityProgressUpdatable callback) {
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
            ArrayList<Repository> repositories = gitHubController.getRepositories();
            ArrayList<Issue> issues = new ArrayList<>();
            ArrayList<IssueLabel> issueLabels = new ArrayList<>();
            int sizeRepositories = repositories.size();
            ArrayList<Issue> issuesGitHub = new ArrayList<>();
            HashMap<String, Label> labelHashMap = new HashMap<>();

            for (Repository repository : repositories) {
                ArrayList<Milestone> milestones = gitHubController.getMilestones(repository.owner.login, repository.name);

                for (Milestone milestone : milestones) {
                    if (milestone.title.toLowerCase().equals("backlog") || milestone.title.toLowerCase().equals("product backlog")) {
                        IssueLabel issueLabel;

                        issuesGitHub.addAll(gitHubController.getIssues(repository.owner.login, repository.name, milestone.number));
                        for (Issue issue : issuesGitHub) {
                            issue.repository = repository;
                            issue.nameMilestone = milestone.title;

                            for (Label label : issue.labels) {
                                issueLabel = new IssueLabel();
                                issueLabel.issue = issue;
                                issueLabel.label = label;
                                issueLabel.repository = repository;

                                labelHashMap.put(label.url, label);
                                issueLabels.add(issueLabel);
                            }
                        }
                        issues.addAll(issuesGitHub);
                    }
                }

                publishProgress(1, sizeRepositories);
            }
            db = RepositoryDAO.getInstance(context).getConnectionDataBase();
            db.beginTransaction();

            RepositoryDAO.getInstance(context).createOrUpdate(repositories);
            IssueDAO.getInstance(context).createOrUpdate(issues);
            LabelDAO.getInstance(context).createOrUpdate(new ArrayList<>(labelHashMap.values()));
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
        callbackProgressBarActivity.updateProgressBar(values[0], values[1]);
    }

    @Override
    protected void onPostExecute(Boolean result) {

    }

}

//    public void doUpdate(final ProgressBar progressBar) {
//
//        repositories = new ArrayList<>();
//
//        final Type milestoneType = new TypeToken<List<Milestone>>() {
//        }.getType();
//        final Type issueType = new TypeToken<List<Issue>>() {
//        }.getType();
//        Type repositoryType = new TypeToken<List<Repository>>() {
//        }.getType();
//        // Request para obter repositorios
//
//        GenericRequest<ArrayList<Repository>> requestRepositories = new GenericRequest<>(Constantes.URL_API_REPOSITORIES.concat(accessToken), repositoryType, new Response.Listener<ArrayList<Repository>>() {
//            @Override
//            public void onResponse(ArrayList<Repository> response) {
//                repositories.addAll(response);
//
//                ((UpdateActivity)context).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressBar.setIndeterminate(false);
//                        progressBar.setMax(repositories.size());
//                        progressBar.setProgress(0);
//                    }
//                });
//
////                for (int i = 0; i < countRepo; i++) {
////                    final int indiceRepo = i;
////                    // Request para obter Milestones
////                    requestQueue.add(new GenericRequest<>(Constantes.URL_API_REPOS
////                            .concat(repositories.get(i).owner.login)
////                            .concat("/")
////                            .concat(repositories.get(i).name)
////                            .concat(Constantes.URL_API_MILESTONE)
////                            .concat(accessToken), milestoneType, new Response.Listener<ArrayList<Milestone>>() {
////                        @Override
////                        public void onResponse(ArrayList<Milestone> response) {
////                            for (Milestone milestone : response)
////                                if (milestone.title.toLowerCase().equals("backlog") || milestone.title.toLowerCase().equals("product backlog")) {
////                                    requestQueue.add(new GenericRequest<>(Constantes.URL_API_REPOS
////                                            .concat(repositories.get(indiceRepo).owner.login)
////                                            .concat("/")
////                                            .concat(repositories.get(indiceRepo).name)
////                                            .concat(Constantes.URL_API_ISSUES)
////                                            .concat(accessToken
////                                                    .concat("&milestone=")
////                                                    .concat(String.valueOf(milestone.number))
////                                                    .concat("&labels=feature")), issueType, new Response.Listener<ArrayList<Issue>>() {
////                                        @Override
////                                        public void onResponse(ArrayList<Issue> response) {
////                                            repositories.get(indiceRepo).issues.addAll(response);
////                                        }
////                                    }, new Response.ErrorListener() {
////                                        @Override
////                                        public void onErrorResponse(VolleyError error) {
////                                            Log.i("Response", error.getMessage());
////                                        }
////                                    }, params, true));
////
////                                    break;
////                                }
////
////                            progressBar.incrementProgressBy(1);
////                        }
////                    }, new Response.ErrorListener() {
////                        @Override
////                        public void onErrorResponse(VolleyError error) {
////                            Log.i("Response", error.getMessage());
////                        }
////                    }, params, true));
////                }
//
//                SQLiteDatabase db = null;
//                try {
//                    db = RepositoryDAO.getInstance(context).getConnectionDataBase();
//                    db.beginTransaction();
//
//                    for (Repository repository:repositories){
//                        RepositoryDAO.getInstance(context).createOrUpdate(repository);
//                        ((UpdateActivity)context).runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                progressBar.incrementProgressBy(1);
//                                progressBar.invalidate();
//                            }
//                        });
//                    }
//
//                    db.setTransactionSuccessful();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }finally {
//                    if (db != null) {
//                        db.endTransaction();
//                    }
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.i("Response", error.getMessage());
//            }
//        }, params, true);
//
//
//        requestQueue.add(requestRepositories);
//    }