/*
 * SPDX-FileCopyrightText: DerpFest AOSP
 * SPDX-License-Identifier: Apache-2.0
 */

package com.libremobileos.clock

import android.view.View
import com.android.systemui.log.core.LogLevel
import com.android.systemui.log.core.LogMessage
import com.android.systemui.log.core.Logger
import com.android.systemui.log.core.MessageBuffer

class ClockLogger(private val view: View?, buffer: MessageBuffer, tag: String) :
    Logger(buffer, tag) {

    companion object {
        @JvmStatic
        fun escapeTime(timeStr: String?): String? {
            return timeStr?.replace("\n", "\\n")
        }
    }

    fun onDraw(str: String?) {
        d({ "onDraw(${escapeTime(str1)})" }) { str1 = str ?: "" }
    }

    fun onMeasure(widthSpec: Int, heightSpec: Int) {
        d({ "onMeasure(${getSpecText(int1)}, ${getSpecText(int2)})" }) {
            int1 = widthSpec
            int2 = heightSpec
        }
    }

    fun invalidate() {
        d("invalidate()")
    }

    fun animateDoze(isDozing: Boolean, isAnimated: Boolean) {
        d({ "animateDoze(isDozing=$bool1, isAnimated=$bool2)" }) {
            bool1 = isDozing
            bool2 = isAnimated
        }
    }

    fun animateCharge() {
        d("animateCharge()")
    }

    fun animateFoldAppear() {
        d("animateFoldAppear")
    }

    fun animateAppearOnLockscreen() {
        d("animateAppearOnLockscreen")
    }

    private fun getSpecText(spec: Int): String {
        val size = View.MeasureSpec.getSize(spec)
        val mode = View.MeasureSpec.getMode(spec)
        val modeText = when (mode) {
            View.MeasureSpec.EXACTLY -> "EXACTLY"
            View.MeasureSpec.AT_MOST -> "AT MOST"
            View.MeasureSpec.UNSPECIFIED -> "UNSPECIFIED"
            else -> "$mode"
        }
        return "($size, $modeText)"
    }
}

