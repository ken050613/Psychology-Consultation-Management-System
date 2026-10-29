/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javaproject;

import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author kenny.kong
 */
class Feedback {

    private final String studentID;
    private String lecturerID, rating, comments;

    public Feedback(String studentID, String lecturerID, String rating, String comments) {
        this.studentID = studentID;
        this.lecturerID = lecturerID;
        this.rating = rating;
        this.comments = comments;
    }

    public void saveFeedback() {
        try {
            FileWriter writer = new FileWriter("feedback.txt", true);
            writer.write(studentID + "," + lecturerID + "," + rating + "," + comments + "\n");
            writer.close();
            System.out.println("Feedback saved successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while saving feedback.");
        }
    }
}