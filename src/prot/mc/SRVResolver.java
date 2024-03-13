package prot.mc;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

public class SRVResolver {
    private static final DirContext srvContext;

    static {
        Hashtable<String, String> env = new Hashtable<>();
        env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
        env.put("java.naming.provider.url", "dns:");
        try {
            srvContext = new InitialDirContext(env);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String resolveSrv(String domain, String type) {
        Attribute attrib;
        try {
            Attributes e = srvContext.getAttributes("_" + type + "._tcp." + domain, new String[]{"SRV"});
            if (e != null && (attrib = e.get("srv")) != null) {
                Object obj = attrib.get(0);
                String[] array = obj.toString().split(" ");
                return array[3].substring(0, array[3].length() - 1) + ":" + array[2];
            }
            return "";
        } catch (Exception e2) {
            return "";
        }
    }

    public static String resolveMc(String ip) {
        if (ip.contains(":")) {
            return ip;
        }
        String resolved = resolveSrv(ip, "minecraft");
        if (resolved.isEmpty()) {
            return ip + ":25565";
        }
        return resolved;
    }
}
