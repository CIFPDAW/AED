# GameHub XML – AE1.3 CRUD con fichero XML

Aplicación de consola de **GameHub** para gestionar videojuegos. El CRUD trabaja sobre un `ArrayList` en memoria. El fichero `data/videojuegos.xml` solo se lee o se escribe cuando el usuario lo pide.

---

## 1. Descripción breve

1. La opción 1 carga el XML a la lista.
2. Añadir, buscar, listar, modificar y eliminar cambian esa lista.
3. La opción 7 (o la pregunta al salir) vuelve a escribir el XML.
4. La opción 8 busca un id directamente en el fichero con XPath, sin usar la lista de memoria.

---

## 2. Estructura (MVC)

```
XML/
├── data/
│   └── videojuegos.xml
├── src/main/java/dev/danoglez/
│   ├── Main.java
│   ├── models/Videojuego.java
│   ├── views/IVista.java
│   ├── views/VistaTexto.java
│   ├── controllers/Controlador.java
│   ├── services/VideojuegoService.java
│   ├── repositories/IVideojuegoRepository.java
│   ├── repositories/XMLVideojuegoRepository.java
│   └── exception/VideojuegoFileException.java
└── README.md
```

| Capa | Qué hace | Qué no hace |
|---|---|---|
| Modelo | Datos del videojuego | Teclado, pantalla, fichero |
| Vista | Menú y mensajes | Abrir el XML |
| Controlador | Traduce la opción del menú | `DocumentBuilder` ni `Transformer` |
| Servicio | Lista en memoria y validaciones | Pedir datos |
| Repositorio | Lee, crea y guarda el XML. Busca por id con XPath | Conocer el menú |

El `main` solo ensambla las piezas:

```java
public static void main(String[] args) {
    VideojuegoService servicio = new VideojuegoService();

    try (IVista vista = new VistaTexto()) {
        Controlador controlador = new Controlador(vista, servicio);
        controlador.ejecutar();
    }
}
```

---

## 3. XML

El `id` es un atributo. El resto son elementos hijos.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<gamehub>
    <videojuegos>
        <videojuego id="1">
            <titulo>Hollow Knight</titulo>
            <plataforma>PC</plataforma>
            <genero>Metroidvania</genero>
            <horasJugadas>45.5</horasJugadas>
            <finalizado>true</finalizado>
        </videojuego>
    </videojuegos>
</gamehub>
```

Si `data/videojuegos.xml` no existe, el repositorio lo crea con `<gamehub>` y `<videojuegos>` vacíos.

Al guardar se reescribe el documento entero con `Transformer`, codificación UTF-8 y sangría.

La carga y la búsqueda en fichero usan XPath:

- `/gamehub/videojuegos/videojuego` para leer todos.
- `/gamehub/videojuegos/videojuego[@id='X']` para buscar uno.

La opción 3 del menú busca en el `ArrayList`, no en el disco. Así se ve la diferencia entre lo que hay en memoria y lo que ya está guardado.

---

## 4. Menú

```text
==============================
         GAMEHUB XML
==============================
1. Cargar datos desde XML
2. Anadir videojuego
3. Buscar videojuego por ID
4. Mostrar todos los videojuegos
5. Modificar videojuego
6. Eliminar videojuego
7. Guardar cambios en XML
8. Buscar por ID en el XML (XPath)
0. Salir
```

Reglas:

- No se repite un id y el id tiene que ser mayor que 0.
- Título, plataforma y género no pueden ir vacíos.
- Las horas no pueden ser negativas.
- Modificar no cambia el id.
- Eliminar pide confirmación.
- Si hay cambios sin guardar, cargar y salir preguntan antes de pisarlos.

Los errores de fichero salen como `VideojuegoFileException`. Un id repetido o un título vacío salen como `IllegalArgumentException`. La vista muestra el mensaje. No hay `catch` vacíos.

---

## 5. Cómo ejecutar

Desde la carpeta `entrega/XML` (la ruta del fichero es `data/videojuegos.xml`):

```bash
javac -encoding UTF-8 -d target/classes $(find src -name '*.java')
java -cp target/classes dev.danoglez.Main
```

En IntelliJ: abrir el proyecto Maven y ejecutar `dev.danoglez.Main`.

Flujo de prueba:

1. Opción **1** y luego **4**: carga y listado.
2. Opción **2**: alta en memoria.
3. Opción **8**: ese alta todavía no está en el fichero.
4. Opción **7**: guardar.
5. Opción **0**: si no quedan cambios, sale directo.
