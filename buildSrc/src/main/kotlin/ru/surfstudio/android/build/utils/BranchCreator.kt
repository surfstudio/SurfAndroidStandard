package ru.surfstudio.android.build.utils

import kotlin.random.Random

object BranchCreator {
    private val data = listOf(
            "Achelois",
            "Alcyone",
            "Alectrona",
            "Amphitrite",
            "Antheia",
            "Aphaea",
            "Aphrodite",
            "Artemis",
            "Astraea",
            "Athena",
            "Atropos",
            "Bia",
            "Brizo",
            "Greek",
            "Calliope",
            "Calypso",
            "Celaeno",
            "Ceto",
            "Circe",
            "Clio",
            "Clotho",
            "Cybele",
            "Demeter",
            "Doris",
            "Eileithyia",
            "Electra",
            "Elpis",
            "Enyo",
            "Eos",
            "Erato",
            "Eris",
            "Euterpe",
            "Gaia",
            "Greek",
            "Harmonia",
            "Hebe",
            "Hecate",
            "Hemera",
            "Hera",
            "Hestia",
            "Hygea",
            "Iris",
            "Kotys",
            "Lacheses",
            "Maia",
            "Mania",
            "Melpomene",
            "Merope",
            "Metis",
            "Nemesis",
            "Nike",
            "Nyx",
            "Greek",
            "Peitha",
            "Persephone",
            "Pheme",
            "Polyhymnia",
            "Rhea",
            "Selene",
            "Sterope",
            "Styx",
            "Greek",
            "Taygete",
            "Terpsichore",
            "Thalia",
            "Themis",
            "Thetis",
            "Tyche",
            "Urania"
    )

    private var i = 0

    fun generateBranchName(not: List<String>): String {
        var name = data[i++]
        while (not.contains(name)) {
            name = data[i++]
        }
        return name
    }
}