package net.caiena.github.Util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.reflect.TypeToken;

import net.caiena.github.model.DAO.RepositoryDAO;
import net.caiena.github.model.bean.Issue;
import net.caiena.github.model.bean.Milestone;
import net.caiena.github.model.bean.Repository;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpdateController {

    private static UpdateController instance;
    private RequestQueue requestQueue;
    private String accessToken;
    private HashMap<String, String> params;
    private ArrayList<Repository> repositories;
    static private Context context;

    private UpdateController(Context context, String accessToken) {
        this.requestQueue = Volley.newRequestQueue(context);
        this.accessToken = accessToken;

        this.params = new HashMap<>();
        this.params.put("Accept", "application/vnd.github.v3.full+json");
        this.params.put("charset", "utf-8");
    }

    public static UpdateController getInstance(Context contextActivity, @Nullable String accessToken) {
        if (instance == null)
            instance = new UpdateController(contextActivity, accessToken);
        context = contextActivity;
        return instance;
    }

    public void doUpdate(final ProgressBar progressBar) {

        repositories = new ArrayList<>();

        final Type milestoneType = new TypeToken<List<Milestone>>() {
        }.getType();
        final Type issueType = new TypeToken<List<Issue>>() {
        }.getType();
        Type repositoryType = new TypeToken<List<Repository>>() {
        }.getType();
        // Request para obter repositorios

        GenericRequest<ArrayList<Repository>> requestRepositories = new GenericRequest<>(Constantes.URL_API_REPOSITORIES.concat(accessToken), repositoryType, new Response.Listener<ArrayList<Repository>>() {
            @Override
            public void onResponse(ArrayList<Repository> response) {
                repositories.addAll(response);
                int countRepo = repositories.size();

                progressBar.setIndeterminate(false);
                progressBar.setMax(countRepo);

                for (int i = 0; i < countRepo; i++) {
                    final int indiceRepo = i;
                    // Request para obter Milestones
                    requestQueue.add(new GenericRequest<>(Constantes.URL_API_REPOS
                            .concat(repositories.get(i).owner.login)
                            .concat("/")
                            .concat(repositories.get(i).name)
                            .concat(Constantes.URL_API_MILESTONE)
                            .concat(accessToken), milestoneType, new Response.Listener<ArrayList<Milestone>>() {
                        @Override
                        public void onResponse(ArrayList<Milestone> response) {
                            for (Milestone milestone : response)
                                if (milestone.title.toLowerCase().equals("backlog") || milestone.title.toLowerCase().equals("product backlog")) {
                                    requestQueue.add(new GenericRequest<>(Constantes.URL_API_REPOS
                                            .concat(repositories.get(indiceRepo).owner.login)
                                            .concat("/")
                                            .concat(repositories.get(indiceRepo).name)
                                            .concat(Constantes.URL_API_ISSUES)
                                            .concat(accessToken
                                                    .concat("&milestone=")
                                                    .concat(String.valueOf(milestone.number))
                                                    .concat("&labels=feature")), issueType, new Response.Listener<ArrayList<Issue>>() {
                                        @Override
                                        public void onResponse(ArrayList<Issue> response) {
                                            repositories.get(indiceRepo).issues.addAll(response);
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.i("Response", error.getMessage());
                                        }
                                    }, params, true));

                                    break;
                                }

                            progressBar.incrementProgressBy(1);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("Response", error.getMessage());
                        }
                    }, params, true));
                }

                try {
                    RepositoryDAO.getInstance(context).createOrUpdate(repositories);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Response", error.getMessage());
            }
        }, params, true);


        requestQueue.add(requestRepositories);
    }


}
