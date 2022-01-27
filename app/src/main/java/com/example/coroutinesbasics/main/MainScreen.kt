package com.example.coroutinesbasics.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coroutinesbasics.R
import com.example.coroutinesbasics.ui.theme.CoroutinesBasicsTheme
import kotlinx.coroutines.Dispatchers

@Composable
fun MainScreen(viewModel: MainViewModel) {
    CoroutinesBasicsTheme {
        Scaffold(
            topBar = { TopAppBar (title = { Text(stringResource(R.string.app_name)) })
            }
        ) {
            MainUI(viewModel)
        }
    }
}

@Composable
fun MainUI(viewModel: MainViewModel) {

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        MainText(text1 = viewModel.data1.toString(), text2 = viewModel.data2.toString())

        DefaultButton(buttonName = "Launch", onButtonClick = {
            viewModel.onButtonClick(useAsync = false)
        })
        Spacer(modifier = Modifier.height(10.dp))

        DefaultButton(buttonName = "Async", onButtonClick = {
            viewModel.onButtonClick(useAsync = true)
        })
        Spacer(modifier = Modifier.height(10.dp))

        DefaultButton(buttonName = "Cancel", onButtonClick = viewModel::onCancelButtonClick)
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun MainText(text1: String, text2: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        DefaultText(text = text1, modifier = Modifier.fillMaxWidth().weight(1F))
        DefaultText(text = text2, modifier = Modifier.fillMaxWidth().weight(1F))
    }

    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
private fun DefaultText(text: String, modifier: Modifier) {
    Surface(
        modifier = modifier.padding(all = 5.dp),
        color = Color.LightGray
    ) {
        Text(
            text = text,
            fontSize = 120.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun DefaultButton(
    buttonName: String,
    onButtonClick: () -> Unit) {

    Button(
        onClick = onButtonClick,
        modifier = Modifier.size(width = 150.dp, height = 60.dp)
    ) {
        Text(
            text = buttonName,
            fontSize = 30.sp)
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    val viewModel = MainViewModel(Dispatchers.Default)
    MainScreen(viewModel)
}