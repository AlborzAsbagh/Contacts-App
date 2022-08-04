package alborz.rad.mycontacts.Activities


import alborz.rad.mycontacts.R
import alborz.rad.mycontacts.Utils.ContactModel
import alborz.rad.mycontacts.Utils.DataBaseHelper
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast


class AddNewContact : AppCompatActivity() {
    lateinit var inputName: EditText
    lateinit var inputPhone: EditText
    lateinit var inputEmail: EditText
    lateinit var saveButton: Button
    lateinit var db: DataBaseHelper
    lateinit var profImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_contact)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        inputName = findViewById(R.id.inputName)
        inputPhone = findViewById(R.id.inputPhone)
        inputEmail = findViewById(R.id.inputEmail)
        saveButton = findViewById(R.id.saveButton)
        db = DataBaseHelper(this)
        db.openDataBase()
        profImage = findViewById(R.id.profile_image)


        var isNameFull = false
        var isPhoneFull = false
        saveButton.setOnClickListener {
            if (!inputName.text.toString().startsWith(" ") && inputName.text.toString().length != 0)
                isNameFull = true
            if (!inputPhone.text.toString()
                    .startsWith(" ") && inputPhone.text.toString().length != 0
            )
                isPhoneFull = true
            if (isNameFull && isPhoneFull) {
                var contactModel: ContactModel = ContactModel(
                    inputName.text.toString(),
                    inputPhone.text.toString(),
                )
                if (inputEmail.text.toString().length != 0 && !inputEmail.text.toString()
                        .startsWith(" ")
                ) {
                    contactModel.email = inputEmail.text.toString()
                }
                db.insertContact(contactModel)
                finish()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Can not save without name or number",
                    Toast.LENGTH_SHORT
                ).show();
            }
        }

    }
}




