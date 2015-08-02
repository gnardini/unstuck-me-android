package ar.com.wolox.unstuckme.model;

import com.google.gson.annotations.SerializedName;

public class Option {

    @SerializedName("id") int mId;
    @SerializedName("option") String mImageUrl;
    @SerializedName("votes") int mVotes;
    @SerializedName("percentage") int mPercentage;

    public int getPercentage() {
        return mPercentage;
    }

    public void setPercentage(int percentage) {
        mPercentage = percentage;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getVotes() {
        return mVotes;
    }

    public void setVotes(int votes) {
        mVotes = votes;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
    }
}
