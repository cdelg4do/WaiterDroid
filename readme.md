# Waiter Droid

This is a small app to manage the orders of a pub/restaurant, made with Android Studio 2.2.2 using SDK 25.0.1 for Android 4.1+ devices (API 16+).

It can download a remote JSON file with the necessary information about the premises: number of tables, currency used for prices, tax rate, a list of existing allergens and the list of available dishes (including name, picture, price, allergens and description). The URL used can be customized via the settings screen, examples can be found <a href="http://bit.ly/WaiterDroidMenu2">here</a> and <a href="http://www.mocky.io/v2/5848fa091100002e11590b72">here</a>.

After loading the JSON data, the app shows a list with all the existing tables. The detail of a table features a list with all the orders in that table. Each order is a reference to a dish, plus (optionally) a note with the special request the client made for that specific dish. By default all tables are created empty but, for testing purposes, there is an option in the settings to generate random orders and special requests.

The user can add orders to a table at any point, choosing each new dish from a list with the available dishes. In case the app is running in landscape mode, the list will show a detailed description of each dish. In portrait mode, a simplified version of the list (without description) is shown. Also, it is possible to visualize the invoice for a table and empty a table, leaving it ready for the next client(s).

The use of 3rd party libraries has been intentionally omitted for this project. See the *Additional considerations* section for further details.

&nbsp;
### Screenshots:

<kbd> <img alt="screenshot 1" src="https://cloud.githubusercontent.com/assets/18370149/26097470/f722e068-3a24-11e7-8ce7-d6923c2fc278.png" width="256"> </kbd> &nbsp; <kbd> <img alt="screenshot 2" src="https://cloud.githubusercontent.com/assets/18370149/26097471/f731c0ce-3a24-11e7-86c0-d354087f2a62.png" width="256"> </kbd> &nbsp; <kbd> <img alt="screenshot 3" src="https://cloud.githubusercontent.com/assets/18370149/26097472/f740d85c-3a24-11e7-992f-0b4722f64f93.png" width="256"> </kbd>

<kbd> <img alt="screenshot 4" src="https://cloud.githubusercontent.com/assets/18370149/26097473/f75e75e2-3a24-11e7-830c-a50eba0a196d.png" width="256"> </kbd> &nbsp; <kbd> <img alt="screenshot 5" src="https://cloud.githubusercontent.com/assets/18370149/26097474/f76dacba-3a24-11e7-8ff2-3b52a94bbf8d.png" width="256"> </kbd> &nbsp; <kbd> <img alt="screenshot 6" src="https://cloud.githubusercontent.com/assets/18370149/26097475/f78023fe-3a24-11e7-9bdc-aab462cf7abf.png" width="256"> </kbd>

&nbsp;
<kbd> <img alt="screenshot 7" src="https://cloud.githubusercontent.com/assets/18370149/26097477/f7b73d94-3a24-11e7-9ee4-91b78bde2001.png" width="768"> </kbd>

&nbsp;
<kbd> <img alt="screenshot 8" src="https://cloud.githubusercontent.com/assets/18370149/26097476/f7ad82cc-3a24-11e7-9aac-48ee3a50ce63.png" width="768"> </kbd>

&nbsp;
#### Additional considerations:

- The class RestaurantManager has a central role in the application, concentrating all create/read/update operations on the model. It is build as a singleton so that it can be accessed from any point. This way, instead of passing the model objects between activities (which should be serialized and passed by value, making necessary to report back any change to all previous activities), only the object position (table, order) is passed. This approach simplifies the development process and resembles the way it should work in case the model were persisted on a local database.

- The main activity has two different layouts: one with the table list on the left and the table detail on the right (to show in tablets in landscape mode), and other with just the table list for any other case. This is achieved by combining several fragments: TableListFragment & TablePagerFragment.

- The fragment that shows a the table detail (TablePagerFragment) is a ViewPager that allows the user to swipe both sides to iterate over the tables. Each table is represented by another fragment (TableOrdersFragment).

- One last fragment (DishListFragment) is used to represent the list of available dishes.

- The **BackgroundTaskRunnable** interface has been used to describe tasks that should be executed in background. The **BackgroundTaskHandler** class, which extends AsyncTask, is meant to execute that tasks and invoke a given object implementing the **BackgroundTaskListener** interface. These object are in charge to receive the result of the background operations and process them in the main thread. This schema is used when downloading and parsing the remote JSON file (the main thread is blocked by using a progress dialog), and when downloading remote images (no dialog is shown, so that the user can keep working with the app).

- Since the application does not persist any data (apart from the setting options), all downloaded images are stored on a memory cache, represented by the ImageCache class. This is a dictionary that connects the image URL with its binary data, and has been built as a singleton. Every time a remote image is needed, first it is fetched in the dictionary, and in case it does not exist yet, it is downloaded and stored in the cache.

- All text literals, including the random special requests used for testing, have been translated to English and Spanish, and will show depending on the system language (being English the default choice). Data in the JSON file comes just in one language, though.

&nbsp;
#### To-Do list:

- Modify the JSON file format to describe the allergens and dishes in several languages. This will require changes in the RestaurantManager class to parse the JSON data and to access the model data.
