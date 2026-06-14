package com.example.menzago.data.location

import android.location.Location
import com.example.menzago.data.model.Canteen

object DistanceUtils {

    fun distanceToCanteenMeters(
        userLocation: Location,
        canteen: Canteen
    ): Float {
        val result = FloatArray(1)

        Location.distanceBetween(
            userLocation.latitude,
            userLocation.longitude,
            canteen.latitude,
            canteen.longitude,
            result
        )

        return result[0]
    }

    fun findNearestCanteen(
        userLocation: Location,
        canteens: List<Canteen>
    ): Canteen? {
        return canteens.minByOrNull {
            distanceToCanteenMeters(userLocation, it)
        }
    }
}