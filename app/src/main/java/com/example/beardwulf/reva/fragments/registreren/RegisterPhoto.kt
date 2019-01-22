package com.example.beardwulf.reva.fragments.registreren

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.beardwulf.reva.ImageHelper
import com.example.beardwulf.reva.R
import com.example.beardwulf.reva.activities.registreren.Registreren
import kotlinx.android.synthetic.main.fragment_register_photo.*
import org.jetbrains.anko.find
import android.os.StrictMode
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import com.example.beardwulf.reva.R.id.imageView
import android.R.attr.data
import android.content.ContentResolver
import java.net.SocketOption
import android.content.ContentValues
import android.R.attr.thumbnail
import android.arch.lifecycle.ViewModelProviders
import android.os.Environment
import android.support.v4.content.FileProvider
import android.util.Log
import com.example.beardwulf.reva.domain.PhotoViewModel
import com.example.beardwulf.reva.interfaces.RegisterCallbacks
import java.io.IOException


class RegisterPhoto : Fragment() {

    private lateinit  var photoViewModel : PhotoViewModel
    private val CAMERA_REQUEST = 1888;
    var mCameraFileName = ""
    lateinit var image: Uri
    lateinit var values: ContentValues
    lateinit var parent: RegisterPhotoCallbacks
    lateinit var photo : Bitmap
    lateinit var photoUri : Uri
    var mCurrentPhotoPath: String = ""
    val REQUEST_TAKE_PHOTO = 1

    /**
     * Deze methode wordt gebruikt om informatie over de staat van uw activiteit op te slaan en te herstellen.
     * In gevallen zoals oriëntatieveranderingen, de app afsluiten of een ander scenario dat leidt tot het opnieuw oproepen van onCreate(),
     * kan de savedInstanceState bundel gebruikt worden om de vorige toestandsinformatie opnieuw te laden.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * onCreateView wordt opgeroepen om de lay-out van het fragment "op te halen",
     * d.w.z. dat de grafische initialisatie meestal hier plaatsvindt.
     * Het wordt altijd aangeroepen na de onCreate methode.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_register_photo, container, false)
        var conf = Bitmap.Config.ARGB_8888
        photo = Bitmap.createBitmap(306, 306, conf)
        parent = (activity as RegisterPhotoCallbacks)
        photoViewModel = ViewModelProviders.of(activity!!).get( PhotoViewModel::class.java)
        return view
    }

    /**
     * Bij doorgaan van de app nadat de onPause methode is opgeroepen wordt de onResume methode aangegroepen.
     * Het is één van de methodes van de activity life cycle.
     * Aanmaken van de clicklisteners van de knoppen
     * Als laatste wordt de imageview photoViewer opgevuld met het statische variable photo van de Registeren activity
     */
    override fun onResume() {
        super.onResume()
        cmdVolgende.isEnabled = photoViewModel.isValue
        if(photoViewModel.isValue) {
            cmdVolgende.alpha = 1F
        } else {
            cmdVolgende.alpha = 0.4F
        }

        cmdNeemFoto.setOnClickListener {
            dispatchTakePictureIntent()
        }
        cmdVolgende.setOnClickListener {
         //   photoViewModel.photo = photo
        //    photoViewModel.photoUri = photoUri
            parent.goToGroupDetails()
        }
       photoViewer.setImageBitmap(photoViewModel.photo)
    }

    /**
     * Bitmap uitpakken en in het statische fotovariabele steken,
     * hierna wordt de knop om naar het volgende scherm te gaan aangezet
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        print("test")
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            var foto : Bitmap
            foto = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, photoViewModel.photoUri)
            foto = ImageHelper.getRoundedCornerBitmap(foto, foto.width / 2)
            photoViewModel.photo = foto
            print(foto)
            photoViewModel.isValue = true
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
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoViewModel.photoUri)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    /**
     * TODO
     */
    interface RegisterPhotoCallbacks {
        fun goToGroupDetails()
    }

    /**
     * Dit object is een singleton-object dat met de naam van de klasse genoemd kan worden. Elke methode in dit object kan gebruikt worden in andere klassen.
     */
    companion object {
        fun newInstance(): RegisterPhoto {
            return RegisterPhoto()
        }
    }
}
