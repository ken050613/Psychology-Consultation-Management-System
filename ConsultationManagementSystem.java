/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javaproject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author kenny.kong
 */
public class ConsultationManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Psychology Consultation Management System");

        while (true) {
            System.out.println("\n1. Register\n2. Login\n3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter ID: ");
                    String id = scanner.nextLine();
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter Password: ");
                    String password = scanner.nextLine();
                    System.out.print("Enter Role (Student/Lecturer): ");
                    String role = scanner.nextLine();

                    if (role.equalsIgnoreCase("Student")) {
                        Student student = new Student(id, name, email, password);
                        student.register();
                    } else if (role.equalsIgnoreCase("Lecturer")) {
                        Lecturer lecturer = new Lecturer(id, name, email, password);
                        lecturer.register();
                    } else {
                        System.out.println("Invalid role entered.");
                    }
                    break;

                case 2:
                    System.out.print("Enter Email: ");
                    String loginEmail = scanner.nextLine();
                    System.out.print("Enter Password: ");
                    String loginPassword = scanner.nextLine();

                    boolean authenticated = false;
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader("users.txt"));
                        String line;

                        while ((line = reader.readLine()) != null) {
                            String[] data = line.split(",");
                            if (data[2].equals(loginEmail) && data[3].equals(loginPassword)) {
                                authenticated = true;
                                if (data[4].equals("Student")) {
                                    System.out.println("Welcome, Student " + data[1]);
                                    Student student = new Student(data[0], data[1], data[2], data[3]);
                                    student.viewLecturers();
                                } else if (data[4].equals("Lecturer")) {
                                    System.out.println("Welcome, Lecturer " + data[1]);
                                    Lecturer lecturer = new Lecturer(data[0], data[1], data[2], data[3]);
                                    lecturer.manageSlots("2024-12-15", "10:00 AM");
                                }
                                break;
                            }
                        }

                        reader.close();

                        if (!authenticated) {
                            System.out.println("Invalid credentials. Please try again.");
                        }

                    } catch (IOException e) {
                        System.out.println("An error occurred during login.");
                    }
                    break;

                case 3:
                    System.out.println("Exiting system. Goodbye!");
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
