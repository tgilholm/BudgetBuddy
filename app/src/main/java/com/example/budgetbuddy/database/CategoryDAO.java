package com.example.budgetbuddy.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.budgetbuddy.entities.Category;

import java.util.List;


/**
 * Data Access Object for the Category Entity. Defines CRUD methods
 */
@Dao
public interface CategoryDAO
{
    /**
     * Get all categories
     * @return a <code>LiveData</code> object containing a list of <code>Category</code> objects
     */
    @Query("SELECT * FROM 'category'")
    LiveData<List<Category>> getAll();

    /**
     * Get a category by its ID
     * @param id the ID of the category
     * @return a <code>Category</code> object
     */
    @Query("SELECT * FROM 'category' WHERE categoryID = (:id)")
    Category getCategoryByID(long id);

    /**
     * Insert a new category into the database
     * @param categories the <code>Category</code> object to insert
     */
    @Insert
    void insertCategory(Category... categories);

    /**
     * Delete a category from the database
     * @param category the <code>Category</code> object to delete
     */
    @Delete
    void delete(Category category);


}
