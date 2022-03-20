package com.oneparchy.simplecontacts

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.oneparchy.simplecontacts.models.Contact

class EditContactActivity : AppCompatActivity() {
    private companion object {
        private const val TAG="EditContactActivity"
    }

    lateinit var etName: TextView
    lateinit var etPhone1: TextView
    lateinit var etPhone2: TextView
    lateinit var etEmail: TextView
    lateinit var etAddress: TextView
    lateinit var etGender: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_contact)

        //populate data from contact ID on create
        val contactId = intent.getStringExtra("contact_id")

        val db = Firebase.firestore
        val currentUser = auth.currentUser!!
        val docRef = db.collection("users").document(currentUser.uid).collection("contacts").document("$contactId")

        docRef.get().addOnSuccessListener { documentSnapshot ->
            val contact = documentSnapshot.toObject<Contact>()!!
            Log.i(TAG, "Contact get success: $contact")
            //grab views in layout
            etName = findViewById(R.id.etName)
            etPhone1 = findViewById(R.id.etPhone1)
            etPhone2 = findViewById(R.id.etPhone2)
            etEmail = findViewById(R.id.etEmail)
            etAddress = findViewById(R.id.etAddress)
            etGender = findViewById(R.id.etGender)
            //bind data from firestore to view
            etName.text = contact.name
            etPhone1.text = contact.phone1
            etPhone2.text = contact.phone2
            etEmail.text = contact.email
            etAddress.text = contact.address
            etGender.text = contact.gender
        }

    }

    //populate menu options for this view
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit, menu)
        //Only show delete button if contact is not new
        val isNew = intent.getBooleanExtra("new",false)
        val deleteBtn = menu?.findItem(R.id.miDelete)
        if (isNew) {
            deleteBtn?.isVisible = false
            deleteBtn?.isEnabled = false
        } else {
            deleteBtn?.isVisible = true
            deleteBtn?.isEnabled = true
        }
        return true
    }

    //when user clicks a menu item
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val contactId = intent.getStringExtra("contact_id")!!
        val isNew = intent.getBooleanExtra("new",false)
        val db = Firebase.firestore
        val currentUser = auth.currentUser!!
        val docRef = db.collection("users").document(currentUser.uid).collection("contacts")
            .document(contactId)
        when (item.itemId) {
            R.id.miSave -> {
                //Save new contact to Firestore & return to MainActivity
                val contact = Contact(
                    etName.text.toString(),
                    etPhone1.text.toString(),
                    etPhone2.text.toString(),
                    etEmail.text.toString(),
                    etAddress.text.toString(),
                    etGender.text.toString()
                )
                docRef.set(contact).addOnSuccessListener { Log.d(TAG, "Contact successfully updated") }
                    .addOnFailureListener { e -> Log.w(TAG, "Error saving contact", e) }
                Toast.makeText(this, "Contact saved", Toast.LENGTH_SHORT).show()
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                finish()
            }
            R.id.miDiscard -> {
                //Discard changes & return to MainActivity
                val dialog: AlertDialog = AlertDialog.Builder(this)
                    .setTitle("Discard Changes?").setNegativeButton("CANCEL", null)
                    .setPositiveButton("OK", null)
                    .show()
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                    if (isNew) {
                        docRef.delete().addOnSuccessListener { Log.d(TAG, "Blank contact successfully deleted") }
                            .addOnFailureListener { e -> Log.w(TAG, "Error deleting blank contact", e) }
                    }
                    Toast.makeText(this, "Changes discarded", Toast.LENGTH_SHORT).show()
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                    dialog.dismiss()
                    finish()
                }
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener {
                    dialog.dismiss()
                }
            }
            R.id.miDelete -> {
                //Delete contact from Firestore & return to MainActivity
                val dialog: AlertDialog = AlertDialog.Builder(this)
                    .setTitle("Delete contact?").setNegativeButton("CANCEL", null)
                    .setPositiveButton("OK", null)
                    .show()
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                    if (!isNew) {
                        docRef.delete().addOnSuccessListener { Log.d(TAG, "Existing contact successfully deleted") }
                            .addOnFailureListener { e -> Log.w(TAG, "Error deleting existing contact", e) }
                    }
                    Toast.makeText(this, "Contact deleted", Toast.LENGTH_SHORT).show()
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                    dialog.dismiss()
                    finish()
                }
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener {
                    dialog.dismiss()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}