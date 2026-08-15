/*
 * SPDX-FileCopyrightText: DerpFest AOSP
 * SPDX-License-Identifier: Apache-2.0
 */

package com.libremobileos.clock

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.BOTTOM
import androidx.constraintlayout.widget.ConstraintSet.END
import androidx.constraintlayout.widget.ConstraintSet.PARENT_ID
import androidx.constraintlayout.widget.ConstraintSet.START
import androidx.constraintlayout.widget.ConstraintSet.TOP
import androidx.constraintlayout.widget.ConstraintSet.WRAP_CONTENT
import com.android.compose.animation.scene.MovableElementContentScope
import com.android.systemui.plugins.keyguard.ui.clocks.AodClockBurnInModel
import com.android.systemui.plugins.keyguard.ui.clocks.ClockFaceLayout
import com.android.systemui.plugins.keyguard.ui.clocks.ClockPreviewConfig
import com.android.systemui.plugins.keyguard.ui.clocks.ClockViewIds
import com.android.systemui.plugins.keyguard.ui.composable.elements.BaseLockscreenElement.ElementSource
import com.android.systemui.plugins.keyguard.ui.composable.elements.LockscreenElementKeys
import com.android.systemui.plugins.keyguard.ui.composable.elements.LockscreenScope
import com.android.systemui.plugins.keyguard.ui.composable.elements.MovableLockscreenElement

class SimpleClockFaceLayout(
    private val view: View,
    private val isLargeClock: Boolean
) : ClockFaceLayout {
    private val viewId: Int = if (isLargeClock) {
        ClockViewIds.LOCKSCREEN_CLOCK_VIEW_LARGE
    } else {
        ClockViewIds.LOCKSCREEN_CLOCK_VIEW_SMALL
    }

    init {
        view.id = viewId
    }

    override val views: List<View> = listOf(view)

    override val elements: List<MovableLockscreenElement> by lazy {
        listOf(if (isLargeClock) LargeClockElement() else SmallClockElement())
    }

    private inner class SmallClockElement : MovableLockscreenElement {
        override val key = LockscreenElementKeys.Clock.Small
        override val context: Context = view.context
        override val source = ElementSource.DYNAMIC

        @Composable
        override fun LockscreenScope<MovableElementContentScope>.LockscreenElement() {
            // Override the default View.TEXT_DIRECTION_FIRST_STRONG, since the time only contains
            // numbers and special characters, which are directionally weak. This means the Unicode
            // Bidirectional Algorithm would fall back to the locale default direction, yet the time
            // should always have LTR directionality.
            view.textDirection = View.TEXT_DIRECTION_LTR

            ClockView(
                view,
                // Compose hands out AT_MOST widths that are too tight while the scene transition
                // layout animates this element, which would otherwise wrap the time onto a second
                // line. Measuring unbounded keeps it on a single line and lets the time overflow
                // the slot instead. AnimatableClockView cannot use setSingleLine() for this: that
                // turns on horizontal scrolling, which lays the text out into a VERY_WIDE (1M px)
                // Layout, and since the clock is always center aligned the time would then be
                // drawn half a million pixels off screen.
                //
                // Compose is served by SystemUI at runtime, where R8 has already stripped every
                // entry point SystemUI itself does not call. Only the default argument bridge of
                // wrapContentWidth() survives there, so the alignment cannot be passed explicitly.
                Modifier.wrapContentWidth(unbounded = true)
                    .fillMaxHeight()
                    .burnInAware(isClock = true)
                    .nonAuthUI(),
            )
        }
    }

    private inner class LargeClockElement : MovableLockscreenElement {
        override val key = LockscreenElementKeys.Clock.Large
        override val context: Context = view.context
        override val source = ElementSource.DYNAMIC

        @Composable
        override fun LockscreenScope<MovableElementContentScope>.LockscreenElement() {
            ClockView(
                view,
                Modifier.wrapContentSize().burnInAware(isClock = true).nonAuthUI(),
            )
        }
    }

    override fun applyConstraints(constraints: ConstraintSet): ConstraintSet {
        view.id = viewId
        return constraints
    }

    override fun applyExternalDisplayPresentationConstraints(
        constraints: ConstraintSet
    ): ConstraintSet {
        if (isLargeClock) {
            return constraints.apply {
                constrainWidth(ClockViewIds.LOCKSCREEN_CLOCK_VIEW_LARGE, WRAP_CONTENT)
                constrainHeight(ClockViewIds.LOCKSCREEN_CLOCK_VIEW_LARGE, WRAP_CONTENT)
                connect(ClockViewIds.LOCKSCREEN_CLOCK_VIEW_LARGE, TOP, PARENT_ID, TOP)
                connect(ClockViewIds.LOCKSCREEN_CLOCK_VIEW_LARGE, BOTTOM, PARENT_ID, BOTTOM)
                connect(ClockViewIds.LOCKSCREEN_CLOCK_VIEW_LARGE, START, PARENT_ID, START)
                connect(ClockViewIds.LOCKSCREEN_CLOCK_VIEW_LARGE, END, PARENT_ID, END)
            }
        }
        return constraints
    }

    override fun applyPreviewConstraints(
        clockPreviewConfig: ClockPreviewConfig,
        constraints: ConstraintSet,
    ): ConstraintSet {
        val res = view.context.resources
        return constraints.apply {
            if (isLargeClock) {
                constrainWidth(ClockViewIds.LOCKSCREEN_CLOCK_VIEW_LARGE, WRAP_CONTENT)
                constrainHeight(ClockViewIds.LOCKSCREEN_CLOCK_VIEW_LARGE, WRAP_CONTENT)
                constrainMaxHeight(ClockViewIds.LOCKSCREEN_CLOCK_VIEW_LARGE, 0)

                val largeClockTopMargin = clockPreviewConfig.statusBarHeight + 
                    clockPreviewConfig.clockTopMargin

                connect(
                    ClockViewIds.LOCKSCREEN_CLOCK_VIEW_LARGE,
                    TOP,
                    PARENT_ID,
                    TOP,
                    largeClockTopMargin,
                )
                connect(ClockViewIds.LOCKSCREEN_CLOCK_VIEW_LARGE, START, PARENT_ID, START)
                connect(ClockViewIds.LOCKSCREEN_CLOCK_VIEW_LARGE, END, PARENT_ID, END)

                clockPreviewConfig.udfpsTop?.let { udfpsTop ->
                    connect(
                        ClockViewIds.LOCKSCREEN_CLOCK_VIEW_LARGE,
                        BOTTOM,
                        PARENT_ID,
                        BOTTOM,
                        (res.displayMetrics.heightPixels - udfpsTop).toInt(),
                    )
                } ?: clockPreviewConfig.lockViewId?.let { lockViewId ->
                    connect(ClockViewIds.LOCKSCREEN_CLOCK_VIEW_LARGE, BOTTOM, lockViewId, TOP)
                } ?: run {
                    connect(
                        ClockViewIds.LOCKSCREEN_CLOCK_VIEW_LARGE,
                        BOTTOM,
                        PARENT_ID,
                        BOTTOM,
                    )
                }
            } else {
                constrainWidth(ClockViewIds.LOCKSCREEN_CLOCK_VIEW_SMALL, WRAP_CONTENT)
                constrainHeight(ClockViewIds.LOCKSCREEN_CLOCK_VIEW_SMALL, WRAP_CONTENT)

                connect(
                    ClockViewIds.LOCKSCREEN_CLOCK_VIEW_SMALL,
                    START,
                    PARENT_ID,
                    START,
                    clockPreviewConfig.statusViewMarginHorizontal,
                )

                val smallClockTopMargin = clockPreviewConfig.getSmallClockTopPadding()
                connect(
                    ClockViewIds.LOCKSCREEN_CLOCK_VIEW_SMALL,
                    TOP,
                    PARENT_ID,
                    TOP,
                    smallClockTopMargin,
                )
            }
        }
    }

    override fun applyAodBurnIn(aodBurnInModel: AodClockBurnInModel) {
        view.scaleX = aodBurnInModel.scale
        view.scaleY = aodBurnInModel.scale
        view.translationX = aodBurnInModel.translationX
        view.translationY = aodBurnInModel.translationY
    }

    private companion object {
        @Composable
        fun ClockView(view: View, modifier: Modifier) {
            AndroidView(
                factory = {
                    FrameLayout(it).apply {
                        // Clock views regularly draw outside their bounds, and this is the only
                        // layer that clips by default.
                        clipChildren = false
                        clipToPadding = false
                    }
                },
                update = { parent ->
                    parent.removeAllViews()
                    (view.parent as? ViewGroup)?.removeView(view)
                    parent.addView(view)
                },
                modifier = modifier,
            )
        }
    }
}

