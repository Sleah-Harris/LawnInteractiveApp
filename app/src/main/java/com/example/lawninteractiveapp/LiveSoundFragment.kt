package com.example.lawninteractiveapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.lawninteractiveapp.databinding.ActivityMainBinding
import com.example.lawninteractiveapp.databinding.FragmentLiveSoundBinding

/**
 * A simple [Fragment] subclass.
 * Use the [LiveSoundFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LiveSoundFragment : Fragment() {
    private lateinit var binding : FragmentLiveSoundBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_live_sound, container, false)

        val button = view.findViewById<Button>(R.id.detectButton)

        button.setOnClickListener{
            Log.d("Live Sound", "Detect Button Pressed")

            val intent = Intent(activity, LiveSoundDetection::class.java)
            startActivity(intent)
        }

        // Inflate the layout for this fragment
        return view
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LiveSoundFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LiveSoundFragment().apply {
            }
    }
}