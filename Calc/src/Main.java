import java.util.Scanner;
import java.util.TreeMap;

public class Main {
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";

    private static Converter converter = new Converter();
    private static boolean isRoman = false;
    private static String[] actions = {"+", "-", "/", "*"};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(calc(scanner.nextLine()));
        scanner.close();
    }
    
    public static String calc(String input) {
        String[] calcLine = input.split(" ");
        // throw MathOperationFormatException
        try {
            if (calcLine.length > 3) throw new MathOperationFormatException();
        } catch (MathOperationFormatException e) {
            return RED + e + RESET;
        }

        int i1 = 0;
        int i2 = 0;
        int index = -1;

        for (int i = 0; i < actions.length; i++) {
            if (calcLine[1].contains(actions[i])) {
                index = i;
                break;
            }
        }

        // throw ArithmeticActionException;
        try {
            if (index == -1) throw new ArithmeticActionException();
        } catch (ArithmeticActionException e) {
            return RED + e.getMessage() + RESET;
        }

        // throw NumberCompatibilityException;
        try {
            if (converter.isRoman(calcLine[0]) == converter.isRoman(calcLine[2])) {
                isRoman = converter.isRoman(calcLine[0]);
                if (isRoman) {
                    i1 = converter.romanToArabian(calcLine[0]);
                    i2 = converter.romanToArabian(calcLine[2]);
                } else {
                    i1 = Integer.parseInt(calcLine[0]);
                    i2 = Integer.parseInt(calcLine[2]);
                }
            } else {
                throw  new NumberCompatibilityException();
            }
        } catch (NumberCompatibilityException e) {
            return RED + e + RESET;
        }

        int result = 0;

        // throw NumberTooLargeException;
        try {
            if (i1 > 10 || i2 > 10) throw new NumberTooLargeException();
            else switch (calcLine[1]) {
                case "+" -> result = add(i1, i2);
                case "-" -> result = subtract(i1, i2);
                case "*" -> result = multiply(i1, i2);
                case "/" -> result = divide(i1, i2);
            }
        } catch (NumberTooLargeException e) {
            return RED + e + RESET;
        }

        return isRoman ? converter.arabianToRoman(result) : String.valueOf(result);
    }

    public static int add(int i1, int i2) {
        return i1 + i2;
    }

    public static int subtract(int i1, int i2) {
        return i1 - i2;
    }

    public static int divide(int i1, int i2) {
        return i1 / i2;
    }

    public static int multiply(int i1, int i2) {
        return i1 * i2;
    }
}

class Converter {
    protected static TreeMap<Character, Integer> toArabian = new TreeMap<>();
    protected static TreeMap<Integer, String> toRoman = new TreeMap<>();

    public Converter() {
        toArabian.put('I', 1);
        toArabian.put('V', 5);
        toArabian.put('X', 10);
        toArabian.put('L', 50);
        toArabian.put('C', 100);

        toRoman.put(100, "C");
        toRoman.put(90, "XC");
        toRoman.put(50, "L");
        toRoman.put(40, "XL");
        toRoman.put(20, "XX");
        toRoman.put(10, "X");
        toRoman.put(9, "IX");
        toRoman.put(5, "V");
        toRoman.put(4, "IV");
        toRoman.put(1, "I");
    }

    // check roman number contains similar key from TreeMap
    public boolean isRoman(String num) {
        return toArabian.containsKey(num.charAt(0));
    }

    // convert from arabian to roman number
    public String arabianToRoman(int n) {
        int letter = toRoman.floorKey(n);
        if (n == letter) return toRoman.get(n);
        return toRoman.get(letter) + arabianToRoman(n - letter);
    }

    // convert from roman to arabian number
    public int romanToArabian(String s) {
        int end = s.length() - 1;
        char[] arr = s.toCharArray();
        int arabian;
        int result = toArabian.get(arr[end]);

        for (int i = end - 1; i >= 0; i--) {
            arabian = toArabian.get(arr[i]);

            if (arabian < toArabian.get(arr[i + 1])) result -= arabian;
            else result += arabian;
        }
        return result;
    }
}

class NumberTooLargeException extends Exception {
    public NumberTooLargeException() {
        super("Number is too large");
    }
}

class NumberCompatibilityException extends Exception {
    public NumberCompatibilityException() {
        super("Arabian numbers is not compatibility with roman numbers");
    }
}

class ArithmeticActionException extends Exception {
    public ArithmeticActionException() {
        super("Arithmetic action not defined");
    }
}

class MathOperationFormatException extends Exception {
    public MathOperationFormatException() {
        super("The format of a mathematical operation does not satisfy the condition: two operands and one operator (+, -, /, *)");
    }
}
