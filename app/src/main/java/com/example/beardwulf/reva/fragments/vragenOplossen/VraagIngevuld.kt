package com.example.beardwulf.reva.fragments.vragenOplossen

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.beardwulf.reva.R
import com.example.beardwulf.reva.activities.vragenOplossen.VragenOplossen
import com.example.beardwulf.reva.domain.Exhibitor
import com.example.beardwulf.reva.fragments.EindeSpel
import com.example.beardwulf.reva.interfaces.QuestionCallbacks
import kotlinx.android.synthetic.main.fragment_vraag_ingevuld.*

/**
 * Fragment ter bevestiging van een antwoord invullen en versturen
 */
class VraagIngevuld : Fragment() {

    lateinit var parent: QuestionCallbacks

    /**
     * Deze methode wordt gebruikt om informatie over de staat van uw activiteit op te slaan en te herstellen.
     * In gevallen zoals oriëntatieveranderingen, de app afsluiten of een ander scenario dat leidt tot het opnieuw oproepen van onCreate(),
     * kan de savedInstanceState bundel gebruikt worden om de vorige toestandsinformatie opnieuw te laden.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parent = (activity as QuestionCallbacks)
    }

    /**
     * Indien alle vragen zijn inguvld, open het eindscherm fragment. Anders toon de volgende vraag fragment.
     * onCreateView wordt opgeroepen om de lay-out van het fragment "op te blazen"(inflate),
     * d.w.z. dat de grafische initialisatie meestal hier plaatsvindt.
     * Het wordt altijd aangeroepen na de onCreate methode.
    */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_vraag_ingevuld, container, false)
        return view
    }

    /**
     * Bij doorgaan van de app nadat de onPause methode is opgeroepen wordt de onResume methode aangegroepen.
     * Het is één van de methodes van de activity life cycle.
     */
    override fun onResume() {
        super.onResume()
        btnOke.setOnClickListener {
            parent.determineNextMove()
        }
    }

    /**
     * Dit object is een singleton-object dat met de naam van de klasse genoemd kan worden. Elke methode in dit object kan gebruikt worden in andere klassen.
     */
    companion object {
        fun newInstance(): VraagIngevuld {
            return VraagIngevuld()
        }
    }
}
