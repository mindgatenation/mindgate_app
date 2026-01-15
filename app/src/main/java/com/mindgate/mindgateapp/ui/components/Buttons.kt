package com.mindgate.mindgateapp.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindgate.mindgateapp.accentColor
import com.mindgate.mindgateapp.greenColor
import com.mindgate.mindgateapp.reg_font

@Composable
fun ActionButton(
    text: String,
    placeholder: @Composable () -> Unit,
    plcHldrRight : Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(onClick = onClick, modifier= modifier, shape = RoundedCornerShape(50.dp), colors = ButtonDefaults.buttonColors(containerColor = accentColor)) {
        Row(
            modifier = modifier.padding(vertical = 5.dp, horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!plcHldrRight) {
                placeholder()
            }
            Text(
                text = text,
                fontFamily = reg_font,
                color = greenColor,
                fontSize = 15.sp,
                modifier = Modifier.padding(end = 5.dp)
            )
            if (plcHldrRight) {
                placeholder()
            }
        }
    }
    
}


@Preview
@Composable
private fun ButtonsPrev() {

}

