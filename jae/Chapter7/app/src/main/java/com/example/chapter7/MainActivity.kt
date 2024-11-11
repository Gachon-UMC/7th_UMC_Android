package com.example.chapter7

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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

        customAdapter = CustomAdapter(list, this)

        binding.mainProfileLv.adapter = customAdapter
    }
}