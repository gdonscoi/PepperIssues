package net.caiena.github;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.caiena.github.Util.Constantes;
import net.caiena.github.model.bean.Issue;
import net.caiena.github.model.bean.Milestone;
import net.caiena.github.model.bean.Repository;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GitHubController {

    private String accessToken;
    private Gson gson;

    public GitHubController(String accessToken) {
        this.accessToken = accessToken;
        this.gson = new Gson();
    }

    public ArrayList<Repository> getRepositories() throws Throwable {
        URL urlRepositories = new URL(Constantes.URL_API_REPOSITORIES.concat(accessToken));
        HttpURLConnection repositoryConn = (HttpURLConnection) urlRepositories.openConnection();
        Type repositoryType = new TypeToken<List<Repository>>() {
        }.getType();
        String response = "";
        try {
            repositoryConn.setRequestProperty("Accept", "application/vnd.github.v3.full+json");
            repositoryConn.setRequestProperty("Content-Type", "application/json");
            repositoryConn.setRequestProperty("charset", "utf-8");
            repositoryConn.setRequestMethod("GET");

            int responseCode = repositoryConn.getResponseCode();
            Log.d("getRepositories", "The response is: " + responseCode);
            if (responseCode < 200 && responseCode > 299)
                throw new IllegalStateException();
            response = readIt(repositoryConn.getInputStream());
        }catch (Exception e){
            throw e.getCause();
        } finally {
            if (repositoryConn != null) {
                repositoryConn.disconnect();
            }
        }

        return gson.fromJson(response, repositoryType);
    }

    public ArrayList<Milestone> getMilestones(String owner, String repository) throws Throwable {
        URL urlRepositories = new URL(Constantes.URL_API_REPOS
                .concat(owner)
                .concat("/")
                .concat(repository)
                .concat(Constantes.URL_API_MILESTONE)
                .concat(accessToken));
        HttpURLConnection repositoryConn = (HttpURLConnection) urlRepositories.openConnection();
        Type MilestoneType = new TypeToken<List<Milestone>>() {
        }.getType();
        String response = "";
        try {
            repositoryConn.setRequestMethod("GET");
            repositoryConn.setRequestProperty("Content-Type", "application/json");
            repositoryConn.setRequestProperty("Accept", "application/vnd.github.v3.full+json");
            repositoryConn.setRequestProperty("charset", "utf-8");

            int responseCode = repositoryConn.getResponseCode();
            Log.d("getMilestones", "The response is: " + responseCode);
            if (responseCode < 200 && responseCode > 299)
                throw new IllegalStateException();
            response = readIt(repositoryConn.getInputStream());
        }catch (Exception e){
            throw e.getCause();
        } finally {
            if (repositoryConn != null) {
                repositoryConn.disconnect();
            }
        }

        return gson.fromJson(response, MilestoneType);
    }

    // Somente Issues com label feature
    public ArrayList<Issue> getIssues(String owner, String repository, String milestoneNumber) throws Throwable {
        URL urlRepositories = new URL(Constantes.URL_API_REPOS
                .concat(owner)
                .concat("/")
                .concat(repository)
                .concat(Constantes.URL_API_ISSUES)
                .concat(accessToken
                        .concat("&milestone=")
                        .concat(milestoneNumber)
                        .concat("&labels=feature")));
        HttpURLConnection repositoryConn = (HttpURLConnection) urlRepositories.openConnection();
        Type issueType = new TypeToken<List<Issue>>() {
        }.getType();
        String response = "";
        try {
            repositoryConn.setRequestMethod("GET");
            repositoryConn.setRequestProperty("Content-Type", "application/json");
            repositoryConn.setRequestProperty("Accept", "application/vnd.github.v3.full+json");
            repositoryConn.setRequestProperty("charset", "utf-8");

            int responseCode = repositoryConn.getResponseCode();
            Log.d("getMilestones", "The response is: " + responseCode);
            if (responseCode < 200 && responseCode > 299)
                throw new IllegalStateException();
            response = readIt(repositoryConn.getInputStream());
        }catch (Exception e){
            throw e.getCause();
        } finally {
            if (repositoryConn != null) {
                repositoryConn.disconnect();
            }
        }

        return gson.fromJson(response, issueType);
    }

    private String readIt(InputStream stream) throws IOException {
        String stringReturn = "";
        try {
            InputStreamReader inR = new InputStreamReader(stream, "UTF-8");
            BufferedReader buf = new BufferedReader(inR);
            String line;
            while ((line = buf.readLine()) != null)
                stringReturn = stringReturn.concat(line);
        } finally {
            if (stream != null)
                stream.close();
        }
        return stringReturn;
    }
}
