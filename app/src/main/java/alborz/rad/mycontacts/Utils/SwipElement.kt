package alborz.rad.mycontacts.Utils

import alborz.rad.mycontacts.Activities.MainActivity
import alborz.rad.mycontacts.Adapter.ContactAdapter
import alborz.rad.mycontacts.R
import android.app.AlertDialog
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipElement(
    var adapter: ContactAdapter,
    var activity: MainActivity,
    var viewUpdate: View,
    var viewDelete: View
) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val pos = viewHolder.adapterPosition
        if (direction == ItemTouchHelper.LEFT) {
            var alertDialog: AlertDialog.Builder = AlertDialog.Builder(activity)
            if (viewDelete.parent != null) {
                (viewDelete.parent as ViewGroup).removeView(viewDelete)
            }
            alertDialog.setView(viewDelete)
            val mAlertDialog = alertDialog.create()
            mAlertDialog.show()
            val deleteButton: Button = viewDelete.findViewById(R.id.deleteButton)
            val buttonCancel: Button = viewDelete.findViewById(R.id.saveDeleteDialogButton)
            adapter.notifyDataSetChanged()
            deleteButton.setOnClickListener {
                adapter.deleteItem(pos)
                mAlertDialog.dismiss()
            }
            buttonCancel.setOnClickListener {
                adapter.notifyDataSetChanged()
                mAlertDialog.dismiss()
            }

        } else {
            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(activity)
            if (viewUpdate.parent != null) {
                (viewUpdate.parent as ViewGroup).removeView(viewUpdate)
            }
            alertDialog.setView(viewUpdate)
            val mAlertDialog = alertDialog.create()
            mAlertDialog.show()
            val buttonSave: Button = viewUpdate.findViewById(R.id.buttonSave)
            val buttonCancel: Button = viewUpdate.findViewById(R.id.buttonCancel)

            val dialogEditName: EditText = viewUpdate.findViewById(R.id.dialogName)
            val dialogEditPhone: EditText = viewUpdate.findViewById(R.id.dialogPhone)
            val dialogEditEmail: EditText = viewUpdate.findViewById(R.id.dialogEmail)

            val contactModel: ContactModel = adapter.getModel(viewHolder.adapterPosition)
            dialogEditName.hint = contactModel.name
            dialogEditPhone.hint = contactModel.phone
            if (contactModel.email.length == 0) {
                dialogEditEmail.hint = "example@email.com"
            } else {
                dialogEditEmail.hint = contactModel.email
            }
            adapter.notifyDataSetChanged()
            buttonSave.setOnClickListener {

                if (!dialogEditName.text.toString()
                        .startsWith(" ") && dialogEditName.text.toString().length != 0
                ) {
                    contactModel.name = dialogEditName.text.toString()
                }
                if (!dialogEditPhone.text.toString()
                        .startsWith(" ") && dialogEditPhone.text.toString().length != 0
                ) {
                    contactModel.phone = dialogEditPhone.text.toString()
                }
                if (!dialogEditEmail.text.toString()
                        .startsWith(" ") && dialogEditEmail.text.toString().length != 0
                ) {
                    contactModel.email = dialogEditEmail.text.toString()
                }
                adapter.editItem(contactModel, pos)


                dialogEditName.text.clear()
                dialogEditPhone.text.clear()
                dialogEditEmail.text.clear()

                mAlertDialog.dismiss()
            }


            buttonCancel.setOnClickListener {
                adapter.notifyDataSetChanged()
                mAlertDialog.dismiss()
            }
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        var icon: Drawable
        var backGround: ColorDrawable
        var view: View = viewHolder.itemView
        var backGroundCornerOffset: Int = 20

        if (dX > 0) {
            icon = ContextCompat.getDrawable(activity, R.drawable.ic_baseline_edit_24)!!
            backGround = ColorDrawable(ContextCompat.getColor(activity, R.color.green))
        } else {
            icon = ContextCompat.getDrawable(activity, R.drawable.ic_baseline_delete_24)!!
            backGround = ColorDrawable(ContextCompat.getColor(activity, R.color.red))
        }
        var iconMargin: Int = (view.height - icon.intrinsicHeight) / 2
        var iconTop: Int = view.top + iconMargin
        var iconBottom: Int = iconTop + icon.intrinsicHeight

        if (dX > 0) {
            var iconLeft: Int = view.left + iconMargin
            var iconRight: Int = view.left + iconMargin + icon.intrinsicWidth
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            backGround.setBounds(
                view.left,
                view.top,
                view.left + (dX.toInt()) + backGroundCornerOffset,
                view.bottom
            )
        } else if (dX < 0) {
            var iconLeft: Int = view.right - iconMargin - icon.intrinsicWidth
            var iconRight: Int = view.right - iconMargin
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            backGround.setBounds(
                view.right + (dX.toInt()) - backGroundCornerOffset,
                view.top,
                view.right,
                view.bottom
            )
        } else {
            backGround.setBounds(0, 0, 0, 0)
        }
        backGround.draw(c)
        icon.draw(c)
    }
}