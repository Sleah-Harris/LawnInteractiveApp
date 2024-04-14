package com.example.lawninteractiveapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.fragment.app.Fragment
import com.example.lawninteractiveapp.databinding.ActivityMainBinding
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    private var CHANNEL_ID = "lawnNotifications"
    private var currNotifIndex = 0
    private val notifTitles = arrayOf(
        "Loud Public Spaces?",
        "Lawnmowers!",
        "Backseat Barking")
    private val notifMessages = arrayOf(
        "We noticed you’ve been working in very talkative spaces! Let’s help quiet the chatter.",
        "Someone is cutting the weeds nearby! We can help you avoid the noise.",
        "Sounds like your furry friend is making a ruckus. Here's how we suggest finding some quiet")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Stops app from appearing in Dark Mode
        //Todo: Add Dark Mode Compatibility
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(initViewBinding(layoutInflater))
        replaceFragment(HomeFragment())

        requestNotificationPermission()

        //Notification Channel Creation
        //Initially set into its own function but that caused errors. Needs to be fixed
        //Creates a notification channel that all notifications are posted through.
        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            notificationManager.createNotificationChannel(channel)
        }

        //Posts notification when user presses button
        //TODO: Replace notification with automatic notifications in future iterations
        binding.notifButton.setOnClickListener{
            Log.d("Main", "Button Clicked")

            currNotifIndex = (currNotifIndex + 1) % notifTitles.size
            val notification = createNotification(
                notifTitles[currNotifIndex], notifMessages[currNotifIndex])

            notificationManager.notify(1, notification.build())
        }

        //Navigation Menu/Fragment Functionality
        binding.bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId){
                R.id.bottom_home -> replaceFragment(HomeFragment())
                R.id.bottom_live_sound -> replaceFragment(LiveSoundFragment())
                R.id.bottom_suggestions -> replaceFragment(SuggestionsFragment())
                R.id.bottom_stats -> replaceFragment(StatsFragment())

                else -> {}
            }
            true
        }
    }

    /**
     * Creates a notification with necessary parameters
     * Rotates through notification options each time it's called (check onClickListener)
     * Todo: Make notification take you to suggestion page when clicked
     *
     * @return the notification builder containing notification information
     */
    private fun createNotification
                (title: String, message: String): NotificationCompat.Builder {

        //Determines Intent - What happens when user select notification
        val intent = Intent(this, MainActivity ::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        //Creates notification using inputted parameters
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_live_sound)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        return builder
    }

    /**
     * Replaces the current page fragment being displayed
     * Alternates between Home, LiveSound, Stats, and Suggestion fragments based on navigation
     */
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    /**
     * Initializes the view binding for the activity.
     *
     * @param inflater The layout inflater used to inflate the binding.
     * @return The root view of the inflated binding.
     */
    private fun initViewBinding(inflater: LayoutInflater): View {
        binding = ActivityMainBinding.inflate(inflater)
        return binding.root
    }


    /**
     * An ActivityResultLauncher for requesting notification permission.
     * It logs whether the user granted or denied permission
     */
    private var requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                Log.d("POST_NOTIFICATION_PERMISSION", "USER DENIED PERMISSION")
            } else {
                Log.d("POST_NOTIFICATION_PERMISSION", "USER GRANTED PERMISSION")
            }
        }

    /**
     * Requests notification permission if it's not granted.
     * Shows a toast message indicating the permission status.
     * Todo: Make it so that the Toast only appears once and not on every app startUp
     */
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            when {
                ContextCompat.checkSelfPermission(
                    this, permission
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Action to take when permission is already granted
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show()
                }

                shouldShowRequestPermissionRationale(permission) -> {
                    // Action to take when permission was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
                }

                else -> {
                    // Request permission
                    requestPermissionLauncher.launch(permission)
                }
            }
        } else {
            // Device does not support required permission
            Toast.makeText(this, "No required permission", Toast.LENGTH_LONG).show()
        }
    }

}