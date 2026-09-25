# Componentes de JavaFX utilizados en la aplicación

Este documento resume los componentes de JavaFX que usa la aplicación, a partir del análisis previo del flujo de la interfaz y la navegación.

## 1. Base de la interfaz JavaFX

La aplicación está construida sobre JavaFX, con una estructura típica de escritorio:

- `Launcher`: punto de entrada que inicia la aplicación.
- `Main`: inicializa JavaFX y configura el escenario principal.
- `SceneManager`: centraliza la carga de vistas y la navegación entre pantallas.
- `Stage`: escenario principal de la aplicación.
- `Scene`: contenedor visual de cada vista cargada.
- `FXMLLoader`: carga las vistas defini-das en archivos `.fxml`.

## 2. Vistas principales del sistema

La aplicación usa varias pantallas FXML, cada una con su controlador correspondiente:

- `LoginView.fxml` — inicio de sesión.
- `RegisterView.fxml` — registro de usuario.
- `DashboardView.fxml` — panel principal según rol.
- `CatalogView.fxml` — catálogo de libros.
- `BorrowingView.fxml` — préstamo directo.
- `PendingRequestsView.fxml` — solicitudes pendientes.
- `MyLoansView.fxml` — préstamos del estudiante.
- `LibrarianView.fxml` — gestión de bibliotecarios.

Cada vista se carga mediante `SceneManager`, y cada una tiene un controlador responsable de la lógica de interacción y validación.

## 3. Controles de entrada y formularios

Los formularios de la aplicación utilizan componentes habituales de JavaFX para capturar y validar información:

- `TextField`: para correo, nombre, código, título, ISBN, carnet, etc.
- `PasswordField`: para contraseñas.
- `TextArea`: cuando el diseño requiere detalle o texto libre.
- `DatePicker`: para seleccionar fechas de préstamo, devolución o respuesta.
- `ComboBox`: para elegir valores predefinidos, por ejemplo estados o tipos de acciones.
- `ChoiceBox`: si alguna vista usa selecciones simples.
- `TableView`: para listar libros, usuarios, solicitudes y préstamos.
- `TableColumn`: para definir columnas visibles en las tablas.

## 4. Controles de navegación y acciones

La interfaz emplea controles para guiar al usuario entre pantallas y ejecutar acciones:

- `Button`: acciones principales como iniciar sesión, guardar, eliminar, aprobar, rechazar, buscar, cerrar sesión.
- `Hyperlink`: para navegación entre login y registro.
- `Label`: textos informativos, títulos y mensajes del sistema.
- `ImageView`: para mostrar iconos o material visual de la aplicación.
- `MenuItem` o elementos de menú si se usan en la interfaz.

## 5. Contenedores y layout

Las vistas siguen una organización visual basada en contenedores JavaFX:

- `AnchorPane`: contenedor principal de varias pantallas.
- `VBox`: apilado vertical de componentes.
- `HBox`: agrupación horizontal de controles.
- `GridPane`: organización estructurada por filas y columnas.
- `FlowPane`: cuando se requiere disposición flexible.
- `BorderPane`: posible uso para distribuir encabezado, centro y pie de vista.

Estos layouts permiten estructurar formularios, tablas, paneles de filtros y bloques de acciones.

## 6. Componentes de listado y consulta

Las pantallas de gestión usan componentes de visualización de datos en forma tabular:

- `TableView`: listado de libros, bibliotecarios, préstamos y solicitudes.
- `TableColumn`: columnas con propiedades como nombre, código, título, estado, fechas, etc.
- `ObservableList`: estructura de datos reactiva para mostrar registros en tablas.
- `SelectionModel`: selección de elementos en tablas para editar o consultar información.

## 7. Alertas y mensajes al usuario

La experiencia de usuario incluye componentes para informar resultados y errores:

- `Alert`: para mensajes de éxito, error, confirmación o advertencia.
- `AlertUtils`: utilidad que encapsula la creación de alertas.
- `AppStatus`: objeto o mecanismo para reflejar el estado visible del sistema.

## 8. Componentes asociados a sesion y roles

La aplicación mantiene la sesión del usuario y adapta la interfaz según el rol actual:

- `SessionManager`: almacena el usuario autenticado.
- `DashboardController`: muestra u oculta opciones según el rol del usuario.
- `DashboardView.fxml`: presenta accesos diferentes para:
  - `STUDENT`
  - `LIBRARIAN`
  - `MANAGER`

Esto provoca que la interfaz sea dinámica en cuanto a visibilidad de botones y secciones.

## 9. Componentes principales por módulo

### Login y registro

- `TextField` para correo y código.
- `PasswordField` para contraseña.
- `Button` para iniciar sesión.
- `Hyperlink` para abrir la vista de registro.
- `Label` para mensajes y validaciones.

### Catálogo

- `TextField` para filtros por título o ISBN.
- `TableView` para mostrar libros.
- `Button` para agregar, editar, eliminar o solicitar préstamo.
- `Label` para stock disponible y total.

### Préstamos

- `TextField` para buscar estudiante por carnet.
- `TextField` para buscar libros por ISBN o título.
- `DatePicker` para elegir la fecha límite.
- `TableView` para mostrar datos del préstamo o detalles.
- `Button` para crear y confirmar el préstamo.

### Solicitudes pendientes

- `TableView` para mostrar solicitudes con estado `PENDING`.
- `Button` para aprobar o rechazar.
- `Label` para indicar el estado de la acción.

### Mis préstamos

- `TableView` para listar préstamos del usuario autenticado.
- `Label` para mostrar nombre de libro, fecha y estado.

### Gestión de bibliotecarios

- `TextField` para búsqueda por código, nombre o correo.
- `TableView` para listar usuarios con rol bibliotecario.
- `Button` para crear, editar o eliminar registros.
- `PasswordField` para proteger credenciales.

## 10. Resumen

Los componentes JavaFX que usa la aplicación se centran en:

- escenarios y navegación por `Stage` y `SceneManager`
- pantallas cargadas desde FXML con `FXMLLoader`
- formularios con `TextField`, `PasswordField`, `DatePicker`, `ComboBox` y `Button`
- listados con `TableView` y `TableColumn`
- diseño responsive con `AnchorPane`, `VBox`, `HBox`, `GridPane`
- alertas con `Alert`
- adaptación visual por rol usando controladores y sesión activa

En conjunto, la aplicación combina una interfaz JavaFX con un flujo de navegación modular, listas dinámicas y formularios orientados al manejo de biblioteca.
