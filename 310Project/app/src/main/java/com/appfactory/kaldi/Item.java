package com.appfactory.kaldi;

import java.util.Random;

public class
Item
{

    public String name;
    public int caffeine;
    public double price;

    public Item()
    {

    }

    /**
     *
     */
    public Item(String name, int caffeine)
    {

        byte[] array = new byte[7];
        new Random().nextBytes(array);
        this.name = name;
        this.caffeine = caffeine;
    }

    /**
     *
     * @return
     */
    public String getName()
    {
        return this.name;
    }

    /**
     *
     * @return
     */
    public int getCaffeine()
    {
        return this.caffeine;
    }
}
