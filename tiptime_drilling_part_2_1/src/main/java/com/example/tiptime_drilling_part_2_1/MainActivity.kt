package com.example.tiptime_drilling_part_2_1

import android.os.Bundle
import android.widget.GridLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiptime_drilling_part_2_1.ui.theme.TipTimeTheme
import java.text.NumberFormat
import kotlin.jvm.internal.Intrinsics

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipTimeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TipTimeApp()
                }
            }
        }
    }
}

@Composable
fun TipTimeApp() {

    var amountInput by remember{ mutableStateOf(" ")}
    var percentInput by remember{ mutableStateOf(" ")}
    var switchInput by remember { mutableStateOf(false)}

    val amount = amountInput.toDoubleOrNull()?: 0.0
    val percent = percentInput.toDoubleOrNull()?: 0.0

    val tipTotal = calculateTip(amount, percent, switchInput)

    val focusManager = LocalFocusManager.current

Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
        text = "Посчитать чаевые",
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(32.dp),
        fontSize = 24.sp,
    )
    EditNumberField(
        value = amountInput,
        onValueChange = {amountInput = it},
        labelId = R.string.amount_input,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions (
            onNext = {focusManager.moveFocus(FocusDirection.Down)}
                )
    )
    Spacer(modifier = Modifier.height(20.dp))

    EditNumberField(
        value = percentInput,
        onValueChange = { percentInput = it},
        labelId = R.string.percent_input,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {focusManager.clearFocus()}
        )
    )
    Spacer(modifier = Modifier.height(20.dp))
    Text(text = stringResource(id = R.string.total, tipTotal),
        fontSize = 18.sp,
        modifier = Modifier
            .align(Alignment.CenterHorizontally))
    Spacer(modifier = Modifier.height(20.dp))
    SwitchAndText(
        checked = switchInput,
        onCheckedChange = {switchInput = it}
    )
}

}
@Composable
fun EditNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes labelId: Int,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions

) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(labelId)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )

}



@Composable
fun SwitchAndText(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
){
    Row(
        Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Округлить?")
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                uncheckedThumbColor = Color.DarkGray,

            ), modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)

        )
    }
}

fun calculateTip(amount:Double, percent:Double, switchInput: Boolean) : String{
    var tip = percent / 100 * amount
    if(switchInput){
        tip = kotlin.math.ceil(tip)
    }
    return NumberFormat.getCurrencyInstance().format(tip)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipTimeTheme {
        TipTimeApp()
    }
}