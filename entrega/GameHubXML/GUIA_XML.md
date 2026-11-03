# Guía: cómo funciona el XML y cómo modificarlo

Toda la lógica del XML está en **una sola clase**: `repository/XmlGameHubRepository.java`.
Si cambia el XML, casi siempre solo hay que tocar esa clase (y a veces `Game.java`).

---

## 1. La idea en 30 segundos

Un XML es un **árbol**. DOM lo carga entero en memoria como objetos Java:

```
Document  (el fichero entero)
 └── <gamehub>              ← raíz:  doc.getDocumentElement()
      └── <videojuegos>
           └── <videojuego id="1">        ← Element.  id es un ATRIBUTO
                ├── <titulo>Hollow Knight</titulo>   ← Element hijo con TEXTO
                └── <horasJugadas>45.5</horasJugadas>
```

| Quiero... | Código |
|---|---|
| Leer un **atributo** (`id="1"`) | `elemento.getAttribute("id")` |
| Leer el **texto de un hijo** (`<titulo>X</titulo>`) | `textoHijo(elemento, "titulo")` |
| Obtener **todas** las etiquetas `<videojuego>` | `doc.getElementsByTagName("videojuego")` |
| **Crear** una etiqueta | `doc.createElement("titulo")` |
| Ponerle **texto** | `e.setTextContent("Hollow Knight")` |
| Ponerle un **atributo** | `e.setAttribute("id", "1")` |
| **Meter** una etiqueta dentro de otra | `padre.appendChild(hijo)` |
| **Borrar** una etiqueta | `e.getParentNode().removeChild(e)` |

Todo en XML es **texto**. Por eso al leer hay que convertir (`Integer.parseInt`, `Double.parseDouble`...) y al escribir hay que pasar a texto (`String.valueOf(...)`).

### Flujo del programa

```
CARGAR:   videojuegos.xml ──DocumentBuilder──> Document ──recorrer nodos──> ArrayList<Game>
TRABAJAR: añadir / modificar / eliminar  → solo en el ArrayList (memoria)
GUARDAR:  ArrayList<Game> ──crear elementos──> Document ──Transformer──> videojuegos.xml
```

### Métodos de `XmlGameHubRepository`

| Método | Qué hace |
|---|---|
| `cargar()` | Crea el fichero si no existe, lo lee y devuelve la lista de `Game`. |
| `guardar(lista)` | Crea un `Document` nuevo, mete un `<videojuego>` por cada `Game` y lo escribe. |
| `buscarPorId(id)` | Busca en el fichero con XPath. |
| `crearXmlSiNoExiste()` | Escribe `<gamehub><videojuegos/></gamehub>` si no hay fichero. |
| `cargarDocumento()` | Fichero → `Document` (con la configuración segura). |
| `guardarDocumento(doc)` | `Document` → fichero (con `Transformer`). |
| `leerVideojuego(e)` | **`<videojuego>` → `Game`** ← lo que cambias si cambia la LECTURA |
| `crearElementoVideojuego(doc, g)` | **`Game` → `<videojuego>`** ← lo que cambias si cambia la ESCRITURA |
| `textoHijo(e, "etiqueta")` | Devuelve el texto de un hijo, o `""` si no existe. |

---

## 2. Casos típicos de cambio

### Caso A: cambian solo los NOMBRES de las etiquetas

Ejemplo, el profesor da este XML:

```xml
<catalogo>
    <juegos>
        <juego codigo="1">
            <nombre>Hollow Knight</nombre>
            <consola>PC</consola>
            ...
```

Solo cambias las **constantes** del principio de `XmlGameHubRepository`:

```java
private static final String ETIQUETA_RAIZ = "catalogo";
private static final String ETIQUETA_CONTENEDOR = "juegos";
private static final String ETIQUETA_VIDEOJUEGO = "juego";
private static final String ATRIBUTO_ID = "codigo";
private static final String ETIQUETA_TITULO = "nombre";
private static final String ETIQUETA_PLATAFORMA = "consola";
```

Y la ruta del fichero en el constructor si se llama distinto:

```java
this.ruta = Path.of("data", "catalogo.xml");
```

Nada más. Lectura, escritura, XPath y creación del fichero usan esas constantes.

### Caso B: añadir un CAMPO nuevo (ej. `<precio>29.99</precio>`)

Son 4 pasos, siempre los mismos:

1. **`Game.java`**: añade `private double precio;`, el parámetro en el constructor, getter, setter (con validación si hace falta) y ponlo en `toString()`.
2. **`XmlGameHubRepository`**: constante nueva
   ```java
   private static final String ETIQUETA_PRECIO = "precio";
   ```
3. **Leer** (en `leerVideojuego`):
   ```java
   double precio = Double.parseDouble(textoHijo(e, ETIQUETA_PRECIO));
   return new Game(id, titulo, plataforma, genero, horas, finalizado, precio);
   ```
4. **Escribir** (en `crearElementoVideojuego`):
   ```java
   eJuego.appendChild(elementoTexto(doc, ETIQUETA_PRECIO, String.valueOf(videojuego.getPrecio())));
   ```

Después el compilador te marcará en rojo los sitios donde se hace `new Game(...)` (controlador y servicio): añade ahí el precio (pedirlo con `vista.pedirDouble("Precio: ")`).

> Si el campo puede **no existir** en XML antiguos, `textoHijo` devuelve `""` y `parseDouble("")` fallaría. Puedes poner un valor por defecto:
> ```java
> String textoPrecio = textoHijo(e, ETIQUETA_PRECIO);
> double precio = textoPrecio.isEmpty() ? 0 : Double.parseDouble(textoPrecio);
> ```

### Caso C: un campo pasa de ELEMENTO a ATRIBUTO (o al revés)

Ejemplo: `<videojuego id="1" plataforma="PC">` en vez de `<plataforma>PC</plataforma>`.

- Leer: cambia `textoHijo(e, ETIQUETA_PLATAFORMA)` por `e.getAttribute(ETIQUETA_PLATAFORMA)`.
- Escribir: cambia
  `eJuego.appendChild(elementoTexto(doc, ETIQUETA_PLATAFORMA, ...))`
  por `eJuego.setAttribute(ETIQUETA_PLATAFORMA, videojuego.getPlataforma());`

Al revés (el `id` como elemento `<id>1</id>`):

- Leer: `int id = Integer.parseInt(textoHijo(e, "id"));`
- Escribir: `eJuego.appendChild(elementoTexto(doc, "id", String.valueOf(videojuego.getId())));`
- XPath: `videojuego[@id='2']` pasa a ser `videojuego[id='2']` (sin la `@`; la `@` significa atributo).

### Caso D: NO hay contenedor (los `<videojuego>` cuelgan directamente de la raíz)

```xml
<gamehub>
    <videojuego id="1">...</videojuego>
</gamehub>
```

- **Leer**: no hay que cambiar nada, `getElementsByTagName` los encuentra estén donde estén.
- **Guardar** (`guardar`): mete los juegos en la raíz en vez de en el contenedor:
  ```java
  for (Game videojuego : videojuegos) {
      raiz.appendChild(crearElementoVideojuego(doc, videojuego));
  }
  ```
  y borra las líneas que crean `contenedor`.
- **Crear vacío** (`crearXmlSiNoExiste`): la base pasa a ser `<gamehub/>`.
- **XPath**: quita `ETIQUETA_CONTENEDOR` de la expresión → `/gamehub/videojuego[@id='1']`.

### Caso E: datos ANIDADOS (un hijo dentro de otro hijo)

```xml
<videojuego id="1">
    <titulo>Hollow Knight</titulo>
    <estudio>
        <nombre>Team Cherry</nombre>
        <pais>Australia</pais>
    </estudio>
</videojuego>
```

Leer: primero coges el elemento intermedio y luego lees dentro de él.

```java
Element eEstudio = (Element) e.getElementsByTagName("estudio").item(0);
String nombreEstudio = textoHijo(eEstudio, "nombre");
String pais = textoHijo(eEstudio, "pais");
```

Escribir: creas el intermedio, le metes los hijos y lo metes en el videojuego.

```java
Element eEstudio = doc.createElement("estudio");
eEstudio.appendChild(elementoTexto(doc, "nombre", videojuego.getEstudio()));
eEstudio.appendChild(elementoTexto(doc, "pais", videojuego.getPais()));
eJuego.appendChild(eEstudio);
```

> Ojo: `getElementsByTagName("nombre")` busca a CUALQUIER profundidad. Si `<videojuego>` tuviera también un `<nombre>` directo, podría coger el del estudio. Por eso es mejor buscar desde el elemento intermedio (`eEstudio`), como arriba.

### Caso F: un campo con VARIOS valores (lista)

```xml
<plataformas>
    <plataforma>PC</plataforma>
    <plataforma>Switch</plataforma>
</plataformas>
```

En `Game` sería `List<String> plataformas`. Leer:

```java
List<String> plataformas = new ArrayList<>();
NodeList nodos = e.getElementsByTagName("plataforma");
for (int i = 0; i < nodos.getLength(); i++) {
    plataformas.add(nodos.item(i).getTextContent().trim());
}
```

Escribir:

```java
Element ePlataformas = doc.createElement("plataformas");
for (String p : videojuego.getPlataformas()) {
    ePlataformas.appendChild(elementoTexto(doc, "plataforma", p));
}
eJuego.appendChild(ePlataformas);
```

---

## 3. XPath en 1 minuto

| Expresión | Significa |
|---|---|
| `/gamehub/videojuegos/videojuego` | Todos los videojuegos (ruta desde la raíz) |
| `//videojuego` | Todos los `<videojuego>` estén donde estén |
| `//videojuego[@id='2']` | El que tiene el atributo id = 2 |
| `//videojuego[plataforma='PC']` | Los que tienen un hijo `<plataforma>` con texto PC |
| `//videojuego[horasJugadas > 50]` | Los que tienen más de 50 horas |
| `//videojuego[@id='2']/titulo` | El `<titulo>` del videojuego 2 |

- `XPathConstants.NODE` → devuelve **un** `Node` (el primero) o `null`.
- `XPathConstants.NODESET` → devuelve un `NodeList` con **todos**. Ejemplo para buscar todos los de PC:

```java
NodeList lista = (NodeList) xpath.evaluate("//videojuego[plataforma='PC']", doc, XPathConstants.NODESET);
for (int i = 0; i < lista.getLength(); i++) {
    Game g = leerVideojuego((Element) lista.item(i));
}
```

---

## 4. Errores típicos

| Error que ves | Causa probable |
|---|---|
| `El fichero XML está mal formado` | Etiqueta sin cerrar, mayúsculas distintas (`<Titulo>...</titulo>`), un `&` suelto escrito a mano (debe ser `&amp;`). |
| `La raíz del XML debería ser <gamehub>...` | El XML tiene otra raíz: cambia `ETIQUETA_RAIZ`. |
| `Datos incorrectos ... For input string: ""` | Falta una etiqueta o su nombre no coincide con la constante (XML distingue mayúsculas). |
| `Datos incorrectos ... For input string: "45,5"` | En el XML los decimales van con **punto**. |
| Se cargan **0** videojuegos y el XML tiene datos | `ETIQUETA_VIDEOJUEGO` no coincide con el nombre real de la etiqueta. |
| No encuentra el fichero / crea uno vacío | Se está ejecutando desde otra carpeta. La ruta `data/...` es relativa a la carpeta del proyecto. |
