package com.elysium.lightningrod.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jay on 11/20/16.
 */

public class Clouds {

    @SerializedName("all")
    @Expose
    private Integer all;

    /**
     *
     * @return
     *     The all
     */
    public Integer getAll() {
        return all;
    }

    /**
     *
     * @param all
     *     The all
     */
    public void setAll(Integer all) {
        this.all = all;
    }
}
