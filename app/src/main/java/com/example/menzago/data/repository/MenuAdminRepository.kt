package com.example.menzago.data.repository

import com.example.menzago.data.model.DailyMenu
import com.example.menzago.data.model.Dish
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MenuAdminRepository {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun addOrUpdateDish(dish: Dish) {
        firestore.collection("dishes")
            .document(dish.id.toString())
            .set(dish)
            .await()
    }

    suspend fun deleteDish(dishId: Int) {
        firestore.collection("dishes")
            .document(dishId.toString())
            .delete()
            .await()
    }

    suspend fun getDishes(): List<Dish> {
        return firestore.collection("dishes")
            .get()
            .await()
            .documents
            .mapNotNull { it.toObject(Dish::class.java) }
    }

    suspend fun saveDailyMenu(menu: DailyMenu) {
        val documentId = "${menu.canteenId}_${menu.date}"

        firestore.collection("daily_menus")
            .document(documentId)
            .set(menu.copy(id = documentId))
            .await()
    }

    suspend fun getDailyMenu(
        canteenId: Int,
        date: String
    ): DailyMenu? {
        val documentId = "${canteenId}_${date}"

        return firestore.collection("daily_menus")
            .document(documentId)
            .get()
            .await()
            .toObject(DailyMenu::class.java)
    }
}