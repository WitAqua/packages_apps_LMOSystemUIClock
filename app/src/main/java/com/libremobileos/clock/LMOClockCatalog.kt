/*
 * SPDX-FileCopyrightText: 2024-2025 The LibreMobileOS Foundation
 * SPDX-FileCopyrightText: DerpFest AOSP
 * SPDX-License-Identifier: Apache-2.0
 */

package com.libremobileos.clock

import android.content.Context
import androidx.annotation.DimenRes
import androidx.annotation.FontRes
import androidx.annotation.StringRes

const val ALBERT_SANS_CLOCK_ID = "AlbertSansClock"
const val BLAKA_CLOCK_ID = "BlakaClock"
const val CREEPSTER_CLOCK_ID = "CreepsterClock"
const val KABLAMMO_CLOCK_ID = "KablammoClock"
const val MODAK_CLOCK_ID = "ModakClock"
const val MYSTERY_QUEST_CLOCK_ID = "MysteryQuestClock"
const val RUBIK_DIRT_CLOCK_ID = "RubikDirtClock"
const val RUBIK_DISTRESSED_CLOCK_ID = "RubikDistressedClock"
const val RUBIK_GEMSTONES_CLOCK_ID = "RubikGemstonesClock"
const val RUBIK_MARKER_HATCH_CLOCK_ID = "RubikMarkerHatchClock"
const val SUBWAY_CLOCK_ID = "SubwayClock"
const val RIDGE_CLOCK_ID = "RidgeClock"
const val BEAUTY_CLOCK_ID = "BeautyClock"
const val SFPRO_CLOCK_ID = "SFProClock"
const val SPACEGAME_CLOCK_ID = "SpaceGameClock"
const val ACCURATIST_CLOCK_ID = "AccuratistClock"
const val NOTHINGDOT_CLOCK_ID = "NothingDotClock"
const val ASIMOVIAN_CLOCK_ID = "AsimovianClock"
const val CABINSKETCH_CLOCK_ID = "CabinSketchClock"
const val INDIEFLOWER_CLOCK_ID = "IndieFlowerClock"
const val SPECIALELITE_CLOCK_ID = "SpecialEliteClock"
const val DEADJIM_CLOCK_ID = "DeadJimClock"

/** Shared metadata for LMO lockscreen clocks. */
object LMOClockCatalog {
    data class Entry(
        val id: String,
        @FontRes val font: Int,
        @StringRes val name: Int,
        @StringRes val description: Int,
        @DimenRes val lineSpacing: Int = R.dimen.keyguard_clock_line_spacing_scale,
    )

    val entries: List<Entry> =
        listOf(
            Entry(
                ALBERT_SANS_CLOCK_ID,
                R.font.albertsans,
                R.string.clock_albert_sans_name,
                R.string.clock_albert_sans_description,
            ),
            Entry(
                BLAKA_CLOCK_ID,
                R.font.blaka,
                R.string.clock_blaka_name,
                R.string.clock_blaka_description,
                R.dimen.keyguard_clock_line_spacing_scale_blaka,
            ),
            Entry(
                CREEPSTER_CLOCK_ID,
                R.font.creepster,
                R.string.clock_creepster_name,
                R.string.clock_creepster_description,
            ),
            Entry(
                KABLAMMO_CLOCK_ID,
                R.font.kablammo,
                R.string.clock_kablammo_name,
                R.string.clock_kablammo_description,
            ),
            Entry(
                MODAK_CLOCK_ID,
                R.font.modak,
                R.string.clock_modak_name,
                R.string.clock_modak_description,
                R.dimen.keyguard_clock_line_spacing_scale_modak,
            ),
            Entry(
                MYSTERY_QUEST_CLOCK_ID,
                R.font.mysteryquest,
                R.string.clock_mystery_quest_name,
                R.string.clock_mystery_quest_description,
            ),
            Entry(
                RUBIK_DIRT_CLOCK_ID,
                R.font.rubikdirt,
                R.string.clock_rubik_dirt_name,
                R.string.clock_rubik_dirt_description,
            ),
            Entry(
                RUBIK_DISTRESSED_CLOCK_ID,
                R.font.rubikdistressed,
                R.string.clock_rubik_distressed_name,
                R.string.clock_rubik_distressed_description,
            ),
            Entry(
                RUBIK_GEMSTONES_CLOCK_ID,
                R.font.rubikgemstones,
                R.string.clock_rubik_gemstones_name,
                R.string.clock_rubik_gemstones_description,
            ),
            Entry(
                RUBIK_MARKER_HATCH_CLOCK_ID,
                R.font.rubikmarkerhatch,
                R.string.clock_rubik_marker_hatch_name,
                R.string.clock_rubik_marker_hatch_description,
            ),
            Entry(
                SUBWAY_CLOCK_ID,
                R.font.subway,
                R.string.clock_subway_name,
                R.string.clock_subway_description,
                R.dimen.keyguard_clock_line_spacing_scale_subway,
            ),
            Entry(
                RIDGE_CLOCK_ID,
                R.font.ridge,
                R.string.clock_ridge_name,
                R.string.clock_ridge_description,
            ),
            Entry(
                BEAUTY_CLOCK_ID,
                R.font.beauty,
                R.string.clock_beauty_name,
                R.string.clock_beauty_description,
            ),
            Entry(
                SFPRO_CLOCK_ID,
                R.font.sfpro_semibold_rounded,
                R.string.clock_sfpro_name,
                R.string.clock_sfpro_description,
                R.dimen.keyguard_clock_line_spacing_scale_sfpro,
            ),
            Entry(
                SPACEGAME_CLOCK_ID,
                R.font.spacegame,
                R.string.clock_spacegame_name,
                R.string.clock_spacegame_description,
            ),
            Entry(
                ACCURATIST_CLOCK_ID,
                R.font.accuratist,
                R.string.clock_accuratist_name,
                R.string.clock_accuratist_description,
            ),
            Entry(
                NOTHINGDOT_CLOCK_ID,
                R.font.nothingdot,
                R.string.clock_nothingdot_name,
                R.string.clock_nothingdot_description,
                R.dimen.keyguard_clock_line_spacing_scale_nothingdot,
            ),
            Entry(
                ASIMOVIAN_CLOCK_ID,
                R.font.asimovian,
                R.string.clock_asimovian_name,
                R.string.clock_asimovian_description,
            ),
            Entry(
                CABINSKETCH_CLOCK_ID,
                R.font.cabinsketch,
                R.string.clock_cabinsketch_name,
                R.string.clock_cabinsketch_description,
            ),
            Entry(
                INDIEFLOWER_CLOCK_ID,
                R.font.indieflower,
                R.string.clock_indieflower_name,
                R.string.clock_indieflower_description,
            ),
            Entry(
                SPECIALELITE_CLOCK_ID,
                R.font.specialelite,
                R.string.clock_specialelite_name,
                R.string.clock_specialelite_description,
            ),
            Entry(
                DEADJIM_CLOCK_ID,
                R.font.deadjim,
                R.string.clock_deadjim_name,
                R.string.clock_deadjim_description,
            ),
        )

    private val byId: Map<String, Entry> = entries.associateBy { it.id }

    val clockIds: List<String> = entries.map { it.id }

    fun require(clockId: String): Entry =
        byId[clockId] ?: throw IllegalArgumentException("Unsupported clock: $clockId")

    fun getOrNull(clockId: String): Entry? = byId[clockId]

    fun name(context: Context, clockId: String): String =
        context.getString(require(clockId).name)

    fun description(context: Context, clockId: String): String =
        context.getString(require(clockId).description)

    @FontRes
    fun font(clockId: String): Int = getOrNull(clockId)?.font ?: R.font.modak

    @DimenRes
    fun lineSpacing(clockId: String): Int =
        getOrNull(clockId)?.lineSpacing ?: R.dimen.keyguard_clock_line_spacing_scale
}
