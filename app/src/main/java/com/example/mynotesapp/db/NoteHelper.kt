package com.example.mynotesapp.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.mynotesapp.db.DatabaseContract.NoteColumns.Companion.DATE
import com.example.mynotesapp.db.DatabaseContract.NoteColumns.Companion.DESCRIPTION
import com.example.mynotesapp.db.DatabaseContract.NoteColumns.Companion.TABLE_NAME
import com.example.mynotesapp.db.DatabaseContract.NoteColumns.Companion.TITLE
import com.example.mynotesapp.db.DatabaseContract.NoteColumns.Companion._ID
import com.example.mynotesapp.entity.Note
import java.sql.SQLException

class NoteHelper(context: Context) {

    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: NoteHelper? = null

        fun getInstance(context: Context): NoteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: NoteHelper(context)
            }
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    // fungsi untuk mengambil data dari semua note yang da di dalam database
    //return cursor hasil queryAll
    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC"
        )
    }

    // fungsi utk mengambil data dengan id tertenu
    // param id id note yang dicari
    // return cursor hasil queryAll
    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$_ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }

    // fun utk menyimpan data ke dalam database
    // param vales nilai data yang akan disimpan
    // return long id dari data yang baru saja di masukkan
    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    //fun untuk update data
    //param id   data dengan id berapa yang akan di update
    //param values nilai data baru
    //return int jumlah data yang terupdate
    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$_ID = ?", arrayOf(id))
    }

    //fun utk menghapus data di dalam database
    //param id data dengan id berapa yang akan di delete
    //return int jumlah data yang terhapus
    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$_ID = '$id'", null)
    }

    // gunakan method ini untuk ambil semua note yang ada
    // otomatis di parsing ke dalam model Note
    // return hasil getGetAllNotes berbentuk array model note
    fun getAllNotes(): ArrayList<Note> {
        val arrayList = ArrayList<Note>()
        val cursor = database.query(DATABASE_TABLE, null, null, null, null, null,
            "$_ID ASC", null)
        cursor.moveToFirst()
        var note: Note
        if (cursor.count > 0) {
            do {
                note = Note()
                note.id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID))
                note.title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE))
                note.descrription = cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION))
                note.date = cursor.getString(cursor.getColumnIndexOrThrow(DATE))

                arrayList.add(note)
                cursor.moveToNext()

            } while (!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

    // gunakan method ini untuk insertNote
    // param note model note yang akan dimasukkan
    // return id dari data yang baru saja dimasukan
    fun insertNotes(note: Note): Long {
        val args = ContentValues()
        args.put(TITLE, note.title)
        args.put(DESCRIPTION, note.descrription)
        args.put(DATE, note.date)
        return database.insert(DATABASE_TABLE, null, args)
    }

    // gunakan method untuk updateNote
    // param note model note yang akan diubah
    // return int jumlah dari row yang ter-updateNote, jika tidak ada yang diupdate maka nilainya 0
    fun updateNotes(note: Note): Int {
        val args = ContentValues()
        args.put(TITLE, note.title)
        args.put(DESCRIPTION, note.descrription)
        args.put(DATE, note.date)
        return database.update(DATABASE_TABLE, args, _ID + "= '" + note.id + "'", null)
    }

    fun deleteNote(id: Int): Int {
        return database.delete(TABLE_NAME,"$_ID = '$id", null)
    }
}
























