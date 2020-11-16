package com.xboxbedrock;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Paste {
    private Exstats api;
    private String key;
    private String name;
    private int size;
    private int hits;
    private Date date;
    private Date expiredate;
    private PrivacyLevel level;
    private Format format;
    private URL url;

    protected Paste(Exstats api, ArrayList<String> args) throws PastebinException {
        this.api = api;
        Iterator var4 = args.iterator();

        while(var4.hasNext()) {
            String arg = (String)var4.next();
            arg = arg.substring(arg.indexOf("_") + 1, arg.lastIndexOf("<"));
            if (arg.startsWith("key")) {
                this.key = this.sub(arg);
            } else if (arg.startsWith("date")) {
                this.date = new Date(Long.valueOf(this.sub(arg)));
            } else if (arg.startsWith("title")) {
                this.name = this.sub(arg);
            } else if (arg.startsWith("size")) {
                this.size = Integer.valueOf(this.sub(arg));
            } else if (arg.startsWith("expire_date")) {
                this.expiredate = new Date(Long.valueOf(this.sub(arg)));
            } else if (arg.startsWith("private")) {
                this.level = PrivacyLevel.valueOf(Integer.parseInt(this.sub(arg)));
            } else if (arg.startsWith("format_long")) {
                this.format = Format.valueOf(this.sub(arg));
            } else if (arg.startsWith("url")) {
                try {
                    this.url = new URL(this.sub(arg));
                } catch (MalformedURLException var6) {
                    throw new PastebinException("Invalid URL returned!");
                }
            } else if (arg.startsWith("hits")) {
                this.hits = Integer.valueOf(this.sub(arg));
            }
        }

    }

    private String sub(String str) {
        return str.substring(str.indexOf(">") + 1);
    }

    public String getKey() {
        return this.key;
    }

    public String getName() {
        return this.name;
    }

    public int getSize() {
        return this.size;
    }

    public int getHits() {
        return this.hits;
    }

    public Date getDate() {
        return new Date(this.date.getTime());
    }

    public Date getExpireDate() {
        return new Date(this.expiredate.getTime());
    }

    public PrivacyLevel getLevel() {
        return this.level;
    }

    public Format getFormat() {
        return this.format;
    }

    public URL getURL() {
        return this.url;
    }

    public String[] getText() throws PastebinException {
        Poster p = this.api.getNewPoster();

        try {
            return p.withURL(new URL("http://pastebin.com/raw.php?i=" + this.key)).post();
        } catch (MalformedURLException var3) {
            return null;
        }
    }

    public String getText(String lineSeparator) throws PastebinException {
        StringBuffer result = new StringBuffer();
        String[] var6;
        int var5 = (var6 = this.getText()).length;

        for(int var4 = 0; var4 < var5; ++var4) {
            String str = var6[var4];
            result.append(str + lineSeparator);
        }

        return result.toString();
    }
}
