package com.example.todoapp2025

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp2025.data.Todo
import com.example.todoapp2025.vm.*
import java.text.SimpleDateFormat
import java.util.*
import android.content.Intent
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    private val factory by lazy { TodoVMFactory(application) }
    private val vm: TodoViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App(vm) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(vm: TodoViewModel = viewModel()) {
    val state by vm.state.collectAsState()

    MaterialTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("To-Do") },
                    actions = { SortMenu(state.sort, onPick = { vm.setSort(it, toggleAscIfSame = true) }) }
                )
            },
            bottomBar = {
                BottomAppBar(actions = {
                    TextButton(onClick = vm::clearCompleted) { Text("Clear completed") }
                })
            }
        ) { padding ->
            Column(Modifier.padding(padding).padding(16.dp)) {
                AddRow(onAdd = { t, c, d, p -> vm.add(t, c, d, p) })
                Spacer(Modifier.height(12.dp))
                TodoList(
                    items = state.items,
                    onToggle = vm::toggleComplete,
                    onDelete = vm::delete
                )

                // 👇 Add this block right here
                val context = LocalContext.current

                Button(
                    onClick = {
                        val intent = Intent(context, ReferenceActivity1::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Go to Cat Reference 🐱")
                }
            }
        }
    }
}

@Composable
fun SortMenu(current: SortSpec, onPick: (SortBy) -> Unit) {
    var open by remember { mutableStateOf(false) }
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 8.dp)) {
        AssistChip(
            onClick = { open = true },
            label = { Text("Sort: ${current.by.name} " + if (current.ascending) "↑" else "↓") }
        )
    }
    DropdownMenu(expanded = open, onDismissRequest = { open = false }) {
        SortBy.values().forEach { opt ->
            DropdownMenuItem(
                text = { Text(opt.name) },
                onClick = { open = false; onPick(opt) }
            )
        }
    }
}

@Composable
fun AddRow(onAdd: (String, String, Long?, Int) -> Unit) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var dueText by remember { mutableStateOf("") }   // yyyy-MM-dd HH:mm (optional)
    var priority by remember { mutableStateOf("3") }

    fun parseDue(): Long? = try {
        if (dueText.isBlank()) null
        else SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(dueText)?.time
    } catch (_: Exception) { null }

    Column(Modifier.fillMaxWidth()) {
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Category") }, modifier = Modifier.weight(1f))
            OutlinedTextField(value = dueText, onValueChange = { dueText = it }, label = { Text("Due (yyyy-MM-dd HH:mm)") }, modifier = Modifier.weight(1f))
            OutlinedTextField(
                value = priority,
                onValueChange = { priority = it.filter { ch -> ch.isDigit() }.take(1) },
                label = { Text("P(1-5)") },
                modifier = Modifier.width(90.dp)
            )
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(
                onClick = {
                    onAdd(title, category, parseDue(), priority.toIntOrNull()?.coerceIn(1,5) ?: 3)
                    title = ""; category = ""; dueText = ""; priority = "3"
                },
                enabled = title.isNotBlank()
            ) { Text("Add") }
        }
    }
}

@Composable
fun TodoList(items: List<Todo>, onToggle: (Todo) -> Unit, onDelete: (Todo) -> Unit) {
    if (items.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No items yet — add your first task!")
        }
        return
    }
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items, key = { it.id }) { t ->
            TodoRow(t, onToggle, onDelete)
        }
    }
}

@Composable
fun TodoRow(t: Todo, onToggle: (Todo) -> Unit, onDelete: (Todo) -> Unit) {
    val due = t.dueAt?.let {
        SimpleDateFormat("MMM d, yyyy HH:mm", Locale.getDefault()).format(Date(it))
    } ?: "—"
    ElevatedCard(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = t.completed, onCheckedChange = { onToggle(t) })
            Column(Modifier.weight(1f).padding(start = 8.dp)) {
                Text(
                    t.title,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = if (t.completed) TextDecoration.LineThrough else TextDecoration.None
                )
                Text("Category: ${t.category.ifBlank { "—" }} • Due: $due • Priority: ${t.priority}")
            }
            Text("Delete", modifier = Modifier.padding(start = 12.dp).clickable { onDelete(t) })
        }
    }
}