package com.namnoit.insets

import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsAnimation
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding


class InsetsAnimationCallback(private val root: View) :
    WindowInsetsAnimation.Callback(DISPATCH_MODE_STOP),
    OnApplyWindowInsetsListener {

    private var startBottom = 0

    private var endBottom = 0

    private val insetsTypes = WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()

    /**
     * If animateKeyboard == true, onApplyWindowInsets will update padding to view. This case happen
     * when insets applied without keyboard animation.
     * Otherwise, onApplyWindowInsets just calculate end bottom to animate.
     */
    private var animateKeyboard = false

    override fun onPrepare(animation: WindowInsetsAnimation) {
        super.onPrepare(animation)
        val insets = ViewCompat.getRootWindowInsets(root)
        startBottom = insets?.getInsets(insetsTypes)?.bottom ?: 0
        animateKeyboard = true
    }

    override fun onApplyWindowInsets(
        v: View?,
        insets: WindowInsetsCompat?
    ): WindowInsetsCompat {
        insets?.let {
            if (!animateKeyboard) {
                // Apply insets using padding
                val typeInsets = it.getInsets(insetsTypes)
                root.updatePadding(
                    top = typeInsets.top,
                    bottom = typeInsets.bottom
                )
            } else {
                endBottom = it.getInsets(insetsTypes).bottom
            }
        }
        return WindowInsetsCompat.CONSUMED
    }

    override fun onProgress(
        p0: WindowInsets,
        p1: MutableList<WindowInsetsAnimation>
    ): WindowInsets {
        val animation = p1[0]
        root.updatePadding(bottom = (startBottom + (endBottom - startBottom) * animation.interpolatedFraction).toInt())
        return p0
    }

    override fun onEnd(animation: WindowInsetsAnimation) {
        root.updatePadding(bottom = endBottom)
        animateKeyboard = false
    }

}