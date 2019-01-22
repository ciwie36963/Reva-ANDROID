package com.example.beardwulf.reva.fragments.registreren

import android.content.Intent
import com.example.beardwulf.reva.extensions.InputRegex
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.beardwulf.reva.R
import com.example.beardwulf.reva.activities.registreren.Registreren
import com.example.beardwulf.reva.domain.testApplicationClass
import com.example.beardwulf.reva.interfaces.RegisterCallbacks
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_registreer_groep.*

class RegistreerGroep : Fragment() {

    /**
     * Registreer de gegevens van de groep
     * groepsnaam, groepsleden
     */
    lateinit var parent: RegisterGroupCallbacks

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
        var view = inflater.inflate(R.layout.fragment_registreer_groep, container, false)
        parent = (activity as RegisterGroupCallbacks)
        return view
    }

    /**
     * Bij doorgaan van de app nadat de onPause methode is opgeroepen wordt de onResume methode aangegroepen.
     * Het is één van de methodes van de activity life cycle.
     */
    override fun onResume() {
        super.onResume()
            cmdNaarCategorie.setOnClickListener {
                if (InputRegex.controleerLettersCijfers(txtGroepsnaam.text.toString())) {
                    (activity!!.applicationContext as testApplicationClass).group.name = txtGroepsnaam.text.toString()
                    (activity!!.applicationContext as testApplicationClass).group.description = txtGroepsLeden.text.toString()
                    parent.goToCategories()
                        //parent.setFragment(RegisterCategories.newInstance())
                } else {
                    txtGroepsnaam.setError("groepsnaam: enkel letters en cijfers aub");
                }
            }
    }

    /**
     * TODO
     */
    interface RegisterGroupCallbacks {
        fun goToCategories()

    }

    /**
     * Dit object is een singleton-object dat met de naam van de klasse genoemd kan worden. Elke methode in dit object kan gebruikt worden in andere klassen.
     */
    companion object {
        fun newInstance(): RegistreerGroep {
            return RegistreerGroep()
        }
    }
}
