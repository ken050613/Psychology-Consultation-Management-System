/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javaproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author kenny.kong
 */
class Lecturer extends User {
    public Lecturer(String id, String name, String email, String password) {
        super(id, name, email, password, "Lecturer");
    }

    @Override
    public void register() {
        try {
            FileWriter writer = new FileWriter("users.txt", true);
            writer.write(id + "," + name + "," + email + "," + password + ",Lecturer\n");
            writer.close();
            FileWriter slotWriter = new FileWriter("lecturers.txt", true);
            slotWriter.write(id + "," + name + "\n");
            slotWriter.close();
            System.out.println("Lecturer registered successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred during registration.");
        }
    }

    public void manageSlots(String date, String time) {
        try {
            FileWriter writer = new FileWriter("slots.txt", true);
            writer.write(id + "," + date + "," + time + ",Available\n");
            writer.close();
            System.out.println("Slot added successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while managing slots.");
        }
    }

    public void viewAppointments() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("appointments.txt"));
            String line;
            System.out.println("Upcoming and Past Appointments:");
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details[2].equals(id)) {
                    System.out.println("Appointment ID: " + details[0] + ", Student ID: " + details[1] + ", Date: " + details[3] + ", Time: " + details[4] + ", Status: " + details[5]);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error reading appointments.");
        }
    }

    public void approveReschedule(String appointmentID, String newDate, String newTime) {
        try {
            File inputFile = new File("appointments.txt");
            File tempFile = new File("tempAppointments.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            FileWriter writer = new FileWriter(tempFile);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details[0].equals(appointmentID) && details[2].equals(id)) {
                    writer.write(details[0] + "," + details[1] + "," + details[2] + "," + newDate + "," + newTime + ",Approved\n");
                } else {
                    writer.write(line + "\n");
                }
            }
            reader.close();
            writer.close();

            inputFile.delete();
            tempFile.renameTo(inputFile);

            System.out.println("Reschedule request approved.");
        } catch (IOException e) {
            System.out.println("Error approving reschedule request.");
        }
    }

    public void rejectReschedule(String appointmentID) {
        try {
            File inputFile = new File("appointments.txt");
            File tempFile = new File("tempAppointments.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            FileWriter writer = new FileWriter(tempFile);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details[0].equals(appointmentID) && details[2].equals(id)) {
                    writer.write(details[0] + "," + details[1] + "," + details[2] + "," + details[3] + "," + details[4] + ",Rejected\n");
                } else {
                    writer.write(line + "\n");
                }
            }
            reader.close();
            writer.close();

            inputFile.delete();
            tempFile.renameTo(inputFile);

            System.out.println("Reschedule request rejected.");
        } catch (IOException e) {
            System.out.println("Error rejecting reschedule request.");
        }
    }

    public void provideFeedback(String studentID, String comments) {
        try {
            FileWriter writer = new FileWriter("lecturerFeedback.txt", true);
            writer.write(id + "," + studentID + "," + comments + "\n");
            writer.close();
            System.out.println("Feedback provided successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while providing feedback.");
        }
    }

    public void viewFeedback() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("feedback.txt"));
            String line;
            System.out.println("Student Feedback:");
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details[1].equals(id)) {
                    System.out.println("Student ID: " + details[0] + ", Rating: " + details[2] + ", Comments: " + details[3]);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error reading feedback.");
        }
    }
}
