package com.example.beardwulf.reva

import android.util.Log
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Retrofit
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.IOException
import java.lang.reflect.Type


class StringConverterFactory : Converter.Factory() {

    /**
     * TODO
     */
    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, *>? {
        return if (String::class.java == type) {
            object : Converter<ResponseBody, String> {

                @Throws(IOException::class)
                override fun convert(value: ResponseBody): String {
                    return value.string()
                }
            }
        } else null
    }

    /**
     * TODO
     */
    override fun requestBodyConverter(type: Type, parameterAnnotations: Array<Annotation>, methodAnnotations: Array<Annotation>, retrofit: Retrofit): Converter<*, RequestBody>? {
        return if (String::class.java == type) {
            object : Converter<String, RequestBody> {
                @Throws(IOException::class)
                override fun convert(value: String): RequestBody {
                    return RequestBody.create(MEDIA_TYPE, value)
                }
            }
        } else null

    }

    /**
     * Dit object is een singleton-object dat met de naam van de klasse genoemd kan worden. Elke methode in dit object kan gebruikt worden in andere klassen.
     */
    companion object {
        private val MEDIA_TYPE = MediaType.parse("text/plain")
        fun create(): StringConverterFactory {
            return StringConverterFactory()
        }
    }

}