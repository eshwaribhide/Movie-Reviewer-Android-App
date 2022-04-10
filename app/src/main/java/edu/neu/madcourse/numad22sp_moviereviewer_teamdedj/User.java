package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

public class User {
    public String fullName;
    public Genre genres;
    public String badgeStatus;



    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String fullName, Genre genres) {
        this.fullName = fullName;
        this.genres = genres;
        this.badgeStatus = "Bronze";
    }
}
