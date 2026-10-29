/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javaproject;

/**
 *
 * @author kenny.kong
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class ConsultationManagementGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}

class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

        add(panel);
        setVisible(true);

        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
                String line;
                boolean authenticated = false;

                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data[2].equals(email) && data[3].equals(password)) {
                        authenticated = true;
                        if (data[4].equals("Student")) {
                            new StudentDashboard(data[0], data[1], data[2], data[3]);
                        } else if (data[4].equals("Lecturer")) {
                            new LecturerDashboard(data[0], data[1], data[2], data[3]);
                        }
                        dispose();
                        break;
                    }
                }

                if (!authenticated) {
                    JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error reading user data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> {
            new RegisterFrame();
            dispose();
        });
    }
}

class RegisterFrame extends JFrame {
    public RegisterFrame() {
        setTitle("Register");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        JLabel idLabel = new JLabel("ID:");
        JTextField idField = new JTextField();

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JLabel roleLabel = new JLabel("Role:");
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"Student", "Lecturer"});

        JButton registerButton = new JButton("Register");

        panel.add(idLabel);
        panel.add(idField);
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(roleLabel);
        panel.add(roleComboBox);
        panel.add(new JLabel());
        panel.add(registerButton);

        add(panel);
        setVisible(true);

        registerButton.addActionListener(e -> {
            String id = idField.getText();
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();

            if (id.isEmpty() || name.isEmpty() || email.isEmpty() || password.isEmpty() || role.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
                writer.write(id + "," + name + "," + email + "," + password + "," + role + "\n");
                JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                new LoginFrame();
                dispose();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving user data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

class StudentDashboard extends JFrame {
    private final String studentId;

    public StudentDashboard(String id, String name, String email, String password) {
        this.studentId = id;

        setTitle("Student Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("Welcome, " + name, SwingConstants.CENTER);
        JButton viewLecturersButton = new JButton("View Lecturers");
        JButton makeAppointmentButton = new JButton("Make Appointment");
        JButton viewAppointmentsButton = new JButton("View Appointments");
        JButton cancelAppointmentButton = new JButton("Cancel Appointment");
        JButton giveFeedbackButton = new JButton("Give Feedback");
        JButton backButton = new JButton("Back");

        JPanel panel = new JPanel(new GridLayout(7, 1, 10, 10));

        panel.add(welcomeLabel);
        panel.add(viewLecturersButton);
        panel.add(makeAppointmentButton);
        panel.add(viewAppointmentsButton);
        panel.add(cancelAppointmentButton);
        panel.add(giveFeedbackButton);
        panel.add(backButton);

        add(panel);
        setVisible(true);

        viewLecturersButton.addActionListener(e -> viewLecturers());
        makeAppointmentButton.addActionListener(e -> makeAppointment());
        viewAppointmentsButton.addActionListener(e -> viewAppointments());
        cancelAppointmentButton.addActionListener(e -> cancelAppointment());
        giveFeedbackButton.addActionListener(e -> giveFeedback());

        backButton.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });
    }

    private void viewLecturers() {
        try (BufferedReader reader = new BufferedReader(new FileReader("slots.txt"))) {
            StringBuilder lecturers = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                lecturers.append(line).append("\n");
            }
            JOptionPane.showMessageDialog(this, lecturers.toString(), "Available Lecturers", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading lecturers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void makeAppointment() {
        String lecturerId = JOptionPane.showInputDialog(this, "Enter Lecturer ID for the appointment:");
        String slotDetails = JOptionPane.showInputDialog(this, "Enter slot details for the appointment:");

        if (lecturerId != null && slotDetails != null && !lecturerId.trim().isEmpty() && !slotDetails.trim().isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("appointments.txt", true))) {
                writer.write(studentId + "," + lecturerId + "," + slotDetails + "\n");
                JOptionPane.showMessageDialog(this, "Appointment booked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving appointment.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewAppointments() {
        try (BufferedReader reader = new BufferedReader(new FileReader("appointments.txt"))) {
            StringBuilder appointments = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(studentId + ",")) {
                    appointments.append(line).append("\n");
                }
            }
            JOptionPane.showMessageDialog(this, appointments.toString(), "Your Appointments", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading appointments.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelAppointment() {
        String appointmentDetails = JOptionPane.showInputDialog(this, "Enter appointment details to cancel:");
        if (appointmentDetails != null && !appointmentDetails.trim().isEmpty()) {
            try {
                File inputFile = new File("appointments.txt");
                File tempFile = new File("appointments_temp.txt");

                try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                     BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

                    String line;
                    boolean found = false;
                    while ((line = reader.readLine()) != null) {
                        if (!line.equals(appointmentDetails)) {
                            writer.write(line + "\n");
                        } else {
                            found = true;
                        }
                    }
                    if (found) {
                        JOptionPane.showMessageDialog(this, "Appointment canceled successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Appointment not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                inputFile.delete();
                tempFile.renameTo(inputFile);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error canceling appointment.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No details provided.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void giveFeedback() {
        String feedback = JOptionPane.showInputDialog(this, "Enter your feedback:");
        if (feedback != null && !feedback.trim().isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("feedback.txt", true))) {
                writer.write(studentId + "," + feedback + "\n");
                JOptionPane.showMessageDialog(this, "Feedback submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error submitting feedback.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No feedback provided.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }
}

class LecturerDashboard extends JFrame {
    private final String lecturerId;

    public LecturerDashboard(String id, String name, String email, String password) {
        this.lecturerId = id;

        setTitle("Lecturer Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("Welcome, " + name, SwingConstants.CENTER);
        JButton manageSlotsButton = new JButton("Manage Slots");
        JButton viewAppointmentsButton = new JButton("View Appointments");
        JButton viewFeedbackButton = new JButton("View Feedback");
        JButton backButton = new JButton("Back");

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));

        panel.add(welcomeLabel);
        panel.add(manageSlotsButton);
        panel.add(viewAppointmentsButton);
        panel.add(viewFeedbackButton);
        panel.add(backButton);

        add(panel);
        setVisible(true);

        manageSlotsButton.addActionListener(e -> manageSlots());
        viewAppointmentsButton.addActionListener(e -> viewAppointments());
        viewFeedbackButton.addActionListener(e -> viewFeedback());

        backButton.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });
    }

    private void manageSlots() {
        String slotDetails = JOptionPane.showInputDialog(this, "Enter slot details:");
        if (slotDetails != null && !slotDetails.trim().isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("slots.txt", true))) {
                writer.write(lecturerId + "," + slotDetails + "\n");
                JOptionPane.showMessageDialog(this, "Slot added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving slot.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No details provided.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void viewAppointments() {
        try (BufferedReader reader = new BufferedReader(new FileReader("appointments.txt"))) {
            StringBuilder appointments = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("," + lecturerId + ",")) {
                    appointments.append(line).append("\n");
                }
            }
            JOptionPane.showMessageDialog(this, appointments.toString(), "Your Appointments", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading appointments.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewFeedback() {
        try (BufferedReader reader = new BufferedReader(new FileReader("feedback.txt"))) {
            StringBuilder feedback = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                feedback.append(line).append("\n");
            }
            JOptionPane.showMessageDialog(this, feedback.toString(), "Student Feedback", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading feedback.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
