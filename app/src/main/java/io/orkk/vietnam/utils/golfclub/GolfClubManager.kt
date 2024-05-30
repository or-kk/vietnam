package io.orkk.vietnam.utils.golfclub

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.orkk.vietnam.model.ClubPosItem
import io.orkk.vietnam.model.CourseData
import io.orkk.vietnam.model.GeoPoint
import timber.log.Timber

class GolfClubManager {

    private val _golfClubName = MutableLiveData<String>()
    val golfClubName: LiveData<String>
        get() = _golfClubName

    private val _golfClubCode = MutableLiveData<String>()
    val golfClubCode: LiveData<String>
        get() = _golfClubCode

    private val worldAreaList: ArrayList<GeoPoint> = ArrayList<GeoPoint>()

    //    private var worldAreaMinGeo: GeoPoint? = null
    private var worldAreaMinGeo: GeoPoint? = null

    //    private var worldAreaMaxGeo: GeoPoint? = null
    private var worldAreaMaxGeo: GeoPoint? = null

    private val clubAreaList: ArrayList<ClubPosItem> = ArrayList<ClubPosItem>()

    private val courseDataList: ArrayList<CourseData> = ArrayList<CourseData>()

    fun setGolfClubName(name: String) {
        _golfClubName.value = name
    }

    fun setGolfClubCode(code: String) {
        _golfClubCode.value = code
    }

    fun setWorldArea() {
        worldAreaMinGeo = null
        worldAreaMaxGeo = null
        for (i in worldAreaList.indices) {
            if (worldAreaMinGeo == null || worldAreaMaxGeo == null) {
                worldAreaMinGeo = GeoPoint(worldAreaList[i].x, worldAreaList[i].y)
                worldAreaMaxGeo = GeoPoint(worldAreaList[i].x, worldAreaList[i].y)
            } else {
                if (worldAreaMaxGeo!!.x < worldAreaList[i].x) {
                    worldAreaMaxGeo!!.x = worldAreaList[i].x
                } else if (worldAreaMinGeo!!.x > worldAreaList[i].x) {
                    worldAreaMinGeo!!.x = worldAreaList[i].x
                }
                if (worldAreaMaxGeo!!.y < worldAreaList[i].y) {
                    worldAreaMaxGeo!!.y = worldAreaList[i].y
                } else if (worldAreaMinGeo!!.y > worldAreaList[i].y) {
                    worldAreaMinGeo!!.y = worldAreaList[i].y
                }
            }
        }
    }

    fun getCourseName(courseNumber: Int?): String? {
        try {
            if (courseNumber == 0) return null
            for (course in courseDataList) {
                if (course.number == courseNumber) {
                    return course.name
                }
            }
        } catch (e: Exception) {
            Timber.e("getCourseName Exception -> ${e.message}")
        }
        return null
    }
}