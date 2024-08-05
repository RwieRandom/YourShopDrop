package de.your.yourshopdrop


//TODO: Mengenangaben, Gewicht
//TODO: ListItem in ListItemManager umverlagern --> weniger files
data class ListItem (
    var title: String,
    var isChecked: Boolean = false
)