package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import java.util.Date;

public class MovieCard {
    public String currentUser;
    public String movieId;
    public String title;
    public String description;
    public String releaseDate;
    public String poster;

    public MovieCard(String currentUser, String movieId, String title, String description, String releaseDate, String poster) {
        this.currentUser = currentUser;
        this.movieId = movieId;
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.poster = poster;
    }
}
