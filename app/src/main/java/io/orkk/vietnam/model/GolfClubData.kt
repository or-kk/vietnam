package io.orkk.vietnam.model

import timber.log.Timber

data class GolfClubData(
    var mName: String = "",
    var mCode: String = "",
    private val mArWBR: ArrayList<GeoPoint> = ArrayList(),
    var mWMinGeo: GeoPoint? = null,
    var mWMaxGeo: GeoPoint? = null,
    private val mArClubArea: ArrayList<ClubPosItem> = ArrayList(),
    private val mArMBR: ArrayList<GeoPoint> = ArrayList(),
    var mArCrsData: ArrayList<CourseData> = ArrayList()
) {
    fun getCourseListData(): ArrayList<CourseData> {
        return mArCrsData
    }

    fun setArCrsData(course: ArrayList<CourseData>) {
        mArCrsData = course
    }

    fun setClubArea(ca: ArrayList<ClubPosItem>) {
        mArClubArea.clear()
        mArClubArea.addAll(ca)
    }

    fun addClubArea(caGP: ClubPosItem) {
        mArClubArea.add(caGP)
    }

    fun getClubArea(): ArrayList<ClubPosItem> {
        return mArClubArea
    }

    fun getClubAreaIndex(idx: Int): ClubPosItem {
        return mArClubArea[idx]
    }

    fun getClubAreaCnt(): Int {
        return mArClubArea.size
    }

    fun setMBR(mbr: ArrayList<GeoPoint>) {
        mArMBR.clear()
        mArMBR.addAll(mbr)
    }

    fun getMBR(): ArrayList<GeoPoint> {
        return mArMBR
    }

    fun setWBR(wbr: ArrayList<GeoPoint>) {
        mArWBR.clear()
        mArWBR.addAll(wbr)
    }

    fun addWBR(wbrGP: GeoPoint) {
        mArWBR.add(wbrGP)
    }

    fun getWBR(): ArrayList<GeoPoint> {
        return mArWBR
    }

    fun setWorldArea() {
        mWMinGeo = null
        mWMaxGeo = null
        for (i in mArWBR.indices) {
            if (mWMinGeo == null || mWMaxGeo == null) {
                mWMinGeo = GeoPoint(mArWBR[i].x, mArWBR[i].y)
                mWMaxGeo = GeoPoint(mArWBR[i].x, mArWBR[i].y)
            } else {
                if (mWMaxGeo!!.x < mArWBR[i].x) {
                    mWMaxGeo!!.x = mArWBR[i].x
                } else if (mWMinGeo!!.x > mArWBR[i].x) {
                    mWMinGeo!!.x = mArWBR[i].x
                }
                if (mWMaxGeo!!.y < mArWBR[i].y) {
                    mWMaxGeo!!.y = mArWBR[i].y
                } else if (mWMinGeo!!.y > mArWBR[i].y) {
                    mWMinGeo!!.y = mArWBR[i].y
                }
            }
        }
    }

    fun getMinGeo(): GeoPoint? {
        return mWMinGeo
    }

    fun getMaxGeo(): GeoPoint? {
        return mWMaxGeo
    }

    fun getCourseData(courseName: String): CourseData? {
        for (data in mArCrsData) {
            if (data.mName == courseName) {
                return data
            }
        }
        return null
    }

    fun getCrsName(crsNo: Int): String? {
        try {
            for (course in mArCrsData) {
                if (course.mNo == crsNo) {
                    return course.mName
                }
            }
        } catch (e: Exception) {
            Timber.e("${e.message}")
        }
        return null
    }
}
