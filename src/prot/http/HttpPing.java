package prot.http;

import prot.PingProtocol;

import java.net.HttpURLConnection;
import java.net.URL;

public class HttpPing extends PingProtocol {
    @Override
    public String doPing(String host) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(host).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; PingBot/1.0)");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            if (connection.getResponseCode() == 200) {
                return "Type:" + connection.getHeaderField("Content-Type");
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
