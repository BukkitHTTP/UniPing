package ctrl;

import nano.http.bukkit.api.debug.DebugMain;

public class Main {
    public static void main(String[] args) throws Exception {
        DebugMain.debug(HttpMain.class, "/ping");
    }
}
