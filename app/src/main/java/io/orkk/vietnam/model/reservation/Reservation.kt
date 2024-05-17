import android.os.Parcelable
import io.orkk.vietnam.model.player.Player
import kotlinx.parcelize.Parcelize

@Parcelize
data class Reservation(
    var date: String = "",
    var time: String = "",
    var course: MutableList<Int> = mutableListOf(0, 0, 0), // 0, 1 -> 예약 코스, 2 -> 예약 외 나머지 코스
    var players: MutableList<Player>? = mutableListOf(),
    var team: String? = null,
    var caddy: String? = null
) : Parcelable