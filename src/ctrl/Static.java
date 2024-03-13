package ctrl;

import java.util.Scanner;

public class Static {
    public static final String html;

    static {
        Scanner sc = new Scanner(Static.class.getResourceAsStream("/sample.html"), "UTF-8");
        StringBuilder sb = new StringBuilder();
        while (sc.hasNextLine()) {
            sb.append(sc.nextLine()).append("\n");
        }
        html = sb.toString();
    }
}
