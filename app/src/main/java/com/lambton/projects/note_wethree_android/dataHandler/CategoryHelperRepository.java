package com.lambton.projects.note_wethree_android.dataHandler;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.lambton.projects.note_wethree_android.dataHandler.dao.CategoryDataInterface;
import com.lambton.projects.note_wethree_android.dataHandler.entity.Category;

import java.util.List;
import java.util.concurrent.ExecutionException;

/*
    Helper class for performing operations on Category Data
    Method signatures:

    public void insertNewCategoryInDatabase(Category category);

    public void deleteCategoryFromDatabase(Category category);

    public List<Category> getAllCategories();

 */


public class CategoryHelperRepository {

    private CategoryDataInterface categoryDataInterface;
    public CategoryHelperRepository(Application application) {
        NoteDatabase noteDatabase = NoteDatabase.getInstance(application);
        categoryDataInterface = noteDatabase.categoryDataInterface();

    }
//    category operations
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void insertNewCategoryInDatabase(Category category) {
        if(category == null) {
            new NullPointerException();
        }
        else {
            new CategoryHelperRepository.InsertCategory(categoryDataInterface).execute(category);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Category> getAllCategories() {
        List<Category> categoryList = null;
        try {
            categoryList = new CategoryHelperRepository.FetchAllCategories(categoryDataInterface).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return categoryList;
    }

    public void deleteCategoryFromDatabase(Category category) {
        new CategoryHelperRepository.DeleteCategory(categoryDataInterface).execute(category);
    }

//    async classes for operations

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static class InsertCategory extends AsyncTask<Category, Void, Void> {

        private CategoryDataInterface categoryDataInterface;

        private InsertCategory(CategoryDataInterface categoryDataInterface) {
            this.categoryDataInterface = categoryDataInterface;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            categoryDataInterface.insertCategory(categories[0]);
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static class FetchAllCategories extends AsyncTask<Void, Void, List<Category>> {

        private CategoryDataInterface categoryDataInterface;

        private FetchAllCategories(CategoryDataInterface categoryDataInterface) {
            this.categoryDataInterface = categoryDataInterface;
        }

        @Override
        protected List<Category> doInBackground(Void... voids) {
            return categoryDataInterface.loadAllCategories();
        }
    }

    private static class DeleteCategory extends AsyncTask<Category, Void, Void> {

        private CategoryDataInterface categoryDataInterface;

        private DeleteCategory(CategoryDataInterface categoryDataInterface) {
            this.categoryDataInterface = categoryDataInterface;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            categoryDataInterface.deleteCategory(categories[0]);
            return null;
        }
    }


}
