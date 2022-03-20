package com.oneparchy.simplecontacts

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.oneparchy.simplecontacts.models.Contact


class ContactsAdapter(private val context: Context, options: FirestoreRecyclerOptions<Contact>)
    : FirestoreRecyclerAdapter<Contact, ContactsAdapter.ContactViewHolder>(options) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val context = parent.context
        //inflate the views using our contacts model
        val view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int, model: Contact) {
        //bind data from firestore to view
        holder.bind(model)
        //Format & styling
        when (model.gender) {
            "female","Female","F","f" -> holder.tvGender.setTextColor(Color.rgb(227,28,121))
            "male","Male","M","m" -> holder.tvGender.setTextColor(Color.rgb(50,50,222))
            else -> { holder.tvGender.setTextColor(Color.rgb(0,0,0)) }
        }
        if (model.phone1 == "") {
            holder.phoneIcon1.visibility = View.INVISIBLE
        } else {
            holder.phoneIcon1.visibility = View.VISIBLE
        }
        if (model.phone2 == "") {
            holder.phoneIcon2.visibility = View.INVISIBLE
        } else {
            holder.phoneIcon2.visibility = View.VISIBLE
        }
        if (model.email == "") {
            holder.emailIcon.visibility = View.INVISIBLE
        } else {
            holder.emailIcon.visibility = View.VISIBLE
        }
        if (model.address == "") {
            holder.houseIcon.visibility = View.INVISIBLE
        } else {
            holder.houseIcon.visibility = View.VISIBLE
        }
    }

    inner class ContactViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnLongClickListener {
        //grab views in layout
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvPhone1: TextView = itemView.findViewById(R.id.tvPhone1)
        val tvPhone2: TextView = itemView.findViewById(R.id.tvPhone2)
        val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
        val tvGender: TextView = itemView.findViewById(R.id.tvGender)
        val phoneIcon1: ImageView = itemView.findViewById(R.id.phoneicon)
        val phoneIcon2: ImageView = itemView.findViewById(R.id.phoneicon2)
        val emailIcon: ImageView = itemView.findViewById(R.id.mailicon)
        val houseIcon: ImageView = itemView.findViewById(R.id.houseicon)

        //set onLongClickListener when created
        init {
            itemView.setOnLongClickListener(this)
        }

        fun bind(model: Contact) {
            //map view attributes to contact attributes
            tvName.text = model.name
            tvPhone1.text = model.phone1
            tvPhone2.text = model.phone2
            tvEmail.text = model.email
            tvAddress.text = model.address
            tvGender.text = model.gender
        }

        //what happens when user long clicks
        override fun onLongClick(p0: View?): Boolean {
            val docId = snapshots.getSnapshot(position).id
            //Toast.makeText(context, docId,Toast.LENGTH_SHORT).show()
            //Send docId to contacts activity for editing
            val i = Intent(context, EditContactActivity::class.java)
            i.putExtra("contact_id", docId)
            context.startActivity(i)
            return true
        }
    }
}