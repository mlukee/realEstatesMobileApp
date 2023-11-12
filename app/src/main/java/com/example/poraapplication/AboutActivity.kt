package com.example.poraapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.poraapplication.databinding.ActivityAboutBinding
import com.example.poraapplication.databinding.ActivityMain1Binding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding

    companion object {
        private const val KEY_ACTIVITY_OPENS_PREFIX = "activity_opens_"

        fun incrementActivityOpenCount(activity: AppCompatActivity) {
            val sharedPreferences = activity.getSharedPreferences(MyApplication.PREFS_NAME, MODE_PRIVATE)
            val className = activity.localClassName
            val currentCount = sharedPreferences.getInt(KEY_ACTIVITY_OPENS_PREFIX + className, 0)
            sharedPreferences.edit().putInt(KEY_ACTIVITY_OPENS_PREFIX + className, currentCount + 1).apply()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        incrementActivityOpenCount(this)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val text = getString(R.string.app_author_name, "mlukee")
        binding.tvAuthor.text = text
        val version = getString(R.string.app_version_tv, "3.0")
        binding.tvAppVersion.text = version

        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val uuid = sharedPreferences.getString("uuid", "")!!
        binding.tvUUID.text = getString(R.string.UUID_tv, uuid)
    }
}