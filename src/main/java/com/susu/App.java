
package com.susu;

import java.math.BigDecimal;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SusuService service;

        System.out.println("=== Susu Group Tracker ===");

        try {
            System.out.print("Enter weekly contribution amount: ");
            BigDecimal contribution =
                    new BigDecimal(scanner.nextLine().trim());

            service = new SusuService(contribution);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid contribution amount: "
                    + e.getMessage());
            scanner.close();
            return;
        }

        boolean running = true;

        while (running) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Add member");
            System.out.println("2. Record contribution");
            System.out.println("3. Close current week");
            System.out.println("4. View report");
            System.out.println("5. List members");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        System.out.print("Enter member name: ");
                        service.addMember(scanner.nextLine());
                        System.out.println("Member added successfully.");
                        break;

                    case "2":
                        System.out.print("Enter member name: ");
                        String memberName = scanner.nextLine();

                        System.out.print("Enter payment amount: ");
                        BigDecimal amount =
                                new BigDecimal(scanner.nextLine().trim());

                        service.recordContribution(memberName, amount);
                        System.out.println("Contribution recorded.");
                        break;

                    case "3":
                        System.out.println(service.closeWeek());
                        break;

                    case "4":
                        System.out.println(service.getReport());
                        break;

                    case "5":
                        System.out.println("Members: "
                                + service.getMembers());
                        break;

                    case "0":
                        running = false;
                        System.out.println("Goodbye!");
                        break;

                    default:
                        System.out.println(
                                "Invalid option. Please choose from the menu.");
                }
            } catch (NumberFormatException e) {
                System.out.println(
                        "Invalid amount. Enter a valid decimal number.");
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
