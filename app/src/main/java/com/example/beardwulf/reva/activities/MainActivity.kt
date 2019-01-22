package com.example.beardwulf.reva.activities
import com.example.beardwulf.reva.extensions.InputRegex
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.test.espresso.idling.CountingIdlingResource
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.beardwulf.reva.Endpoint
import com.example.beardwulf.reva.R
import com.example.beardwulf.reva.RetrofitClientInstance
import com.example.beardwulf.reva.activities.registreren.Registreren
import com.example.beardwulf.reva.activities.vragenOplossen.VragenOplossen
import com.example.beardwulf.reva.domain.Group
import com.example.beardwulf.reva.domain.testApplicationClass
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.activityManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.ref.WeakReference
import java.util.*
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    private val idlingResource= CountingIdlingResource("Registreren");

    fun getIdlingResourceInTest(): CountingIdlingResource {
        return idlingResource
    }
    /**
     * Deze methode wordt gebruikt om informatie over de staat van uw activiteit op te slaan en te herstellen.
     * In gevallen zoals oriëntatieveranderingen, de app afsluiten of een ander scenario dat leidt tot het opnieuw oproepen van onCreate(),
     * kan de savedInstanceState bundel gebruikt worden om de vorige toestandsinformatie opnieuw te laden.
     * De activity_main layout wordt geladen en er wordt een "luisteraar" op de loginknop gezet
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnLogin.setOnClickListener {
            login()
        }
    }

    /**
     * Methode login controleert de cijfers voor het inloggen op de applicatie en doet een call naar de databank adhv Retrofit om de
     * code te controleren.
     * Hierna start de activity VragenOplossen.
     */
    fun login() {
    if(InputRegex.controleerLettersCijfers(txtInput.text.toString())) {
        idlingResource.increment()
        val service = RetrofitClientInstance().getRetrofitInstance()!!.create(Endpoint::class.java!!)
        val call = service.getGroup(txtInput.text.toString())
        call.enqueue(object : Callback<Group> {
            override fun onResponse(call: Call<Group>, response: Response<Group>) {
                if (response.code() == 200) {
                    var body = response.body()
                    var group = body!!
                    var test = applicationContext as testApplicationClass
                    test.group = group;
                    group.name?.let {
                        startActivity(Intent(applicationContext, VragenOplossen::class.java))
                    } ?: run {
                        startActivity(Intent(applicationContext, Registreren::class.java))
                    }
                } else {
                    txtInput.setError("Code niet correct");
                }
            }

            override fun onFailure(call: Call<Group>, t: Throwable) {
                Log.d("Error", t.message)
            }
        })
        idlingResource.decrement();
        }else{
        txtInput.setError("Voer enkel cijfers en letters in aub");
    }
    }

}
