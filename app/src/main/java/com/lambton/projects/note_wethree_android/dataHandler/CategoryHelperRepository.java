package com.lambton.projects.note_wethree_android.dataHandler;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.lambton.projects.note_wethree_android.dataHandler.dao.CategoryDataInterface;
import com.lambton.projects.note_wethree_android.dataHandler.dao.NoteDataInterface;
import com.lambton.projects.note_wethree_android.dataHandler.entity.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/*
    Helper class for performing operations on Category Data
    Method signatures:

    public void insertNewCategoryInDatabase(Category category);

    public void deleteCategoryFromDatabase(Category category);

    public List<Category> getAllCategories();

    public List<Category> getAllCategoriesSortedByTitle()

    public int getNoteCountForCategory(int categoryId)

 */


public class CategoryHelperRepository {

    private CategoryDataInterface categoryDataInterface;
    private NoteDataInterface noteDataInterface;

    public CategoryHelperRepository(Application application) {
        NoteDatabase noteDatabase = NoteDatabase.getInstance(application);
        categoryDataInterface = noteDatabase.categoryDataInterface();
        noteDataInterface = noteDatabase.noteDataInterface();
    }
//    category operations
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void insertNewCategoryInDatabase(Category category) {
        if(category == null) {
            Log.i("Category insertion error!", "Trying to set null value");
        }
        else {
            new CategoryHelperRepository.InsertCategory(categoryDataInterface).execute(category);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        try {
            categoryList = new CategoryHelperRepository.FetchAllCategories(categoryDataInterface).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return categoryList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Category> getAllCategoriesSortedByTitle() {
        List<Category> categoryList = new ArrayList<>();
        try {
            categoryList = new CategoryHelperRepository.FetchAllCategoriesSortedByTitle(categoryDataInterface).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return categoryList;
    }

    public int getNoteCountForCategory(int categoryId) {
        int noteCount = 0;
        try {
            noteCount =  new CategoryHelperRepository.FetchNoteCountForCategory(noteDataInterface).execute(categoryId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return noteCount;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static class FetchAllCategoriesSortedByTitle extends AsyncTask<Void, Void, List<Category>> {

        private CategoryDataInterface categoryDataInterface;

        private FetchAllCategoriesSortedByTitle(CategoryDataInterface categoryDataInterface) {
            this.categoryDataInterface = categoryDataInterface;
        }

        @Override
        protected List<Category> doInBackground(Void... voids) {
            return categoryDataInterface.loadAllCategoriesSortedByTitle();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static class FetchNoteCountForCategory extends AsyncTask<Integer, Void, Integer> {

        private NoteDataInterface noteDataInterface;

        private FetchNoteCountForCategory(NoteDataInterface noteDataInterface) {
            this.noteDataInterface = noteDataInterface;
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            return noteDataInterface.getNoteCountForCategory(integers[0]);
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
