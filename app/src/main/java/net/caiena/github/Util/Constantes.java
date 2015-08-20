package net.caiena.github.Util;

public interface Constantes {

    public static final String CLIENT_ID = "75138c0316d81f593c13";

    public static final String CLIENT_SECRET = "ab8412ede76a1e19d7a23e8f7992f7705207e2b6";

    public static final String SHAREDPREFERENCE_AUTH = "Auth";

    public static final String URL_PARAM_ACESS_TOKEN = "access_token=";

    public static final String URL_GITHUB = "https://github.com/";

    public static final String URL_AUTH = URL_GITHUB.concat("login/oauth/authorize?scope=repo&client_id=").concat(CLIENT_ID);

    public static final String URL_ACESS_TOKEN = URL_GITHUB.concat("login/oauth/access_token");


    public static final String URL_GITHUB_API = "https://api.github.com/";

    public static final String URL_API_REPOS = URL_GITHUB_API.concat("repos/");

    //  GET /repos/:owner/:repo/issues
    public static final String URL_API_ISSUES = "/issues?per_page=1000&".concat(URL_PARAM_ACESS_TOKEN);

    //  GET /repos/:owner/:repo/milestones
    public static final String URL_API_MILESTONE = "/milestones?per_page=1000&".concat(URL_PARAM_ACESS_TOKEN);

    public static final String URL_API_AUTORIZATION_USER = URL_GITHUB_API.concat("user?").concat(URL_PARAM_ACESS_TOKEN);

    public static final String URL_API_REPOSITORIES = URL_GITHUB_API.concat("user/repos?per_page=1000&").concat(URL_PARAM_ACESS_TOKEN);
}
