package com.appfactory.kaldi;

import java.util.ArrayList;
import java.util.List;

public class Merchant extends Drinker
{
    public List<Store> stores;

    /**
     * Default constructor
     */
    public Merchant()
    {

    }

    /**
     * Constructor
     */
    public Merchant(String name, String password, String email, String storeName, String location, Menu menu)
    {
        super(name, password, email);
        this.stores = new ArrayList<Store>();
        this.stores.add(new Store(storeName, location, menu));
        submitToDatabase();
    }

    /**
     *
     * @param name
     * @param password
     * @param email
     * @param storeName
     * @param location
     * @param menu
     * @param id
     */
    public Merchant(String name, String password, String email, String storeName, String location, Menu menu, String id)
    {
        this(name, password, email, storeName, location, menu);
        this.id = id;
    }

    /**
     * 
     */
    @Override
    public void submitToDatabase()
    {
        this.database.child("merchants").child(id).setValue(this);
    }

    /**
     *
     */
    public void addstore(Store store)
    {
        stores.add(store);
    }
}