package com.example.expensemanagement.presentation.home.component

import android.graphics.Paint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensemanagement.domain.models.Fund
import com.example.expensemanagement.domain.models.Participant
import com.example.expensemanagement.presentation.common.Category
import kotlin.math.round

@Composable
fun FundBarChart(
    oxLabel: String,
    oyLabel: String,
    chartData: List<Pair<Fund, Double>>
) {
    val spacingFromLeft = 50f
    val spacingFromBottom = 40f

    val upperValue = remember { (chartData.maxOfOrNull { it.second }?.plus(0.0) ?: 0.0) }
    val lowerValue = remember { (chartData.minOfOrNull { it.second } ?: 0.0) }

    val density = LocalDensity.current

    //paint for the text shown in data values
    val textPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.BLACK
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    androidx.compose.foundation.Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .background(Color.White)
            .padding(16.dp)
    ) {

        val canvasHeight = size.height
        val canvasWidth = size.width
        val spacePerData = (canvasWidth - spacingFromLeft) / chartData.size
        val spaceName = 90f
        val topOy = 50f
        val rightOx = 40f
        val maxValueColumn = canvasHeight - spacingFromBottom - topOy

        //loop through each index by step of 1
        //data shown horizontally
        (chartData.indices step 1).forEach { i ->
            val text = if (chartData[i].first.fundName.length > 6) {
                "${chartData[i].first.fundName.take(6)}..."
            } else {
                chartData[i].first.fundName
            }
//            val  = chartData[i].first.fundName
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    text,
                    spacingFromLeft + spaceName + i * spacePerData,
                    canvasHeight,
                    textPaint
                )
            }
        }

        //Vertical line
        drawLine(
            start = Offset(spacingFromLeft, canvasHeight - spacingFromBottom),
            end = Offset(spacingFromLeft, topOy),
            color = Color.Black,
            strokeWidth = 3f
        )

        //Horizontal line
        drawLine(
            start = Offset(spacingFromLeft, canvasHeight - spacingFromBottom),
            end = Offset(canvasWidth - rightOx, canvasHeight - spacingFromBottom),
            color = Color.Black,
            strokeWidth = 3f
        )

        //draw bars
        chartData.forEachIndexed { index, chartPair ->

            //draw text at top of each bar
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    chartPair.second.toString(),
                    spacingFromLeft + spaceName + index * spacePerData,
                    ((upperValue - chartPair.second.toFloat()) / upperValue * maxValueColumn + 40f).toFloat(),
                    textPaint
                )
            }

            //draw Bar for each value
            drawRoundRect(
                color = Color.Magenta,
                topLeft = Offset(
                    spacingFromLeft + 50f + index * spacePerData,
                    (((upperValue - chartPair.second ) / upperValue) * maxValueColumn + topOy).toFloat()
                ),
                size = Size(
                        55f,
                        ((chartPair.second / upperValue) * (maxValueColumn) ).toFloat()
                    ),
                cornerRadius = CornerRadius(10f, 10f)
            )
        }
        val axisLabelPaint = Paint().apply {
            color = android.graphics.Color.BLACK
            textAlign = Paint.Align.CENTER
            textSize = density.run { 16.sp.toPx() }
        }

        drawContext.canvas.nativeCanvas.apply {
            drawText(
                oyLabel,  // Nhãn trục y
                70f,
                0f,  // Đặt nhãn ở phía trên bên trái
                axisLabelPaint
            )
        }

        // Vẽ nhãn trục x (ngang)
        drawContext.canvas.nativeCanvas.apply {
            drawText(
                oxLabel,  // Nhãn trục x
                canvasWidth / 2,
                canvasHeight + 40f,  // Đặt nhãn ở giữa phía dưới
                axisLabelPaint
            )
        }
    }
}
//
//@Preview(showSystemUi = true)
//@Composable
//fun FundBarChartPreview(){
//    val fund1 = Fund(1, "Quy2", 2)
//    val fund2 = Fund(1, "Quy3", 2)
//    val chartData = listOf(
//        Pair(fund1, 1.0),
//        Pair(fund1, 90.0),
//        Pair(fund1, 190.0),
//        Pair(fund2, 0.0),
//    )
//    FundBarChart(oxLabel = "Ox", oyLabel = "Oy", chartData)
//}


@Composable
fun ParBarChart(
    oxLabel: String,
    oyLabel: String,
    chartData: List<Pair<Participant, Double>>
) {
    val spacingFromLeft = 50f
    val spacingFromBottom = 40f

    val upperValue = remember { (chartData.maxOfOrNull { it.second }?.plus(0.0) ?: 0.0) }
    val lowerValue = remember { (chartData.minOfOrNull { it.second } ?: 0.0) }

    val density = LocalDensity.current

    //paint for the text shown in data values
    val textPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.BLACK
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    androidx.compose.foundation.Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .background(Color.White)
            .padding(16.dp)
    ) {

        val canvasHeight = size.height
        val canvasWidth = size.width
        val spacePerData = (canvasWidth - spacingFromLeft) / chartData.size
        val spaceName = 90f
        val topOy = 50f
        val rightOx = 40f
        val maxValueColumn = canvasHeight - spacingFromBottom - topOy

        //loop through each index by step of 1
        //data shown horizontally
        (chartData.indices step 1).forEach { i ->
            val text = if (chartData[i].first.participantName.length > 6) {
                "${chartData[i].first.participantName.take(6)}..."
            } else {
                chartData[i].first.participantName
            }
//            val  = chartData[i].first.fundName
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    text,
                    spacingFromLeft + spaceName + i * spacePerData,
                    canvasHeight,
                    textPaint
                )
            }
        }

        //Vertical line
        drawLine(
            start = Offset(spacingFromLeft, canvasHeight - spacingFromBottom),
            end = Offset(spacingFromLeft, topOy),
            color = Color.Black,
            strokeWidth = 3f
        )

        //Horizontal line
        drawLine(
            start = Offset(spacingFromLeft, canvasHeight - spacingFromBottom),
            end = Offset(canvasWidth - rightOx, canvasHeight - spacingFromBottom),
            color = Color.Black,
            strokeWidth = 3f
        )

        //draw bars
        chartData.forEachIndexed { index, chartPair ->

            //draw text at top of each bar
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    chartPair.second.toString(),
                    spacingFromLeft + spaceName + index * spacePerData,
                    ((upperValue - chartPair.second.toFloat()) / upperValue * maxValueColumn + 40f).toFloat(),
                    textPaint
                )
            }

            //draw Bar for each value
            drawRoundRect(
                color = Color.Magenta,
                topLeft = Offset(
                    spacingFromLeft + 50f + index * spacePerData,
                    (((upperValue - chartPair.second ) / upperValue) * maxValueColumn + topOy).toFloat()
                ),
                size = Size(
                    55f,
                    ((chartPair.second / upperValue) * (maxValueColumn) ).toFloat()
                ),
                cornerRadius = CornerRadius(10f, 10f)
            )
        }
        val axisLabelPaint = Paint().apply {
            color = android.graphics.Color.BLACK
            textAlign = Paint.Align.CENTER
            textSize = density.run { 16.sp.toPx() }
        }

        drawContext.canvas.nativeCanvas.apply {
            drawText(
                oyLabel,  // Nhãn trục y
                70f,
                0f,  // Đặt nhãn ở phía trên bên trái
                axisLabelPaint
            )
        }

        // Vẽ nhãn trục x (ngang)
        drawContext.canvas.nativeCanvas.apply {
            drawText(
                oxLabel,  // Nhãn trục x
                canvasWidth / 2,
                canvasHeight + 40f,  // Đặt nhãn ở giữa phía dưới
                axisLabelPaint
            )
        }
    }
}

//@Preview(showSystemUi = true)
//@Composable
//fun ParBarChartPreview(){
//    val par1 = Participant(1, "Quy2")
//    val par2 = Participant(1, "Quy3")
//    val chartData = listOf(
//        Pair (par1, 1.0),
//        Pair (par1, 300.0),
//        Pair (par1, 1000.0),
//        Pair(par2, 0.0),
//    )
//    ParBarChart(oxLabel = "Participant", oyLabel = "Money", chartData)
//}


@Composable
fun CategoryChart(
    oxLabel: String,
    oyLabel: String,
    chartData: List<Pair<Category, Double>>
) {
//    val listVector = chartData.map {(category, amount) ->
//        ImageVector.vectorResource(id = category.iconRes) to amount
//    }
//    val painter = listVector.map { rememberVectorPainter(image = it.first) to it.second }
    val spacingFromLeft = 50f
    val spacingFromBottom = 40f

    val upperValue = remember { (chartData.maxOfOrNull { it.second }?.plus(0.0) ?: 0.0) }
    val lowerValue = remember { (chartData.minOfOrNull { it.second } ?: 0.0) }

    val density = LocalDensity.current

    //paint for the text shown in data values
    val textPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.BLACK
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    androidx.compose.foundation.Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .background(Color.White)
            .padding(16.dp)
    ) {

        val canvasHeight = size.height
        val canvasWidth = size.width
        val spacePerData = (canvasWidth - spacingFromLeft) / chartData.size
        val spaceName = 90f
        val topOy = 50f
        val rightOx = 40f
        val maxValueColumn = canvasHeight - spacingFromBottom - topOy

        //loop through each index by step of 1
        //data shown horizontally
        (chartData.indices step 1).forEach { i ->
            val text = if (chartData[i].first.title.length > 6) {
                "${chartData[i].first.title.take(6)}..."
            } else {
                chartData[i].first.title
            }
//            val  = chartData[i].first.fundName
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    text,
                    spacingFromLeft + spaceName + i * spacePerData,
                    canvasHeight,
                    textPaint
                )
            }
        }

        //Vertical line
        drawLine(
            start = Offset(spacingFromLeft, canvasHeight - spacingFromBottom),
            end = Offset(spacingFromLeft, topOy),
            color = Color.Black,
            strokeWidth = 3f
        )

        //Horizontal line
        drawLine(
            start = Offset(spacingFromLeft, canvasHeight - spacingFromBottom),
            end = Offset(canvasWidth - rightOx, canvasHeight - spacingFromBottom),
            color = Color.Black,
            strokeWidth = 3f
        )

        //draw bars
        chartData.forEachIndexed { index, chartPair ->

            //draw text at top of each bar
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    chartPair.second.toString(),
                    spacingFromLeft + spaceName + index * spacePerData,
                    ((upperValue - chartPair.second.toFloat()) / upperValue * maxValueColumn + 40f).toFloat(),
                    textPaint
                )
            }

            //draw Bar for each value
            drawRoundRect(
                color = Color.Magenta,
                topLeft = Offset(
                    spacingFromLeft + 50f + index * spacePerData,
                    (((upperValue - chartPair.second ) / upperValue) * maxValueColumn + topOy).toFloat()
                ),
                size = Size(
                    55f,
                    ((chartPair.second / upperValue) * (maxValueColumn) ).toFloat()
                ),
                cornerRadius = CornerRadius(10f, 10f)
            )
        }
        val axisLabelPaint = Paint().apply {
            color = android.graphics.Color.BLACK
            textAlign = Paint.Align.CENTER
            textSize = density.run { 16.sp.toPx() }
        }

        drawContext.canvas.nativeCanvas.apply {
            drawText(
                oyLabel,  // Nhãn trục y
                70f,
                0f,  // Đặt nhãn ở phía trên bên trái
                axisLabelPaint
            )
        }

        // Vẽ nhãn trục x (ngang)
        drawContext.canvas.nativeCanvas.apply {
            drawText(
                oxLabel,  // Nhãn trục x
                canvasWidth / 2,
                canvasHeight + 40f,  // Đặt nhãn ở giữa phía dưới
                axisLabelPaint
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CategoryChartPreview(){

    val chartData = listOf(
        Pair(Category.EDUCATION, 10.1),
        Pair(Category.EDUCATION, 10.1)
    )
    CategoryChart(oxLabel = "Participant", oyLabel = "Money", chartData)
}