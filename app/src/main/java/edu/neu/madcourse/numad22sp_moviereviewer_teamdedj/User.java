package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;


// Need to incorporate followers
// we go to a user, and have a reviews child node also, users you are following, go to user id,
// and then see review Ids for that user, get those reviews
public class User {
    public String fullName;
    public Genre genres;
    public String badgeStatus;
    public Integer reviewCount;



    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String fullName, Genre genres) {
        this.fullName = fullName;
        this.genres = genres;
        this.badgeStatus = "Bronze";
        this.reviewCount = 0;
    }
}
