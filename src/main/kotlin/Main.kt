import Decision.*

class Situation(
    val text: String,
    val rightDecision: List<Decision> = listOf(),
    val next: MutableList<Situation> = mutableListOf()
) {
    fun add(situation: Situation) = next.add(situation)

    fun attachTo(situation: Situation): Situation {
        situation.add(this)
        return this
    }
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
    ).attachTo(sit2)

    val sit2_1a = Situation(
        text = "Обнаружена вспышка в головной части поезда",
        rightDecision = listOf(
            PVU_DEACT_TKPR_1_2,
            VO_CHECK_TKPR,
            DISP_KV_ON
        )
    ).attachTo(sit2_1)

    val sit2_1b = Situation(
        text = "Обнаружена вспышка в хвостовой части поезда",
        rightDecision = listOf(
            PVU_DEACT_TKPR_3,
            VO_CHECK_TKPR,
            DISP_KV_ON
        )
    ).attachTo(sit2_1)

    val sit2_2a = Situation(
        text = "После отжатия башмаков токоприемников на головной части вагонов\n" +
                "поезда напряжение 825 В на контактный рельс не подается",
        rightDecision = listOf(
            PVU_ACT_TKPR_1_2,
            PVU_DEACT_TKPR_3,
            VO_CHECK_TKPR,
            DISP_KV_ON
        )
    ).attachTo(sit2_1a)

    val sit2_2b = Situation(
        text = "После отжатия башмаков токоприемников на хвостовой части вагонов\n" +
                "поезда напряжение 825 В на контактный рельс не подается",
        rightDecision = listOf(
            PVU_ACT_TKPR_3,
            PVU_DEACT_TKPR_1_2,
            VO_CHECK_TKPR,
            DISP_KV_ON
        )
    ).attachTo(sit2_1b)

    val sit2_3 = Situation(
        text = "После отжатия башмаков токоприемников на части вагонов\n" +
                "поезда напряжение 825 В на контактный рельс подалось",
        rightDecision = listOf(
            FOLLOW_35,
            DROP_OFF_PASSENGERS_NEXT_STATION,
            FOLLOW_OFF
        )
    ).attachTo(sit2_1a)
        .attachTo(sit2_1b)
        .attachTo(sit2_2a)
        .attachTo(sit2_2b)

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
    ).also {
        sit3.add(it)
    }.apply {
        add(sit2_2b)
    }

    val sit4_a = Situation(
        text = "Невозможно дистанционно отжать башмаки\n" +
                "токоприемников вагонов по причине наличия давления воздуха в НМ менее\n" +
                "3,5 кгс/см2\n" +
                "или неисправности привода отжатия башмаков токоприемников",
        rightDecision = listOf(
            DISP,
            DISP_KV_OFF,
            CABIN_OFF,
            TRACK_LEFT_TKPR_OFF_1_2,
            TRACK_RIGHT_TKPR_OFF_1_2,
            TRACK_SHORT_OFF,
            DISP_KV_ON
        )
    ).also {
        sit2_1a.add(it)
    }

    val sit4_b = Situation(
        text = "Невозможно дистанционно отжать башмаки\n" +
                "токоприемников вагонов по причине наличия давления воздуха в НМ менее\n" +
                "3,5 кгс/см2\n" +
                "или неисправности привода отжатия башмаков токоприемников",
        rightDecision = listOf(
            DISP,
            DISP_KV_OFF,
            CABIN_OFF,
            TRACK_LEFT_TKPR_OFF_3,
            TRACK_RIGHT_TKPR_OFF_3,
            TRACK_SHORT_OFF,
            DISP_KV_ON,
        )
    ).also {
        sit2_1b.add(it)
    }

    val sit4_c = Situation(
        text = "Невозможно дистанционно отжать башмаки\n" +
                "токоприемников вагонов по причине наличия давления воздуха в НМ менее\n" +
                "3,5 кгс/см2\n" +
                "или неисправности привода отжатия башмаков токоприемников",
        rightDecision = listOf(
            DISP,
            DISP_KV_OFF,
            CABIN_OFF,
            TRACK_LEFT_TKPR_OFF_3,
            TRACK_RIGHT_TKPR_OFF_3,
            TRACK_SHORT_OFF,
            DISP_KV_ON
        )
    ).also {
        sit3_1.add(it)
    }

    val sit5_1a = Situation(
        text = "После отжатия башмаков токоприемников на 1 и 2 вагонах состава\n" +
                "напряжение 825 В на контактный рельс не подается",
        rightDecision = listOf(
            DISP_KV_OFF,
            CABIN_OFF,
            TRACK_LEFT_TKPR_ON_1_2,
            TRACK_LEFT_TKPR_OFF_3,
            TRACK_RIGHT_TKPR_OFF_3,
            TRACK_RIGHT_TKPR_ON_1_2,
            TRACK_SHORT_OFF,
            DISP_KV_ON,
        )
    )

    val sit5_1bc = Situation(
        text = "После отжатия башмаков токоприемников на 3 вагоне состава\n" +
                "напряжение 825 В на контактный рельс не подается",
        rightDecision = listOf(
            DISP_KV_OFF,
            CABIN_OFF,
            TRACK_LEFT_TKPR_OFF_1_2,
            TRACK_LEFT_TKPR_ON_3,
            TRACK_RIGHT_TKPR_ON_3,
            TRACK_RIGHT_TKPR_OFF_1_2,
            TRACK_SHORT_OFF,
            DISP_KV_ON,
        )
    ).also {
        sit4_b.add(it)
        sit4_c.add(it)
    }

    val sit5_a = Situation(
        text = "После отжатия башмаков токоприемников на 1 и 2 вагонах\n" +
                "напряжение 825 В на контактный рельс подалось и не снимается",
        rightDecision = listOf(
            FOLLOW_35,
            DROP_OFF_PASSENGERS_NEXT_STATION,
            FOLLOW_OFF,
        )
    ).also {
        sit4_a.add(it)
        sit5_1bc.add(it)
    }

    val sit5_bc = Situation(
        text = "После отжатия башмаков токоприемников на 3 вагоне\n" +
                "напряжение 825 В на контактный рельс подалось и не снимается",
        rightDecision = listOf(
            FOLLOW_35,
            DROP_OFF_PASSENGERS_NEXT_STATION,
            FOLLOW_OFF,
        )
    ).also {
        sit4_b.add(it)
        sit4_c.add(it)
        sit5_1a.add(it)
    }

    val sit6 = Situation(
        text = "По информации поездного диспетчера напряжение 825 В на\n" +
                "контактном рельсе есть",
        rightDecision = listOf(
            VO_CHECK_TKPR,
            VPU_CHECK_TKPR,
        )
    ).also {
        sit3.add(it)
    }

    val sit6_1 = Situation(
        text = "По монитору машиниста выявлено отжатие токоприемников на\n" +
                "всем составе",
        rightDecision = listOf(
            PVZ_1_TKPR_OFF,
            KV_CHECK_825,
        )
    ).also {
        sit6.add(it)
    }

    val sit6_2 = Situation(
        text = "На головном вагоне напряжение 825 В появилось",
        rightDecision = listOf(
            PVZ_2_3_TKPR_OFF
        )
    ).also {
        sit6_1.add(it)
    }

    val sit6_3 = Situation(
        text = "Напряжение 825 В восстановилось на всех вагонах",
        rightDecision = listOf(
            DISP,
            FOLLOW_WORK
        )
    ).also {
        sit6_2.add(it)
    }

    val sitFinish = Situation(
        text = CASE_FINISHED_DESC
    )
        .attachTo(sit6_3)
        .attachTo(sit5_a)
        .attachTo(sit5_bc)
        .attachTo(sit2_3)

    val case = Case(
        name = "Снятие высокого напряжения на выбеге",
        startSituation = sit2
    ).test()
    // Start case

}