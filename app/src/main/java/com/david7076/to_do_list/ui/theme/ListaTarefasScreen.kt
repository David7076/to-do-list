package com.david7076.to_do_list.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.david7076.to_do_list.data.Tarefa
import com.david7076.to_do_list.viewmodel.TarefaViewModel

@Composable
fun ListaTarefasScreen(
    viewModel: TarefaViewModel,
    onNovaTarefa: () -> Unit,
    onEditarTarefa: (Int) -> Unit
) {
    val tarefas by viewModel.tarefas.collectAsStateWithLifecycle()

    ListaTarefasContent(
        tarefas = tarefas,
        onNovaTarefa = onNovaTarefa,
        onEditarTarefa = onEditarTarefa,
        onAlternarConcluida = { tarefa ->
            viewModel.atualizar(tarefa.copy(concluida = !tarefa.concluida))
        },
        onExcluirTarefa = { tarefa ->
            viewModel.deletar(tarefa)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaTarefasContent(
    tarefas: List<Tarefa>,
    onNovaTarefa: () -> Unit,
    onEditarTarefa: (Int) -> Unit,
    onAlternarConcluida: (Tarefa) -> Unit,
    onExcluirTarefa: (Tarefa) -> Unit,
    modifier: Modifier = Modifier,
    tarefaInicialEmExclusao: Tarefa? = null
) {
    var tarefaParaExcluir by remember { mutableStateOf<Tarefa?>(tarefaInicialEmExclusao) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text("Minhas Tarefas") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNovaTarefa) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Tarefa")
            }
        }
    ) { innerPadding ->
        if (tarefas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhuma tarefa cadastrada.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(tarefas, key = { it.id }) { tarefa ->
                    ItemTarefa(
                        tarefa = tarefa,
                        onClick = { onEditarTarefa(tarefa.id) },
                        onCheckChange = { onAlternarConcluida(tarefa) },
                        onDeleteClick = { tarefaParaExcluir = tarefa }
                    )
                    HorizontalDivider()
                }
            }
        }

        // Diálogo de confirmação Material 3
        tarefaParaExcluir?.let { tarefa ->
            AlertDialog(
                onDismissRequest = { tarefaParaExcluir = null },
                title = { Text(text = "Excluir tarefa") },
                text = {
                    Text(
                        text = "Deseja realmente excluir a tarefa \"${tarefa.titulo}\"? Esta ação não pode ser desfeita."
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onExcluirTarefa(tarefa)
                            tarefaParaExcluir = null
                        }
                    ) {
                        Text("Excluir", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { tarefaParaExcluir = null }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun ItemTarefa(
    tarefa: Tarefa,
    onClick: () -> Unit,
    onCheckChange: (Boolean) -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = tarefa.concluida,
            onCheckedChange = onCheckChange
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = tarefa.titulo,
                style = MaterialTheme.typography.titleMedium,
                textDecoration = if (tarefa.concluida) TextDecoration.LineThrough else null
            )
            if (tarefa.descricao.isNotBlank()) {
                Text(
                    text = tarefa.descricao,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        IconButton(onClick = onDeleteClick) {
            Icon(Icons.Default.Delete, contentDescription = "Excluir Tarefa")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListaTarefasPreview() {
    ListaTarefasContent(
        tarefas = listOf(
            Tarefa(1, "Estudar Kotlin", "Revisar Jetpack Compose", false),
            Tarefa(2, "Comprar café", "", true)
        ),
        onNovaTarefa = {},
        onEditarTarefa = {},
        onAlternarConcluida = {},
        onExcluirTarefa = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ConfirmacaoExclusaoDialogPreview() {
    val tarefaExemplo = Tarefa(1, "Apresentar trabalho de Android", "Preparar slides para a banca", false)
    ListaTarefasContent(
        tarefas = listOf(tarefaExemplo),
        onNovaTarefa = {},
        onEditarTarefa = {},
        onAlternarConcluida = {},
        onExcluirTarefa = {},
        tarefaInicialEmExclusao = tarefaExemplo
    )
}