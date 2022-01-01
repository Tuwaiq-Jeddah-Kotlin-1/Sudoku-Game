package com.renad.sudoku.ui.main_activity.fragments.game_fragment.custom_view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.renad.sudoku.R
import kotlin.math.min

class SudokuBoardView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private var sqrtSize = 3
    private var size = 9

    private var cellSizePixels = 0F
    private var noteSizePixels = 0F

    private var selectedRow = -1
    private var selectedCol = -1

    private var listener: SudokuBoardView.OnTouchListener? = null

    private val thickLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = resources.getColor(R.color.colorOnPrimary)
        strokeWidth = 6F
    }

    private val thinLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = resources.getColor(R.color.colorOnPrimary)
        strokeWidth = 2F
    }

    private val yellowCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = resources.getColor(R.color.yellow)
    }

    private val pinkCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = resources.getColor(R.color.pink)
    }

    private val purpleCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = resources.getColor(R.color.purple)
    }

    private val blueCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = resources.getColor(R.color.blue)
    }

    private val selectedCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = resources.getColor(R.color.gray2)
    }

    private val conflictingCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = resources.getColor(R.color.gray1)
    }

    private val textPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = resources.getColor(R.color.darkGray)
    }

    private val startingCellTextPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = resources.getColor(R.color.white)
        typeface = Typeface.DEFAULT_BOLD
    }

    private val startingCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#66000000")
    }

    private val noteTextPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePixels = min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(sizePixels, sizePixels)
    }

    override fun onDraw(canvas: Canvas) {
        updateMeasurements(width)
        boardColoring(canvas)
        drawLines(canvas)
    }

    private fun updateMeasurements(width: Int) {
        cellSizePixels = width / size.toFloat()
        noteSizePixels = cellSizePixels / sqrtSize.toFloat()
        noteTextPaint.textSize = cellSizePixels / sqrtSize.toFloat()
        textPaint.textSize = cellSizePixels / 1.5F
        startingCellTextPaint.textSize = cellSizePixels / 1.5F
    }



    private fun fillCell(canvas: Canvas, r: Int, c: Int, paint: Paint) {
        canvas.drawRect(
            c * cellSizePixels,
            r * cellSizePixels,
            (c + 1) * cellSizePixels,
            (r + 1) * cellSizePixels,
            paint
        )

    }

    private fun fillColor(canvas: Canvas, c: Int, r: Int, paint: Paint) {
        canvas.drawRect(
            c * cellSizePixels,
            r * cellSizePixels,
            (c + 3) * cellSizePixels,
            (r + 3) * cellSizePixels,
            paint
        )
    }

    private fun boardColoring(canvas: Canvas) {
        for (r in 0..3) {
            for (c in 0..3) {
                if ((r == 0 && c == 0) || (r == 1 && c == 2) || (r == 2 && c == 0)) {
                    fillColor(canvas, c * 3, r * 3, yellowCellPaint)
                } else if ((r == 0 && c == 1) || (r == 2 && c == 1)) {
                    fillColor(canvas, c * 3, r * 3, pinkCellPaint)
                } else if (((r == 1 && c == 1))) {
                    fillColor(canvas, c * 3, r * 3, purpleCellPaint)
                } else {
                    fillColor(canvas, c * 3, r * 3, blueCellPaint)
                }
            }
        }
    }

    private fun drawLines(canvas: Canvas) {
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), thickLinePaint)

        for (i in 1 until size) {
            val paintToUse = when (i % sqrtSize) {
                0 -> thickLinePaint
                else -> thinLinePaint
            }

            canvas.drawLine(
                i * cellSizePixels,
                0F,
                i * cellSizePixels,
                height.toFloat(),
                paintToUse

            )

            canvas.drawLine(
                0F,
                i * cellSizePixels,
                width.toFloat(),
                i * cellSizePixels,
                paintToUse
            )
        }
    }



    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                handleTouchEvent(event.x, event.y)
                true
            }
            else -> false
        }
    }

    private fun handleTouchEvent(x: Float, y: Float) {
        val possibleSelectedRow = (y / cellSizePixels).toInt()
        val possibleSelectedCol = (x / cellSizePixels).toInt()
        listener?.onCellTouched(possibleSelectedRow, possibleSelectedCol)
    }

    fun updateSelectedCellUI(row: Int, col: Int) {
        selectedRow = row
        selectedCol = col
        invalidate()
    }



    fun registerListener(listener: SudokuBoardView.OnTouchListener) {
        this.listener = listener
    }

    interface OnTouchListener {
        fun onCellTouched(row: Int, col: Int)
    }
}