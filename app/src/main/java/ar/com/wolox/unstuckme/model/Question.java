package ar.com.wolox.unstuckme.model;

import java.util.List;

public class Question {

    private static final int MAX_PERCENTAGE = 100;

    private int id;
    private boolean voted ;
    private boolean unlocked;
    private List<Option> options;

    public int getId() {
        return id;
    }

    public List<Option> getOptions() {
        return options;
    }

    public boolean isVoted() {
        return voted;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void unlock() {
        unlocked = true;
    }

    public int getWinnerIndex() {
        if (options == null) return 0;
        int maxVotes = options.get(0).getVotes();
        int maxIndex = 0;
        for (int i = 1 ; i < options.size() ; i++) {
            if (options.get(i).getVotes() > maxVotes) {
                maxVotes = options.get(i).getVotes();
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    public int getTotalVotes() {
        int votes = 0;
        for (Option option : options) votes += option.getVotes();
        return votes;
    }

    public void calculatePercentages() {
        int total = getTotalVotes();
        for (Option option : options) option.setPercentage(MAX_PERCENTAGE
                * option.getVotes() / total);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Question)) return false;
        Question q = (Question) o;
        return id == q.getId();
    }
}
