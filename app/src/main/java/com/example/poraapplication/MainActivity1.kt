package com.example.poraapplication

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.lib.RealEstate
import com.example.poraapplication.databinding.ActivityMain1Binding
import kotlinx.serialization.json.Json
import android.os.Vibrator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity1 : AppCompatActivity() {
    private lateinit var binding: ActivityMain1Binding
    lateinit var app: MyApplication
    private lateinit var recyclerView: RecyclerView
    private lateinit var realEstateAdapter: RealEstateAdapter

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.to_bottom_anim
        )
    }

    private var clicked = false

    companion object {
        private const val KEY_ACTIVITY_OPENS_PREFIX = "activity_opens_"

        fun incrementActivityOpenCount(activity: AppCompatActivity) {
            val sharedPreferences =
                activity.getSharedPreferences(MyApplication.PREFS_NAME, MODE_PRIVATE)
            val className = activity.localClassName
            val currentCount = sharedPreferences.getInt(KEY_ACTIVITY_OPENS_PREFIX + className, 0)
            sharedPreferences.edit().putInt(KEY_ACTIVITY_OPENS_PREFIX + className, currentCount + 1)
                .apply()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        incrementActivityOpenCount(this)
        binding = ActivityMain1Binding.inflate(layoutInflater)

        applyTheme()

        setContentView(binding.root)
        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        app = application as MyApplication

        realEstateAdapter = RealEstateAdapter(app)

        realEstateAdapter.setOnItemClickListener(object: RealEstateAdapter.OnItemClickListener {
            override fun onItemClick(realEstate: RealEstate) {
                val intent = Intent(this@MainActivity1, MainActivity::class.java)
                intent.putExtra("AREA", realEstate.area)
                intent.putExtra("PRICE", realEstate.price)
                intent.putExtra("PROPERTY_TYPE", realEstate.propertyType)
                intent.putExtra("POSITION", app.transactions.realEstates.indexOf(realEstate))
                updateDataFromMainActivity.launch(intent)
            }

            override fun onItemLongClick(realEstate: RealEstate): Boolean {
                val builder = AlertDialog.Builder(this@MainActivity1)
                builder.setTitle("Delete RealEstate")
                builder.setMessage("Are you sure you want to delete this RealEstate?")
                builder.setPositiveButton("Yes") { _, _ ->
                    realEstateAdapter.removeRealEstate(realEstate)
                    Toast.makeText(
                        this@MainActivity1,
                        "Successfully removed: ${realEstate.propertyType}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                builder.setNeutralButton("Cancel") { _, _ ->
                    Toast.makeText(
                        this@MainActivity1,
                        "Cancelled",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                builder.setNegativeButton("No") { _, _ ->
                    Toast.makeText(
                        this@MainActivity1,
                        "Cancelled",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
                return true
            }
        })

        recyclerView.adapter = realEstateAdapter

    }

    private val getDataFromMainActivity1 =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val propertyType = intent?.getStringExtra(InputTypes.PROPERTY_TYPE.name)
                val area = intent?.getDoubleExtra(InputTypes.AREA.name, 0.0)
                val price = intent?.getDoubleExtra(InputTypes.PRICE.name, 0.0)
                val realEstate = RealEstate(propertyType!!, area!!, price!!)
                realEstateAdapter.addRealEstate(realEstate)
            }
        }

    private val updateDataFromMainActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val propertyType = intent?.getStringExtra(InputTypes.PROPERTY_TYPE.name)
                val area = intent?.getDoubleExtra(InputTypes.AREA.name, 0.0)
                val price = intent?.getDoubleExtra(InputTypes.PRICE.name, 0.0)
                val position = intent?.getIntExtra("POSITION", 0)
                val realEstate = RealEstate(propertyType!!, area!!, price!!)
                realEstateAdapter.updateRealEstate(realEstate, position!!)
            }
        }

    //TODO: Update doesn't work

    private val getDataFromQRCode =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val scannedValue = intent?.getStringExtra("SCAN_RESULT")

                val parsedRealestate = parseJSON(scannedValue)

                if (parsedRealestate != null) {
                    Toast.makeText(this, "Got RealEstate.", Toast.LENGTH_SHORT).show()
                    vibrate()
                    realEstateAdapter.addRealEstate(parsedRealestate)
                }
            } else {
                Toast.makeText(this, "QR Code scanning failed", Toast.LENGTH_SHORT).show()
            }
        }

    private fun parseJSON(scannedValue: String?): RealEstate? {
        return try {
            val realEstate = Json.decodeFromString<RealEstate>(scannedValue!!)
            realEstate
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "Failed to parse text.", Toast.LENGTH_SHORT).show()
            null
        }
    }

    private fun vibrate() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) { // Vibrator availability checking
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        500,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator.vibrate(500) // Vibrate method for below API Level 26
            }
        }
    }

    fun onAddButtonClick(view: View) {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked

        val intent = Intent(this, MainActivity::class.java)
        getDataFromMainActivity1.launch(intent)
    }

    fun onQRCButtonClick(view: View) {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked

        try {
            val intent = Intent("com.google.zxing.client.android.SCAN")
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE")
            getDataFromQRCode.launch(intent)
        } catch (e: Exception) {
            val marketUri = Uri.parse("market://details?id=com.google.zxing.client.android")
            val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
            startActivity(marketIntent)
        }
    }

    fun onAboutButtonClick(view: View) {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }

    fun onMenuButtonClick(view: View) {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    fun onSettingsButtonClick(view: View) {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.fabAbout.visibility = View.VISIBLE
            binding.fabAdd.visibility = View.VISIBLE
            binding.fabQRCode.visibility = View.VISIBLE
        } else {
            binding.fabAdd.visibility = View.INVISIBLE
            binding.fabQRCode.visibility = View.INVISIBLE
            binding.fabAbout.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.fabAdd.startAnimation(fromBottom)
            binding.fabQRCode.startAnimation(fromBottom)
            binding.fabAbout.startAnimation(fromBottom)
            binding.fabMenu.startAnimation(rotateOpen)
        } else {
            binding.fabAdd.startAnimation(toBottom)
            binding.fabQRCode.startAnimation(toBottom)
            binding.fabAbout.startAnimation(toBottom)
            binding.fabMenu.startAnimation(rotateClose)
        }
    }

    private fun applyTheme() {
        // Set the theme mode based on the SharedPreferences before setting the content view.
        val sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)
        val isNightMode = sharedPreferences.getBoolean("theme", false)
        val mode =
            if (isNightMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        if (AppCompatDelegate.getDefaultNightMode() != mode) {
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }

}