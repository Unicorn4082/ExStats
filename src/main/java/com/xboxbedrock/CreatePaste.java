package com.xboxbedrock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CreatePaste {
    private Exstats api;
    private User user;
    private File file;
    private String text;
    private String name;
    private Format format;
    private PrivacyLevel privacylevel;
    private ExpireDate expiredate;

    protected CreatePaste(Exstats api) {
        this.privacylevel = PrivacyLevel.PUBLIC;
        this.expiredate = ExpireDate.NEVER;
        this.api = api;
    }

    protected CreatePaste withUser(User user) {
        this.user = user;
        return this;
    }

    public CreatePaste withText(String text) {
        this.text = text;
        return this;
    }

    public CreatePaste withFile(File file) {
        this.file = file;
        return this;
    }

    public CreatePaste withName(String pastename) {
        this.name = pastename;
        return this;
    }

    public CreatePaste withFormat(Format pasteformat) {
        this.format = pasteformat;
        return this;
    }

    public CreatePaste withExpireDate(ExpireDate pasteexpiredate) {
        this.expiredate = pasteexpiredate;
        return this;
    }

    public CreatePaste withPrivacyLevel(PrivacyLevel privacylevel) {
        this.privacylevel = privacylevel;
        return this;
    }

    public String post() throws PastebinException, IOException {
        Poster p = this.api.getNewPoster();
        p.withArg("api_option", "paste");
        if (this.text != null) {
            p.withArg("api_paste_code", this.text);
        } else if (this.file != null) {
            StringBuffer contents = new StringBuffer();
            BufferedReader read = new BufferedReader(new FileReader(this.file));

            while(read.ready()) {
                contents.append(read.readLine() + "\n");
            }

            read.close();
            p.withArg("api_paste_code", contents.toString());
        }

        if (this.name != null) {
            p.withArg("api_paste_name", this.name);
        }

        if (this.format != null) {
            p.withArg("api_paste_format", this.format.getFormat());
        }

        p.withArg("api_paste_expire_date", this.expiredate.getCode());
        p.withArg("api_paste_private", this.privacylevel.getLevel());
        if (this.user != null) {
            p.withArg("api_user_key", this.user.getUserKey());
        }

        return p.post()[0];
    }
}