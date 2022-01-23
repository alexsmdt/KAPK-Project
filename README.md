# FoodTracker - Kapk-Assignment

## General Info
The FoodTracker app is an app to track your daily consumed food. You can also use the app to track calories, carbohydrates, proteins and fats. The app uses a real-time database from Firebase and allows each user to add new entries to the global list of foods. Authentication is required when starting the app in order to save the consumed food for each user. The authentication could basically be done via a Google or GitHub account.

## General Functionalities
* Add + List global food items 
* Add + List + Delete + Update consumed food items for each user
* Authentication via Google (I accidentely delted the OAuth-key from the Google Console, so that this login activity is not working right now. Unfortunately it was not yet possible to create a new one, because Google needs a few days to make changes at this point)
* Authentication via GitHub (Implemented because of the Google Login problem)
* Filtering foods by Product Name, Brand or Barcode
* Barcodescanning by using camera or gallery (local or Google Drive)

## Persistence

For persistence, I used the Firebase Relative Database. All conventional database operations (CRUD - create, read, update, delete) can be applied user-friendly from the app. The database contains a section for global data, which can be accessed by all users, and separate data for each user, which are subdivided for each date.

## User Interface

I tried to make the app as user-friendly as possible. The app uses app navigation via menus, simple UI elements, spinners to select product categories for example, a Nav Drawer and other elements. Furthermore I implemented an animated SplashScren, as well as the possibility to switch betwenn Night and Normal Mode

## Sample app run-through

The application always starts with a SplashscreenActivity which will lead us to the GoogleLoginActivity. In this Activity we check if a user is already registrated and logged in. If a user is already logged in, he will be led to the OverviewActivity after the two FoodStore instances were successfully initialized. If not one has to choose between Login via Google or GitHub Account. 

Now the app should show the total consumed calories, fats, proteins and carbs on today's day. Furthermore, a ProgressBar shows how much of the daily reference amount of an adult has already been consumed.

If you swipe your finger from the left edge to the right, a navigator menu appears, which offers you the possibility to log out, return to the Home activity or turn NightMode on or off.

To continue using the app, the user must now press one of the Meal buttons to add consumed products. After pressing the button, the user will get a list of the products consumed today for the selected meal. When using the app for the first time today, this list is initially empty.

If you want to add something to the meal, you can do so by pressing the button in the upper right corner. A new activity opens with a list of globally available products in the database. With a SearchBar a filtering can be done by product name, brand or barcode by scan.

If your desired product does not exist, it can be newly created in the database. Two activities are used to guide you through the process of creating a new product. Again a barcode scanner can be used for adding a produkts barcode.

Now, if you want to add a selected product to the consumed ones, you have to click on the product and specify the serving size within the next activity. 

When you return to the consumed products of a meal by pressing the Cancel button, you can click on them to add them again, edit them or delete them. All operations are also modified directly in the database.

Navigating back to the OverviewActivity you can see the updated summed values and progress bars

## References
### Firebase
* https://firebase.google.com/docs/android/setup
* https://firebase.google.com/docs
* https://firebase.google.com/docs/auth/android/google-signin
* https://developers.google.com/ml-kit/vision/barcode-scanning/android
* https://firebase.google.com/docs/auth/android/github-auth

### Splash Screen
* https://www.youtube.com/watch?v=YcUip0Y8CUg
* https://lottiefiles.com

### others
* https://developer.android.com/guide/navigation/navigation-ui
* https://developer.android.com
* https://developer.android.com/guide/topics/ui/controls/spinner
* https://www.youtube.com/watch?v=QhGf8fGJM8U
* https://www.youtube.com/watch?v=sJ-Z9G0SDhc
