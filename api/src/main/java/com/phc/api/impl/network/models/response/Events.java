package com.phc.api.impl.network.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Events {

    @SerializedName("available")
    @Expose
    private long available;
    @SerializedName("collectionURI")
    @Expose
    private String collectionURI;
    @SerializedName("items")
    @Expose
    private List<Object> items = new ArrayList<Object>();
    @SerializedName("returned")
    @Expose
    private long returned;

    /**
     * 
     * @return
     *     The available
     */
    public long getAvailable() {
        return available;
    }

    /**
     * 
     * @param available
     *     The available
     */
    public void setAvailable(long available) {
        this.available = available;
    }

    /**
     * 
     * @return
     *     The collectionURI
     */
    public String getCollectionURI() {
        return collectionURI;
    }

    /**
     * 
     * @param collectionURI
     *     The collectionURI
     */
    public void setCollectionURI(String collectionURI) {
        this.collectionURI = collectionURI;
    }

    /**
     * 
     * @return
     *     The items
     */
    public List<Object> getItems() {
        return items;
    }

    /**
     * 
     * @param items
     *     The items
     */
    public void setItems(List<Object> items) {
        this.items = items;
    }

    /**
     * 
     * @return
     *     The returned
     */
    public long getReturned() {
        return returned;
    }

    /**
     * 
     * @param returned
     *     The returned
     */
    public void setReturned(long returned) {
        this.returned = returned;
    }

}
