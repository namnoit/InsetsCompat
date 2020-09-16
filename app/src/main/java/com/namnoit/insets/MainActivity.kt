package com.namnoit.insets

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var currentWindowInsets: WindowInsetsCompat = WindowInsetsCompat.Builder().build()
    private val currentInsetTypes = mutableSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.setDecorFitsSystemWindows(true) // API 30+
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(root) { _, windowsInsets ->
            currentWindowInsets = windowsInsets
            applyInsets()
        }

//        currentInsetTypes.add(WindowInsetsCompat.Type.statusBars())
//        currentInsetTypes.add(WindowInsetsCompat.Type.navigationBars())
        currentInsetTypes.add(WindowInsetsCompat.Type.systemBars())
    }

    private fun applyInsets(): WindowInsetsCompat {
        val currentInsetTypeMask = currentInsetTypes.fold(0) { accumulator, type ->
            accumulator or type
        }
        val insets = currentWindowInsets.getInsets(currentInsetTypeMask)
        root.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            updateMargins(insets.left, insets.top, insets.right, insets.bottom)
        }
        return WindowInsetsCompat.Builder()
            .setInsets(currentInsetTypeMask, insets)
            .build()
    }
}