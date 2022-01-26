package com.example.coroutinesbasics.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coroutinesbasics.R
import com.example.coroutinesbasics.ui.theme.CoroutinesBasicsTheme

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
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        MainText(text = viewModel.data.toString())

        RowButtons(
            runButtonName = "Launch",
            onRunButtonClick = viewModel::onLaunchButtonClick,
            onCancelButtonClick = viewModel::onCancelButtonClick
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = viewModel::onLaunchButtonClick,
            modifier = Modifier.size(width = 200.dp, height = 80.dp)
        ) {
            Text("button",
                fontSize = 40.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = viewModel::onLaunchButtonClick,
            modifier = Modifier.size(width = 200.dp, height = 80.dp)
        ) {
            Text("button",
                fontSize = 40.sp)
        }

    }
}

@Composable
private fun MainText(text: String) {
    Text(
        text = text,
        fontSize = 180.sp,
        fontWeight = FontWeight.Bold)
}

@Composable
private fun RowButtons(
    runButtonName: String,
    onRunButtonClick: () -> Unit,
    onCancelButtonClick: () -> Unit,
){
    Row {
        Button(
            onClick = onRunButtonClick,
            modifier = Modifier.size(width = 150.dp, height = 80.dp)
        ) {
            Text(
                text = runButtonName,
                fontSize = 30.sp)
        }

        Spacer(modifier = Modifier.width(10.dp))

        Button(
            onClick = onCancelButtonClick,
            modifier = Modifier.size(width = 150.dp, height = 80.dp)
        ) {
            Text(
                text = "Cancel",
                fontSize = 30.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    val viewModel = MainViewModel()
    MainScreen(viewModel)
}