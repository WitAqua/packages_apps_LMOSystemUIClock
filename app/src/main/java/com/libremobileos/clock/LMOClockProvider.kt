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

val LMO_CLOCKS = LMOClockCatalog.clockIds

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

        // Pass already-resolved strings (not resource IDs). SysUI cannot load plugin R.string.*
        // from its own package context, but String values from the plugin context are fine.
        return ClockPickerConfig(
            id = clockId,
            name = LMOClockCatalog.name(pluginContext, clockId),
            description = LMOClockCatalog.description(pluginContext, clockId),
            thumbnail = generateThumbnail(clockId, pluginContext),
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

        val typeface =
            ResourcesCompat.getFont(context, LMOClockCatalog.font(clockId)) ?: Typeface.DEFAULT

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

}
