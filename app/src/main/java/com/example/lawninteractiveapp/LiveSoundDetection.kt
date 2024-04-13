package com.example.lawninteractiveapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.lawninteractiveapp.databinding.ActivityLiveSoundDetectionBinding
import com.example.lawninteractiveapp.databinding.ActivityMainBinding
import org.tensorflow.lite.task.audio.classifier.AudioClassifier
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class LiveSoundDetection : AppCompatActivity() {
    private lateinit var binding : ActivityLiveSoundDetectionBinding
    var TAG = "LiveSoundDetection"

    // TODO 2.1: defines the model to be used
    var modelPath = "1.tflite"

    // TODO 2.2: defining the minimum threshold
    var probabilityThreshold: Float = 0.3f

    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(initViewBinding(layoutInflater))

        val REQUEST_RECORD_AUDIO = 1337
        requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_RECORD_AUDIO)

        textView = findViewById<TextView>(R.id.output)
        val recorderSpecsTextView = findViewById<TextView>(R.id.textViewAudioRecorderSpecs)

        // TODO 2.3: Loading the model from the assets folder
        val classifier = AudioClassifier.createFromFile(this, modelPath)

        // TODO 3.1: Creating an audio recorder
        val tensor = classifier.createInputTensorAudio()

        // TODO 3.2: showing the audio recorder specification
        val format = classifier.requiredTensorAudioFormat
        val recorderSpecs = "Number Of Channels: ${format.channels}\n" +
                "Sample Rate: ${format.sampleRate}"
        recorderSpecsTextView.text = recorderSpecs

        // TODO 3.3: Creating
        val record = classifier.createAudioRecord()
        record.startRecording()

        Timer().scheduleAtFixedRate(1, 500) {

            // TODO 4.1: Classifing audio data
            val numberOfSamples = tensor.load(record)
            val output = classifier.classify(tensor)

            // TODO 4.2: Filtering out classifications with low probability
            val filteredModelOutput = output[0].categories.filter {
                it.score > probabilityThreshold
            }

            // TODO 4.3: Creating a multiline string with the filtered results
            val outputStr =
                filteredModelOutput.sortedBy { -it.score }
                    .joinToString(separator = "\n") { "${it.label} -> ${it.score} " }

            // TODO 4.4: Updating the UI
            if (outputStr.isNotEmpty())
                runOnUiThread {
                    textView.text = outputStr
                }
        }

        binding.returnButton.setOnClickListener {
            Log.d("Sound Detection", "Button Clicked")
            finish()
        }
    }

    /**
     * Initializes the view binding for the activity.
     *
     * @param inflater The layout inflater used to inflate the binding.
     * @return The root view of the inflated binding.
     */
    private fun initViewBinding(inflater: LayoutInflater): View {
        binding = ActivityLiveSoundDetectionBinding.inflate(inflater)
        return binding.root
    }
}