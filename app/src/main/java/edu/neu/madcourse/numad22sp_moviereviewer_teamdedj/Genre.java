package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

public class Genre {
    public boolean comedyGenreSelected;
    public boolean actionGenreSelected;
    public boolean dramaGenreSelected;
    public boolean animationGenreSelected;
    public boolean horrorGenreSelected;
    public boolean romanceGenreSelected;
    public boolean sciFiGenreSelected;
    public boolean crimeGenreSelected;
    public boolean documentaryGenreSelected;
    public boolean historyGenreSelected;

    public Genre() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Genre(boolean comedyGenreSelected,
                 boolean actionGenreSelected,
                 boolean dramaGenreSelected,
                 boolean animationGenreSelected,
                 boolean horrorGenreSelected,
                 boolean romanceGenreSelected,
                 boolean sciFiGenreSelected,
                 boolean crimeGenreSelected,
                 boolean documentaryGenreSelected,
                 boolean historyGenreSelected
                 ) {
        this.comedyGenreSelected = comedyGenreSelected;
        this.actionGenreSelected = actionGenreSelected;
        this.dramaGenreSelected = dramaGenreSelected;
        this.animationGenreSelected = animationGenreSelected;
        this.horrorGenreSelected = horrorGenreSelected;
        this.romanceGenreSelected = romanceGenreSelected;
        this.sciFiGenreSelected = sciFiGenreSelected;
        this.crimeGenreSelected = crimeGenreSelected;
        this.documentaryGenreSelected = documentaryGenreSelected;
        this.historyGenreSelected = historyGenreSelected;
    }
}
