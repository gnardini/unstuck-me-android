package ar.com.wolox.unstuckme.model.event;

public class VotesSentEvent {

    private int mVotes;

    public VotesSentEvent(int votes) {
        mVotes = votes;
    }

    public int getVotes() {
        return mVotes;
    }
}
