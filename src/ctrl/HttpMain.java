package ctrl;

import nano.http.bukkit.api.BukkitServerProvider;
import nano.http.d2.console.Logger;
import nano.http.d2.consts.Mime;
import nano.http.d2.consts.Status;
import nano.http.d2.core.Response;
import utils.Cache;

import java.io.File;
import java.util.Properties;

public class HttpMain extends BukkitServerProvider {
    @Override
    public void onEnable(String name, File dir, String uri) {
        Logger.info("Loaded!");
    }

    @Override
    public void onDisable() {
        Logger.info("Unloaded!");
    }

    @Override
    public Response serve(String uri, String method, Properties header, Properties parms, Properties files) {
        return new Response(Status.HTTP_OK, Mime.MIME_PLAINTEXT, Cache.getWithCache(parms.getOrDefault("dest", "").toString()));
    }

    @Override
    public Response fallback(String uri, String method, Properties header, Properties parms, Properties files) {
        return new Response(Status.HTTP_NOTFOUND, Mime.MIME_HTML, Static.html);
    }
}
