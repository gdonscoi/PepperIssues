package net.caiena.github.Util;

public interface Constantes {

    public static final String CLIENT_ID= "";

    public static final String CLIENT_SECRET= "";

    public static final String SHAREDPREFERENCE_AUTH = "Auth";

    public static final String URL_PARAM_ACESS_TOKEN = "acess_token=";

    public static final String URL_GITHUB = "https://github.com/";

    public static final String URL_AUTH = URL_GITHUB.concat("login/oauth/authorize?scope=repo&client_id=").concat(CLIENT_ID);

    public static final String URL_ACESS_TOKEN = URL_GITHUB.concat("login/oauth/access_token");

    public static final String URL_GITHUB_API = "https://api.github.com/";

    public static final String URL_API_ISSUES = URL_GITHUB_API.concat("user/issues?").concat(URL_PARAM_ACESS_TOKEN);

    public static final String URL_API_AUTORIZATION_USER = URL_GITHUB_API.concat("user?").concat(URL_PARAM_ACESS_TOKEN);

    public static final String URL_API_REPOSITORIES = URL_GITHUB.concat("user/repos?per_page=1000&").concat(URL_PARAM_ACESS_TOKEN);
}
