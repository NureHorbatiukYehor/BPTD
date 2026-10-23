import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            if (args.length == 1 && args[0].equals("--demo")) {
                for (int task = 1; task <= 5; task++) run(task);
                return;
            }
            Scanner input = new Scanner(System.in, StandardCharsets.UTF_8);
            if (args.length == 1) { run(Integer.parseInt(args[0])); return; }
            while (true) {
                System.out.println("\nПЗ №1 \n1. Перше завдання\n2. Друге завдання\n3. Третє завдання\n4. Четверте завдання\n5. П`яте завдання\n0. Вихід");
                System.out.print("> ");
                if (!input.hasNextLine()) return;
                String choice = input.nextLine().trim();
                if (choice.equals("0")) return;
                try { run(Integer.parseInt(choice)); }
                catch (IllegalArgumentException e) { System.out.println("Помилка: " + e.getMessage()); }
            }
        } catch (Exception e) { System.err.println("Помилка: " + e.getMessage()); System.exit(1); }
    }
    static void run(int task) {
        System.out.println("\nЗавдання: " + task);
        switch (task) {
            case 1 -> {
                String result = Ciphers.caesar(Variant2.CAESAR_CIPHER, -11);
                show(Variant2.CAESAR_CIPHER, "11", result);
                compare(result, Variant2.CAESAR_EXPECTED);
                System.out.println("Зворотне шифрування: " + Ciphers.caesar(result, 11).equals(Variant2.CAESAR_CIPHER));
            }
            case 2 -> {
                String result = Ciphers.vigenere(Variant2.VIGENERE_CIPHER, Variant2.VIGENERE_KEY, true);
                show(Variant2.VIGENERE_CIPHER, Variant2.VIGENERE_KEY, result);
                compare(result, Variant2.VIGENERE_EXPECTED);
                System.out.println("Зворотне шифрування: " + Ciphers.vigenere(result, Variant2.VIGENERE_KEY, false).equals(Variant2.VIGENERE_CIPHER));
            }
            case 3 -> {
                String result = Ciphers.vigenere(Variant2.HINT_CIPHER, Variant2.HINT_KEY, true);
                show(Variant2.HINT_CIPHER, Variant2.HINT_KEY, result);
                System.out.println("Збіг із підказкою: " + result.startsWith(Variant2.HINT_PREFIX));
                String encrypted = Ciphers.vigenere(result, Variant2.HINT_KEY, false);
                System.out.println("Повторно зашифрований текст: " + encrypted);
                System.out.println("Зворотне шифрування: " + encrypted.equals(Variant2.HINT_CIPHER));
            }
            case 4 -> {
                String key = Ciphers.encode(Variant2.DECIMAL_KEY);
                String digits = Ciphers.decimal(Variant2.DECIMAL_CIPHER, key, true);
                String result = Ciphers.decode(digits);
                show(Variant2.DECIMAL_CIPHER, Variant2.DECIMAL_KEY + " → " + key, result);
                System.out.println("Цифрове представлення: " + digits);
                String encrypted = Ciphers.decimal(Ciphers.encode(result), key, false);
                System.out.println("Повторно зашифрований текст: " + encrypted);
                System.out.println("Зворотне шифрування: " + encrypted.equals(Variant2.DECIMAL_CIPHER));
            }
            case 5 -> {
                String[] days = {"понеділок", "вівторок", "середа", "четвер", "п'ятниця", "субота", "неділя"};
                LocalDate date = LocalDate.now(ZoneId.of("Europe/Kyiv"));
                String day = days[date.getDayOfWeek().getValue() - 1];
                String key = day.replace("'", "");
                String digits = Ciphers.encode("123456");
                String cipher = Ciphers.decimal(digits, Ciphers.encode(key), false);
                System.out.println("Дата: " + date + ", день тижня: " + day);
                show("123456 → " + digits, key, cipher);
                System.out.println("Перевірка: " + Ciphers.decode(Ciphers.decimal(cipher, Ciphers.encode(key), true)));
            }
            default -> throw new IllegalArgumentException("Номер завдання має бути від 1 до 5.");
        }
    }
    private static void show(String cipher, String key, String result) {
        System.out.println("Вхідні дані: " + cipher + "\nКлюч: " + key + "\nРезультат: " + result);
    }
    static void compare(String actual, String expected) {
        System.out.println("Еталон із методички: " + expected);
        boolean same = actual.strip().equals(expected.strip());
        System.out.println("Точний збіг з еталоном: " + same);
        if (!same) System.out.println("Дані методички збережено без виправлень; результат отримано алгоритмом, а не підставлено з еталона.");
    }
}
