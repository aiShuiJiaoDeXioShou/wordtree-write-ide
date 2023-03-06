package lh.wordtree.comm.entity

data class Figure(
    val name: String,
    val description: String,
    val power: String = "",
    val relationship: String = "",
    var figures: ArrayList<Figure>
)