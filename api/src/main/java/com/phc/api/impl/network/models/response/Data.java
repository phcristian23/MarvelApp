package com.phc.api.impl.network.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Data {

    @SerializedName("offset")
    @Expose
    private long offset;
    @SerializedName("limit")
    @Expose
    private long limit;
    @SerializedName("total")
    @Expose
    private long total;
    @SerializedName("count")
    @Expose
    private long count;
    @SerializedName("results")
    @Expose
    private List<Result> results = new ArrayList<Result>();

    /**
     * 
     * @return
     *     The offset
     */
    public long getOffset() {
        return offset;
    }

    /**
     * 
     * @param offset
     *     The offset
     */
    public void setOffset(long offset) {
        this.offset = offset;
    }

    /**
     * 
     * @return
     *     The limit
     */
    public long getLimit() {
        return limit;
    }

    /**
     * 
     * @param limit
     *     The limit
     */
    public void setLimit(long limit) {
        this.limit = limit;
    }

    /**
     * 
     * @return
     *     The total
     */
    public long getTotal() {
        return total;
    }

    /**
     * 
     * @param total
     *     The total
     */
    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * 
     * @return
     *     The count
     */
    public long getCount() {
        return count;
    }

    /**
     * 
     * @param count
     *     The count
     */
    public void setCount(long count) {
        this.count = count;
    }

    /**
     * 
     * @return
     *     The results
     */
    public List<Result> getResults() {
        return results;
    }

    /**
     * 
     * @param results
     *     The results
     */
    public void setResults(List<Result> results) {
        this.results = results;
    }

}
