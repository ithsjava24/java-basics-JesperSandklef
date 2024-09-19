package org.example;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

public class App {
    private static final int HoursInDay = 24;
    private static int[] prices = new int[HoursInDay];

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char choice;
        String menuvg = """
                Elpriser
                 ========
                1. Inmatning
                2. Min, Max och Medel
                3. Sortera
                4. Bästa Laddningstid (4h)
                5. Visualisering
                e. Avsluta
                """;

        do {
            System.out.print(menuvg);
            choice = scanner.next().charAt(0);
            menuChoice(choice, scanner);
        } while (choice != 'e' && choice != 'E');}

        private static void menuChoice(char choice, Scanner scanner) {
        switch (choice) {
            case '1':
                inputPrices(scanner);
                break;
            case '2':
                printMinMaxAve();
                break;
            case '3':
                sort();
                break;
            case '4':
                bestChargingTime();
                break;
            case '5':
                visualize();
            case 'e':
            case 'E':
                System.out.println("Programmet avslutas");
                break;
            default:
                System.out.println("Invalid choice");
        }
        }

        private static void inputPrices (Scanner scanner){
            for (int i = 0; i < HoursInDay; i++) {
                boolean validInput = false;
                while (!validInput) {
                    System.out.printf("Ange pris för varje timma %02d - %02d: ", i, i+1);
                    if (scanner.hasNextInt()){
                        int price = scanner.nextInt();
                        scanner.nextLine();
                            prices[i] = price;
                            validInput = true;
                    }
                    else {
                        System.out.println("Ogiltig inmatning ange ören i heltal");
                        scanner.next();
                    }
                }
            }
        }

        private static void printMinMaxAve () {
            int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE, sum = 0;
            int minHour = 0, maxHour = 0;

            for (int i = 0; i < HoursInDay; i++) {
                if (prices[i] < min) {
                    min = prices[i];
                    minHour = i;

                }
                if (prices[i] > max) {
                    max = prices[i];
                    maxHour = i;
                }
                sum += prices[i];}

            double ave = (double) sum / HoursInDay;
            System.out.printf("Lägsta pris: %02d-%02d, %d öre/kWh %n" +
                            "Högsta pris: %02d-%02d, %d öre/kWh %n" +
                            "Medelpris: %.2f öre/kWh%n",
                        minHour, minHour + 1, min, maxHour, maxHour + 1, max, ave); }

        private static void sort () {
            Arrays.sort(prices);
            System.out.println("Priser är sorterade i stigande ordning");
            for (int i = 0; i < HoursInDay; i++) {
                    System.out.printf("%02d-%02d: %d öre%n", i, i + 1, prices[i]);
            }
        }

        private static void bestChargingTime () {
            int bestStartHour = 0;
            float minSum = Float.MAX_VALUE;

            for (int i = 0; i <= HoursInDay - 4; i++) {
                float sum = 0;
                for (int j = 0; j < 4; j++) {
                    sum += prices[i + j];
                }
                if (sum < minSum) {
                    minSum = sum;
                    bestStartHour = i;
                }
            }
            System.out.printf("Påbörja laddning kl %02d%n Medelpris 4h: %.2f öre/kWh%n",
                    bestStartHour, minSum / 4);
        }

        private static void visualize() {
        int maxPrice = Arrays.stream(prices).max().getAsInt();
        int minPrice = Arrays.stream(prices).min().getAsInt();
        int scaleFactor = 10;

        for (int level = maxPrice; level >= minPrice; level -= scaleFactor) {
            System.out.printf("%3d|", level);
            for (int price : prices) {
                if (price >= level) {
                    System.out.print("x  ");
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println();
        }
        System.out.print("   ");
        for (int i = 0; i < HoursInDay; i++) {
            System.out.printf("%02d ", i);
        }
            System.out.println();
    }
}



