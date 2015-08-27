package net.caiena.github.Util;

public interface Constantes {

    String CLIENT_ID = "75138c0316d81f593c13";

    String CLIENT_SECRET = "ab8412ede76a1e19d7a23e8f7992f7705207e2b6";

    String SHAREDPREFERENCE_AUTH = "Auth";

    String URL_PARAM_ACESS_TOKEN = "access_token=";

    String URL_GITHUB = "https://github.com/";

    String URL_AUTH = URL_GITHUB.concat("login/oauth/authorize?scope=repo&client_id=").concat(CLIENT_ID);

    String URL_ACESS_TOKEN = URL_GITHUB.concat("login/oauth/access_token");


    String URL_GITHUB_API = "https://api.github.com/";

    String URL_API_REPOS = URL_GITHUB_API.concat("repos/");

    String URL_PARAM_PER_PAGE = "?per_page=1000&";

    String URL_API_ISSUES_STRING = "/issues";

    //  GET /repos/:owner/:repo/issues
    String URL_API_ISSUES = URL_API_ISSUES_STRING.concat(URL_PARAM_PER_PAGE).concat(URL_PARAM_ACESS_TOKEN);

    //  GET /repos/:owner/:repo/milestones
    String URL_API_MILESTONE = "/milestones".concat(URL_PARAM_PER_PAGE).concat(URL_PARAM_ACESS_TOKEN);

    String URL_API_AUTORIZATION_USER = URL_GITHUB_API.concat("user?").concat(URL_PARAM_ACESS_TOKEN);

    String URL_API_REPOSITORIES = URL_GITHUB_API.concat("user/repos").concat(URL_PARAM_PER_PAGE).concat(URL_PARAM_ACESS_TOKEN);

    String URL_API_COMMENTS = "/comments".concat(URL_PARAM_PER_PAGE).concat(URL_PARAM_ACESS_TOKEN);
}
