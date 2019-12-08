package com.appfactory.kaldi;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable
{
    public List<Item> items;

    public Order()
    {
        items = new ArrayList<Item>();
    }



}