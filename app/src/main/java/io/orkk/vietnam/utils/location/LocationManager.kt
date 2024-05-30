package io.orkk.vietnam.utils.location

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.orkk.vietnam.model.GolfClubData
import io.orkk.vietnam.model.game.GameElapsedTimeType
import io.orkk.vietnam.model.game.GamePlayStateType
import io.orkk.vietnam.model.section.SectionType
import io.orkk.vietnam.utils.game.GamePlayManager
import io.orkk.vietnam.utils.game.GameTimeManager
import io.orkk.vietnam.utils.golfclub.GolfClubManager
import io.orkk.vietnam.utils.reservation.ReservationManager
import timber.log.Timber
import javax.inject.Inject

class LocationManager {

    @Inject
    lateinit var reservationManager: ReservationManager

    @Inject
    lateinit var gameTimeManager: GameTimeManager

    @Inject
    lateinit var gamePlayManager: GamePlayManager

    @Inject
    lateinit var golfClubManager: GolfClubManager

    // Current latitude live data start.
    private val _currentLatitude = MutableLiveData<Double>()
    val currentLatitude: LiveData<Double>
        get() = _currentLatitude

    private fun setCurrentLatitude(latitude: Double) {
        _currentLatitude.value = latitude
    }
    // Current latitude live data end.

    // Current longitude live data start.
    private val _currentLongitude = MutableLiveData<Double>()
    val currentLongitude: LiveData<Double>
        get() = _currentLongitude

    private fun setCurrentLongitude(longitude: Double) {
        _currentLongitude.value = longitude
    }
    // Current longitude live data end.

    // Base latitude live data start.
    private val _baseLatitude = MutableLiveData<Double>()
    val baseLatitude: LiveData<Double>
        get() = _baseLatitude

    private fun setBaseLatitude(latitude: Double) {
        _baseLatitude.value = latitude
    }
    // Base latitude live data end.

    // Base longitude live data start.
    private val _baseLongitude = MutableLiveData<Double>()
    val baseLongitude: LiveData<Double>
        get() = _baseLongitude

    private fun setBaseLongitude(longitude: Double) {
        _baseLongitude.value = longitude
    }
    // Base longitude live data end.

    // Current course index live data start.
    private val _currentCourseIndex = MutableLiveData<Int>()
    val currentCourseIndex: LiveData<Int>
        get() = _currentCourseIndex

    private fun setCurrentCourseIndex(courseIndex: Int) {
        _currentCourseIndex.value = courseIndex
    }
    // Current course index live data end.

    // Current hole live data start.
    private val _currentHoleIndex = MutableLiveData<Int>()
    val currentHoleIndex: LiveData<Int>
        get() = _currentHoleIndex

    private fun setCurrentHoleIndex(holeIndex: Int) {
        _currentHoleIndex.value = holeIndex
    }
    // Current hole live data end.

    // Current section index live data start.
    private val _currentSectionIndex = MutableLiveData<Int>(-1)
    val currentSectionIndex: LiveData<Int>
        get() = _currentSectionIndex

    private fun setCurrentSectionIndex(sectionIndex: Int) {
        _currentSectionIndex.value = sectionIndex
    }
    // Current section index live data end.


    fun whereAmI(latitude: Double, longitude: Double) {
        analyzePosition(latitude, longitude)
        showCurrentLocationLog()

        if (currentHoleIndex.value == 0 && currentSectionIndex.value == 0) { // when hole index is 0 and section index is 0

            if (reservationManager.isAddedGame()) {
                if (gamePlayManager.isGameFinished.value == true || gameTimeManager.elapsedTime == GameElapsedTimeType.ADD_GAME_END_TIME.code) {
                    gamePlayManager.setGamePlayState(GamePlayStateType.VIEW_STATE_NONE_GAME.code)
                } else {
                    gamePlayManager.setGamePlayState(GamePlayStateType.VIEW_STATE_WAIT.code)
                }
            } else {
                if (gamePlayManager.isGameFinished.value == true || gameTimeManager.elapsedTime == GameElapsedTimeType.SECOND_HALF_END_TIME.code) {
                    gamePlayManager.setGamePlayState(GamePlayStateType.VIEW_STATE_NONE_GAME.code)
                } else {
                    gamePlayManager.setGamePlayState(GamePlayStateType.VIEW_STATE_WAIT.code)
                }
            }
        } else {
            if (gamePlayManager.isCourseStart.value == true ||
                currentSectionIndex.value == SectionType.HOLE_SECTION_TEE.code ||
                currentSectionIndex.value == SectionType.HOLE_SECTION_SECOND1.code ||
                currentSectionIndex.value == SectionType.HOLE_SECTION_SECOND2.code ||
                currentSectionIndex.value == SectionType.HOLE_SECTION_GREEN.code ||
                currentSectionIndex.value!! >= SectionType.HOLE_SECTION_END.code
            ) {
                if (currentHoleIndex.value == 9 && currentSectionIndex.value!! >= SectionType.HOLE_SECTION_END.code) { // for 9 hole
                    setCurrentHoleIndex(0)
                    setCurrentSectionIndex(0)
                } else {
                    val courseName = golfClubManager.getCourseName(courseNumber = currentCourseIndex.value)
                    if (courseName != null) {
                        gamePlayManager.setGamePlayState(GamePlayStateType.VIEW_STATE_HOLE.code)

                        if (currentSectionIndex.value == SectionType.HOLE_SECTION_TEE.code) {
                            handleCourseProgress()
                        }
                    }
                }
            }
        }
    }

    private fun handleCourseProgress() {
        when (gamePlayManager.progressCourseCount) {
            0 -> initProgressCourse()
            1, 2, 3 -> checkAndFinishCourse(gamePlayManager.progressCourseCount)
        }
    }

    private fun initProgressCourse() {
        gamePlayManager.progressCourseCount = 1
        gamePlayManager.progressHoleCount = 0
    }

    private fun checkAndFinishCourse(courseCount: Int) {
        if (currentHoleIndex.value == 9 && gamePlayManager.isCourseCountActive.value == true) {
            when (courseCount) {
                1 -> gamePlayManager.setFirstCourseFinished()
                2 -> gamePlayManager.setSecondCourseFinished()
                3 -> gamePlayManager.setAddGameCourseFinished()
            }
        }
    }

    private fun showCurrentLocationLog() {
        Timber.v("Current location is lat -> $currentLatitude lon -> $currentLongitude")
    }

    private fun analyzePosition(latitude: Double, longitude: Double) { // 위치 분석
        setCurrentLatitude(latitude)
        setCurrentLongitude(longitude)
    }

    fun isAreaGolfClub(location: Location): Boolean {
        return (location.longitude < GolfClubData().mWMaxGeo!!.x
                && location.longitude > GolfClubData().mWMinGeo!!.x
                && location.latitude < GolfClubData().mWMaxGeo!!.y
                && location.latitude > GolfClubData().mWMinGeo!!.y)
    }
}