package org.example;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class App {
    private static final int HoursInDay = 24;
    private static int[] prices = new int[HoursInDay];

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char choice;
        String menu = """
                Elpriser
                ========
                1. Inmatning
                2. Min, Max och Medel
                3. Sortera
                4. Bästa Laddningstid (4h)
                e. Avsluta
                "]
                """;

        do {
            System.out.print(menu);
            choice = scanner.next().charAt(0);
            menuChoice(choice, scanner);
            Locale.setDefault(Locale.of("sv", "SE"));
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
            case 'e':
            case 'E':
                System.out.print("Programmet avslutas\n");
                break;
            default:
                System.out.print("Invalid choice\n");
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
                        System.out.print("Ogiltig inmatning ange ören i heltal\n");
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
            String result = String.format("""
                              "Lägsta pris: %02d-%02d, %d öre/kWh
                            Högsta pris: %02d-%02d, %d öre/kWh
                            Medelpris: %.2f öre/kWh
                            "
                            """,
                        minHour, minHour + 1, min, maxHour, maxHour + 1, max, ave);
            System.out.printf(result);
    }


        private static void sort () {
            int[] sorted = new int[HoursInDay];

            for (int i = 0; i < HoursInDay; i++) {
                sorted[i] = i;
            }
            for (int i = 0; i < HoursInDay; i++) {
                for (int j = i + 1; j < HoursInDay; j++) {
                    if (prices[sorted[i]] < prices[sorted[j]] ||
                        (prices[sorted[i]] == prices[sorted[j]] && sorted[i] > sorted[j])) {
                            int temp = sorted[i];
                            sorted[i] = sorted[j];
                            sorted[j] = temp;
                    }
                }
            }

            for (int i : sorted) {
                int nextHour = (i + 1) % 24;
                String response;

                // Specialregel för att visa "23-24" istället för "23-00"
                if (i == 23) {
                    response = String.format("""
            %02d-%02d %d öre
            """, i, HoursInDay, prices[i]);
                }
                else {
                    response = String.format("""
                            %02d-%02d %d öre
                            """, i, nextHour, prices[i]);
                }
                System.out.print(response);
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
            String response = String.format("""
                       "Påbörja laddning klockan %02d
                    Medelpris 4h: %.1f öre/kWh
                    "
                    """, bestStartHour, minSum / 4);
            System.out.printf(response);

        }
}



