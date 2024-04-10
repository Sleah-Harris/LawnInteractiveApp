package com.example.lawninteractiveapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.fragment.app.Fragment
import com.example.lawninteractiveapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView;


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

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

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()

    }

}