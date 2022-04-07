package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import java.util.Date;

public class MovieCard {
    public int movieId;
    public int reviewCount;
    public String title;
    public String description;
    //public Date releaseDate;

    public MovieCard(int movieId, int reviewCount, String title, String description) {
        this.movieId = movieId;
        this.reviewCount = reviewCount;
        this.title = title;
        this.description = description;
    }
}
