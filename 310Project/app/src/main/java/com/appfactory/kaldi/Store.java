package com.appfactory.kaldi;

public class Store
{
    public String storeName;
    public String location;
    public Menu menu;


    /**
     *
     */
    public Store() {

    }
    /**
     *
     * @param storeName
     * @param location
     * @param menu
     */
    public Store(String storeName, String location, Menu menu)
    {
        this.storeName = storeName;
        this.location = location;
        this.menu = menu;
    }

    /**
     *
     * @return
     */
    public String getStoreName()
    {
        return storeName;
    }

    /**
     *
     * @param storeName
     */
    public void setStoreName(String storeName)
    {
        this.storeName = storeName;
    }

    /**
     *
     * @return
     */
    public String getLocation()
    {
        return location;
    }

    /**
     *
     * @param location
     */
    public void setLocation(String location)
    {
        this.location = location;
    }

    /**
     *
     * @return
     */
    public Menu getMenu()
    {
        return menu;
    }

    /**
     *
     * @param menu
     */
    public void setMenu(Menu menu)
    {
        this.menu = menu;
    }
}