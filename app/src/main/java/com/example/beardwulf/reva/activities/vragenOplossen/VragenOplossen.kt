package com.example.beardwulf.reva.activities.vragenOplossen

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.res.Configuration
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.util.Log
import com.example.beardwulf.reva.Endpoint
import com.example.beardwulf.reva.R
import com.example.beardwulf.reva.RetrofitClientInstance
import com.example.beardwulf.reva.domain.*
import com.example.beardwulf.reva.fragments.EindeSpel
import com.example.beardwulf.reva.interfaces.QuestionCallbacks
import kotlinx.android.synthetic.main.activity_vragen_oplossen.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import com.google.gson.Gson
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.content.res.Configuration.ORIENTATION_SQUARE
import android.view.Display
import android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE
import android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK
import android.view.View
import android.view.View.*
import android.widget.Button
import com.example.beardwulf.reva.activities.MainActivity
import com.example.beardwulf.reva.fragments.vragenOplossen.*
import kotlinx.android.synthetic.main.fragment_einde_spel.*
import kotlinx.android.synthetic.main.fragment_kaart.*

/**
 * Activity die het tonen van alle vragen en inlezen van alle antwoorden verzorgt
 */
class VragenOplossen : AppCompatActivity(), QuestionCallbacks, Kaart.MapCallbacks, VraagInvullen.QuestionAnswerCallbacks, VraagInvullenFoto.QuestionAnswerPhotoCallbacks, EindeSpel.EndGameCallBack {

    var questionNr = 0
    override var maxQuestion = 5
    var questions = arrayOf(
            "Hoeveel spelers zijn er op het veld tijdens een wedstrijd rolstoelbasketbal? (Beide ploegen samen opgeteld)",
            "Hoeveel kost de nieuwste kruk van VIGO?"
    )

    lateinit var kaart: Kaart
    lateinit var vraagInvullen: VraagInvullen
    lateinit var vraagIngevuld: VraagIngevuld
    lateinit var eindeSpel: EindeSpel

    lateinit var exhibitors: ArrayList<Exhibitor>
    lateinit var currentExhibitor: ExhibitorViewModel
    override var firstQuestion : Boolean = true

    /**
     * Deze methode wordt gebruikt om informatie over de staat van uw activiteit op te slaan en te herstellen.
     * In gevallen zoals oriëntatieveranderingen, de app afsluiten of een ander scenario dat leidt tot het opnieuw oproepen van onCreate(),
     * kan de savedInstanceState bundel gebruikt worden om de vorige toestandsinformatie opnieuw te laden.
     * Opent de fragment voor het tonen en beantwoorden van een vraag
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vragen_oplossen)
        currentExhibitor = ViewModelProviders.of(this).get( ExhibitorViewModel::class.java);
        setNextExhibitor()
        overridePendingTransition(0, 0);
    }

    /**
     * Bij doorgaan van de app nadat de onPause methode is opgeroepen wordt de onResume methode aangegroepen. Het is één van de methodes
     * van de activity life cycle.
     */
    override fun onResume() {
        super.onResume()
    }

    /**
     * Methode makeExhibitors creëert een lege arraylijst van exposanten
     */
    fun makeExhibitors() {
        exhibitors= ArrayList(2)
    }

    /**
     * Methode setFragment krijgt een fragment mee en een int. Dit integer representeert het nummer van de vraag.
     * Hierdoor kan men elke keer het huidige vraagfragment vervangen door het volgende.
     */
    fun setFragment(fragment: Fragment, int: Int) {
        var fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(int, fragment)
        fragmentTransaction.commit()
    }

    /**
     * Methode removeFragment verwijdert het huidige framgment van de activity.
     */
    fun removeFragment(fragment: Fragment) {
        var fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.remove(fragment)
        fragmentTransaction.commit()
    }

    /**
     * Methode currentExhibitor geeft de huidige exposant terug
     */
    fun currentExhibitor(): Exhibitor {
        return exhibitors[questionNr]
    }

    /**
     * Methode unFocusMap zorgt ervoor dat er kan uitgezoomd worden op de map
     */
    fun unfocusMap() {
       fragment.alpha = 0.1F
    }

    /**
     * Methode unFocusMap zorgt ervoor dat er kan ingezoomd worden op de map
     */
    fun focusMap(){
        fragment.alpha = 1.0F
    }

    /**
     * TODO
     */
    override fun setNextExhibitor() {
        Log.d("NEXTE", "ANOTHER ONE")

    if(currentExhibitor.isNew) {
        Log.d("EXNEW", "NEW");
       currentExhibitor.getNextExhibitor((applicationContext as testApplicationClass).group._id!!);
        currentExhibitor.getExhibitor().observe(this, Observer{ exhibitor ->
            Log.d("NEWONE", "NEW")
            showMap()
        })
    } else {
        Log.d("EXNEW", "OLD");
        showMap()
    }
    }

    /**
     * TODO
     */
    override fun setAnswer(answer: String) {
        val service = RetrofitClientInstance().getRetrofitInstance()!!.create(Endpoint::class.java!!)
        val call = service.postAnwser((applicationContext as testApplicationClass).group._id!!, answer)
        call.enqueue(object : Callback<Group> {
            override fun onResponse(call: Call<Group>, response: Response<Group>) {
                showMap()
                unfocusMap()
                val vraagIngevuld = VraagIngevuld.newInstance()
                firstQuestion = false
                setFragment(vraagIngevuld, R.id.fragment2)
                this@VragenOplossen.vraagIngevuld = vraagIngevuld
            }

            override fun onFailure(call: Call<Group>, t: Throwable) {
                Log.d("Error", t.message)
            }
        })
    }

    /**
     * TODO
     */
    override fun setAnswer(photo: Bitmap) {
        val service = RetrofitClientInstance().getRetrofitInstance()!!.create(Endpoint::class.java!!)
        val group = (applicationContext as testApplicationClass).group
        val f = File(cacheDir, "answerPhoto.png")
        val outputStream : FileOutputStream = FileOutputStream(f)
        val byteArrayOutputStream = ByteArrayOutputStream()
        photo.compress(Bitmap.CompressFormat.PNG, 30 /*ignored for PNG*/, byteArrayOutputStream);
        val bitmapdata = byteArrayOutputStream.toByteArray()

        outputStream.write(bitmapdata)
        outputStream.flush()
        outputStream.close()
        val filePart = MultipartBody.Part.createFormData("photo", f.name, RequestBody.create(MediaType.parse("image/*"), f))
        val call = service.postAnwser((applicationContext as testApplicationClass).group._id!!, filePart)
        call.enqueue(object : Callback<Group> {
            override fun onResponse(call: Call<Group>, response: Response<Group>) {
                showMap()
                unfocusMap()
                val vraagIngevuld = VraagIngevuld.newInstance()
                firstQuestion = false
                setFragment(vraagIngevuld, R.id.fragment2)
                this@VragenOplossen.vraagIngevuld = vraagIngevuld
            }

            override fun onFailure(call: Call<Group>, t: Throwable) {
                Log.d("Error", t.message)
            }
        })
    }

    /**
     * Methode goToNext Question verwijdert het fragment dat de vraag is ingevuld(tenzij bij eerste vraag) en set het fragment
     * van de volgende vraag, dit kan een vraag zijn met enkel tekst of een vraag waar een foto moet genomen worden
     */
    override fun goToNextQuestion() {
        if (!firstQuestion)
            removeFragment(vraagIngevuld)
        if(currentExhibitor.getExhibitor().value!!.question.type == QuestionType.TEXT)
            setFragment(VraagInvullen.newInstance(), R.id.fragment)
        else
            setFragment(VraagInvullenFoto.newInstance(), R.id.fragment)
    }

    /**
     * Methode determineNextMove controleert of het de laatste vraag is, het wijst een nieuwe exposant aan indien niet, indien wel toont hij het fragment dat het spel gedaan is
     */
    override fun determineNextMove() {
        if (currentExhibitor.getExhibitor().value!!.question.counter + 1 < maxQuestion) {
            removeFragment(vraagIngevuld)
            currentExhibitor.isNew = true
            setNextExhibitor()
            focusMap()
        } else {
            val eindeSpel = EindeSpel.newInstance()
            this.eindeSpel = eindeSpel
            setFragment(eindeSpel, R.id.fragment2)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(applicationContext,MainActivity::class.java))

    }

    /**
     * Methode showMap controleert de size van het device, bij large of xlarge controleert hij de oriëntatie.
     * Indien dit portrait is plaatst hij het fragment van de kaart over heel het scherm.
     * Indien dit landscape is plaatst hij jet vraagInvullen fragment naast het Kaart fragment
     */
    fun showMap() {
        if (getSizeName(applicationContext) === "large" || getSizeName(applicationContext) === "xlarge") {
            if (determineOrientation(applicationContext) == true) {
                setFragment(Kaart.newInstance(), R.id.fragment)
                if(currentExhibitor.getExhibitor().value!!.question.type == QuestionType.TEXT)
                    setFragment(VraagInvullen.newInstance(), R.id.fragment2)
                else
                    setFragment(VraagInvullenFoto.newInstance(), R.id.fragment2)
            } else {
                setFragment(Kaart.newInstance(), R.id.fragment)
            }
        } else {
            setFragment(Kaart.newInstance(), R.id.fragment)
        }
    }

    override fun goBackToMap() {
        removeFragment(eindeSpel)
        setFragment(KaartEnkel.newInstance(), R.id.fragment)
        focusMap()
    }

    /**
     * Dit object is een singleton-object dat met de naam van de klasse genoemd kan worden. Elke methode in dit object kan gebruikt worden in andere klassen.
     */
    companion object {
        fun newInstance(): VragenOplossen {
            return newInstance()
        }

        /**
         * Methode getSizeName kijkt hoe groot het device is -> Small/Normal/Large en Xlarge
         * en kan zo gebruikt worden om landscape mode te bepalen of portrait mode
         */
        public fun getSizeName(context: Context): String {
            var screenLayout = context.resources.configuration.screenLayout
            screenLayout = screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
            when (screenLayout) {
                Configuration.SCREENLAYOUT_SIZE_SMALL -> return "small"
                Configuration.SCREENLAYOUT_SIZE_NORMAL -> return "normal"
                Configuration.SCREENLAYOUT_SIZE_LARGE -> return "large"
                Configuration.SCREENLAYOUT_SIZE_XLARGE -> return "xlarge"
                else -> return "undefined"
            }
        }

        /**
         * Methode determineOrientation checkt de layouts.xml files in res/values-land en res/values-port.
         * Hiermee weet hij in welke oriëntatie het device op dat moment gebruikt wordt
         */
        public fun determineOrientation(context : Context) : Boolean {
            return context.getResources().getBoolean(R.bool.is_landscape)
        }
    }
}
