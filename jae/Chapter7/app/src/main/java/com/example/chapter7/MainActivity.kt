package com.example.chapter7

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chapter7.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var list = ArrayList<Profile>()
    lateinit var customAdapter: CustomAdapter
    lateinit var db: ProfileDatabase

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = ProfileDatabase.getInstance(this)!!
        Thread {
            val savedContacts = db.profileDao().getAll()
            if (savedContacts.isNotEmpty()) {
                list.addAll(savedContacts)
            }
        }.start()

        binding.button.setOnClickListener {
            Thread {
                list.add(Profile("jae", "22", "7382"))
                db.profileDao().insert((Profile("jae", "22", "7382")))

                val list = db.profileDao().getAll()
                Log.d("Inserted Primary Key", list[list.size-1].id.toString())
            }.start()
            customAdapter.notifyDataSetChanged()
        }

        customAdapter = CustomAdapter(list, this)

        binding.mainProfileLv.adapter = customAdapter
    }
}