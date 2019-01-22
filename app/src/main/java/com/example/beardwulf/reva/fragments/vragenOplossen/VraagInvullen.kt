package com.example.beardwulf.reva.fragments.vragenOplossen

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.beardwulf.reva.extensions.InputRegex
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.beardwulf.reva.R
import com.example.beardwulf.reva.domain.Exhibitor
import com.example.beardwulf.reva.domain.ExhibitorViewModel
import kotlinx.android.synthetic.main.fragment_vraag_invullen.*


/**
 * Fragment voor het tonen van een vraag en inlezen van een antwoord
 */
class VraagInvullen : Fragment() {

    lateinit var parent: QuestionAnswerCallbacks
    lateinit var currentExhibitor: Exhibitor
    lateinit var currentExhibitorViewModel: ExhibitorViewModel

    /**
     * Deze methode wordt gebruikt om informatie over de staat van uw activiteit op te slaan en te herstellen.
     * In gevallen zoals oriëntatieveranderingen, de app afsluiten of een ander scenario dat leidt tot het opnieuw oproepen van onCreate(),
     * kan de savedInstanceState bundel gebruikt worden om de vorige toestandsinformatie opnieuw te laden.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parent = (activity as QuestionAnswerCallbacks)
    }

    /**
     * onCreateView wordt opgeroepen om de lay-out van het fragment "op te blazen"(inflate),
     * d.w.z. dat de grafische initialisatie meestal hier plaatsvindt.
     * Het wordt altijd aangeroepen na de onCreate methode.
     * Toont de vraag en een veld om een antwoord in te vullen.
     * Indien er een antwoord wordt inguvuld, opent het bevestigings fragment
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = (inflater.inflate(R.layout.fragment_vraag_invullen, container, false))
        currentExhibitorViewModel = ViewModelProviders.of(activity!!).get( ExhibitorViewModel::class.java);
        currentExhibitor =currentExhibitorViewModel.getExhibitor().value!!
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
        btnVulIn.setOnClickListener {
            if (InputRegex.controleerLettersCijfers(txtInput.text.toString())) {
                 parent.setAnswer(txtInput.text.toString())
            } else {
                txtInput.setError("Enkel tekst invullen aub");
            }
        }
    }

    /**
     * TODO
     */
    interface QuestionAnswerCallbacks {
        fun setAnswer(answer : String)

        var maxQuestion : Int
    }

    /**
     * Dit object is een singleton-object dat met de naam van de klasse genoemd kan worden. Elke methode in dit object kan gebruikt worden in andere klassen.
     */
    companion object {
        fun newInstance(): VraagInvullen {
            return VraagInvullen()
        }
    }
}
