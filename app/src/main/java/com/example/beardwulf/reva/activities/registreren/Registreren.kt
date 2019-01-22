package com.example.beardwulf.reva.activities.registreren

import android.arch.lifecycle.ViewModelProviders
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.beardwulf.reva.R
import com.example.beardwulf.reva.activities.vragenOplossen.VragenOplossen
import com.example.beardwulf.reva.domain.Category
import com.example.beardwulf.reva.fragments.registreren.RegisterCategories
import com.example.beardwulf.reva.fragments.registreren.RegisterPhoto
import com.example.beardwulf.reva.fragments.registreren.RegistreerGroep
import com.example.beardwulf.reva.interfaces.RegisterCallbacks
import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import com.example.beardwulf.reva.Endpoint
import com.example.beardwulf.reva.RetrofitClientInstance
import com.example.beardwulf.reva.domain.Group
import com.example.beardwulf.reva.domain.PhotoViewModel
import com.example.beardwulf.reva.domain.testApplicationClass
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.MultipartBody
import java.io.File
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream

class Registreren : AppCompatActivity(), RegisterCategories.RegisterCategoriesCallBacks, RegisterPhoto.RegisterPhotoCallbacks, RegistreerGroep.RegisterGroupCallbacks {

    private lateinit var currentFragment: Fragment
    /**
     * Deze methode wordt gebruikt om informatie over de staat van uw activiteit op te slaan en te herstellen.
     * In gevallen zoals oriëntatieveranderingen, de app afsluiten of een ander scenario dat leidt tot het opnieuw oproepen van onCreate(),
     * kan de savedInstanceState bundel gebruikt worden om de vorige toestandsinformatie opnieuw te laden.
     * Het fragment(RegisterPhoto) wordt geladen waar men een groepsfoto kan nemen adhv de setFragment methode.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registreren)
        overridePendingTransition(0, 0);
        var conf = Bitmap.Config.ARGB_8888
        setFragment(RegisterPhoto.newInstance(), "PHOTO")

    }

    /**
     * Methode om het registreerLayout fragment met een ander fragment die wordt meegegeven te wisselen
     */
    fun setFragment(fragment: Fragment, tag : String) {
        //Kijk of fragment al is toegevoegd geweest, behoud huidige fragment status bij rotatie
        var oldFragment = supportFragmentManager.findFragmentByTag(tag)
        oldFragment?: run{
            var fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.registreerlayout, fragment, tag)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

    }

    /**
     * Methode die toelaat om de groep te registeren. Er wordt adhv retrofit een call gedaan naar de backend.
     * Na het registeren van de groep succesvol is verlopen wordt men doorgestuurd naar de Kaart met exposanten
     */
    override fun registerAndGoToMap() {
        val group = (applicationContext as testApplicationClass).group
        val f = File(cacheDir, "groupImage.png")
        val outputStream : FileOutputStream = FileOutputStream(f)
        val byteArrayOutputStream = ByteArrayOutputStream()
        val photo = ViewModelProviders.of(this).get( PhotoViewModel::class.java).photo;
        photo.compress(Bitmap.CompressFormat.PNG, 30 /*ignored for PNG*/, byteArrayOutputStream);
        val bitmapdata = byteArrayOutputStream.toByteArray()

        outputStream.write(bitmapdata)
        outputStream.flush()
        outputStream.close()
        val filePart = MultipartBody.Part.createFormData("groupImage", f.name, RequestBody.create(MediaType.parse("image/*"), f))
        var categoriesBody = ArrayList<RequestBody>()
        group.categories.forEach {c ->
            categoriesBody.add(RequestBody.create(MediaType.parse("text/plain"), c.name));
        }
        val service = RetrofitClientInstance().getRetrofitInstance()!!.create(Endpoint::class.java!!)
        val call = service.registerGroup((application as testApplicationClass).group._id, filePart,group.description, group.name,categoriesBody)

       call.enqueue(object : Callback<Group> {
            override fun onResponse(call: Call<Group>, response: Response<Group>) {
                startActivity(Intent(applicationContext,VragenOplossen::class.java))
            }

            override fun onFailure(call: Call<Group>, t: Throwable) {
                Log.d("Error", t.message)
            }
        })
    }

    /**
     * Methode die de setFragment methode aanroept waardoor het registreerLayout fragment wordt verwisseld met met het RegisterCategories(registeren van
     * de categorieën) fragment
     */
    override fun goToCategories() {
       //SET INFO
        setFragment(RegisterCategories.newInstance(), "CATEGORIES")
    }

    /**
     * Methode die de setFragment methode aanroept waardoor het registreerLayout fragment wordt verwisseld met met het RegistreerGroep(registeren van
     * de groep) fragment
     */
    override fun goToGroupDetails() {
        setFragment(RegistreerGroep.newInstance(), "DETAILS")
    }

    /**
     * Dit object is een singleton-object dat met de naam van de klasse genoemd kan worden. Elke methode in dit object kan gebruikt worden in andere klassen.
     */
    companion object {
    }
}
