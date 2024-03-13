package utils;

import nano.http.d2.json.NanoJSON;
import prot.PingProtocol;
import prot.http.HttpPing;
import prot.mc.McPing;
import prot.tcp.TcpPing;

public class Protocol {
    private static final PingProtocol[] protocols = new PingProtocol[]{
            new HttpPing(),
            new TcpPing(),
            new McPing()
    };

    public static String query(String url) {
        String result = query0(url);
        NanoJSON json = new NanoJSON();
        if (result == null) {
            json.put("online", false);
            json.put("result", "offline");
        } else if (result.isEmpty()) {
            json.put("online", false);
            json.put("result", "wrong protocol");
        } else {
            json.put("online", true);
            json.put("result", result);
        }
        return json.toString();
    }

    private static String query0(String url) {
        String[] parts = url.split("://");
        if (parts.length != 2) {
            return "";
        }
        switch (parts[0]) {
            case "http":
            case "https":
                return protocols[0].doPing(url);
            case "tcp":
                return protocols[1].doPing(parts[1]);
            case "mc":
                return protocols[2].doPing(parts[1]);
            default:
                return "";
        }
    }
}
