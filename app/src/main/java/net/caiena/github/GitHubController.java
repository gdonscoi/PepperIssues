package net.caiena.github;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.caiena.github.Util.Constantes;
import net.caiena.github.model.bean.Comment;
import net.caiena.github.model.bean.Issue;
import net.caiena.github.model.bean.Milestone;
import net.caiena.github.model.bean.Repository;
import net.caiena.github.model.bean.User;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
        Type repositoryType = new TypeToken<List<Repository>>() {
        }.getType();
        String response = getURL(Constantes.URL_API_REPOSITORIES.concat(accessToken));
        return gson.fromJson(response, repositoryType);
    }

    public ArrayList<Milestone> getMilestones(String owner, String repository) throws Throwable {
        String response = getURL(Constantes.URL_API_REPOS
                .concat(owner)
                .concat("/")
                .concat(repository)
                .concat(Constantes.URL_API_MILESTONE)
                .concat(accessToken));
        Type MilestoneType = new TypeToken<List<Milestone>>() {
        }.getType();
        return gson.fromJson(response, MilestoneType);
    }

    // Somente Issues com label feature
    public ArrayList<Issue> getIssues(String owner, String repository, String milestoneNumber) throws Throwable {
        String response = getURL(Constantes.URL_API_REPOS
                .concat(owner)
                .concat("/")
                .concat(repository)
                .concat(Constantes.URL_API_ISSUES)
                .concat(accessToken
                        .concat("&milestone=")
                        .concat(milestoneNumber)
                        .concat("&labels=feature")));
        Type issueType = new TypeToken<List<Issue>>() {
        }.getType();
        return gson.fromJson(response, issueType);
    }

    public User getUser() throws Throwable {
        String response = getURL(Constantes.URL_API_AUTORIZATION_USER
                .concat(accessToken));
        return gson.fromJson(response, User.class);
    }

    public byte[] getAvatarUser(String url) {
        InputStream is = null;
        ByteArrayOutputStream stream = null;
        try {
            is = (InputStream) new URL(url).getContent();

            Drawable d = Drawable.createFromStream(is, "avatar");

            Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
            stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        } catch (IOException e) {
            return null;
        }
        return stream.toByteArray();
    }

    public ArrayList<Comment> getComments(String owner, String repository, int issueNumber) throws Throwable {
        String response = getURL(Constantes.URL_API_REPOS
                .concat(owner)
                .concat("/")
                .concat(repository)
                .concat(Constantes.URL_API_ISSUES_STRING)
                .concat("/" + issueNumber)
                .concat(Constantes.URL_API_COMMENTS)
                .concat(accessToken));
        Type commentType = new TypeToken<List<Comment>>() {
        }.getType();
        return gson.fromJson(response, commentType);
    }

    private String getURL(String url) throws Throwable {
        URL urlRepositories = new URL(url);
        HttpURLConnection repositoryConn = (HttpURLConnection) urlRepositories.openConnection();
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
        } catch (Exception e) {
            throw e.getCause();
        } finally {
            if (repositoryConn != null) {
                repositoryConn.disconnect();
            }
        }
        return response;
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
