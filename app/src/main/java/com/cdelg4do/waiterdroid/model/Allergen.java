package com.cdelg4do.waiterdroid.model;

import java.net.MalformedURLException;
import java.net.URL;


// This class represents a possible allergen that might be present in a dish.
// ----------------------------------------------------------------------------

public class Allergen {

    // Object attributes
    public final int id;
    public final String name;
    public final URL imageUrl;


    // Class constructor
    public Allergen(int id, String name, String imageUrl) {

        this.id = id;
        this.name = name;

        URL url;
        try                             {   url = new URL(imageUrl);  }
        catch (MalformedURLException e) {   url = null;               }

        this.imageUrl = url;
    }


    // Other methods:

    // Returns a string with the allergen info (for debugging)
    @Override
    public String toString() {

        String res = "Id: " + id + ", Name: " + name + ", Image: ";

        if (imageUrl == null)
            res += "<none>";
        else
            res += imageUrl.toString();

        return res;
    }
}
