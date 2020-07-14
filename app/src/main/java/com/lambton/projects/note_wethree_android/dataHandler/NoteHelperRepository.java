package com.lambton.projects.note_wethree_android.dataHandler;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;


import com.lambton.projects.note_wethree_android.dataHandler.dao.NoteDataInterface;
import com.lambton.projects.note_wethree_android.dataHandler.entity.Note;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class NoteHelperRepository {
    private NoteDataInterface noteDataInterface;
    private LiveData<List<Note>> noteList;

    public NoteHelperRepository(Application application) {
        NoteDatabase noteDatabase = NoteDatabase.getInstance(application);
        noteDataInterface = noteDatabase.noteDataInterface();
        noteList = noteDataInterface.getAllNotes();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void insertNewNoteInDatabase(Note note) {
        if(note == null) {
            new NullPointerException();
        }
        else {
            new NoteHelperRepository.InsertNote(noteDataInterface).execute(note);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateNoteInDatabase(Note note) {
        if(note == null) {
            new NullPointerException();
        }
        else {
            new NoteHelperRepository.UpdateNote(noteDataInterface).execute(note);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void delteNoteFromDatabase(Note note) {
        new NoteHelperRepository.DeleteNote(noteDataInterface).execute(note);
    }

    public List<Note> getNotesForCategory(int categoryId) {
        List<Note> noteList = null;
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

    public LiveData<List<Note>> getNoteList() {
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
            LiveData<List<Note>> noteList = noteDataInterface.getAllNotesForCategory(integers[0].intValue());
            return (List<Note>) noteList;
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
