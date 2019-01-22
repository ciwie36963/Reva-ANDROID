package com.example.beardwulf.reva.fragments.vragenOplossen

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.beardwulf.reva.Endpoint
import com.example.beardwulf.reva.ImageHelper

import com.example.beardwulf.reva.R
import com.example.beardwulf.reva.RetrofitClientInstance
import com.example.beardwulf.reva.activities.MainActivity
import com.example.beardwulf.reva.activities.vragenOplossen.VragenOplossen
import com.example.beardwulf.reva.domain.Exhibitor
import com.example.beardwulf.reva.domain.ExhibitorViewModel
import com.example.beardwulf.reva.domain.Group
import com.example.beardwulf.reva.domain.PhotoViewModel
import com.example.beardwulf.reva.interfaces.QuestionCallbacks
import kotlinx.android.synthetic.main.fragment_vraag_invullen_foto.*
import org.jetbrains.anko.find
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Fragment voor het tonen van een vraag en inlezen van een antwoord
 */
class VraagInvullenFoto : Fragment() {

    lateinit var parent: QuestionAnswerPhotoCallbacks
    lateinit var photoViewModel: PhotoViewModel
    lateinit var currentExhibitor: Exhibitor
    lateinit var currentExhibitorViewModel: ExhibitorViewModel
    var mCurrentPhotoPath: String = ""
    val REQUEST_TAKE_PHOTO = 1

    /**
     * Deze methode wordt gebruikt om informatie over de staat van uw activiteit op te slaan en te herstellen.
     * In gevallen zoals oriëntatieveranderingen, de app afsluiten of een ander scenario dat leidt tot het opnieuw oproepen van onCreate(),
     * kan de savedInstanceState bundel gebruikt worden om de vorige toestandsinformatie opnieuw te laden.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parent = (activity as QuestionAnswerPhotoCallbacks)
    }

    /**
     * Toont de vraag en een veld om een antwoord in te vullen.
     * Indien er een antwoord wordt inguvuld, opent het bevestigings fragment
     * onCreateView wordt opgeroepen om de lay-out van het fragment "op te blazen"(inflate),
     * d.w.z. dat de grafische initialisatie meestal hier plaatsvindt.
     * Het wordt altijd aangeroepen na de onCreate methode.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = (inflater.inflate(R.layout.fragment_vraag_invullen_foto, container, false))
        var conf = Bitmap.Config.ARGB_8888
        photoViewModel = ViewModelProviders.of(activity!!).get( PhotoViewModel::class.java)
        photoViewModel.photo = Bitmap.createBitmap(306, 306, conf)
        currentExhibitorViewModel = ViewModelProviders.of(activity!!).get( ExhibitorViewModel::class.java);
        currentExhibitor = currentExhibitorViewModel.getExhibitor().value!!
        return view
    }

    /**
     * Bij doorgaan van de app nadat de onPause methode is opgeroepen wordt de onResume methode aangegroepen.
     * Het is één van de methodes van de activity life cycle.
     */
    override fun onResume() {
        super.onResume()
        textView2.text = currentExhibitor.question.body
        textView3.text = (currentExhibitor.question.counter).toString()
        cmdNeemFoto.setOnClickListener {
            dispatchTakePictureIntent()

        }
        btnVulIn.setOnClickListener {
            parent.setAnswer(photoViewModel.photo);
        }
        photoViewer.setImageBitmap(photoViewModel.photo)
    }

    /**
     * Bitmap uitpakken en in het statische fotovariabele steken,
     * hierna wordt de knop om naar het volgende scherm te gaan aangezet
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            var foto : Bitmap
            foto = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, photoViewModel.photoUri)
            foto = ImageHelper.getRoundedCornerBitmap(foto, foto.width / 2)
            photoViewModel.photo = foto
            print(foto)
            btnVulIn.setEnabled(true);
            cmdNeemFoto.setText(getString(R.string.fotowijzigen))
        }
    }

    /**
     * Methode createImageFile maakt en retourneert een image met een datum en een tijd
     */
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }

    /**
     * Methode dispatchTakePictureIntent controleert of er een camera aanwezig is op het device en creëert de file waar image in moet.
     */
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            activity!!,
                            "com.example.beardwulf.fileprovider",
                            it
                    )
                    photoViewModel.photoUri = photoURI
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    /**
     * TODO
     */
    interface QuestionAnswerPhotoCallbacks {
        fun setAnswer(answer : Bitmap)
        var maxQuestion : Int
    }

    /**
     * Dit object is een singleton-object dat met de naam van de klasse genoemd kan worden. Elke methode in dit object kan gebruikt worden in andere klassen.
     */
    companion object {
        fun newInstance(): VraagInvullenFoto {
            return VraagInvullenFoto()
        }
    }
}
