package com.example.testviewpager2

import android.view.View
import androidx.annotation.Px
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

/**
 * @author p.sabaev
 */
internal class CardPageTransformer(
    @Px private val cardWidth: Int,
    @Px private val distanceBetweenCards: Int
) : ViewPager2.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        with(page) {
            val scale = (1 - abs(position) * (1 - SIDE_CARDS_SCALE)).coerceAtLeast(SIDE_CARDS_SCALE)
            scaleX = scale
            scaleY = scale

            alpha = 1 - abs(position) * (1 - SIDE_CARDS_ALPHA)

            translationX = -1 * position * calculateTranslationX(page.width, scale)
        }
    }

    private fun calculateTranslationX(pageWidth: Int, scale: Float): Float {
        val edgeSpace = (pageWidth - cardWidth) / 2f
        val scaledEdgeSpace = (pageWidth - cardWidth * scale) / 2f
        return edgeSpace + scaledEdgeSpace - distanceBetweenCards
    }

    private companion object {
        const val SIDE_CARDS_SCALE = 0.8f
        const val SIDE_CARDS_ALPHA = 0.5f
    }
}
