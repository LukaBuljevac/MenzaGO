package com.example.menzago.data.mock

import com.example.menzago.data.model.Canteen
import com.example.menzago.data.model.Comment
import com.example.menzago.data.model.Dish

object MockData {

    val canteens = listOf(
        Canteen(
            id = 1,
            name = "Studentska menza Campus",
            location = "Kampus",
            distanceMeters = 350,
            isOpen = true,
            workingHours = "08:00 - 20:00",
            latitude = 45.55664573198663,
            longitude = 18.707788611664668,
            isFavorite = true
        ),
        Canteen(
            id = 2,
            name = "Studentska menza Istarska",
            location = "STUC",
            distanceMeters = 700,
            isOpen = true,
            workingHours = "08:00 - 20:00",
            latitude = 45.55621205614703,
            longitude = 18.69397807672979,
            isFavorite = false
        ),
        Canteen(
            id = 3,
            name = "Studentska menza Gaudeamus",
            location = "STUC",
            distanceMeters = 1100,
            isOpen = true,
            workingHours = "08:00 - 20:00",
            latitude = 45.55597511299069,
            longitude = 18.6941793998167,
            isFavorite = false
        )
    )

    val dishes = listOf(
        Dish(
            id = 1,
            name = "Piletina s rižom",
            description = "Pečena piletina poslužena s kuhanom rižom i sezonskom salatom.",
            category = "Glavno jelo",
            calories = 620,
            allergens = listOf("Mlijeko"),
            rating = 4.6,
            isFavorite = true
        ),
        Dish(
            id = 2,
            name = "Pasta bolognese",
            description = "Tjestenina u bogatom umaku od mljevenog mesa i rajčice.",
            category = "Glavno jelo",
            calories = 710,
            allergens = listOf("Gluten", "Jaja"),
            rating = 4.8,
            isFavorite = false
        ),
        Dish(
            id = 3,
            name = "Povrtni rižoto",
            description = "Kremasti rižoto s tikvicama, mrkvom i paprikom.",
            category = "Glavno jelo",
            calories = 540,
            allergens = listOf("Celer"),
            rating = 4.3,
            isFavorite = false
        ),
        Dish(
            id = 4,
            name = "Juha od rajčice",
            description = "Lagana domaća juha od rajčice s krutonima.",
            category = "Juha",
            calories = 180,
            allergens = listOf("Gluten"),
            rating = 4.2,
            isFavorite = false
        ),
        Dish(
            id = 5,
            name = "Palačinke s marmeladom",
            description = "Desert od mekanih palačinki punjenih marmeladom od marelice.",
            category = "Desert",
            calories = 420,
            allergens = listOf("Gluten", "Jaja", "Mlijeko"),
            rating = 4.9,
            isFavorite = true
        )
    )

    val comments = listOf(
        Comment(
            id = 1,
            userName = "Ana",
            text = "Jelo je bilo stvarno ukusno i porcija je bila super.",
            rating = 5
        ),
        Comment(
            id = 2,
            userName = "Marko",
            text = "Dobro, ali moglo je biti malo toplije.",
            rating = 4
        ),
        Comment(
            id = 3,
            userName = "Petra",
            text = "Jedno od boljih jela ovaj tjedan.",
            rating = 5
        )
    )
}