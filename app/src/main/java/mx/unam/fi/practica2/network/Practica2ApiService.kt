package mx.unam.fi.practica2.network

import mx.unam.fi.practica2.model.Practica2Model
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://www.carqueryapi.com/api/0.3/"//"https://www.carqueryapi.com/api/0.3/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface Practica2ApiService {

    @GET("?callback=?&cmd=getModels")
    suspend fun getPractica2(
        @Query("make") make: String,
        @Query("year") year: Int
    ): List<Practica2Model>
}

object Practica2Api {
    val retrofitService: Practica2ApiService by lazy {
        retrofit.create(Practica2ApiService::class.java)
    }

    suspend fun getCarModelsByMakeAndYear(make: String, year: Int): List<Practica2Model> {
        return retrofitService.getPractica2(make, year)
    }
}
