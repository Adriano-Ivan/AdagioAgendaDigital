package br.com.adagio.adagioagendadigital.data.tag;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.adagio.adagioagendadigital.data.DbLayer;
import br.com.adagio.adagioagendadigital.data.task.DbTaskStructure;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoCreate;
import br.com.adagio.adagioagendadigital.models.dto.task.TaskDtoRead;
import br.com.adagio.adagioagendadigital.models.entities.Tag;

public class TagDAO {

    private static TagDAO instance;

    private SQLiteDatabase db;

    private TagDAO(Context context){
        DbLayer dbLayer = DbLayer.getInstance(context);

        db = dbLayer.getReadableDatabase();
    }

    public static TagDAO getInstance(Context context){
        if (instance == null) {
            instance = new TagDAO(context);
        }

        return instance;
    }

    public List<Tag> list(int limit, int offset){

        List<Tag> tags = new ArrayList<>();

        String query = String.format("SELECT * FROM %s LIMIT %s OFFSET %s;",
                DbTagStructure.TABLE_NAME, limit, offset);

        try(Cursor c = db.rawQuery(query, null)){

            if(c.moveToFirst()){
                do {
                    Tag tag = fromCursor(c);
                    tags.add(tag);
                }while(c.moveToNext());
            }

            return tags;
        }

    }

    private static Tag fromCursor(Cursor c){
        @SuppressLint("Range") int id = c.getInt(c.getColumnIndex(DbTagStructure.Columns.ID));
        @SuppressLint("Range") String name = c.getString(c.getColumnIndex(DbTagStructure.Columns.NAME));

        return new Tag(id,name);
    }

    public void save(Tag tag) {
        ContentValues values = new ContentValues();
        values.put(DbTagStructure.Columns.NAME, tag.getName());

        long id = db.insert(DbTagStructure.TABLE_NAME, null, values);
    }

    public Tag get(int id){
        String query = String.format("SELECT * FROM %s WHERE %s = %s;",
                DbTagStructure.TABLE_NAME,DbTagStructure.Columns.ID, id);

        Tag tag = null;
        try(Cursor c = db.rawQuery(query, null)){

            if(c.moveToFirst()){
                do {
                    tag = fromCursor(c);

                }while(c.moveToNext());
            }

        }

        return tag;
    }

    public void delete(long id){
        db.delete(DbTagStructure.TABLE_NAME,String.format(
                "%s = %s", DbTagStructure.Columns.ID,
                id
        ) ,null);
    }

    public void update(int id, Tag tag) {
        ContentValues values = new ContentValues();
        values.put(DbTagStructure.Columns.NAME, tag.getName());

        db.update(DbTagStructure.TABLE_NAME,values,
                DbTagStructure.Columns.ID + " = ?",
                new String[] {String.valueOf(id)});
    }
}
