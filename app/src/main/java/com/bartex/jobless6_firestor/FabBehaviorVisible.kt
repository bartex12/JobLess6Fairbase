package com.bartex.jobless6_firestor

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

/*
* Класс определяет поведение плавающей кнопки при прокрутке списка заметок
* если движение вверх и кнопка видна, делаем её невидимой
* если движение вниз и кнопка не видна, делаем её видимой
* класс на Java сделан в дз A3L5_home по материальному дизайну
* */
class FabBehaviorVisible(context: Context, attrs: AttributeSet) : FloatingActionButton.Behavior() {

    @SuppressLint("RestrictedApi")
    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        super.onNestedScroll(
            coordinatorLayout,
            child,
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type
        )

        //если движение вверх и кнопка видна, делаем невидимой
        if (dyConsumed > 0 && child.visibility == View.VISIBLE) {
            child.visibility = View.INVISIBLE
            //если движение вниз и кнопка не видна, делаем видимой
        } else if (dyConsumed < 0 && child.visibility != View.VISIBLE) {
            child.visibility = View.VISIBLE
        }
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(
            coordinatorLayout,
            child,
            directTargetChild,
            target,
            axes,
            type
        )
    }
}