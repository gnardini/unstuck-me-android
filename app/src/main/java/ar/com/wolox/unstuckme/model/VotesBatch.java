package ar.com.wolox.unstuckme.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VotesBatch {
    @SerializedName("votes")
    List<Integer> mVotes;

    public VotesBatch(List<Integer> votes) {
        mVotes = votes;
    }
}
