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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.oneparchy.simplecontacts.models.Contact

class EditContactActivity : AppCompatActivity() {
    private companion object {
        private const val TAG="EditContactActivity"
        private val currentUser = Firebase.auth.currentUser!!
    }

    //declare layout views
    private lateinit var etName: TextView
    private lateinit var etPhone1: TextView
    private lateinit var etPhone2: TextView
    private lateinit var etEmail: TextView
    private lateinit var etAddress: TextView
    private lateinit var etGender: TextView

    //Initialize cloud firestore db, current user, and contacts collection reference
    private val db = Firebase.firestore
    private val collectionRef = db.collection("users").document(currentUser.uid).collection("contacts")
    //associated document reference (contact ID) will be pulled in (or created) later
    private lateinit var docRef: DocumentReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_contact)
        //bind views in layout
        etName = findViewById(R.id.etName)
        etPhone1 = findViewById(R.id.etPhone1)
        etPhone2 = findViewById(R.id.etPhone2)
        etEmail = findViewById(R.id.etEmail)
        etAddress = findViewById(R.id.etAddress)
        etGender = findViewById(R.id.etGender)
        //Are we creating a new contact?
        val isNew = intent.getBooleanExtra("new",false)
        //If not a new contact, fetch data from firebase & populate views
        if (!isNew) {
            docRef = collectionRef.document(intent.getStringExtra("contact_id")!!)      //assert non-null since contact is not new
            docRef.get().addOnSuccessListener { documentSnapshot ->
                val contact = documentSnapshot.toObject<Contact>()!!
                Log.i(TAG, "Contact get success: $contact")
                //bind data from firestore to view
                etName.text = contact.name
                etPhone1.text = contact.phone1
                etPhone2.text = contact.phone2
                etEmail.text = contact.email
                etAddress.text = contact.address
                etGender.text = contact.gender
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Contact get failed with ", exception)
            }
        }
    }

    //populate menu options for this view
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit, menu)
        //Only show & enable delete button if contact is not new
        val deleteBtn = menu?.findItem(R.id.miDelete)
        //Is this a new contact?
        val isNew = intent.getBooleanExtra("new",false)
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
        //Is this a new contact?
        val isNew = intent.getBooleanExtra("new",false)
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
                //create new document (contact) if contact is new
                //do this explicitly when saving, so if the app is closed before saving it does not leave a blank contact in firebase
                if (isNew) {
                    docRef = collectionRef.document()
                }
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
                    docRef.delete().addOnSuccessListener { Log.d(TAG, "Existing contact successfully deleted") }
                        .addOnFailureListener { e -> Log.w(TAG, "Error deleting existing contact", e) }
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