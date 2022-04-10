package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

public class Genre {
    public boolean comedyGenreSelected;
    public boolean actionGenreSelected;
    public boolean dramaGenreSelected;



    public Genre() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Genre(boolean comedyGenreSelected, boolean actionGenreSelected, boolean dramaGenreSelected) {
        this.comedyGenreSelected = comedyGenreSelected;
        this.actionGenreSelected = actionGenreSelected;
        this.dramaGenreSelected = dramaGenreSelected;
    }
}
