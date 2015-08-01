package ar.com.wolox.unstuckme.model;

import java.util.List;

public class Question {

    private int id;
    private List<Option> options;

    public int getId() {
        return id;
    }

    public List<Option> getOptions() {
        return options;
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
}
