package com.oneparchy.simplecontacts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.oneparchy.simplecontacts.models.Contact

class MainActivity : AppCompatActivity() {

    private companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var rvContacts: RecyclerView

    //Access a cloud firestore instance from the activity
    val db = Firebase.firestore
    val currentUser = auth.currentUser!!      //Init Firebase user
    //Init query to  contacts collection under current user & setup Recycler Options
    val query: CollectionReference = db.collection("users").document(currentUser.uid).collection("contacts")
    val options = FirestoreRecyclerOptions.Builder<Contact>().setQuery(query, Contact::class.java)
        .setLifecycleOwner(this).build()

    //create the main activity view and bind the recyclerView adapter (Firebase)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Check back here
        rvContacts = findViewById(R.id.rvContacts)
        val adapter = ContactsAdapter(this,options)
        rvContacts.adapter = adapter
        rvContacts.layoutManager = LinearLayoutManager(this)
    }
    //populate menu items
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    //When user clicks menu item
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.miLogout) {
            Log.i(TAG, "Logout")
            //Logout current user
            auth.signOut()
            val logoutIntent = Intent(this, LoginActivity::class.java)
            logoutIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(logoutIntent)
        } else if (item.itemId == R.id.miAdd) {
            //Add a new contact & start EditContactActivity
            val newContact = query.document()       //create new contact ID
            val blankContact = Contact()            //create blank contact
            query.document("$newContact").set(blankContact)   //populate new contact with blank attributes
            val i = Intent(this, EditContactActivity::class.java)
            i.putExtra("contact_id", newContact.toString())
            i.putExtra("new", true)
            startActivity(i)
        }
        return super.onOptionsItemSelected(item)
    }
}