package ar.com.wolox.unstuckme.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by agustinpagnoni on 8/2/15.
 */
public class QuestionNew {

    @SerializedName("options_attribute") OptionNew[] mOptions;
    @SerializedName("exclusive") boolean mExclusive;

    private class OptionNew {

        @SerializedName("option") private String mOption;
    }

}
