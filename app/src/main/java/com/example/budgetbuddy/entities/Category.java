package com.example.budgetbuddy.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Represents a category in the database with a name, colour and a unique ID.
 * <code>nextID</code> is a static long variable used to generate unique IDs.
 * It is shared by all <code>Category</code> objects and is incremented each time a new object is created.
 */
@Entity(tableName = "category")
public final class Category
{
    private static long nextID;         // Static ID generator

    @PrimaryKey
    private long categoryID;            // Unique ID

    @ColumnInfo(name = "name")
    private final String name;          // Category Name

    @ColumnInfo(name = "colour")
    private final int colorID;          // The id of the colour (found in colors.xml)


    /**
     * Constructs a new Category object. <code>categoryID</code> is taken from <code>nextID</code>
     * and <code>nextID++</code> is called to increment it and guarantee uniqueness
     *
     * @param name    the name of the category
     * @param colorID the id of the colour (found in colors.xml)
     */
    public Category(String name, int colorID)
    {
        this.categoryID = nextID++; //
        this.name = name;
        this.colorID = colorID;
    }

    /**
     * Returns the category ID
     *
     * @return the category ID
     */
    public long getCategoryID()
    {
        return categoryID;
    }

    /**
     * Sets the category ID
     *
     * @param id the category ID
     */
    public void setCategoryID(long id)
    {
        this.categoryID = id;
    }

    /**
     * Returns the category name
     *
     * @return the category name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the colour ID
     *
     * @return the colour ID
     */
    public int getColorID()
    {
        return colorID;
    }
}
