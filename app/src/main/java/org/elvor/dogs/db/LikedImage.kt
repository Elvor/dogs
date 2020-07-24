package org.elvor.dogs.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LikedImage(
    @PrimaryKey val url: String,
    @ColumnInfo(name = "breed") val breed: String,
    @ColumnInfo(name = "subbreed") val subbreed: String?
)