package alborz.rad.mycontacts.Activities

import alborz.rad.mycontacts.Adapter.ContactAdapter
import alborz.rad.mycontacts.R
import alborz.rad.mycontacts.Utils.ContactModel
import alborz.rad.mycontacts.Utils.DataBaseHelper
import alborz.rad.mycontacts.Utils.OnClickListener
import alborz.rad.mycontacts.Utils.SwipElement
import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity(), OnClickListener {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ContactAdapter
    lateinit var contactList: MutableList<ContactModel>
    lateinit var db: DataBaseHelper
    lateinit var viewUpdate: View
    lateinit var viewDelete: View
    lateinit var searchBar: EditText

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        var intent: Intent = Intent(this, AddNewContact::class.java)
        var addNewContactButton: FloatingActionButton = findViewById(R.id.newContactAddButton)
        addNewContactButton.setOnClickListener {
            startActivity(intent)
        }

        searchBar = findViewById(R.id.searchBar)
        recyclerView = findViewById(R.id.recycelerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        contactList = ArrayList()
        db = DataBaseHelper(this)
        db.openDataBase()
        adapter = ContactAdapter(this, contactList, db)
        recyclerView.adapter = adapter
        contactList = db.getAllList()

        viewUpdate = layoutInflater.inflate(R.layout.update_dialog, null, false)
        viewDelete = layoutInflater.inflate(R.layout.delete_dialog, null, false)

        var itemTouchHelper: ItemTouchHelper =
            ItemTouchHelper(SwipElement(adapter, this, viewUpdate, viewDelete))
        itemTouchHelper.attachToRecyclerView(recyclerView)
        adapter.notifyDataSetChanged()


        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                filteredList(p0.toString())
            }

        })


    }

    fun filteredList(text:String) {
        var filteredList:MutableList<ContactModel> = ArrayList()
        var i:Int = 0
        while(i<contactList.size) {
            if(contactList.get(i).name.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(contactList.get(i))
            }
            i++
        }
        adapter.filteredList(filteredList)
    }

    override fun onClickPhone(position: Int) {
        var contactModel: ContactModel = contactList.get(position)
        var intent = Intent(Intent.ACTION_CALL)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 101)
        } else {
            intent.data = Uri.parse("tel:" + contactModel.phone)
            Log.e("2121", contactModel.phone)
            startActivity(intent)
        }
    }

    override fun onClickSms(position: Int) {
        var contactModel: ContactModel = contactList.get(position)
        var intent = Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", contactModel.phone, null))
        intent.putExtra("sms body", "")
        startActivity(intent)
    }


    override fun onResume() {
        super.onResume()
        contactList = db.getAllList()
        adapter.contactList = contactList
        adapter.notifyDataSetChanged()
    }

}




