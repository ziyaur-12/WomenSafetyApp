package com.example.womensafetyapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.womensafetyapp.R
import com.example.womensafetyapp.databinding.ActivityContactsBinding
import com.example.womensafetyapp.model.Contact
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ContactsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactsBinding
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference
    private val contactsList = mutableListOf<Contact>()
    private lateinit var adapter: ContactsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ContactsAdapter(contactsList) { contact ->
            deleteContact(contact)
        }
        binding.rvContacts.layoutManager = LinearLayoutManager(this)
        binding.rvContacts.adapter = adapter

        binding.btnAddContact.setOnClickListener {
            val name = binding.etContactName.text.toString()
            val phone = binding.etContactPhone.text.toString()

            if (name.isNotEmpty() && phone.isNotEmpty()) {
                addContact(name, phone)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        fetchContacts()
    }

    private fun addContact(name: String, phone: String) {
        val userId = auth.currentUser?.uid ?: return
        val contactId = database.child("contacts").child(userId).push().key ?: return
        val contact = Contact(contactId, name, phone)
        
        database.child("contacts").child(userId).child(contactId).setValue(contact)
            .addOnSuccessListener {
                binding.etContactName.text?.clear()
                binding.etContactPhone.text?.clear()
                Toast.makeText(this, "Contact added", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchContacts() {
        val userId = auth.currentUser?.uid ?: return
        database.child("contacts").child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                contactsList.clear()
                for (contactSnap in snapshot.children) {
                    val contact = contactSnap.getValue(Contact::class.java)
                    if (contact != null) contactsList.add(contact)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun deleteContact(contact: Contact) {
        val userId = auth.currentUser?.uid ?: return
        database.child("contacts").child(userId).child(contact.id).removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Contact deleted", Toast.LENGTH_SHORT).show()
            }
    }

    class ContactsAdapter(
        private val contacts: List<Contact>,
        private val onDelete: (Contact) -> Unit
    ) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvName: TextView = view.findViewById(android.R.id.text1)
            val tvPhone: TextView = view.findViewById(android.R.id.text2)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_2, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val contact = contacts[position]
            holder.tvName.text = contact.name
            holder.tvPhone.text = contact.phoneNumber
            holder.itemView.setOnLongClickListener {
                onDelete(contact)
                true
            }
        }

        override fun getItemCount() = contacts.size
    }
}
