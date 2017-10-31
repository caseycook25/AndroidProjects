package com.example.listview;

import android.widget.TextView;

import java.util.Comparator;

/**
 * See builderpattern example project for how to do builders
 * they are essential when constructing complicated objects and
 * with many optional fields
 */
public class BikeData {
    public static final int COMPANY = 0;
    public static final int MODEL = 1;
    public static final int PRICE = 2;
    public static final int LOCATION = 3;

    //TODO make all BikeData fields final
    final String Company;
    final String Model;
    final Double Price;
    final String Description;
    final String Location;
    final String Date;
    final String Picture;
    final String Link;

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        // TODO figure out how to print all bikedata out for dialogs

        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");

        result.append("Company:" + Company + NEW_LINE);
        result.append("Model:" + Model + NEW_LINE);
        result.append("Price:" + Price + NEW_LINE);
        result.append("Location:" + Location + NEW_LINE);
        result.append("Date Listed:" + Date + NEW_LINE);
        result.append("Description:" + Description + NEW_LINE);
        result.append("Link:" + Link + NEW_LINE);

        return result.toString();
    }

    private BikeData(Builder b) {
        //TODO
        Company = b.Company;
        Model = b.Model;
        Price = b.Price;
        Description = b.Description;
        Location = b.Location;
        Date = b.Date;
        Picture = b.Picture;
        Link = b.Link;

    }

    /**
     * @author lynn builder pattern, see page 11 Effective Java UserData mydata
     *         = new
     *         UserData.Builder(first,last).addProject(proj1).addProject(proj2
     *         ).build()
     */
    public static class Builder {
        final String Company;
        final String Model;
        final Double Price;
        String Description = "";
        String Location = "";
        String Date = "";
        String Picture = "";
        String Link = "";

        // Model and price required
        Builder(String Company, String Model, Double Price) {
            this.Company = Company;
            this.Model = Model;
            this.Price = Price;
        }

        // the following are setters
        // notice it returns this bulder
        // makes it suitable for chaining
        Builder setDescription(String Description) {
            //TODO manage this

            this.Description = Description;
            //TextView desc = (TextView) findViewById(R.id.Description).setText(Description);


            return this;
        }

        Builder setLocation(String Location) {
            this.Location = Location;

            return this;
        }

        Builder setDate(String Date) {
            this.Date = Date;

            return this;
        }

        Builder setPicture(String Picture) {
            this.Picture = Picture;

            return this;
        }

        Builder setLink(String Link) {
            this.Link = Link;

            return this;
        }

        // use this to actually construct Bikedata
        // without fear of partial construction
        public BikeData build() {
            return new BikeData(this);
        }
    }
}

class MyDataIsCompanyComparator implements Comparator<BikeData> {
    public int compare(BikeData bike1, BikeData bike2) {
        //if both equal then 0
        return (bike1.Company.compareTo(bike2.Company));
    }
}

class MyDataIsModelComparator implements Comparator<BikeData> {
    public int compare(BikeData bike1, BikeData bike2) {
        //if both equal then 0
        return (bike1.Model.compareTo(bike2.Model));
    }
}

class MyDataIsLocationComparator implements Comparator<BikeData> {
    public int compare(BikeData bike1, BikeData bike2) {
        //if both equal then 0
        return (bike1.Location.compareTo(bike2.Location));
    }
}

class MyDataIsPriceComparator implements Comparator<BikeData> {
    public int compare(BikeData bike1, BikeData bike2) {
        //if both equal then 0

        if (bike1.Price > bike2.Price)
            return 1;
        else if (bike1.Price < bike2.Price)
            return -1;
        return 0;
    }
}
