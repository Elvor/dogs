package org.elvor.dogs.backend

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface DogsService {
    @GET("breeds/list/all")
    fun listBreedsAsync(): Deferred<Message<Map<String, List<String>>>>

    @GET("breed/{breed}/list")
    fun listSubbreedsAsync(@Path("breed") breed: String): Deferred<Message<List<String>>>

    @GET("breed/{breed}/images")
    fun getImagesForBreedAsync(@Path("breed") breed: String): Deferred<Message<List<String>>>
}