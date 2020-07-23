package org.elvor.dogs.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

@Dao
interface LikedImageDao {
    @Query("SELECT * FROM likedimage WHERE url == :url limit 1")
    suspend fun findByUrlAsync(url: String): LikedImage?

    @Query("SELECT * FROM likedimage")
    suspend fun listAllAsync(): List<LikedImage>

    @Query("SELECT * FROM likedimage WHERE breed = :breed")
    suspend fun listAllByBreedAsync(breed: String): List<LikedImage>

    @Query("SELECT * FROM likedimage WHERE breed = :breed AND subbreed = :subbreed")
    suspend fun listAllByBreedAsync(breed: String, subbreed: String): List<LikedImage>

    @Insert
    suspend fun insertAsync(likedImage: LikedImage)

    @Delete
    suspend fun deleteAsync(likedImage: LikedImage)
}