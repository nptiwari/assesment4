package util;

/**
 * Display - centralised CLI formatting utility.
 * Provides consistent box-drawing, section headers and table output.
 */
public class Display {

    private static final String TOP   = "+";
    private static final String BOT   = "+";
    private static final String SIDE  = "|";
    private static final String H     = "=";
    private static final int    WIDTH = 60;

    // ── Banner ────────────────────────────────────────────────────────────
    public static void banner(String title) {
        String line = H.repeat(WIDTH - 2);
        System.out.println(TOP + line + TOP);
        System.out.println(centred(title));
        System.out.println(BOT + line + BOT);
    }

    // ── Section header ────────────────────────────────────────────────────
    public static void section(String title) {
        System.out.println();
        String line = "-".repeat(WIDTH - 2);
        System.out.println("+" + line + "+");
        System.out.println("|" + pad(title, WIDTH - 2) + "|");
        System.out.println("+" + line + "+");
    }

    // ── Sub header ────────────────────────────────────────────────────────
    public static void subHeader(String title) {
        System.out.println();
        System.out.println("  -- " + title + " "
            + "-".repeat(Math.max(0, WIDTH - 6 - title.length())));
    }

    // ── Card around a record ──────────────────────────────────────────────
    public static void card(String content) {
        String[] lines = content.split("\n");
        String line = "-".repeat(WIDTH - 2);
        System.out.println("  +" + line + "+");
        for (String l : lines) {
            System.out.println("  | " + padRight(l, WIDTH - 3) + "|");
        }
        System.out.println("  +" + line + "+");
    }

    // ── Status messages ───────────────────────────────────────────────────
    public static void success(String msg) {
        System.out.println("  [OK]   " + msg);
    }

    public static void error(String msg) {
        System.out.println("  [ERR]  " + msg);
    }

    public static void info(String msg) {
        System.out.println("  [INFO] " + msg);
    }

    public static void warn(String msg) {
        System.out.println("  [WARN] " + msg);
    }

    // ── Divider line ──────────────────────────────────────────────────────
    public static void divider() {
        System.out.println("  " + "-".repeat(WIDTH - 2));
    }

    // ── Menu item ─────────────────────────────────────────────────────────
    public static void menuItem(String key, String label) {
        System.out.printf("  [%s] %s%n", key, label);
    }

    // ── Stat row ──────────────────────────────────────────────────────────
    public static void stat(String label, String value) {
        System.out.printf("  %-28s %s%n", label + ":", value);
    }

    // ── Blank line ────────────────────────────────────────────────────────
    public static void blank() {
        System.out.println();
    }

    // ── Helpers ───────────────────────────────────────────────────────────
    private static String centred(String text) {
        int pad = (WIDTH - text.length()) / 2;
        String left = " ".repeat(Math.max(0, pad));
        return SIDE + padRight(left + text, WIDTH - 2) + SIDE;
    }

    private static String pad(String text, int width) {
        int left = (width - text.length()) / 2;
        String s = " ".repeat(Math.max(0, left)) + text;
        return padRight(s, width);
    }

    private static String padRight(String s, int width) {
        if (s.length() >= width) return s.substring(0, width);
        return s + " ".repeat(width - s.length());
    }
}