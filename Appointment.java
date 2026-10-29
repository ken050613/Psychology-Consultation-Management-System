package com.mycompany.javaproject;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kenny.kong
 */
class Appointment {
    private String appointmentID, studentID, lecturerID, date, time, status;

    public Appointment(String appointmentID, String studentID, String lecturerID, String date, String time, String status) {
        this.appointmentID = appointmentID;
        this.studentID = studentID;
        this.lecturerID = lecturerID;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getDetails() {
        return appointmentID + "," + studentID + "," + lecturerID + "," + date + "," + time + "," + status;
    }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }
}