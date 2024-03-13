package prot.tcp;

import prot.PingProtocol;

import java.net.Socket;

public class TcpPing extends PingProtocol {
    @Override
    public String doPing(String host) {
        try {
            String[] parsed = host.split(":");
            String ip = parsed[0];
            int port = Integer.parseInt(parsed[1]);
            long start = System.currentTimeMillis();
            Socket sc = new Socket(ip, port);
            sc.setSoTimeout(1000);
            sc.close();
            return (System.currentTimeMillis() - start) + "ms";
        } catch (Exception ignored) {
        }
        return null;
    }
}
