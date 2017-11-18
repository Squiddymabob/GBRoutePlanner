package com1032.cw2.ew00162.ew00162_assignment2;


public class myMarker {

    public int id = 0;
    public String title = null;
    public Double latitude = 0.00;
    public Double longitude = 0.00;
    public String file = null;

    /**
     * Constructor for Marker
     * @param id
     * @param title
     * @param latitude
     * @param longitude
     * @param file
     */
    public myMarker(int id, String title, Double latitude, Double longitude){
        super();
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
        this.file = file;
    }

    /**
     * Get the Marker's title
     * @return title
     */
    public String getTitle(){
        return title;
    }

    /**
     * Get the Marker's latitude
     * @return latitude
     */
    public Double getLatitude(){
        return latitude;
    }

    /**
     * Get the Marker's longitude
     * @return longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Get the Marker's ID
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Get the Marker's file path
     * @return file
     */
    public String getFile() {
        return file;
    }


}
