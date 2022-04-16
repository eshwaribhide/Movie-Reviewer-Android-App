package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

public class Review {
    public String movieID;
    public String username;
    public String reviewTitle;
    public String reviewContent;
    public String movieTitle;

    public Review() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Review(String movieID, String username, String reviewTitle, String reviewContent, String movieTitle) {
        this.movieID = movieID;
        this.username = username;
        this.reviewTitle = reviewTitle;
        this.reviewContent = reviewContent;
        this.movieTitle = movieTitle;
    }
}
