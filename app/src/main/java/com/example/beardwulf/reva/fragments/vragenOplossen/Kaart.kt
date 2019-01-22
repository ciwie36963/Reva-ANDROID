package com.example.beardwulf.reva.fragments.vragenOplossen

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.davemorrissey.labs.subscaleview.ImageSource

import com.davemorrissey.labs.subscaleview.*
import com.example.beardwulf.reva.R
import com.example.beardwulf.reva.activities.registreren.Registreren
import com.example.beardwulf.reva.activities.vragenOplossen.VragenOplossen
import com.example.beardwulf.reva.domain.Category
import com.example.beardwulf.reva.domain.Exhibitor
import com.example.beardwulf.reva.domain.ExhibitorViewModel
import com.example.beardwulf.reva.interfaces.QuestionCallbacks
import com.example.beardwulf.reva.views.PinView
import kotlinx.android.synthetic.main.fragment_kaart.*
import kotlinx.android.synthetic.main.fragment_vraag_invullen.*
import org.jetbrains.anko.find
import org.jetbrains.anko.image
import kotlin.math.exp

class Kaart : Fragment() {

    lateinit var parent: MapCallbacks
    val beaconSize = 100
    lateinit var currentExhibitor: Exhibitor
    private var previousExhibitor: Exhibitor? = null
    lateinit var currentExhibitorViewModel: ExhibitorViewModel

    /**
     * Deze methode wordt gebruikt om informatie over de staat van uw activiteit op te slaan en te herstellen.
     * In gevallen zoals oriëntatieveranderingen, de app afsluiten of een ander scenario dat leidt tot het opnieuw oproepen van onCreate(),
     * kan de savedInstanceState bundel gebruikt worden om de vorige toestandsinformatie opnieuw te laden.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parent = (activity as MapCallbacks)
    }

    /**
     * onCreateView wordt opgeroepen om de lay-out van het fragment "op te blazen"(inflate),
     * d.w.z. dat de grafische initialisatie meestal hier plaatsvindt.
     * Het wordt altijd aangeroepen na de onCreate methode.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_kaart, container, false)
        Log.d("MAP", "MAP START")
        currentExhibitorViewModel = ViewModelProviders.of(activity!!).get( ExhibitorViewModel::class.java);
        currentExhibitor = currentExhibitorViewModel.getExhibitor().value!!
            previousExhibitor=   currentExhibitorViewModel.getPreviousExhibitor()
        Log.d("MAP", currentExhibitor.name)
        return view
    }

    /**
     * Bij doorgaan van de app nadat de onPause methode is opgeroepen wordt de onResume methode aangegroepen.
     * Het is één van de methodes van de activity life cycle.
     */
    override fun onResume() {
        super.onResume()
        showNextExhibitor(currentExhibitor)
        btnVraag.setOnClickListener {
           parent.goToNextQuestion()
        }
        if (currentExhibitor.question.counter == parent.maxQuestion) {
            btnVraag.isEnabled = false
        }

        if (VragenOplossen.getSizeName(activity!!.applicationContext) === "large" || VragenOplossen.getSizeName(activity!!.applicationContext) === "xlarge") {
            if (VragenOplossen.determineOrientation(activity!!.applicationContext) == true) {
                btnVraag.visibility = View.INVISIBLE
            }
        }
        }

    /**
     * Methode showNextExhibitor toont de volgende exposant op de map adhv een pinpooint,
     * ook de naam en de categorie wordt getoond
     */
    fun showNextExhibitor(exhibitor: Exhibitor) {
        txtExhibitorName.setText((exhibitor.question.counter).toString() + ". " + exhibitor.name)
        txtCategoryName.setText(exhibitor.category)

        if(KaartConstraintLayout.childCount ==2) {
            KaartConstraintLayout.removeViewAt(1)
        }

        var xCo = exhibitor.coordinates.xCo
        var yCo = exhibitor.coordinates.yCo

        imageKaart.setImage(ImageSource.asset("grondplan.jpg"))
        imageKaart.setPin(PointF(xCo.toFloat(), yCo.toFloat()))
        previousExhibitor?.let {
            Log.d("PIN", "SET PIN")
            imageKaart.setPreviousPin(PointF(it.coordinates.xCo.toFloat(), it.coordinates.yCo.toFloat()))
        }
    }

    /**
     * TODO
     */
    interface MapCallbacks {
        fun goToNextQuestion()
        var maxQuestion : Int
    }

    /**
     * Dit object is een singleton-object dat met de naam van de klasse genoemd kan worden. Elke methode in dit object kan gebruikt worden in andere klassen.
     */
    companion object {
        fun newInstance(): Kaart {
            return Kaart()
        }
    }
}
