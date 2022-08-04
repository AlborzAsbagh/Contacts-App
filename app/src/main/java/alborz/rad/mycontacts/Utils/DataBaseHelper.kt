package alborz.rad.mycontacts.Utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper(
    context: Context?,
    name: String = "ContactsDataBase",
    factory: SQLiteDatabase.CursorFactory? = null,
    version: Int = 1
) : SQLiteOpenHelper(context, name, factory, version) {

    lateinit var db: SQLiteDatabase
    var ID: String = "id"
    var NAME: String = "name"
    var PHONE: String = "phone"
    var EMAIL: String = "email"
    var CONTACT_TABLE_NAME = "contactsTable"
    var CREATE_TABLE_SCRIPT =
        "CREATE TABLE " + CONTACT_TABLE_NAME + "( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                NAME + " TEXT," + PHONE + " TEXT," + EMAIL + " TEXT )"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_SCRIPT)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + CONTACT_TABLE_NAME);
        onCreate(db)
    }

    fun openDataBase() {
        db = writableDatabase
    }

    fun insertContact(contactModel: ContactModel) {
        var cv: ContentValues = ContentValues()
        cv.put(NAME, contactModel.name)
        cv.put(PHONE, contactModel.phone)
        cv.put(EMAIL, contactModel.email)
        db.insert(CONTACT_TABLE_NAME, null, cv)

    }

    fun getAllList(): MutableList<ContactModel> {
        var contactList: MutableList<ContactModel> = ArrayList()
        var cr: Cursor
        db.beginTransaction()
        cr = db.query(CONTACT_TABLE_NAME, null, null, null, null, null, null)
        try {
            if (cr != null) {
                if (cr.moveToFirst()) {
                    do {
                        var contactModel: ContactModel = ContactModel(
                            cr.getString(Math.abs(cr.getColumnIndex(NAME))),
                            cr.getString(Math.abs(cr.getColumnIndex(PHONE))),
                        )
                        contactModel.id = cr.getInt(Math.abs(cr.getColumnIndex(ID)))
                        contactModel.email = cr.getString(Math.abs(cr.getColumnIndex(EMAIL)))
                        contactList.add(contactModel)
                    } while (cr.moveToNext())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
            cr.close()
        }
        return contactList
    }

    fun updateName(id: Int, name: String) {
        var cv:ContentValues = ContentValues()
        cv.put(NAME,name)
        db.update(CONTACT_TABLE_NAME,cv,ID+" =?", arrayOf(id.toString()))
    }

    fun updatePhone(id: Int, phone: String) {
        var cv:ContentValues = ContentValues()
        cv.put(PHONE,phone)
        db.update(CONTACT_TABLE_NAME,cv,ID+" =? ", arrayOf(id.toString()))
    }

    fun updateEmail(id: Int, email: String) {
        var cv:ContentValues = ContentValues()
        cv.put(EMAIL,email)
        db.update(CONTACT_TABLE_NAME,cv,ID+" =? ", arrayOf(id.toString()))
    }

    fun deleteItem(id:Int){
        db.delete(CONTACT_TABLE_NAME,ID+" =? ",arrayOf(id.toString()))
    }


}