package com.example.pertemuan14.ui.home.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pertemuan14.R
import com.example.pertemuan14.model.Mahasiswa
import com.example.pertemuan14.ui.PenyediaViewModel
import com.example.pertemuan14.ui.home.viewmodel.HomeUiState
import com.example.pertemuan14.ui.home.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    navigateToItemEntry:()->Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit ,
    viewModel: HomeViewModel = viewModel(factory = PenyediaViewModel.Factory)
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Mahasiswa")
            }
        },
    ) { innerPadding->
        HomeStatus(
            homeUiState = viewModel.mhsUiState,
            retryAction = {viewModel.getMhs()},
            modifier = Modifier.padding(innerPadding),
            onDetailClick = onDetailClick,

        )
    }
}

@Composable
fun HomeStatus(
    homeUiState: HomeUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (Mahasiswa) -> Unit = {},
    onDetailClick: (String) -> Unit
) {
    var deleteConfirm by rememberSaveable { mutableStateOf<Mahasiswa?>(null) }

    when (homeUiState) {
        is HomeUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())

        is HomeUiState.Success -> ListMahasiswa(
            listMhs = homeUiState.data,
            modifier = modifier.fillMaxWidth(),
            onClick = { onDetailClick(it) },
            onDelete = { deleteConfirm = it }
        )

        is HomeUiState.Error -> OnError(
            message = homeUiState.e.message ?: "Error",
            retryAction = retryAction,
            modifier = modifier.fillMaxSize()
        )
    }

    deleteConfirm?.let { data ->
        DeleteConfirmationDialog(
            onDeleteConfirm = {
                onDeleteClick(data)
                deleteConfirm = null
            },
            onDeleteCancel = { deleteConfirm = null }
        )
    }
}

@Composable
fun OnLoading(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(R.drawable.wifierror),
            contentDescription = null
        )
    }
}

@Composable
fun OnError(
    message: String,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.wifierror),
                contentDescription = null
            )
            Text(
                text = message,
                modifier = Modifier.padding(16.dp)
            )
            Button(onClick = retryAction) {
                Text("Coba Lagi")
            }
        }
    }
}

@Composable
fun ListMahasiswa(
    listMhs: List<Mahasiswa>,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit,
    onDelete: (Mahasiswa) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(items = listMhs) { mhs ->
            CardMhs(
                mhs = mhs,
                onClick = { mhs.nim?.let { onClick(it) } },
                onDelete = { onDelete(mhs) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardMhs(
    mhs: Mahasiswa,
    onClick: () -> Unit = {},
    onDelete: (Mahasiswa) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.Person, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                mhs.nama?.let { Text(text = it, fontWeight = FontWeight.Bold, fontSize = 20.sp) }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.DateRange, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                mhs.nim?.let { Text(text = it, fontWeight = FontWeight.Bold, fontSize = 16.sp) }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { onDelete(mhs) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.Home, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                mhs.kelas?.let { Text(text = it, fontWeight = FontWeight.Bold) }
            }
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { onDeleteCancel() },
        title = { Text("Delete Data") },
        text = { Text("Apakah anda yakin ingin menghapus data?") },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) { Text("Cancel") }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) { Text("Yes") }
        }
    )
}