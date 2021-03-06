package com.lambton.projects.note_wethree_android.dataHandler;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;


import com.lambton.projects.note_wethree_android.dataHandler.dao.NoteDataInterface;
import com.lambton.projects.note_wethree_android.dataHandler.entity.Note;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ExecutionException;

/*
    Helper class for performing operations on Note Data
    Method signatures:

    <Basic operations>
    public void insertNewNoteInDatabase(Note note);
    public void updateNoteInDatabase(Note note);
    public void deleteNoteFromDatabase(Note note);

    <fetch operations>
     public List<Note> getNotesForCategory(int categoryId);
     public List<Note> fetchSortedNotesByDate(int categoryId);
     public List<Note> fetchSortedNotesByName(int categoryId);

     <other operations>
     public void moveNotesToCategory(List<Note> noteList, int categoryId);
     public List<Note> searchNotesByKeyword(String searchString, int categoryId);

 */


public class NoteHelperRepository {
    private NoteDataInterface noteDataInterface;

    public NoteHelperRepository(Application application) {
        NoteDatabase noteDatabase = NoteDatabase.getInstance(application);
        noteDataInterface = noteDatabase.noteDataInterface();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void insertNewNoteInDatabase(Note note) {
        if(note == null) {
            Log.i("Note insertion error!","Trying to set null value");
        }
        else {
            new NoteHelperRepository.InsertNote(noteDataInterface).execute(note);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateNoteInDatabase(Note note) {
        if(note == null) {
            Log.i("Note updation error!","Trying to set null value");
        }
        else {
            new NoteHelperRepository.UpdateNote(noteDataInterface).execute(note);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteNoteFromDatabase(Note note) {
        new NoteHelperRepository.DeleteNote(noteDataInterface).execute(note);
    }

    public List<Note> getNotesForCategory(int categoryId) {
        List<Note> noteList = new ArrayList<>();
        try {
            noteList = new FetchNotes(noteDataInterface).execute(categoryId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return noteList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void moveNotesToCategory(List<Note> noteList, int categoryId) {
        if(noteList == null) {
            new NullPointerException();
        }
        else {
            for(Note note: noteList) {
                note.setNoteCategoryId(categoryId);
                new NoteHelperRepository.UpdateNote(noteDataInterface).execute(note);
            }
        }
    }

    public List<Note> searchNotesByKeyword(String searchString, int categoryId) {
        List<Note> noteList = new ArrayList<>();
        if(searchString != null) {
            String string = "%" + searchString + "%";
            Dictionary<String, Integer> sendQuery = new Hashtable();
            sendQuery.put(string, categoryId);
            try {
                noteList = new NoteHelperRepository.SearchNotes(noteDataInterface).execute(sendQuery).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return noteList;
    }

    public List<Note> fetchSortedNotesByDate(int categoryId) {
        List<Note> noteList = new ArrayList<>();
        try {
            noteList =  new SortNotesByDate(noteDataInterface).execute(categoryId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return noteList;
    }

    public List<Note> fetchSortedNotesByName(int categoryId) {
        List<Note> noteList = new ArrayList<>();
        try {
            noteList =  new SortNotesByName(noteDataInterface).execute(categoryId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return noteList;
    }


    //    async classes for operations

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static class InsertNote extends AsyncTask<Note, Void, Void> {

        private NoteDataInterface noteDataInterface;

        private InsertNote(NoteDataInterface noteDataInterface) {
            this.noteDataInterface = noteDataInterface;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDataInterface.insertNote(notes[0]);
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static class UpdateNote extends AsyncTask<Note, Void, Void> {

        private NoteDataInterface noteDataInterface;

        private UpdateNote(NoteDataInterface noteDataInterface) {
            this.noteDataInterface = noteDataInterface;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDataInterface.updateNote(notes[0]);
            return null;
        }
    }

    private static class FetchNotes extends AsyncTask<Integer, Void, List<Note>> {

        private NoteDataInterface noteDataInterface;

        private FetchNotes(NoteDataInterface noteDataInterface) {
            this.noteDataInterface = noteDataInterface;
        }

        @Override
        protected List<Note> doInBackground(Integer... integers) {
            return noteDataInterface.getAllNotesForCategory(integers[0]);
        }
    }

    private static class SortNotesByDate extends AsyncTask<Integer, Void, List<Note>> {

        private NoteDataInterface noteDataInterface;

        private SortNotesByDate(NoteDataInterface noteDataInterface) {
            this.noteDataInterface = noteDataInterface;
        }

        @Override
        protected List<Note> doInBackground(Integer... integers) {
            return noteDataInterface.getSortedNotesByDate(integers[0]);
        }
    }

    private static class SortNotesByName extends AsyncTask<Integer, Void, List<Note>> {

        private NoteDataInterface noteDataInterface;

        private SortNotesByName(NoteDataInterface noteDataInterface) {
            this.noteDataInterface = noteDataInterface;
        }

        @Override
        protected List<Note> doInBackground(Integer... integers) {
            return noteDataInterface.getSortedNotesByName(integers[0]);
        }
    }

    private static class SearchNotes extends AsyncTask<Dictionary<String, Integer>, Void, List<Note>> {

        private NoteDataInterface noteDataInterface;

        private SearchNotes(NoteDataInterface noteDataInterface) {
            this.noteDataInterface = noteDataInterface;
        }

        @Override
        protected List<Note> doInBackground(Dictionary<String, Integer>... dictionaries) {
            String queryString = dictionaries[0].keys().nextElement();
            int categoryId = dictionaries[0].get(queryString);
            return noteDataInterface.getSearchResults(queryString, categoryId);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static class DeleteNote extends AsyncTask<Note, Void, Void> {

        private NoteDataInterface noteDataInterface;

        private DeleteNote(NoteDataInterface noteDataInterface) {
            this.noteDataInterface = noteDataInterface;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDataInterface.deleteNote(notes[0]);
            return null;
        }
    }

}
