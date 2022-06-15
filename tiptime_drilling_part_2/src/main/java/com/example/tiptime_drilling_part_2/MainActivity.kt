package com.example.tiptime_drilling_part_2

import android.os.Bundle
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
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiptime_drilling_part_2.ui.theme.TipTimeTheme
import java.text.NumberFormat

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
fun TipTimeApp(){

    var amountInput by remember { mutableStateOf("")}
    var percentInput by remember { mutableStateOf("")}
    var switchInput by remember { mutableStateOf(false)}

    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val percent = percentInput.toDoubleOrNull() ?: 0.0


    val focusManager = LocalFocusManager.current

    fun calculateTip(amount:Double, percent:Double, roundup:Boolean) : String{
        var tipTemp = percent / 100 * amount

        if (roundup){
            tipTemp = kotlin.math.ceil(tipTemp)
        }

        return NumberFormat.getCurrencyInstance().format(tipTemp)
    }
    val tip = calculateTip(amount, percent, switchInput)

    Column(
        modifier = Modifier.padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = stringResource(id = R.string.caluculate_tip),
        fontSize = 28.sp,
        modifier = Modifier.padding(18.dp))

        Spacer(modifier = Modifier.height(30.dp))
        EditTextField(
            value = amountInput,
            labelId = R.string.bill_amount,
            onValueChange = { amountInput = it },
            keyboardOption = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardAction = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )
        Spacer(modifier = Modifier.height(30.dp))
        EditTextField(
            value = percentInput,
            labelId = R.string.percent_amount,
            onValueChange = {percentInput = it},
            keyboardOption = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardAction = KeyboardActions(
                onDone = {focusManager.clearFocus()}
            )
        )
        Spacer(modifier = Modifier.height(30.dp))
        RoundTheTipRow(roundUp = switchInput, onRoundUpChanged = {switchInput = it})

        Text(text = stringResource(id = R.string.resulting_tip, tip))


    }

}


@Composable
fun EditTextField(
    value: String,
    @StringRes labelId: Int,
    onValueChange: (String) -> Unit,
    keyboardOption: KeyboardOptions,
    keyboardAction: KeyboardActions,

    ) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOption,
        keyboardActions = keyboardAction,
        label = {Text(stringResource(id = labelId))},
        singleLine = true


    )
}

@Composable
fun RoundTheTipRow(
    roundUp: Boolean,
    onRoundUpChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(R.string.round_up))
        Switch(
            colors = SwitchDefaults.colors(
                uncheckedThumbColor = Color.DarkGray),
            checked = roundUp,
            onCheckedChange = onRoundUpChanged,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)

        )
    }
}