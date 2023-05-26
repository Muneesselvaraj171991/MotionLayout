package com.example.motionlayout.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionLayoutDebugFlags
import java.util.EnumSet
import kotlin.math.abs
import com.example.motionlayout.R
import com.example.motionlayout.ui.theme.black
import com.example.motionlayout.ui.theme.white


@OptIn(ExperimentalMotionApi::class)
@Composable
fun MotionLayout() {
    Column {
        var isScanButtonPressed by remember { mutableStateOf(true) }
        var animateButton by remember { mutableStateOf(false) }
        val buttonAnimationProgress by animateFloatAsState(
            targetValue = if (animateButton) 1f else 0f,
            animationSpec = tween(1000))
        val textLabel: String = stringResource(id = R.string.app_name)

        // LOCAL CONSTANT TO DETERMINE SWIPE DIRECTION
        val swipe_right = 0
        val swipe_left = 1
        var direction by remember { mutableStateOf(-1) }



    MotionLayout(
            ConstraintSet(
                """ {


guide1 : {
type: 'vGuideline',
percent: 0.4
},
btn_cart: {
width: 'wrap',
height: 'wrap',
start: ['guide1', 'end'],
},
btn_search: {
width: 'wrap',
height: 'wrap',
start: ['btn_cart', 'end'],
}
} """
            ),

            ConstraintSet(
                """ {
guide1 : {
type: 'vGuideline',
percent: 0.64
},
btn_cart: {
width: 'wrap',
height: 'wrap',
end: ['btn_search', 'start']
},

btn_search: {
width: 'wrap',
height: 'wrap',
start: ['guide1', 'start'],
}
} """
            ),
            progress = buttonAnimationProgress,
            debug = EnumSet.of(MotionLayoutDebugFlags.NONE),
            modifier = Modifier
                .fillMaxWidth()
                .size(dimensionResource(id = R.dimen.motion_layout_size))
                .background(black.copy(alpha = 0.7f))
                .pointerInput(Unit) {
                    detectDragGestures(onDrag = { change, dragAmount ->
                        change.consume()

                        val (x, y) = dragAmount
                        if (abs(x) > abs(y)) {
                            when {
                                x > 0 -> {
                                    //right
                                    direction = swipe_right
                                }

                                x < 0 -> {
                                    // left
                                    direction = swipe_left
                                }
                            }
                        }

                    }, onDragEnd = {
                        when (direction) {
                            swipe_right -> {
                                isScanButtonPressed = true
                                animateButton = false
                            }

                            swipe_left -> {
                                isScanButtonPressed = false
                                animateButton = true
                            }
                        }
                    })
                }


        ) {
        val interactionSource = MutableInteractionSource()
        Row(modifier = Modifier.layoutId("btn_cart"),
                horizontalArrangement = Arrangement.Center
        ) {
                Image(
                    painter = painterResource(
                        id = if (isScanButtonPressed) {
                            R.drawable.bucket_selected
                        } else {
                            R.drawable.bucket_item_un_selected
                        }
                    ),
                    contentDescription = "",
                    modifier = Modifier
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            if (!isScanButtonPressed) {
                                isScanButtonPressed = true
                                animateButton = !animateButton
                            }
                        }


                )

                Image(painter = painterResource(
                        id = if (!isScanButtonPressed) {
                            R.drawable.search_selected
                        } else {
                            R.drawable.search_unselected
                        }
                    ),
                    contentDescription = "",
                    modifier = Modifier
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {                            if (isScanButtonPressed) {
                                isScanButtonPressed = false
                                animateButton = !animateButton
                            }
                    }
                )

            }
    }

        Row(modifier = Modifier
            .background(black.copy(alpha = 0.7f))
            .fillMaxWidth()
            .padding(bottom = dimensionResource(id = R.dimen.padding_))
        ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = textLabel,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.labelMedium.copy(color = white)
                )
            }


    }

}
