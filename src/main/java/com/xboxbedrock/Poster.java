package com.xboxbedrock;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Map.Entry;
public class Poster {
    private Exstats api;
    private HashMap<String, Object> args = new HashMap();
    private URL url;

    protected Poster(Exstats api) {
        this.api = api;
    }

    protected Poster withArg(String key, Object value) {
        this.args.put(key, value);
        return this;
    }

    protected Poster withURL(URL url) {
        this.url = url;
        return this;
    }

    protected String[] post() {
        try {
            StringBuffer a = new StringBuffer("api_dev_key=" + "O-rNCEpqum8MF7ZCbeNa84UelpKbbbPP");
            Iterator var3 = this.args.entrySet().iterator();

            while(var3.hasNext()) {
                Entry<String, Object> e = (Entry)var3.next();
                a.append("&" + (String)e.getKey() + "=" + e.getValue());
            }

            String text = a.toString();
            if (this.url == null) {
                this.url = new URL("http://pastebin.com/api/api_post.php");
            }

            HttpURLConnection connection = (HttpURLConnection)this.url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Length", "" + text.getBytes().length);
            connection.setUseCaches(false);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(text);
            wr.flush();
            wr.close();
            connection.disconnect();
            Scanner s = new Scanner(connection.getInputStream());
            ArrayList output = new ArrayList();

            while(s.hasNext()) {
                String next = s.nextLine();
                output.add(next);
            }

            if (((String)output.get(0)).startsWith("Bad API request")) {
                s.close();
                return null;

            } else {
                s.close();
                return (String[])output.toArray(new String[output.size()]);
            }
        } catch (Exception var8) {
            var8.printStackTrace();
            return null;
        }
    }


}
