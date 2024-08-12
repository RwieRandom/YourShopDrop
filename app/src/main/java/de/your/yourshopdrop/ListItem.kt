package de.your.yourshopdrop

data class ListItem (
    var title: String,
    var quantity: Int = 0,
    var unit: String = "",
    var isChecked: Boolean = false
)