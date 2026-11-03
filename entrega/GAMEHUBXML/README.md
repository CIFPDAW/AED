# GAMEHUBXML – guia para empezar el CRUD XML

Referencia de la actividad AE1.3. El menu, el modelo, el servicio y el controlador ya funcionan sobre un `ArrayList`. La clase `XmlVideojuegoRepositorio` esta vacia a proposito: ahi va el DOM, y esa parte la implementas tu en `entrega/XML`.

Mientras `cargar()` y `guardar()` lancen `GameHubXmlException`, las opciones 1 y 7 muestran el aviso y el resto del menu se puede probar en memoria.

---

## 1. Que pide esta actividad, comparado con GameHubDaniel

GameHubDaniel guardaba en texto (`videojuegos.txt`) y en binario (`videojuegos.dat`). Aqui hay un solo almacen: `data/videojuegos.xml`.

El resto de la idea es la misma:

1. El usuario trabaja con una lista en memoria.
2. Anadir, buscar, listar, modificar y eliminar cambian esa lista.
3. El fichero se lee en la opcion 1 y se escribe en la opcion 7 (y al salir, si confirma).

`Videojuego` no hace falta que sea `Serializable`. Eso era para el `.dat`.

---

## 2. Orden para montar un proyecto asi desde cero

Haz una capa, compilala y pasala a la siguiente. Si empiezas por el XML, el menu y las validaciones se mezclan con el DOM y es mas dificil ver donde falla.

1. **Modelo** `Videojuego`: campos privados, constructor, getters, setters y `toString()`. El `id` no tiene setter.
2. **Excepcion** `GameHubXmlException`: dos constructores, mensaje y mensaje + causa.
3. **Interfaz del repositorio**: solo `cargar()` y `guardar(lista)`.
4. **Servicio**: el `ArrayList`, el flag `hayCambios` y el CRUD. En este punto el repositorio puede ser el stub que lanza excepcion.
5. **Vista**: menu del enunciado y metodos para pedir texto, int, double, boolean y s/n.
6. **Controlador**: un `if` por opcion. Pide datos a la vista y llama al servicio.
7. **Main**: crea servicio, vista y controlador. Nada mas.
8. **Prueba en memoria** (opciones 2, 3, 4, 5, 6) antes de abrir el XML.
9. **Repositorio**, en este orden: crear el XML si no existe, cargar, guardar. Prueba la opcion 1 y despues la 7.
10. **Salida**: si `hayCambios` es true, preguntar y, si dice que si, llamar a `guardarEnXml()`.

En tu proyecto `entrega/XML` puedes ir copiando clase a clase desde aqui y, al llegar al repositorio, escribir el DOM en el tuyo.

---

## 3. Estructura

```
GAMEHUBXML/
├── data/
│   └── videojuegos.xml          # formato que tiene que leer y escribir tu repositorio
├── src/main/java/dev/danoglez/gamehubxml/
│   ├── GameHubXml.java          # main
│   ├── models/Videojuego.java
│   ├── views/Vista.java
│   ├── views/VistaTexto.java
│   ├── controllers/Controlador.java
│   ├── services/VideojuegoService.java
│   ├── repositories/IVideojuegoRepositorio.java
│   ├── repositories/XmlVideojuegoRepositorio.java   # pendiente: DOM
│   └── exception/GameHubXmlException.java
└── README.md
```

| Capa | Responsabilidad | No hace |
|---|---|---|
| Modelo | Datos de un videojuego | Teclado, pantalla, fichero |
| Vista | Menu, preguntas, mensajes | Abrir el XML |
| Controlador | Traducir la opcion del menu | `DocumentBuilder`, `Transformer` |
| Servicio | Lista en memoria, validaciones, `hayCambios` | Pedir datos ni pintar el menu |
| Repositorio | Leer y escribir `data/videojuegos.xml` | Conocer las opciones del menu |

El `main` solo ensambla:

```java
VideojuegoService servicio = new VideojuegoService();

try (Vista vista = new VistaTexto()) {
    Controlador controlador = new Controlador(vista, servicio);
    controlador.ejecutar();
}
```

---

## 4. Menu

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
0. Salir
```

Reglas que ya cubren servicio y controlador:

- No se anade un id repetido ni un id menor o igual que 0.
- Titulo, plataforma y genero no pueden ir vacios.
- Las horas no pueden ser negativas.
- Modificar no cambia el id.
- Eliminar pide confirmacion.
- Alta, modificacion y baja marcan `hayCambios`. Cargar y guardar lo ponen a false.
- Al salir, si hay cambios pendientes, pregunta si guardar.

La busqueda por id del menu recorre el `ArrayList`. Eso cubre la "busqueda equivalente recorriendo nodos" del enunciado. XPath queda como alternativa si quieres buscar dentro del `Document` en vez de en la lista.

---

## 5. Forma del XML

`data/videojuegos.xml`:

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

Correspondencia al construir el objeto:

| Dato | Donde esta en el XML | Tipo Java |
|---|---|---|
| id | atributo de `<videojuego>` | `int` |
| titulo | elemento hijo | `String` |
| plataforma | elemento hijo | `String` |
| genero | elemento hijo | `String` |
| horasJugadas | elemento hijo, con punto decimal | `double` |
| finalizado | elemento hijo, texto `true` o `false` | `boolean` |

Si el fichero no existe, la carga debe crearlo con `<gamehub>` y, dentro, `<videojuegos>`, y devolver una lista vacia. El usuario tiene que ver el mensaje de cuantos juegos se han cargado (0 en ese caso).

---

## 6. Lo que tienes que escribir en el repositorio

Clase: `XmlVideojuegoRepositorio`. La ruta ya esta en el campo `fichero` (`data/videojuegos.xml`).

Clases del JDK que entran aqui (vienen en el enunciado, sin librerias externas): `DocumentBuilderFactory`, `DocumentBuilder`, `Document`, `Element`, `NodeList`, `Transformer`. El flujo de entrada o salida va en try-with-resources. Si el parseo o la escritura fallan, lanza `new GameHubXmlException("...", causa)`.

### 6.1. `cargar()`

1. Comprueba si `fichero` existe.
2. Si no existe, crea el documento base (`gamehub` / `videojuegos`), guardalo y devuelve una lista vacia.
3. Si existe, parsea el fichero a un `Document`.
4. Recorre los elementos `videojuego`.
5. De cada uno lee el atributo `id` y el texto de los cinco hijos.
6. Crea un `Videojuego` y anadelo a un `ArrayList`.
7. Devuelve esa lista. El servicio sustituye la coleccion en memoria y pone `hayCambios` a false.

### 6.2. `guardar(lista)`

1. Crea un `Document` nuevo.
2. Crea el elemento `gamehub` y, dentro, `videojuegos`.
3. Por cada `Videojuego` de la lista, crea un `videojuego`, pon el `id` como atributo y anade los cinco elementos hijos con su texto.
4. Escribe el documento encima de `data/videojuegos.xml`, con sangria y codificacion UTF-8.
5. El mensaje "guardado correctamente" lo muestra el controlador cuando este metodo termina. El repositorio no imprime el menu.

Cuando estos dos metodos dejen de lanzar la excepcion de "pendiente", las opciones 1, 7 y la pregunta al salir quedan enlazadas solas: el servicio ya las llama.

---

## 7. Como ejecutarlo

Desde `entrega/GAMEHUBXML`:

```bash
javac -encoding UTF-8 -d target/classes $(find src -name '*.java')
java -cp target/classes dev.danoglez.gamehubxml.GameHubXml
```

El directorio de trabajo tiene que ser la raiz del proyecto, porque la ruta del fichero es `data/videojuegos.xml`.

Prueba recomendada antes de implementar el DOM:

1. Opcion 2: anade un juego.
2. Opcion 4: comprueba que sale en la lista.
3. Opcion 3, 5 y 6: buscar, modificar y eliminar.
4. Opcion 1 o 7: debe salir el aviso de que el XML esta pendiente.
5. Cuando implementes el repositorio, repite 1 y luego 7, vuelve a arrancar y carga con la opcion 1.

---

## 8. Checklist del enunciado

- [x] MVC separado (el `main` no contiene el menu)
- [x] CRUD en memoria con validaciones
- [x] Menu con las ocho opciones
- [x] Confirmacion al eliminar y al salir si hay cambios
- [x] `data/videojuegos.xml` de ejemplo
- [ ] `XmlVideojuegoRepositorio.cargar()` con DOM
- [ ] `XmlVideojuegoRepositorio.guardar()` con `Transformer`, UTF-8 e indentacion
- [ ] Crear el XML base si el fichero no existe
- [ ] Capturas de ejecucion para la entrega
