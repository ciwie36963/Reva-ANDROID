package com.example.beardwulf.reva

import android.graphics.Bitmap
import com.example.beardwulf.reva.domain.Category
import com.example.beardwulf.reva.domain.Exhibitor
import com.example.beardwulf.reva.domain.Group
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*
import java.util.*
import retrofit2.http.GET
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.POST
import retrofit2.http.Multipart
import kotlin.collections.ArrayList

/**
 * Interface om calls uit te voeren naar de databank
 */
interface Endpoint {

    @GET("/API/student/group/{code}")
    fun getGroup(@Path("code") code: String): Call<Group>

    @GET("/API/student/categories")
    fun getCategories(): Call<ArrayList<String>>

    @POST("/API/student/register/{group}")
    @Multipart
    fun registerGroup(@Path("group") group: String, @Part groupImage: MultipartBody.Part, @Part("description") description: String?, @Part("name") name: String?, @Part("categories") categories: ArrayList<RequestBody>): Call<Group>

    @POST("/API/student/exhibitor/{group}")
    fun getExhibitor(@Path("group") group: String): Observable<Exhibitor>

    @POST("API/student/answer/{group}")
    @FormUrlEncoded
    fun postAnwser(@Path("group") group: String, @Field("answer") answer : String): Call<Group>

    @POST("API/student/answerPhoto/{group}")
    @Multipart
    fun postAnwser(@Path("group") group : String, @Part photo : MultipartBody.Part): Call<Group>
}