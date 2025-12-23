/*
 * SPDX-FileCopyrightText: 2022 The Android Open Source Project
 * SPDX-FileCopyrightText: 2024-2025 The LibreMobileOS Foundation
 * SPDX-FileCopyrightText: 2025 DerpFest AOSP
 * SPDX-License-Identifier: Apache-2.0
 */

package com.libremobileos.clock

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.view.LayoutInflater
import androidx.core.content.res.ResourcesCompat
import com.android.systemui.plugins.annotations.Requires
import com.android.systemui.plugins.keyguard.ui.clocks.ClockController
import com.android.systemui.plugins.keyguard.ui.clocks.ClockMessageBuffers
import com.android.systemui.plugins.keyguard.ui.clocks.ClockMetadata
import com.android.systemui.plugins.keyguard.ui.clocks.ClockPickerConfig
import com.android.systemui.plugins.keyguard.ui.clocks.ClockProviderPlugin
import com.android.systemui.plugins.keyguard.ui.clocks.ClockSettings

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

        val thumbnail = generateThumbnail(clockId, pluginContext)

        // TODO: Check where it's used and fix it correctly
        //       with proper clock names and description.
        //       right now, plugin is broken when using plugin resources.
        return ClockPickerConfig(
            id = clockId,
            name = "Default clock",
            description = "Default clock description",
            thumbnail = thumbnail,
            isReactiveToTone = true,
            axes = emptyList(),
            presetConfig = null,
        )
    }

    private fun generateThumbnail(clockId: String, context: Context): Drawable {
        val width = 144
        val height = 200
        val density = context.resources.displayMetrics.density
        val scaledWidth = (width * density).toInt()
        val scaledHeight = (height * density).toInt()

        val bitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        canvas.drawColor(0x00000000)

        val fontResId = selectFont(clockId)
        val typeface = ResourcesCompat.getFont(context, fontResId) ?: Typeface.DEFAULT

        val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            this.typeface = typeface
            textSize = 96f * density
            color = 0xFFFFFFFF.toInt()
            textAlign = Paint.Align.CENTER
        }

        val clockText = "12\n34"
        val textWidth = textPaint.measureText("12")
        val textHeight = textPaint.descent() - textPaint.ascent()
        
        val x = scaledWidth / 2f
        val y = (scaledHeight / 2f) + (textHeight / 2f) - textPaint.descent()

        val lines = clockText.split("\n")
        val lineHeight = textHeight * 0.7f
        var currentY = y - (lines.size - 1) * lineHeight / 2f
        
        for (line in lines) {
            canvas.drawText(line, x, currentY, textPaint)
            currentY += lineHeight
        }

        return BitmapDrawable(context.resources, bitmap)
    }

    private fun selectFont(clockId: String): Int {
        return when(clockId) {
            ALBERT_SANS_CLOCK_ID -> R.font.albertsans
            BLAKA_CLOCK_ID -> R.font.blaka
            CREEPSTER_CLOCK_ID -> R.font.creepster
            KABLAMMO_CLOCK_ID -> R.font.kablammo
            MODAK_CLOCK_ID -> R.font.modak
            MYSTERY_QUEST_CLOCK_ID -> R.font.mysteryquest
            RUBIK_DIRT_CLOCK_ID -> R.font.rubikdirt
            RUBIK_DISTRESSED_CLOCK_ID -> R.font.rubikdistressed
            RUBIK_GEMSTONES_CLOCK_ID -> R.font.rubikgemstones
            RUBIK_MARKER_HATCH_CLOCK_ID -> R.font.rubikmarkerhatch
            SUBWAY_CLOCK_ID -> R.font.subway
            RIDGE_CLOCK_ID -> R.font.ridge
            BEAUTY_CLOCK_ID -> R.font.beauty
            SFPRO_CLOCK_ID -> R.font.sfpro_semibold_rounded
            SPACEGAME_CLOCK_ID -> R.font.spacegame
            ACCURATIST_CLOCK_ID -> R.font.accuratist
            NOTHINGDOT_CLOCK_ID -> R.font.nothingdot
            ASIMOVIAN_CLOCK_ID -> R.font.asimovian
            CABINSKETCH_CLOCK_ID -> R.font.cabinsketch
            INDIEFLOWER_CLOCK_ID -> R.font.indieflower
            SPECIALELITE_CLOCK_ID -> R.font.specialelite
            else -> R.font.modak
        }
    }
}
