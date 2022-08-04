package alborz.rad.mycontacts.Adapter

import alborz.rad.mycontacts.Activities.MainActivity
import alborz.rad.mycontacts.R
import alborz.rad.mycontacts.Utils.ContactModel
import alborz.rad.mycontacts.Utils.DataBaseHelper
import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ContactAdapter(
    activity: Activity,
    contactList: MutableList<ContactModel>,
    db: DataBaseHelper
) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    var activity: Activity
    var contactList: MutableList<ContactModel>
    var db: DataBaseHelper

    init {
        this.activity = activity
        this.contactList = contactList
        this.db = db
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.contact_sample, parent, false)
        return ContactAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var contactModel: ContactModel = contactList.get(position)
        holder.name.text = contactModel.name
        holder.phoneImg.setOnClickListener {
            (activity as MainActivity).onClickPhone(position)
        }
        holder.smsImg.setOnClickListener {
            (activity as MainActivity).onClickSms(position)
        }
    }

    override fun getItemCount(): Int {
        return contactList.size
    }
    fun filteredList(filteredList:MutableList<ContactModel>) {
        contactList = filteredList
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        db.deleteItem(contactList.get(position).id)
        contactList.remove(contactList.get(position))
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    fun editItem(contactModel: ContactModel, position: Int) {
        db.updateName(contactList.get(position).id, contactModel.name)
        db.updatePhone(contactList.get(position).id, contactModel.phone)
        db.updateEmail(contactList.get(position).id, contactModel.email)
        contactList.set(position, contactModel)
        notifyItemChanged(position)
        notifyDataSetChanged()
    }

    fun getModel(position: Int): ContactModel {
        return contactList.get(position)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.contactName)
        var phoneImg: ImageView = view.findViewById(R.id.phoneCall)
        var smsImg: ImageView = view.findViewById(R.id.sms)
    }
}