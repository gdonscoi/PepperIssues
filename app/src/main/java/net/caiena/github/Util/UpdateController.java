package net.caiena.github.Util;

import android.os.AsyncTask;

public class UpdateController extends AsyncTask<String, Integer, Boolean> {

    private ActivityProgressUpdatable callbackProgressBarActivity;

    public void setCallback(ActivityProgressUpdatable callback) {
        this.callbackProgressBarActivity = callback;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Boolean doInBackground(String... params) {



        publishProgress(1);
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        callbackProgressBarActivity.updateProgressBar(values[0]);
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