import Decision.*

class Situation(
    val text: String,
    val rightDecision: List<Decision>,
    val next: MutableList<Situation> = mutableListOf()
) {
    fun add(situation: Situation) = next.add(situation)
}

class Case(
    name: String,
    val startSituation: Situation
)

fun main() {
    val sit2 = Situation(
        text = "Снимается напряжение 825 В с контактного рельса при стоянке\n" +
                "или следовании поезда на выбеге (тяговые двигатели отключены)",
        rightDecision = listOf(
            WATCH_IN_MIRROR_FLUSH,
            DISP
        )
    )

    val sit2_1 = Situation(
        text = "При подаче напряжения 825 В на контактный рельс, оно\n" +
                "снимается вновь, замечены вспышка или дым",
        rightDecision = listOf(
            STOP_TRAIN_ON_GOOD_PLACE,
            DISP,
        )
    ).also { sit2.add(it) }

    val sit2_1a = Situation(
        text = "Обнаружена вспышка в головной части поезда",
        rightDecision = listOf(
            PVU_DEACT_TKPR_1_2,
            VO_CHECK_TKPR,
            DISP_KV_ON
        )
    ).also { sit2_1.add(it) }

    val sit2_1b = Situation(
        text = "Обнаружена вспышка в хвостовой части поезда",
        rightDecision = listOf(
            PVU_DEACT_TKPR_3,
            VO_CHECK_TKPR,
            DISP_KV_ON
        )
    ).also { sit2_1.add(it) }

    val sit2_2a = Situation(
        text = "После отжатия башмаков токоприемников на головной части вагонов\n" +
                "поезда напряжение 825 В на контактный рельс не подается",
        rightDecision = listOf(
            PVU_ACT_TKPR_1_2,
            PVU_DEACT_TKPR_3,
            VO_CHECK_TKPR,
            DISP_KV_ON
        )
    ).also { sit2_1a.add(it) }

    val sit2_2b = Situation(
        text = "После отжатия башмаков токоприемников на хвостовой части вагонов\n" +
                "поезда напряжение 825 В на контактный рельс не подается",
        rightDecision = listOf(
            PVU_ACT_TKPR_3,
            PVU_DEACT_TKPR_1_2,
            VO_CHECK_TKPR,
            DISP_KV_ON
        )
    ).also { sit2_1b.add(it) }

    val sit2_3 = Situation(
        text = "После отжатия башмаков токоприемников на части вагонов\n" +
                "поезда напряжение 825 В на контактный рельс подалось",
        rightDecision = listOf(
            FOLLOW_35,
            DROP_OFF_PASSENGERS_NEXT_STATION,
            FOLLOW_OFF
        )
    ).also {
        sit2_1a.add(it)
        sit2_1b.add(it)
        sit2_2a.add(it)
        sit2_2b.add(it)
    }

    val sit3 = Situation(
        text = "При подаче напряжения 825 В на контактный рельс, оно\n" +
                "снимается вновь, вспышка или дым не замечены",
        rightDecision = listOf(
            VO_CHECK_TKPR,
            DISP_WHY_KV_OFF
        )
    ).also { sit2.add(it) }

    val sit3_1 = Situation(
        text = "Поездной диспетчер подтверждает неисправность состава",
        rightDecision = listOf(
            PVU_DEACT_TKPR_3
        )
    ).also { sit3.add(it) }

    val sit3_2 = Situation(
        text = "После отжатия башмаков токоприемников на 3 вагоне\n" +
                "напряжение 825 В на контактный рельс не подается",
        rightDecision = listOf()
    )
}