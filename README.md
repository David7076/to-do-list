# To-Do List App

Aplicativo Android nativo para gerenciamento de tarefas (To-Do List) desenvolvido como atividade acadêmica. A aplicação permite listar, criar, editar, alternar status de conclusão e excluir tarefas com persistência local.

---

## Tecnologias Utilizadas

* **Kotlin:** Linguagem principal do projeto.
* **Jetpack Compose:** Toolkit declarativo para construção de interfaces nativas.
* **Room Database:** Camada de persistência local SQLite com suporte a Coroutines/Flow.
* **Coroutines & StateFlow:** Gerenciamento de tarefas assíncronas e fluxo de estados observáveis.
* **ViewModel:** Gerenciamento e retenção de estado da UI com ciclo de vida seguro.
* **Navigation Compose:** Controle de navegação e rotas parametrizadas.

---

## Arquitetura e Componentes

### 1. TarefaRepository
Atua como a única fonte de verdade para os dados da aplicação. Ele isola a camada de dados (Room DAO) do restante do aplicativo, expondo a lista de tarefas através de um `Flow<List<Tarefa>>` e suspend functions para inserção, atualização e deleção.

### 2. TarefaViewModel
Responsável por conter a lógica de apresentação e o estado da interface. Converte os fluxos do repositório em `StateFlow` observável pela UI via `stateIn`, além de expor funções para manipular as operações de CRUD através do `viewModelScope`. Contém uma `Factory` para injeção de dependência manual do repositório.

### 3. ListaTarefasScreen
Observa a lista de tarefas emitida pelo `StateFlow` da ViewModel. Utiliza um componente `LazyColumn` para renderização eficiente da listagem. Cada item permite alternar o status de conclusão (Checkbox), abrir a tela de edição (ao tocar no card) ou excluir o registro (ícone de lixeira).

### 4. FormularioTarefaScreen
Atende aos fluxos de cadastro e edição de tarefas em uma única estrutura:
* **Modo Cadastro:** Ao navegar sem identificador ou com `tarefaId = null`, os campos iniciam vazios e uma nova entidade é criada.
* **Modo Edição:** Ao receber um `tarefaId` válido, observa a tarefa existente via `LaunchedEffect`, preenchendo automaticamente os campos para salvar as alterações.

### 5. AppNavigation e Rotas
Gerencia o grafo de navegação (`NavHost`) com duas rotas declaradas:
* `"lista"`: Rota inicial que exibe a tela de listagem.
* `"formulario?tarefaId={tarefaId}"`: Rota com argumento opcional tipado como `LongType` (`defaultValue = 0L`), permitindo alternar de forma transparente entre o fluxo de criação e de edição por ID.

### 6. MainActivity
Ponto de entrada da aplicação. Instancia a `TarefaViewModel` via `viewModels` e `Factory` passando o repositório configurado com o banco Room. Inicializa o tema e chama a função `AppNavigation` dentro de `setContent`.

---

## Instruções de Execução

1. Clone o repositório em sua máquina local.
2. Abra o projeto no **Android Studio**.
3. Aguarde o término da sincronização do **Gradle** (*Sync Project with Gradle Files*).
4. Selecione um emulador ou dispositivo físico com Android (API 26 ou superior).
5. Clique em **Run 'app'** ou execute via terminal:
   ```bash
   ./gradlew installDebug