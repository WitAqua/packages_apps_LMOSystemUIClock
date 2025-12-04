/*
 * SPDX-FileCopyrightText: DerpFest AOSP
 * SPDX-License-Identifier: Apache-2.0
 */

package com.libremobileos.clock

import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.BOTTOM
import androidx.constraintlayout.widget.ConstraintSet.END
import androidx.constraintlayout.widget.ConstraintSet.PARENT_ID
import androidx.constraintlayout.widget.ConstraintSet.START
import androidx.constraintlayout.widget.ConstraintSet.TOP
import androidx.constraintlayout.widget.ConstraintSet.WRAP_CONTENT
import com.android.systemui.plugins.clocks.AodClockBurnInModel
import com.android.systemui.plugins.clocks.ClockFaceLayout
import com.android.systemui.plugins.clocks.ClockPreviewConfig
import com.android.systemui.plugins.clocks.ClockViewIds

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
}

