# Sistema de Gestión de Biblioteca

**Alumno:** Lucas Valentina Tellería  
**Legajo:** 510136
**Enunciado elegido:** biblioteca

## Descripción

Sistema de gestión de biblioteca desarrollado mediante una API REST con Spring Boot. Permite administrar libros y socios, registrar préstamos, consultar préstamos por socio y gestionar sus estados.

La aplicación incorpora validaciones y reglas de negocio para controlar la disponibilidad de los libros y la cantidad de préstamos activos por socio.

## Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Bean Validation
- H2 Database
- Maven

## Cómo ejecutarlo

**Requisitos:** Java 21 instalado.

Desde la carpeta raíz del proyecto, ejecutar:

En Windows:

```bash
.\mvnw.cmd spring-boot:run
```

En Linux/macOS:

```bash
./mvnw spring-boot:run
```

La aplicación se inicia en:

`http://localhost:8080`

La API está disponible en:

`http://localhost:8080/api`

### Base de datos

Se utiliza H2 como base de datos persistente en archivo.

La información se almacena localmente en la carpeta `data/`.

Consola H2:

`http://localhost:8080/h2-console`

Configuración de conexión:

- JDBC URL: `jdbc:h2:file:./data/biblioteca`
- Usuario: `sa`
- Contraseña: (vacía)

## Modelo

El sistema está compuesto por tres entidades principales:

| Entidad  | Atributos principales       | Relaciones                                            |
| -------- | --------------------------- | ----------------------------------------------------- |
| Libro    | id, titulo, autor, isbn     | Puede tener múltiples préstamos a lo largo del tiempo |
| Socio    | id, nombre, email           | Puede tener múltiples préstamos                       |
| Prestamo | id, fechaDevolucion, estado | Relación muchos a uno con Libro y Socio               |

Cada préstamo referencia un libro y un socio mediante sus identificadores.

### Estados de los préstamos

Los préstamos manejan tres estados:

- `ACTIVO`: el préstamo está vigente.
- `VENCIDO`: el préstamo superó su fecha de devolución.
- `DEVUELTO`: el libro fue devuelto.

Las transiciones permitidas son:

`ACTIVO → VENCIDO → DEVUELTO`

Un préstamo devuelto no puede volver a activarse.

## Endpoints

### Libros

| Método | Ruta               | Descripción            | Respuestas         |
| ------ | ------------------ | ---------------------- | ------------------ |
| GET    | `/api/libros`      | Lista todos los libros | 200                |
| GET    | `/api/libros/{id}` | Busca un libro por ID  | 200, 404           |
| POST   | `/api/libros`      | Registra un libro      | 201, 400, 409      |
| PUT    | `/api/libros/{id}` | Actualiza un libro     | 200, 400, 404, 409 |
| DELETE | `/api/libros/{id}` | Elimina un libro       | 204, 404, 409      |

### Socios

| Método | Ruta                         | Descripción                        | Respuestas |
| ------ | ---------------------------- | ---------------------------------- | ---------- |
| GET    | `/api/socios`                | Lista los socios                   | 200        |
| GET    | `/api/socios/{id}`           | Busca un socio por ID              | 200, 404   |
| POST   | `/api/socios`                | Registra un socio                  | 201, 400   |
| GET    | `/api/socios/{id}/prestamos` | Consulta los préstamos de un socio | 200, 404   |

### Préstamos

| Método | Ruta                             | Descripción                       | Respuestas         |
| ------ | -------------------------------- | --------------------------------- | ------------------ |
| GET    | `/api/prestamos`                 | Lista los préstamos               | 200                |
| GET    | `/api/prestamos?estado=DEVUELTO` | Filtra préstamos por estado       | 200                |
| POST   | `/api/prestamos`                 | Registra un préstamo              | 201, 400, 404, 409 |
| PATCH  | `/api/prestamos/{id}/estado`     | Modifica el estado de un préstamo | 200, 400, 404, 409 |

## Reglas de negocio

1. **ISBN único:** no se permiten libros con ISBN duplicado. Devuelve 409 Conflict.
2. **Disponibilidad del libro:** un libro que ya tiene un préstamo sin devolver no puede prestarse nuevamente. Devuelve 409 Conflict.
3. **Límite de préstamos:** un socio puede tener como máximo tres préstamos sin devolver. Devuelve 409 Conflict al intentar registrar el cuarto.
4. **Fecha de devolución:** no se permite registrar préstamos con una fecha de devolución anterior a la fecha actual. Devuelve 400 Bad Request.
5. **Existencia de entidades:** no se puede registrar un préstamo para un libro o socio inexistente. Devuelve 404 Not Found.
6. **Estados del préstamo:** un préstamo devuelto es definitivo y no puede volver a activarse. Devuelve 409 Conflict.
7. **Eliminación de libros:** no se permite eliminar un libro que tenga un préstamo sin devolver. Devuelve 409 Conflict.

## Manejo de errores

La API utiliza respuestas `ProblemDetail` para informar errores.

Los principales códigos HTTP utilizados son:

- `200 OK`: operación exitosa.
- `201 Created`: recurso creado.
- `204 No Content`: recurso eliminado.
- `400 Bad Request`: datos inválidos o solicitud incorrecta.
- `404 Not Found`: recurso inexistente.
- `409 Conflict`: conflicto con una regla de negocio o dato duplicado.

En los errores de validación se incluyen los campos que presentan inconvenientes.

## Pruebas

El archivo `pruebas.http` contiene peticiones de ejemplo para probar los endpoints desde Visual Studio Code mediante la extensión **REST Client**.

Las pruebas permiten verificar operaciones CRUD, validaciones, relaciones entre entidades, reglas de negocio y cambios de estado de los préstamos.

Además, el proyecto incluye pruebas automatizadas provistas para el enunciado de biblioteca, que permiten comprobar el funcionamiento de la API.
