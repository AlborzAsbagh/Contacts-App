package alborz.rad.mycontacts.Utils


import android.net.Uri
import kotlin.properties.Delegates

class ContactModel(
    var name: String,
    var phone: String,

) {
    var id by Delegates.notNull<Int>()
    var imageUri: Uri? = null
    var email:String = ""
}