# Práctica Fundamentos Android de Carlos Delgado Andrés

**WaiterDroid** es un prototipo de aplicación para Android realizada en Android Studio 2.2.2.

Se trata de un gestor de mesas de restaurante para teléfono y tablet. La app descarga un fichero **JSON** desde un servidor remoto que contiene la siguiente información sobre el restaurante (número de mesas de que dispone el restaurante, divisa en que se aplican los precios, tasa de impuestos, lista de alérgenos existentes y listado de platos disponibles). El usuario puede añadir varios pedidos a cada mesa (un pedido es una referencia a un plato del menú, mas una nota con las variaciones específicas a dicho plato solicitadas por un cliente de dicha mesa). Inicialmente todas las mesas estarán vacías, esto es, sin ningún pedido.

En todo momento se puede consultar a cuánto asciende el importe total de una mesa (el importe de todos sus pedidos más la tasa de impuestos correspondiente), y también se puede vaciar una mesa, dejándola sin pedidos nuevamente. En los ajustes de la aplicación se puede personalizar la url de descarga del servidor con el fichero JSON y se puede especificar que se genere una serie de pedidos aleatorios en diversas mesas, para probar el funcionamiento de la app.

.
### Modelo de la aplicación:

Se emplean las siguientes clases, correspondientes a entidades del modelo:

- **Allergen (alérgeno)**: consta de un nombre, una url al icono que lo representa y un id que lo identifica en el json.

- **Dish (plato del menú)**: consta de nombre, descripción, url a la imagen del plato y precio antes de impuestos. Adicionalmente, puede presentar una lista con los alérgenos que contiene (si es que contiene alguno).

- **Order (pedido)**: contiene una referencia al plato ordenado y (adicionalmente) las notas que el cliente haya especificado sobre dicho plato.

- **Table (mesa)**: consta de un listado de pedidos y de un nombre descriptivo.

- **RestaurantManager**: clase que almacena la información contenida en el JSON descargado y que contiene el estado actual de todas las mesas.

.
### Consideraciones sobre la práctica:

##### - URL remotas:

La url por defecto descarga un menú de 5 platos en español:
http://www.mocky.io/v2/5848fa091100002e11590b72

Se puede configurar en los ajustes una url alternativa que descarga 26 platos en inglés:
http://bit.ly/WaiterDroidMenu2

##### - Distintos layouts:

Se han definido dos layouts para la actividad **MainActivity**: uno que incorpora dos fragments (con el listado general de las mesas y el listado en detalle de los pedidos de una mesa seleccionada) para mostrar en apaisado en dispositivos de pantalla grande, y otro layout con un único fragment (el listado de las mesas) para los demás casos. En este último caso, cuando se selecciona una mesa del listado se invoca a la actividad TablePagerActivity que muestra el detalle de la mesa.

También se han definido dos layouts para las celdas del RecyclerView de **DishListActivity**: uno con la vista reducida de los platos (en modo retrato) y otro extendido que además incluye la descripción de cada plato (en modo apaisado).

##### - Acceso al modelo:

**La clase RestaurantManager se ha construido como un singleton**, de manera que pueda ser fácilmente accesible desde cualquier activity o fragment. La razón por la que se ha optado por esta solución se debe a que el paso de información entre activities/fragments se realiza por valor (serializando los objetos pasados), de manera que un cambio en una entidad del modelo (por ejemplo: añadir un pedido a una mesa) debe escalar progresivamente hacia atrás hasta la actividad principal para que se guarde como permanente, lo cuál dificulta significativamente el desarrollo.

En su lugar, cuando se necesita pasar una entidad del modelo a una activity o fragment, se le pasa solo la posición en que se encuentra dicha entidad en las listas de RestaurantManager, y es desde la nueva activity/fragment desde donde se accede al modelo. Se ha considerado que este modo de proceder simplifica significativamente el desarrollo de la app, facilita el mantenimiento posterior y resulta más acorde con un desarrollo real (en donde la información se almacenaría en una base de datos, a la que se puede acceder como si de un singleton se tratase).

##### - Procesamiento en segundo plano:

La app realiza dos tipos de acciones en segundo plano: la descarga de datos del menú y la descarga de imágenes. En el primer caso, la interfaz de usuario se "bloquea" mediante un diálogo de progreso, impidiendo al usuario hacer nada mientras no se descarguen todos los datos del servidor. En el caso de la descarga de imágenes, se realiza sin mostrar ningún diálogo, de modo que el usuario puede seguir interactuando con la aplicación mientras se descarga la imagen.

Este modelo de procesamiento puede extenderse fácilmente a otras clases que implementen el interfaz **BackgroundTaskRunnable**, en donde se espcifica qué acciones debe realizarse en segundo plano y qué resultados devuelven (si es que devuelven algo).

La clase **BackgroundTaskHandler** (que hereda de AsyncTask) recibe un objeto **BackgroundTaskRunnable** y ejecuta la tarea correspondiente en segundo plano. También tiene que recibir otro objeto que implemente el interfaz **BackgroundTaskListener**, que se encargará de realizar las acciones que correspondan en el hilo principal una vez que la tarea en segundo plano haya concluido.

##### - Cacheado de imagenes:

Dado que la aplicación no persiste ningún dato del modelo (únicamente se almacenan las opciones de la ventana de ajustes), las imagenes descargadas de internet se almacenan en una caché en memoria, representada por un dicionario que relaciona la url de cada imagen con sus datos binarios descargados. Al igual que sucedía con la clase RestaurantManager (y por idénticos motivos), **esta caché se ha implementado como un singleton**.

Cada vez que se debe cargar una imagen asociada a una url, primero se busca en la caché y si no se encuentra entonces se intenta descargar de su ubicación remota. Posteriormente se guardarán estos datos en la caché para que estén disponibles localmente durante el resto de la ejecución.

##### - Literales de texto:

Todos los literales mostrados al usuario han sido extraídos de los ficheros de código y recogidos en el fichero de recursos values/strings.xml, en versiones de español e inglés.

