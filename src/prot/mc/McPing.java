package prot.mc;

import nano.http.d2.json.NanoJSON;
import prot.PingProtocol;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;

public class McPing extends PingProtocol {
    public String doPing(String host) {
        try {
            String[] parsed = SRVResolver.resolveMc(host).split(":");

            String ip = parsed[0];
            int port = Integer.parseInt(parsed[1]);

            Socket sc = new Socket(ip, port);
            DataOutputStream dos = new DataOutputStream(sc.getOutputStream());
            PacketUtils.sendPacket(dos, PacketUtils.createHandshakePacket(ip, port, 47, 1));
            PacketUtils.sendPacket(dos, PacketUtils.createPingPacket(true));
            dos.flush();
            InputStream is = sc.getInputStream();
            int len = PacketUtils.readVarInt(is);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (int i = 0; i < len; i++) {
                baos.write(is.read());
            }
            sc.close();
            String s = baos.toString();
            s = s.substring(s.indexOf("{"));

            NanoJSON json;
            try {
                json = new NanoJSON(s);
            } catch (Exception ex) {
                s = s.substring(0, s.indexOf("favicon"));
                s = s + "favicon\":\"404\"}";
                json = new NanoJSON(s);
            }

            NanoJSON players = json.getJSONObject("players");
            return players.getInt("online") + "/" + players.getInt("max") + "[" + json.getJSONObject("version").getString("name") + "]";
        } catch (Exception ex) {
            return null;
        }
    }
}
