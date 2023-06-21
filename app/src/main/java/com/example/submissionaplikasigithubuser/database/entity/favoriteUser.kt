package com.example.submissionaplikasigithubuser.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity (tableName = "favUser")
@Parcelize
data class favoriteUser(
    @PrimaryKey
    val id: Int,
    val username: String,
    var avatarUrl: String
): Parcelable