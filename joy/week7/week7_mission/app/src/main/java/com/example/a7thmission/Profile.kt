package com.example.a7thmission

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Profile(
    var name: String,
    var age: String,
    var phone: String,
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
