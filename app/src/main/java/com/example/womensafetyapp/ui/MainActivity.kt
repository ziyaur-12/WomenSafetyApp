package com.example.womensafetyapp.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.SensorManager
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.womensafetyapp.R
import com.example.womensafetyapp.services.LocationService
import com.example.womensafetyapp.utils.ShakeDetector
import com.google.android.gms.location.LocationServices
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private var shakeDetector: ShakeDetector? = null
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Safety check: if not logged in, go back to Login
        if (auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setTheme(androidx.appcompat.R.style.Theme_AppCompat_Light_NoActionBar)
        setContentView(R.layout.activity_main)

        // Find views using ID
        val btnSos = findViewById<MaterialButton>(R.id.btnSos)
        val cardContacts = findViewById<MaterialCardView>(R.id.cardContacts)
        val cardMaps = findViewById<MaterialCardView>(R.id.cardMaps)
        val cardFakeCall = findViewById<MaterialCardView>(R.id.cardFakeCall)
        val cardLogout = findViewById<MaterialCardView>(R.id.cardLogout)

        btnSos?.setOnClickListener { triggerSOS() }
        cardContacts?.setOnClickListener { 
            startActivity(Intent(this, ContactsActivity::class.java)) 
        }
        cardMaps?.setOnClickListener { 
            startActivity(Intent(this, MapsActivity::class.java)) 
        }
        cardFakeCall?.setOnClickListener { 
            startActivity(Intent(this, FakeCallActivity::class.java)) 
        }
        cardLogout?.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        setupShakeDetection()
        requestAllPermissions()
    }

    private fun setupShakeDetection() {
        try {
            val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            shakeDetector = ShakeDetector {
                triggerSOS()
                Toast.makeText(this, "Shake Detected! SOS Triggered", Toast.LENGTH_SHORT).show()
            }
            shakeDetector?.start(sensorManager)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun triggerSOS() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                val locLink = if (location != null) 
                    "http://maps.google.com/maps?q=${location.latitude},${location.longitude}"
                else "Location unavailable"
                
                sendSmsToContacts("I am in danger! My location: $locLink")
            }
        } else {
            Toast.makeText(this, "Location permission required for SOS", Toast.LENGTH_SHORT).show()
            requestAllPermissions()
        }
    }

    private fun sendSmsToContacts(message: String) {
        val userId = auth.currentUser?.uid ?: return
        database.child("contacts").child(userId).get().addOnSuccessListener { snapshot ->
            if (snapshot.childrenCount == 0L) {
                Toast.makeText(this, "No contacts found. Please add emergency contacts!", Toast.LENGTH_LONG).show()
                return@addOnSuccessListener
            }
            for (contactSnap in snapshot.children) {
                val phone = contactSnap.child("phoneNumber").value.toString()
                try {
                    val smsManager = SmsManager.getDefault()
                    smsManager.sendTextMessage(phone, null, message, null, null)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            Toast.makeText(this, "SOS Messages Sent!", Toast.LENGTH_LONG).show()
        }
    }

    private fun requestAllPermissions() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.SEND_SMS,
            Manifest.permission.POST_NOTIFICATIONS
        )
        val toRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (toRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, toRequest.toTypedArray(), 100)
        } else {
            // Start location service only if permissions are already there
            startLocationService()
        }
    }

    private fun startLocationService() {
        try {
            val intent = Intent(this, LocationService::class.java)
            startForegroundService(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        shakeDetector?.stop()
        super.onDestroy()
    }
}
