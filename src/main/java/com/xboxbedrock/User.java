package com.xboxbedrock;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
public class User {
    private Exstats api;
    private String username;
    private String password;
    private String userkey;
    private ExpireDate expire;
    private URL avatarURL;
    private URL website;
    private PrivacyLevel privacyLevel;
    private String email;
    private String location;
    private AccountType type;

    protected User(Exstats api, String username, String password) throws PastebinException {
        this.api = api;
        this.username = username;
        this.password = password;
        Poster login = api.getNewPoster();

        try {
            login.withURL(new URL("http://pastebin.com/api/api_login.php"));
        } catch (MalformedURLException var10) {
        }

        login.withArg("api_user_name", username);
        login.withArg("api_user_password", password);
        this.userkey = login.post()[0];
        Poster info = api.getNewPoster();
        info.withArg("api_user_key", this.userkey);
        info.withArg("api_option", "userdetails");
        Parser p = new Parser(info.post());
        p.addKey(new String[]{"expiration, avatar_url", "private", "website", "email", "location", "account_type"});
        HashMap<String, String> ret = p.parse();
        Iterator var9 = ret.keySet().iterator();

        while(var9.hasNext()) {
            String key = (String)var9.next();
            if (key.equals("expiration")) {
                this.expire = ExpireDate.valueOf(key);
            } else if (key.equals("avatar_url")) {
                this.avatarURL = this.getURL(key);
            } else if (key.equals("private")) {
                this.privacyLevel = PrivacyLevel.valueOf(Integer.parseInt(key));
            } else if (key.equals("website")) {
                this.website = this.getURL(key);
            } else if (key.equals("email")) {
                this.email = key;
            } else if (key.equals("location")) {
                this.location = key;
            } else if (key.equals("account_type")) {
                this.type = AccountType.valueOf(Integer.parseInt(key));
            }
        }

    }

    private URL getURL(String url) {
        try {
            return new URL(url);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public Paste[] getPastes() throws PastebinException {
        return this.getPastes(2147483647);
    }

    public Paste[] getPastes(int results_limit) throws PastebinException {
        return this.api.parse(this.api.getNewPoster().withArg("api_results_limit", results_limit).withArg("api_option", "list").post());
    }

    public void removePaste(String pasteKey) throws PastebinException {
        this.api.getNewPoster().withArg("api_user_key", this.userkey).withArg("api_paste_key", pasteKey).withArg("api_option", "delete");
    }

    public void removePaste(Paste p) throws PastebinException {
        this.removePaste(p.getKey());
    }

    public CreatePaste createPaste() {
        return (new CreatePaste(this.api)).withUser(this);
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    protected String getUserKey() {
        return this.userkey;
    }

    public ExpireDate getExpireDate() {
        return this.expire;
    }

    public URL getAvatarURL() {
        return this.avatarURL;
    }

    public URL getWebsite() {
        return this.website;
    }

    public PrivacyLevel getPrivacyLevel() {
        return this.privacyLevel;
    }

    public String getEmail() {
        return this.email;
    }

    public String getLocation() {
        return this.location;
    }

    public AccountType getAccountType() {
        return this.type;
    }
}
