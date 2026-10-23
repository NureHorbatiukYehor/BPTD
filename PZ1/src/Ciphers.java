import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

final class Ciphers {
    static final String ALPHABET = "абвгґдеєжзиіїйклмнопрстуфхцчшщьюя";
    private static final Map<Character, String> ENCODE = new HashMap<>();
    private static final Map<String, Character> DECODE = new HashMap<>();
    static {
        String frequent = "аитесно";
        for (int i = 0; i < frequent.length(); i++) put(frequent.charAt(i), "" + (i + 1));
        row('8', "бвгґдєжзії");
        row('9', "йклмпруфхц");
        row('0', "чшщьюя ");
    }
    private Ciphers() {}
    private static void put(char c, String code) { ENCODE.put(c, code); DECODE.put(code, c); }
    private static void row(char prefix, String letters) {
        for (int i = 0; i < letters.length(); i++) put(letters.charAt(i), "" + prefix + ((i + 1) % 10));
    }
    static String caesar(String text, int shift) {
        StringBuilder out = new StringBuilder();
        int normalized = Math.floorMod(shift, ALPHABET.length());
        for (char c : text.toCharArray()) {
            int index = ALPHABET.indexOf(Character.toLowerCase(c));
            char changed = index < 0 ? c : ALPHABET.charAt((index + normalized) % ALPHABET.length());
            out.append(Character.isUpperCase(c) ? Character.toUpperCase(changed) : changed);
        }
        return out.toString();
    }
    static String vigenere(String text, String key, boolean decrypt) {
        if (key.isEmpty()) throw new IllegalArgumentException("Ключ не може бути порожнім.");
        String lowerKey = key.toLowerCase(Locale.ROOT);
        for (char c : lowerKey.toCharArray())
            if (ALPHABET.indexOf(c) < 0) throw new IllegalArgumentException("Ключ: лише українські літери.");
        StringBuilder out = new StringBuilder();
        int position = 0;
        for (char c : text.toCharArray()) {
            int index = ALPHABET.indexOf(Character.toLowerCase(c));
            if (index < 0) { out.append(c); continue; }
            int offset = ALPHABET.indexOf(lowerKey.charAt(position++ % lowerKey.length()));
            char changed = ALPHABET.charAt(Math.floorMod(index + (decrypt ? -offset : offset), ALPHABET.length()));
            out.append(Character.isUpperCase(c) ? Character.toUpperCase(changed) : changed);
        }
        return out.toString();
    }
    static String encode(String text) {
        StringBuilder out = new StringBuilder();
        for (char c : text.toLowerCase(Locale.ROOT).toCharArray()) {
            if (c >= '0' && c <= '9') out.append("08").append(c);
            else {
                String code = ENCODE.get(c);
                if (code == null) throw new IllegalArgumentException("Символ відсутній у таблиці: " + c);
                out.append(code);
            }
        }
        return out.toString();
    }
    static String decode(String digits) {
        requireDigits(digits);
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < digits.length();) {
            int length = "890".indexOf(digits.charAt(i)) >= 0 ? 2 : 1;
            if (i + length > digits.length()) throw new IllegalArgumentException("Незавершений код у позиції " + i);
            String code = digits.substring(i, i + length);
            i += length;
            if (code.equals("08")) {
                if (i == digits.length()) throw new IllegalArgumentException("Після 08 має бути цифра.");
                out.append(digits.charAt(i++));
            } else {
                Character c = DECODE.get(code);
                if (c == null) throw new IllegalArgumentException("Невідомий код: " + code);
                out.append(c);
            }
        }
        return out.toString();
    }
    static String decimal(String digits, String keyDigits, boolean decrypt) {
        requireDigits(digits); requireDigits(keyDigits);
        if (keyDigits.isEmpty()) throw new IllegalArgumentException("Ключ не може бути порожнім.");
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < digits.length(); i++) {
            int k = keyDigits.charAt(i % keyDigits.length()) - '0';
            out.append(Math.floorMod(digits.charAt(i) - '0' + (decrypt ? -k : k), 10));
        }
        return out.toString();
    }
    private static void requireDigits(String text) {
        if (!text.matches("[0-9]*")) throw new IllegalArgumentException("Очікуються лише цифри 0–9 без пробілів.");
    }
}
