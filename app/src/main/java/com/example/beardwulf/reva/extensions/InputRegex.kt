package com.example.beardwulf.reva.extensions

class InputRegex {

    /**
     * Dit object is een singleton-object dat met de naam van de klasse genoemd kan worden. Elke methode in dit object kan gebruikt worden in andere klassen.
     * controleerLettersCijfers controleert op letters en cijfers
     * controleerLetters controleert op letters
     */
    companion object {
        fun controleerLettersCijfers(input: String) : Boolean = "[a-zA-Z0-9 ]+".toRegex().matches(input)
        fun controleerLetters(input:String):Boolean="[a-zA-Z]+".toRegex().matches(input)
    }

}