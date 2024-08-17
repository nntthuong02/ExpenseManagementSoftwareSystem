package com.example.expensemanagement.presentation.setting


import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.expensemanagement.domain.models.Participant
import com.example.expensemanagement.presentation.home.component.CenterAlignedTopAppBar
import com.example.expensemanagement.presentation.home.component.CheckBoxItem
import com.example.expensemanagement.presentation.home.component.ParBarChart
import com.example.expensemanagement.presentation.home.component.ScrollContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen (
    settingViewModel: SettingViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/octet-stream")
    ) { uri: Uri? ->
        uri?.let {
            settingViewModel.backupDatabase(context, it)
        }
    }

    val restoreLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            settingViewModel.restoreDatabase(context, it)
        }
    }

    CenterAlignedTopAppBar(
        showSnackbarText = "",
        name = "Setting",
        rightIcon1 = 0,
        rightIcon2 = 0,
        iconOnclick1 = { },
        iconOnlick2 = {},
        showIconRight1 = false,
        showIconRight2 = false,
        showIconLeft = false,
        showSnackbar = mutableStateOf(false),
        navController = rememberNavController()
    ) {innerPadding ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(paddingValues = innerPadding)
    ) {


        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                launcher.launch("transactionDB-bkp")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Backup Data")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                restoreLauncher.launch(arrayOf("*/*")) // Mở hộp thoại chọn file
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Restore Data")
        }


        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                Toast.makeText(context, "Preparing data", Toast.LENGTH_LONG).show()
                settingViewModel.shareDatabaseFile(context)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Share Data")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {

        }
    }
}
}

//@Preview(showSystemUi = true)
//@Composable
//fun Preview(){
//    SettingScreen()
//}