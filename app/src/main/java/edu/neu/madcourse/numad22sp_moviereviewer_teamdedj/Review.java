package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

public class Review {
    public String username;
    public String reviewContent;



    public Review() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Review(String username, String reviewContent) {
        this.username = username;
        this.reviewContent = reviewContent;
    }
}
