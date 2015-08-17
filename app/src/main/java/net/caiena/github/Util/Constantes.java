package net.caiena.github.Util;

public interface Constantes {

    public static final String CLIENT_ID= "";

    public static final String CLIENT_SECRET= "";

    public static final String SHAREDPREFERENCE_AUTH = "Auth";

    public static final String URL_GITHUB = "https://github.com/";

    public static final String URL_AUTH = "login/oauth/authorize?client_id=" + CLIENT_ID + "&scope=repo";

    public static final String URL_ACESS_TOKEN = "login/oauth/access_token";

    public static final String URL_GITHUB_API = "https://api.github.com/";

    public static final String URL_API_ISSUES = "repos/caiena/sgc-issues/issues";

    public static final String URL_PARAM_ACESS_TOKEN = "?acess_token=";

    public static final String URL_API_AUTORIZATION_USER = "user?access_token=";
}
