package com.lambton.projects.note_wethree_android.dataHandler;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.lambton.projects.note_wethree_android.dataHandler.dao.CategoryDataInterface;
import com.lambton.projects.note_wethree_android.dataHandler.entity.Category;
import com.lambton.projects.note_wethree_android.dataHandler.entity.Note;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NoteRepository {

    private CategoryDataInterface categoryDataInterface;
    private LiveData<List<Category>> categoryList;
    public NoteRepository(Application application) {
        NoteDatabase noteDatabase = NoteDatabase.getInstance(application);
        categoryDataInterface = noteDatabase.categoryDataInterface();
        categoryList = categoryDataInterface.loadAllCategories();
    }
//    category operations
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void insertNewCategoryInDatabase(Category category) {
        if(category == null) {
            new NullPointerException();
        }
        else {
            new InsertCategory(categoryDataInterface).execute(category);
        }
    }

    public void delteCategoryFromDatabase(Category category) {
        new DeleteCategory(categoryDataInterface).execute(category);
    }

    public LiveData<List<Category>> getCategoryList() {
        return categoryList;
    }

//    async methods for operations
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


//    note operations
    public void insertNoteForCategoryInDatabase(Note note, int categoryId) {
        if(note == null) {
            new NullPointerException();
        }
        else {
            categoryDataInterface.insertNote(note);
        }
    }



}
