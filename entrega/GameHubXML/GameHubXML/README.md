# GameHub XML

Aplicación de consola en Java para gestionar un catálogo de videojuegos (CRUD). Los datos se guardan en un fichero XML (`data/videojuegos.xml`) que se lee y se escribe con **DOM**.

## Funcionalidades

```
==============================
         GAMEHUB XML
==============================
1. Cargar datos desde XML
2. Añadir videojuego
3. Buscar videojuego por ID
4. Mostrar todos los videojuegos
5. Modificar videojuego
6. Eliminar videojuego
7. Guardar cambios en XML
8. Buscar por ID directamente en el XML (XPath)
0. Salir
```

- **Cargar**: lee `data/videojuegos.xml` y pasa los videojuegos a un `ArrayList<Game>` en memoria. Si el fichero no existe, lo crea con `<gamehub>` y `<videojuegos>` vacíos. Informa de cuántos videojuegos se han cargado.
- **Añadir / Modificar / Eliminar**: trabajan sobre la lista en memoria. El XML no cambia hasta que se guarda.
- **Guardar**: convierte la lista en un árbol DOM y lo escribe en el fichero con `Transformer` (indentado y en UTF-8).
- **Buscar en el XML (XPath)**: opción extra. Busca el videojuego directamente en el fichero con la expresión `/gamehub/videojuegos/videojuego[@id='X']`. Sirve para ver la diferencia entre lo que hay en memoria y lo que hay guardado.
- **Salir**: si hay cambios sin guardar, pregunta si se quieren guardar antes de salir.

Validaciones:

- No se puede repetir un ID y el ID debe ser mayor que 0.
- Título, plataforma y género no pueden estar vacíos.
- Las horas jugadas no pueden ser negativas (se acepta `12.5` o `12,5`).
- Si se escribe un dato mal por teclado, se vuelve a pedir.
- Antes de eliminar se pide confirmación.
- Si el XML está mal formado o tiene datos incorrectos, se muestra un error y el programa sigue funcionando.

## Estructura del XML

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

El `id` es un atributo de `<videojuego>` y el resto de campos son elementos hijos.

## Cómo se ha aplicado MVC

```
src/main/java/com/mycompany/gamehub/
├── GameHub.java                         -> main: crea la vista, el servicio y el controlador
├── controller/GameHubController.java    -> Controlador
├── exception/GameHubXmlException.java   -> Excepción propia para errores del XML
├── model/Game.java                      -> Modelo
├── repository/IGameHubRepository.java   -> Interfaz del repositorio
├── repository/XmlGameHubRepository.java -> Repositorio XML (DOM, XPath, Transformer)
├── service/GameHubService.java          -> Servicio
├── view/IGameHubView.java               -> Interfaz de la vista
└── view/GameHubView.java                -> Vista
```

| Parte | Clase | Qué hace |
|---|---|---|
| Modelo | `Game` | Guarda los datos de un videojuego y los valida. No lee teclado ni muestra nada. |
| Vista | `GameHubView` | Muestra el menú y los mensajes y pide datos por consola. No toca el XML. |
| Controlador | `GameHubController` | Recoge la opción del menú, llama al servicio y le dice a la vista qué mostrar. |
| Servicio | `GameHubService` | Gestiona el `ArrayList<Game>` en memoria y las reglas (IDs repetidos, cambios pendientes...). |
| Repositorio | `XmlGameHubRepository` | Única clase que lee, crea y guarda el fichero XML. No tiene código de menú. |
| Excepción | `GameHubXmlException` | Agrupa todos los errores de lectura/escritura/parseo del XML. |

Flujo de una operación: `Vista -> Controlador -> Servicio -> Repositorio -> fichero XML`.

## Requisitos técnicos cumplidos

- DOM con `DocumentBuilderFactory`, `DocumentBuilder`, `Document`, `Element`, `NodeList` y `Transformer`.
- `DocumentBuilderFactory` configurado de forma segura (DTD y entidades externas desactivadas, contra XXE).
- XPath para buscar por id en el fichero.
- `try-with-resources` al abrir `InputStream` y `OutputStream`.
- Sin bloques `catch` vacíos: todos los errores se muestran al usuario.
- Sin bases de datos ni librerías externas.

## Cómo ejecutarlo

Proyecto Maven. Se abre en NetBeans y se ejecuta. La ruta `data/videojuegos.xml` es relativa, así que hay que ejecutarlo desde la carpeta del proyecto (NetBeans lo hace así por defecto).
