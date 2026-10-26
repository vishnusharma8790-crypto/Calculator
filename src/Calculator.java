import java.util.Scanner;

public class Calculator {

    public static void main(String[] args){



                Scanner sc = new Scanner(System.in);

                while (true) {
                    System.out.println("\n===== CALCULATOR =====");
                    System.out.println("Enter an expression (or exit):");

                    String input = sc.nextLine().trim();

                    if (input.equalsIgnoreCase("exit")) {
                        System.out.println("Calculator Closed!");
                        break;
                    }

                    try {
                        Parser parser = new Parser(input);
                        double result = parser.calculate();

                        if (!Double.isFinite(result)) {
                            throw new ArithmeticException("Result is too large!");
                        }

                        System.out.println("Result = " + result);

                    } catch (Exception e) {
                        System.out.println("Invalid expression: " + e.getMessage());
                    }
                }

                sc.close();
            }

            static class Parser {
                String s;
                int i = 0;

                Parser(String s) {
                    this.s = s;
                }

                void skipSpaces() {
                    while (i < s.length() && s.charAt(i) == ' ') {
                        i++;
                    }
                }

                double calculate() {
                    double result = expression();
                    skipSpaces();

                    if (i != s.length()) {
                        throw new IllegalArgumentException("Unexpected character");
                    }

                    return result;
                }

                // Addition and subtraction
                double expression() {
                    double result = term();

                    while (true) {
                        skipSpaces();

                        if (i < s.length() && s.charAt(i) == '+') {
                            i++;
                            result += term();
                        } else if (i < s.length() && s.charAt(i) == '-') {
                            i++;
                            result -= term();
                        } else {
                            break;
                        }
                    }

                    return result;
                }

                // Multiplication, division and modulus
                double term() {
                    double result = factor();

                    while (true) {
                        skipSpaces();

                        if (i < s.length() && s.charAt(i) == '*') {
                            i++;
                            result *= factor();
                        } else if (i < s.length() && s.charAt(i) == '/') {
                            i++;
                            double num = factor();

                            if (num == 0) {
                                throw new ArithmeticException("Cannot divide by zero");
                            }

                            result /= num;
                        } else if (i < s.length() && s.charAt(i) == '%') {
                            i++;
                            double num = factor();

                            if (num == 0) {
                                throw new ArithmeticException("Modulus by zero");
                            }

                            result %= num;
                        } else {
                            break;
                        }
                    }

                    return result;
                }

                // Numbers, brackets and negative numbers
                double factor() {
                    skipSpaces();

                    if (i >= s.length()) {
                        throw new IllegalArgumentException("Number expected");
                    }

                    if (s.charAt(i) == '+') {
                        i++;
                        return factor();
                    }

                    if (s.charAt(i) == '-') {
                        i++;
                        return -factor();
                    }

                    if (s.charAt(i) == '(') {
                        i++;
                        double result = expression();
                        skipSpaces();

                        if (i >= s.length() || s.charAt(i) != ')') {
                            throw new IllegalArgumentException("Missing closing bracket");
                        }

                        i++;
                        return result;
                    }

                    int start = i;
                    boolean dot = false;
                    boolean digit = false;

                    while (i < s.length()) {
                        char ch = s.charAt(i);

                        if (ch >= '0' && ch <= '9') {
                            digit = true;
                            i++;
                        } else if (ch == '.' && !dot) {
                            dot = true;
                            i++;
                        } else {
                            break;
                        }
                    }

                    if (!digit) {
                        throw new IllegalArgumentException("Number expected");
                    }

                    double num = Double.parseDouble(s.substring(start, i));

                    if (!Double.isFinite(num)) {
                        throw new ArithmeticException("Number is too large");
                    }

                    return num;
                }



    }
}
