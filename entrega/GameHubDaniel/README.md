# GameHubDaniel – AE1.1 CRUD con ficheros

Aplicación de consola de **GameHub** (UT1 Acceso a Datos) para gestionar videojuegos:

1. Trabaja con una colección **en memoria** (`ArrayList`).
2. Puede **cargar / guardar** en fichero de texto y en fichero binario.
3. El menú hace el CRUD sobre la lista; los ficheros solo se tocan cuando el usuario lo pide (o al salir).

---

## 1. Descripción breve

El programa no escribe en disco en cada alta o baja. Primero cambia la lista en RAM y luego, con las opciones 8 y 9 (o al salir), vuelca esa lista a:

- `data/videojuegos.txt` → texto legible separado por `:`
- `data/videojuegos.dat` → objetos serializados

Así se cumple el enunciado: *“mantener una colección en memoria y decidir cuándo guardar”*.

---

## 2. Estructura del proyecto (MVC)

```
GameHubDaniel/
├── data/
│   └── videojuegos.txt          # fichero de texto de ejemplo
├── src/main/java/dev/danoglez/gamehubdaniel/
│   ├── GameHubDaniel.java       # main: monta vista + servicio + controlador
│   ├── models/
│   │   └── Videojuego.java      # datos + Serializable
│   ├── views/
│   │   ├── Vista.java           # contrato (interfaz)
│   │   └── VistaTexto.java      # menú y teclado por consola
│   ├── controllers/
│   │   └── Controlador.java     # interpreta la opción del menú
│   ├── services/
│   │   └── VideojuegoService.java   # CRUD y ArrayList en memoria
│   ├── repositories/
│   │   ├── IVideojuegoRepositorio.java
│   │   ├── TextoVideojuegoRepositorio.java
│   │   └── BinarioVideojuegoRepositorio.java
│   └── exception/
│       └── GameHubFileException.java
└── README.md
```

| Capa | Qué hace | Qué **no** hace |
|---|---|---|
| **Modelo** (`Videojuego`) | Guarda los campos del juego | Pedir teclado ni imprimir |
| **Vista** | Menú, preguntas, mensajes | Tocar ficheros |
| **Controlador** | Según la opción, llama al servicio | Abrir `FileReader` / streams |
| **Servicio** | Validar, buscar, añadir, borrar en el `ArrayList` | Hablar con el usuario |
| **Repositorio** | Leer / escribir TXT o DAT | Conocer el menú |

### Arranque (`GameHubDaniel.java`)

El `main` solo ensambla las piezas. No hay lógica de menú ni de ficheros aquí:

```java
public static void main(String[] args) {
    VideojuegoService servicio = new VideojuegoService();

    try (Vista vista = new VistaTexto()) {
        Controlador controlador = new Controlador(vista, servicio);
        controlador.ejecutar();
    }
}
```

`try-with-resources` cierra la vista (y el `Scanner`) al terminar.

---

## 3. Modelo: `Videojuego`

Campos del enunciado: `id`, `titulo`, `plataforma`, `genero`, `horasJugadas`, `finalizado`.

Implementa `Serializable` para poder meter la lista en el `.dat` con `ObjectOutputStream` (apartado 7 del tema):

```java
public class Videojuego implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String titulo;
    private String plataforma;
    private String genero;
    private double horasJugadas;
    private boolean finalizado;

    public Videojuego(int id, String titulo, String plataforma, String genero,
            double horasJugadas, boolean finalizado) {
        assert id > 0 : "El ID debe ser mayor que 0";
        assert titulo != null && !titulo.isBlank() : "El titulo no puede estar vacio";
        assert horasJugadas >= 0 : "Las horas jugadas no pueden ser negativas";
        // ...
    }
}
```

`serialVersionUID` fijo evita problemas al leer un `.dat` antiguo si la clase cambia poco. Los `assert` validan datos básicos al crear el objeto.

---

## 4. Servicio: el CRUD en memoria

El servicio guarda la lista y marca si hay cambios sin guardar:

```java
private List<Videojuego> videojuegos;
private final IVideojuegoRepositorio repositorioTexto;
private final IVideojuegoRepositorio repositorioBinario;
private boolean hayCambios;
```

### Alta (no deja ids repetidos)

```java
public void anadir(Videojuego videojuego) {
    if (buscarPorId(videojuego.getId()) != null) {
        throw new IllegalArgumentException("Ya existe un videojuego con ese ID.");
    }
    videojuegos.add(videojuego);
    hayCambios = true;   // hay que guardar si queremos persistirlo
}
```

### Búsqueda por id

```java
public Videojuego buscarPorId(int id) {
    for (Videojuego videojuego : videojuegos) {
        if (videojuego.getId() == id) {
            return videojuego;
        }
    }
    return null;
}
```

### Modificar / eliminar

- **Modificar**: busca por id, cambia título, plataforma, género, horas y finalizado. **No cambia el id**.
- **Eliminar**: quita el objeto del `ArrayList` (tras confirmación en la vista).

Cargar desde fichero sustituye la lista en memoria. Guardar manda esa lista al repositorio correspondiente.

---

## 5. Repositorios: persistencia

Los dos repositorios cumplen la misma interfaz:

```java
public interface IVideojuegoRepositorio {
    List<Videojuego> cargar() throws GameHubFileException;
    void guardar(List<Videojuego> videojuegos) throws GameHubFileException;
}
```

Así el servicio trata igual el TXT y el DAT.

### 5.1. Fichero de texto (`TextoVideojuegoRepositorio`)

**Formato de cada línea:**

```text
id:titulo:plataforma:genero:horasJugadas:finalizado
```

Ejemplo en `data/videojuegos.txt`:

```text
1:Hollow Knight:PC:Metroidvania:45.5:true
2:Zelda BOTW:Switch:Aventura:120.0:false
3:Celeste:PC:Plataformas:18.0:true
```

**Lectura** con `BufferedReader` (apartado 5.2):

```java
try (BufferedReader reader = new BufferedReader(new FileReader(fichero))) {
    String linea;
    while ((linea = reader.readLine()) != null) {
        if (linea.isBlank()) {
            continue;
        }
        String[] datos = linea.split(":", -1);  // -1 conserva campos vacíos
        if (datos.length != 6) {
            throw new GameHubFileException("Formato incorrecto en la linea: " + linea);
        }
        // parsear id, titulo, plataforma, genero, horas, finalizado...
    }
}
```

**Escritura**: sobrescribe el fichero completo (no append), una línea por juego:

```java
try (BufferedWriter writer = new BufferedWriter(new FileWriter(fichero))) {
    for (Videojuego videojuego : videojuegos) {
        writer.write(videojuego.getId() + ":"
                + videojuego.getTitulo() + ":"
                + videojuego.getPlataforma() + ":"
                + videojuego.getGenero() + ":"
                + videojuego.getHorasJugadas() + ":"
                + videojuego.isFinalizado());
        writer.newLine();
    }
}
```

### 5.2. Fichero binario (`BinarioVideojuegoRepositorio`)

Se serializa **toda la lista** de una vez (apartado 7). No se hace append: cada `ObjectOutputStream` escribe cabecera; si abrieras en append habría dos cabeceras y fallaría al leer.

```java
// Guardar
try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(fichero))) {
    salida.writeObject(new ArrayList<>(videojuegos));
}

// Cargar
try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(fichero))) {
    return (List<Videojuego>) entrada.readObject();
}
```

---

## 6. Controlador y menú

El controlador no abre ficheros: pide datos a la vista y llama al servicio.

```java
int opcion = vista.pedirOpcion();

if (opcion == 1) {
    servicio.cargarDesdeTexto();
} else if (opcion == 3) {
    anadirVideojuego();   // pide datos a la vista y llama a servicio.anadir(...)
} else if (opcion == 8) {
    servicio.guardarEnTexto();
} else if (opcion == 0) {
    salir = confirmarSalida();
}
```

Menú (como el enunciado):

```text
==============================
        GAMEHUB - UT1
==============================
1. Cargar datos desde fichero de texto
2. Cargar datos desde fichero binario
3. Anadir videojuego
4. Buscar videojuego por ID
5. Mostrar todos los videojuegos
6. Modificar videojuego
7. Eliminar videojuego
8. Guardar datos en fichero de texto
9. Guardar datos en fichero binario
0. Salir
```

### Salida (requisito 4.11)

Antes de cerrar pregunta si actualizar **los dos** ficheros:

```java
private boolean confirmarSalida() throws GameHubFileException {
    if (servicio.hayDatosEnMemoria() || servicio.hayCambios()) {
        boolean guardar = vista.pedirConfirmacion(
                "Hay datos en memoria. ¿Desea actualizar los ficheros antes de salir?");
        if (guardar) {
            servicio.guardarEnAmbos();  // TXT + DAT
        }
    }
    return true;
}
```

### Errores

- `GameHubFileException` → problemas de lectura/escritura o formato de línea.
- `IllegalArgumentException` → id repetido, juego inexistente, etc.
- La vista muestra el mensaje; no hay `catch` vacíos.

---

## 7. Cómo ejecutar

Desde la carpeta `GameHubDaniel`:

```bash
javac -encoding UTF-8 -d target/classes $(find src -name '*.java')
java -cp target/classes dev.danoglez.gamehubdaniel.GameHubDaniel
```

En NetBeans / IntelliJ: abrir el proyecto Maven y ejecutar la clase principal  
`dev.danoglez.gamehubdaniel.GameHubDaniel`.

Flujo típico de prueba:

1. Opción **1** → cargar el TXT de ejemplo.
2. Opción **5** → listar.
3. Opción **3** → añadir un juego nuevo.
4. Opción **8** y **9** → guardar TXT y DAT.
5. Opción **0** → salir (responder `s` o `n` a actualizar ficheros).

---

## 8. Decisiones de diseño

1. **Memoria primero**: el CRUD nunca escribe solo; hay que guardar explícitamente.
2. **Dos repositorios, una interfaz**: se puede cambiar el formato sin tocar el menú.
3. **Binario = lista completa serializada**: simple y alineado con el tema (sin append).
4. **`Locale.US` en el `Scanner`**: los decimales se escriben con punto (`45.5`), como en el fichero de ejemplo.
5. **Extras comentados** (sincronizar TXT/DAT, buscar por título, filtrar por finalizado…): variantes útiles, pero **no** están en el menú obligatorio del AE1.1.
6. **Sincronizar (opción 10, extra)**: se mezclan TXT y DAT por id. Si el id solo está en uno, se añade. Si está en los dos, se queda el del texto y el del DAT se copia al final con un id nuevo, aunque el título sea el mismo.

---

## 9. Capturas de ejecución

Añadir aquí capturas (o pegar la salida de consola) de:

1. Menú principal  
2. Carga desde texto + listado  
3. Alta de un videojuego  
4. Guardado en TXT / DAT  
5. Pregunta al salir  

Ejemplo de salida al listar tras cargar:

```text
Id: 1 | Titulo: Hollow Knight | Plataforma: PC | Genero: Metroidvania | Horas: 45.5 | Finalizado: true
Id: 2 | Titulo: Zelda BOTW | Plataforma: Switch | Genero: Aventura | Horas: 120.0 | Finalizado: false
Id: 3 | Titulo: Celeste | Plataforma: PC | Genero: Plataformas | Horas: 18.0 | Finalizado: true
```
