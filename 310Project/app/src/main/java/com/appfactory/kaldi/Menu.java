package com.appfactory.kaldi;

import java.util.ArrayList;
import java.util.List;

public class Menu
{
    public List<Item> menu;

    public Menu()
    {

    }

    /**
     *
     */
    public Menu(Item initialItem)
    {
        this.menu = new ArrayList<Item>();
        this.menu.add(initialItem);
    }

    /**
     *
     * @param i
     */
    public void addItem(Item i)
    {
        //Add only one item
        menu.add(i);
    }

    /**
     *
     */
    public void displayMenu()
    {
        for (Item i : menu)
        {
            System.out.println(i.name);
        }
    }

    /**
     *
     * @param i
     */
    public void removeItem(Item i)
    {
        //remove item if exists
    }

    /**
     *
     * @param i
     */
    public void editItem(Item i)
    {

    }
}