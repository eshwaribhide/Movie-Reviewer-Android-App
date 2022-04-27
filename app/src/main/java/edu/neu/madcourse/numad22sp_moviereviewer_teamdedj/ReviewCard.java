package edu.neu.madcourse.numad22sp_moviereviewer_teamdedj;

import android.os.Parcel;
import android.os.Parcelable;

/*
This review card is used to load reviews in a recycler view in ProfileReviewsFragment and HomePage.
Since this is generated in an activity and passed to a fragment, it needs to implement Parcelable.
 */
public class ReviewCard implements Parcelable {
    public String reviewId;
    public String username;
    public String movieTitle;
    public String reviewTitle;
    public String reviewDate;

    public ReviewCard(String reviewId, String username, String movieTitle, String reviewTitle, String reviewDate) {
        this.reviewId = reviewId;
        this.username = username;
        this.movieTitle = movieTitle;
        this.reviewTitle = reviewTitle;
        this.reviewDate = reviewDate;
    }

    protected ReviewCard(Parcel in) {
        username = in.readString();
        movieTitle = in.readString();
        reviewTitle = in.readString();
        reviewDate = in.readString();
    }

    public static final Creator<ReviewCard> CREATOR = new Creator<ReviewCard>() {
        @Override
        public ReviewCard createFromParcel(Parcel in) {
            return new ReviewCard(in);
        }

        @Override
        public ReviewCard[] newArray(int size) {
            return new ReviewCard[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(movieTitle);
        parcel.writeString(reviewTitle);
        parcel.writeString(reviewDate);
    }
}
