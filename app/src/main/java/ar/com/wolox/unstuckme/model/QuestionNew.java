package ar.com.wolox.unstuckme.model;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by agustinpagnoni on 8/2/15.
 */

public class QuestionNew {

    @SerializedName("options_attributes") List<OptionNew> mOptions ;
    @SerializedName("exclusive") boolean mExclusive;
    @SerializedName("limit") int mLimit;

    public QuestionNew(List<String> cloudinaryPaths, boolean privacy) {
        mExclusive = privacy;
        mOptions = new LinkedList<OptionNew>();
        // TODO descablear
        mLimit = 20;
        for (int i = 0; i < cloudinaryPaths.size(); i++) {
            mOptions.add(new OptionNew(cloudinaryPaths.get(i)));
        }
    }

    private class OptionNew {

        @SerializedName("option") private String mOption;

        public OptionNew(String s) {
            mOption = s;
        }
    }

}
