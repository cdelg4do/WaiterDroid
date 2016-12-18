# Pr�ctica Fundamentos Android de Carlos Delgado Andr�s

**WaiterDroid** es un prototipo de aplicaci�n para Android realizada en Android Studio 2.2.2.

Se trata de un gestor de mesas de restaurante para tel�fono y tablet. La app descarga un fichero **JSON** desde un servidor remoto que contiene la siguiente informaci�n sobre el restaurante (n�mero de mesas de que dispone el restaurante, divisa en que se aplican los precios, tasa de impuestos, lista de al�rgenos existentes y listado de platos disponibles). El usuario puede a�adir varios pedidos a cada mesa (un pedido es una referencia a un plato del men�, mas una nota con las variaciones espec�ficas a dicho plato solicitadas por un cliente de dicha mesa). Inicialmente todas las mesas estar�n vac�as, esto es, sin ning�n pedido.

En todo momento se puede consultar a cu�nto asciende el importe total de una mesa (el importe de todos sus pedidos m�s la tasa de impuestos correspondiente), y tambi�n se puede vaciar una mesa, dej�ndola sin pedidos nuevamente. En los ajustes de la aplicaci�n se puede personalizar la url de descarga del servidor con el fichero JSON y se puede especificar que se genere una serie de pedidos aleatorios en diversas mesas, para probar el funcionamiento de la app.

.
### Modelo de la aplicaci�n:

Se emplean las siguientes clases, correspondientes a entidades del modelo:

- **Allergen (al�rgeno)**: consta de un nombre, una url al icono que lo representa y un id que lo identifica en el json.

- **Dish (plato del men�)**: consta de nombre, descripci�n, url a la imagen del plato y precio antes de impuestos. Adicionalmente, puede presentar una lista con los al�rgenos que contiene (si es que contiene alguno).

- **Order (pedido)**: contiene una referencia al plato ordenado y (adicionalmente) las notas que el cliente haya especificado sobre dicho plato.

- **Table (mesa)**: consta de un listado de pedidos y de un nombre descriptivo.

- **RestaurantManager**: clase que almacena la informaci�n contenida en el JSON descargado y que contiene el estado actual de todas las mesas.

.
### Consideraciones sobre la pr�ctica:

##### - URL remotas:

La url por defecto descarga un men� de 5 platos en espa�ol:
http://www.mocky.io/v2/5848fa091100002e11590b72

Se puede configurar en los ajustes una url alternativa que descarga 26 platos en ingl�s:
http://bit.ly/WaiterDroidMenu2

##### - Distintos layouts:

Se han definido dos layouts para la actividad **MainActivity**: uno que incorpora dos fragments (con el listado general de las mesas y el listado en detalle de los pedidos de una mesa seleccionada) para mostrar en apaisado en dispositivos de pantalla grande, y otro layout con un �nico fragment (el listado de las mesas) para los dem�s casos. En este �ltimo caso, cuando se selecciona una mesa del listado se invoca a la actividad TablePagerActivity que muestra el detalle de la mesa.

Tambi�n se han definido dos layouts para las celdas del RecyclerView de **DishListActivity**: uno con la vista reducida de los platos (en modo retrato) y otro extendido que adem�s incluye la descripci�n de cada plato (en modo apaisado).

##### - Acceso al modelo:

**La clase RestaurantManager se ha construido como un singleton**, de manera que pueda ser f�cilmente accesible desde cualquier activity o fragment. La raz�n por la que se ha optado por esta soluci�n se debe a que el paso de informaci�n entre activities/fragments se realiza por valor (serializando los objetos pasados), de manera que un cambio en una entidad del modelo (por ejemplo: a�adir un pedido a una mesa) debe escalar progresivamente hacia atr�s hasta la actividad principal para que se guarde como permanente, lo cu�l dificulta significativamente el desarrollo.

En su lugar, cuando se necesita pasar una entidad del modelo a una activity o fragment, se le pasa solo la posici�n en que se encuentra dicha entidad en las listas de RestaurantManager, y es desde la nueva activity/fragment desde donde se accede al modelo. Se ha considerado que este modo de proceder simplifica significativamente el desarrollo de la app, facilita el mantenimiento posterior y resulta m�s acorde con un desarrollo real (en donde la informaci�n se almacenar�a en una base de datos, a la que se puede acceder como si de un singleton se tratase).

##### - Procesamiento en segundo plano:

La app realiza dos tipos de acciones en segundo plano: la descarga de datos del men� y la descarga de im�genes. En el primer caso, la interfaz de usuario se "bloquea" mediante un di�logo de progreso, impidiendo al usuario hacer nada mientras no se descarguen todos los datos del servidor. En el caso de la descarga de im�genes, se realiza sin mostrar ning�n di�logo, de modo que el usuario puede seguir interactuando con la aplicaci�n mientras se descarga la imagen.

Este modelo de procesamiento puede extenderse f�cilmente a otras clases que implementen el interfaz **BackgroundTaskRunnable**, en donde se espcifica qu� acciones debe realizarse en segundo plano y qu� resultados devuelven (si es que devuelven algo).

La clase **BackgroundTaskHandler** (que hereda de AsyncTask) recibe un objeto **BackgroundTaskRunnable** y ejecuta la tarea correspondiente en segundo plano. Tambi�n tiene que recibir otro objeto que implemente el interfaz **BackgroundTaskListener**, que se encargar� de realizar las acciones que correspondan en el hilo principal una vez que la tarea en segundo plano haya concluido.

##### - Cacheado de imagenes:

Dado que la aplicaci�n no persiste ning�n dato del modelo (�nicamente se almacenan las opciones de la ventana de ajustes), las imagenes descargadas de internet se almacenan en una cach� en memoria, representada por un dicionario que relaciona la url de cada imagen con sus datos binarios descargados. Al igual que suced�a con la clase RestaurantManager (y por id�nticos motivos), **esta cach� se ha implementado como un singleton**.

Cada vez que se debe cargar una imagen asociada a una url, primero se busca en la cach� y si no se encuentra entonces se intenta descargar de su ubicaci�n remota. Posteriormente se guardar�n estos datos en la cach� para que est�n disponibles localmente durante el resto de la ejecuci�n.

##### - Literales de texto:

Todos los literales mostrados al usuario han sido extra�dos de los ficheros de c�digo y recogidos en el fichero de recursos values/strings.xml, en versiones de espa�ol e ingl�s.

