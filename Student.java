/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javaproject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author kenny.kong
 */
class Student extends User {
    public Student(String id, String name, String email, String password) {
        super(id, name, email, password, "Student");
    }

    @Override
    public void register() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("users.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[2].equals(email)) {
                    System.out.println("Email already exists. Try another email.");
                    reader.close();
                    return;
                }
            }
            reader.close();

            FileWriter writer = new FileWriter("users.txt", true);
            writer.write(id + "," + name + "," + email + "," + password + ",Student\n");
            writer.close();
            System.out.println("Student registered successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred during registration.");
        }
    }

    public void viewLecturers() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("lecturers.txt"));
            BufferedReader slotReader = new BufferedReader(new FileReader("slots.txt"));
            String line;

            System.out.println("Available Lecturers and Slots:");
            while ((line = reader.readLine()) != null) {
                String[] lecturerData = line.split(",");
                System.out.println("Lecturer: " + lecturerData[1]);
                System.out.println("Available Slots:");
                slotReader = new BufferedReader(new FileReader("slots.txt"));
                String slotLine;
                while ((slotLine = slotReader.readLine()) != null) {
                    String[] slotData = slotLine.split(",");
                    if (slotData[0].equals(lecturerData[0]) && slotData[3].equals("Available")) {
                        System.out.println("  Date: " + slotData[1] + ", Time: " + slotData[2]);
                    }
                }
            }
            reader.close();
            slotReader.close();
        } catch (IOException e) {
            System.out.println("Error reading lecturer or slot data.");
        }
    }

    public void makeAppointment(String lecturerID, String date, String time) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("slots.txt"));
            String line;
            boolean slotAvailable = false;

            while ((line = reader.readLine()) != null) {
                String[] slotData = line.split(",");
                if (slotData[0].equals(lecturerID) && slotData[1].equals(date) && slotData[2].equals(time) && slotData[3].equals("Available")) {
                    slotAvailable = true;
                    break;
                }
            }
            reader.close();

            if (!slotAvailable) {
                System.out.println("The selected slot is not available. Please choose another.");
                return;
            }

            FileWriter writer = new FileWriter("appointments.txt", true);
            String appointmentID = UUID.randomUUID().toString();
            writer.write(appointmentID + "," + id + "," + lecturerID + "," + date + "," + time + ",Pending\n");
            writer.close();
            System.out.println("Appointment made successfully.");

            // Update slot status
            List<String> updatedSlots = new ArrayList<>();
            reader = new BufferedReader(new FileReader("slots.txt"));
            while ((line = reader.readLine()) != null) {
                String[] slotData = line.split(",");
                if (slotData[0].equals(lecturerID) && slotData[1].equals(date) && slotData[2].equals(time)) {
                    slotData[3] = "Booked";
                }
                updatedSlots.add(String.join(",", slotData));
            }
            reader.close();

            FileWriter slotWriter = new FileWriter("slots.txt");
            for (String updatedSlot : updatedSlots) {
                slotWriter.write(updatedSlot + "\n");
            }
            slotWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred while booking appointment.");
        }
    }

    public void viewAppointments(String type) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("appointments.txt"));
            String line;
            System.out.println(type + " Appointments:");
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[1].equals(id) && ((type.equals("Upcoming") && data[5].equals("Pending")) || (type.equals("Past") && data[5].equals("Completed")))) {
                    System.out.println("Appointment ID: " + data[0] + ", Lecturer ID: " + data[2] + ", Date: " + data[3] + ", Time: " + data[4] + ", Status: " + data[5]);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error reading appointments.");
        }
    }

    public void cancelAppointment(String appointmentID) {
        try {
            List<String> updatedAppointments = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader("appointments.txt"));
            String line;

            boolean appointmentFound = false;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(appointmentID)) {
                    appointmentFound = true;
                    System.out.println("Appointment canceled successfully.");
                    continue;
                }
                updatedAppointments.add(line);
            }
            reader.close();

            if (!appointmentFound) {
                System.out.println("Appointment not found.");
                return;
            }

            FileWriter writer = new FileWriter("appointments.txt");
            for (String updatedAppointment : updatedAppointments) {
                writer.write(updatedAppointment + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error canceling appointment.");
        }
    }

    public void giveFeedback(String lecturerID, String rating, String comments) {
        Feedback feedback = new Feedback(id, lecturerID, rating, comments);
        feedback.saveFeedback();
    }
}