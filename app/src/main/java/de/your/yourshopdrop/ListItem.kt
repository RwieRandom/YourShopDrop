package de.your.yourshopdrop


//TODO: Mengenangaben, Gewicht mit auswahl von g, kg, Stk usw
data class ListItem (
    var title: String,
    var quantity: String,
    var isChecked: Boolean = false
)