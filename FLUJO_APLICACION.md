# Flujo de la aplicación — Proyecto-B4

## Alcance del análisis

Este documento resume superficialmente el flujo de la aplicación a partir de los archivos `.java` y `.fxml` ubicados dentro de `main`, en la rama `develop`. No se consideraron otros tipos de archivos ni otras carpetas del repositorio.

## 1. Inicio de la aplicación

La ejecución puede comenzar mediante `Launcher`, que delega en `Main`. La clase `Main` inicia JavaFX, configura el escenario principal en `SceneManager`, intenta cargar el icono de la biblioteca y muestra inicialmente `LoginView.fxml`.

`SceneManager` centraliza la navegación entre vistas. Carga los archivos FXML mediante `FXMLLoader`, crea una nueva escena y la asigna al escenario principal.

## 2. Inicio de sesión y registro

### Inicio de sesión

La vista `LoginView.fxml` solicita correo electrónico y contraseña. `LoginController`:

1. Verifica que los campos no estén vacíos.
2. Busca al usuario por correo mediante `UserDao`.
3. Comprueba la contraseña usando `PasswordUtil`, que calcula un hash SHA-256.
4. Guarda el usuario autenticado en `SessionManager`.
5. Navega a `DashboardView.fxml`.

Si ocurre un error de base de datos, se muestra una alerta mediante `AlertUtils` y `AppStatus`.

### Registro

Desde el enlace de la vista de inicio de sesión se abre `RegisterView.fxml`. `RegisterController` valida:

- Campos obligatorios.
- Coincidencia y fortaleza de la contraseña.
- Formato del correo.
- Prefijo del correo para determinar el rol.
- Existencia previa del código de usuario y del correo.

Los prefijos observados son:

- `std.`: estudiante.
- `blt.`: bibliotecario.
- `btj.`: bibliotecario jefe o administrador.

Después, el usuario se crea mediante `UserDao` y se vuelve a la vista de inicio de sesión.

## 3. Panel principal y permisos por rol

`DashboardView.fxml` presenta botones de acceso a las funciones principales. `DashboardController` obtiene el usuario desde `SessionManager`, muestra su nombre y rol, y oculta o muestra opciones según el rol:

- **MANAGER**: gestionar bibliotecarios y revisar solicitudes pendientes.
- **LIBRARIAN**: administrar el catálogo, registrar préstamos y revisar solicitudes.
- **STUDENT**: consultar el catálogo y revisar sus propios préstamos.

Si no existe una sesión activa, el controlador redirige al inicio de sesión. La opción de cerrar sesión limpia la sesión y vuelve a `LoginView.fxml`.

## 4. Catálogo de libros

La vista `CatalogView.fxml` contiene filtros por título o ISBN, una tabla de libros y, cuando corresponde, un formulario de edición. `CatalogController` carga los libros a través de `BookDao` y permite buscar en el catálogo.

Para bibliotecarios y administradores, el flujo permite:

- Agregar libros.
- Actualizar libros seleccionados.
- Eliminar libros.
- Consultar existencias totales y disponibles.
- Abrir el flujo de registro de un préstamo.

Los datos se validan mediante `Validations`, incluyendo ISBN, año y cantidad de copias.

Para estudiantes, la sección de edición permanece oculta. El estudiante puede seleccionar un libro y enviar una solicitud de préstamo. La solicitud se registra con estado `PENDING` mediante `LoanRequestDao`, y su libro asociado se registra mediante `LoanRequestDetailDao`.

## 5. Registro directo de préstamos

El flujo directo se muestra en `BorrowingView.fxml` y es controlado por `BorrowingController`. Está destinado principalmente al personal bibliotecario.

El proceso general es:

1. Buscar al estudiante por su carnet.
2. Buscar libros por ISBN o título.
3. Seleccionar un libro disponible.
4. Elegir la fecha límite de devolución.
5. Crear una solicitud aprobada.
6. Crear el detalle de la solicitud.
7. Crear el préstamo con estado `ACTIVE`.
8. Crear el detalle del préstamo.
9. Reducir en uno las copias disponibles del libro.
10. Actualizar la tabla y limpiar el formulario.

El modelo de préstamo contiene estudiante, bibliotecario, fechas y estado. Los estados definidos son `ACTIVE`, `RETURNED` y `OVERDUE`.

La opción de imprimir comprobante está prevista en la interfaz, pero en el código analizado permanece pendiente: `ReceiptReportGenerator` devuelve `null` y `BorrowingController` solo muestra un mensaje indicando que la integración con JasperReports aún no está implementada.

## 6. Gestión de solicitudes pendientes

`PendingRequestsView.fxml` muestra una tabla de solicitudes y acciones para aprobar, rechazar o actualizar la información. `PendingRequestsController` carga solicitudes con estado `PENDING` mediante `LoanRequestDao`.

### Aprobación

Al aprobar una solicitud, el sistema:

1. Cambia su estado a `APPROVED`.
2. Registra el bibliotecario que responde y la fecha de respuesta.
3. Crea un préstamo activo.
4. Obtiene los detalles de los libros solicitados.
5. Crea los detalles del préstamo.
6. Descuenta las cantidades correspondientes del inventario disponible.
7. Recarga las solicitudes pendientes.

### Rechazo

Al rechazar, cambia el estado a `REJECTED`, registra el bibliotecario y la fecha de respuesta, muestra una confirmación y actualiza la lista.

Los estados de solicitud definidos son `PENDING`, `APPROVED`, `REJECTED` y `CANCELLED`.

## 7. Consulta de préstamos del estudiante

`MyLoansView.fxml` presenta una tabla con los préstamos del usuario. `MyLoansController` obtiene todos los préstamos mediante `LoanDao`, filtra aquellos cuyo estudiante coincide con el usuario de la sesión y muestra:

- Identificador del préstamo.
- Título del libro.
- Fecha del préstamo.
- Fecha límite.
- Estado.

El título se obtiene consultando los detalles del préstamo y el libro relacionado.

## 8. Gestión de bibliotecarios

`LibrarianView.fxml` ofrece búsqueda, formulario, tabla y operaciones CRUD. `LibrarianController`:

- Filtra usuarios con rol `LIBRARIAN`.
- Permite buscar por código, nombre o correo.
- Crea bibliotecarios.
- Actualiza sus datos y estado activo.
- Elimina bibliotecarios.
- Protege las contraseñas usando `PasswordUtil`.

La información se persiste mediante `UserDao`.

## 9. Persistencia y capas principales

La aplicación usa una separación básica por responsabilidades:

- **Vistas FXML**: estructura visual y acciones de los controles.
- **Controladores**: validación de entradas, coordinación del flujo y navegación.
- **Modelos**: representan usuarios, libros, solicitudes, préstamos y sus detalles.
- **DAO**: acceden a la base de datos mediante JDBC, procedimientos almacenados y consultas SQL.
- **Configuración**: `ConectionDB` construye la conexión MySQL usando los valores de `Environment`.
- **Utilidades**: manejan escenas, sesión, contraseñas, validaciones y alertas.

Los DAO principales son `UserDao`, `BookDao`, `LoanRequestDao`, `LoanRequestDetailDao`, `LoanDao` y `LoanDetailDao`.

## 10. Resumen del flujo completo

El flujo principal observado es:

```text
Inicio de JavaFX
    -> Login
        -> Registro (opcional)
        -> Autenticación
            -> Sesión activa
                -> Dashboard según rol
                    -> Catálogo
                        -> Gestión de inventario (personal)
                        -> Solicitud de préstamo (estudiante)
                    -> Registro directo de préstamo (bibliotecario)
                    -> Solicitudes pendientes (personal autorizado)
                        -> Aprobar o rechazar
                    -> Mis préstamos (estudiante)
                    -> Gestión de bibliotecarios (administrador)
                    -> Cerrar sesión
                        -> Login
```

En términos generales, la aplicación conecta una interfaz JavaFX con una base de datos MySQL para administrar usuarios, catálogo, inventario, solicitudes y préstamos. La navegación se realiza mediante escenas FXML y el acceso a las operaciones de datos se concentra en los DAO.

## Observaciones del análisis superficial

- La generación de comprobantes PDF está declarada, pero no implementada.
- La navegación depende de rutas de recursos FXML cargadas por `SceneManager`.
- Las alertas centralizan la comunicación de errores y resultados al usuario.
- El inventario disponible se reduce al crear o aprobar un préstamo.
- La autorización visual se basa principalmente en el rol almacenado en la sesión.
