/*
 * SPDX-FileCopyrightText: 2022 The Android Open Source Project
 * SPDX-FileCopyrightText: 2024-2025 The LibreMobileOS Foundation
 * SPDX-License-Identifier: Apache-2.0
 */

package com.libremobileos.clock

import android.content.Context
import android.view.LayoutInflater
import androidx.core.content.res.ResourcesCompat
import com.android.systemui.plugins.annotations.Requires
import com.android.systemui.plugins.clocks.ClockController
import com.android.systemui.plugins.clocks.ClockMessageBuffers
import com.android.systemui.plugins.clocks.ClockMetadata
import com.android.systemui.plugins.clocks.ClockPickerConfig
import com.android.systemui.plugins.clocks.ClockProviderPlugin
import com.android.systemui.plugins.clocks.ClockSettings

private val TAG = LMOClockProvider::class.simpleName

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

val LMO_CLOCKS = listOf(
    ALBERT_SANS_CLOCK_ID,
    BLAKA_CLOCK_ID,
    CREEPSTER_CLOCK_ID,
    KABLAMMO_CLOCK_ID,
    MODAK_CLOCK_ID,
    MYSTERY_QUEST_CLOCK_ID,
    RUBIK_DIRT_CLOCK_ID,
    RUBIK_DISTRESSED_CLOCK_ID,
    RUBIK_GEMSTONES_CLOCK_ID,
    RUBIK_MARKER_HATCH_CLOCK_ID,
    SUBWAY_CLOCK_ID,
    RIDGE_CLOCK_ID,
    BEAUTY_CLOCK_ID,
    SFPRO_CLOCK_ID,
    SPACEGAME_CLOCK_ID,
    ACCURATIST_CLOCK_ID,
    NOTHINGDOT_CLOCK_ID,
    ASIMOVIAN_CLOCK_ID,
    CABINSKETCH_CLOCK_ID,
    INDIEFLOWER_CLOCK_ID,
    SPECIALELITE_CLOCK_ID,
)

@Requires(target = ClockProviderPlugin::class, version = ClockProviderPlugin.VERSION)
class LMOClockProvider : ClockProviderPlugin {

    private var messageBuffers: ClockMessageBuffers? = null

    private lateinit var pluginContext: Context
    private lateinit var sysuiContext: Context

    override fun onCreate(sysuiCtx: Context, pluginCtx: Context) {
        pluginContext = pluginCtx
        sysuiContext = sysuiCtx
    }

    override fun initialize(buffers: ClockMessageBuffers?) {
        messageBuffers = buffers
    }

    override fun getClocks(): List<ClockMetadata> = LMO_CLOCKS.map { ClockMetadata(it) }

    override fun createClock(ctx: Context, settings: ClockSettings): ClockController? {
        val clockId = settings.clockId
        if (clockId == null || !LMO_CLOCKS.contains(clockId)) {
            throw IllegalArgumentException("${settings.clockId} is unsupported by $TAG")
        }

        return LMOClockController(
            clockId,
            pluginContext,
            sysuiContext,
            LayoutInflater.from(pluginContext),
            pluginContext.resources,
            sysuiContext.resources,
            settings,
            messageBuffers,
        )
    }

    override fun getClockPickerConfig(settings: ClockSettings): ClockPickerConfig {
        val clockId = settings.clockId
        if (clockId == null || !LMO_CLOCKS.contains(clockId) || !this::pluginContext.isInitialized) {
            throw IllegalArgumentException("${settings.clockId} is unsupported by $TAG")
        }

        val thumbnail = ResourcesCompat.getDrawable(
            pluginContext.resources,
            R.drawable.clock_default_thumbnail,
            null
        ) ?: throw NullPointerException("Default thumbnail is null") // not so important but just in case

        // TODO: Check where it's used and fix it correctly
        //       with proper clock names and description.
        //       right now, plugin is broken when using plugin resources.
        return ClockPickerConfig(
            clockId,
            "Default clock",
            "Default clock description",
            // TODO(b/352049256): Update placeholder to actual resource
            thumbnail,
            isReactiveToTone = true,
            axes = emptyList(),
            presetConfig = null,
        )
    }
}
