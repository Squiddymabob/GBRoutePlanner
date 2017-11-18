## GB Route Planner

The GB Route Planner android app was made as part of university coursework for the android development module.
I recieved a mark of 92% for this coursework.

![alt text][img10]

### Features
  * Distance between two markers on the map.
  * Adding a new marker at current location.
  * Showing the fastest road route between two markers on the map.
  * Calculating direct distance between two markers and how long it would take to walk between them.
  
#### Map with Location Markers

The main screen of the app is a map that shows all the location markers stored in the database.

![alt text][img1]

These all have names:

![alt text][img2]

The menu presents options for each feature of the app:

![alt text][img3]

#### Adding a Marker at Current Location

The app gets the current location of the device and shows it on the map:

![alt text][img4]

By selecting "Add Marker At Current Location" a new marker can be named and added to the map via a pop up text field:

![alt text][img7]

#### Planning a Route

Selecting "Route Planner" from the menu will bring up the route planning options.
Selecting "Calculate distance" will calculate the direct distance between the starting point and destination.
It will also calculate how long (based on average walking speed) it would take to walk there, in case you wanted to know:

![alt text][img6]

Selecting "Show Route" draws the fastest road route between the two selected markers.
Here is an example showing a route to Edinburgh:

![alt text][img9]

#### Clearing Custom Markers

Selecting "Clear All Custom Markers" from the menu will delete all location markers that have been added by the user:

![alt text][img8]

[img1]: https://github.com/Squiddymabob/GBRoutePlanner/raw/master/screenshots/1.png
[img2]: https://github.com/Squiddymabob/GBRoutePlanner/raw/master/screenshots/2.png
[img3]: https://github.com/Squiddymabob/GBRoutePlanner/raw/master/screenshots/3.png
[img4]: https://github.com/Squiddymabob/GBRoutePlanner/raw/master/screenshots/4.png
[img5]: https://github.com/Squiddymabob/GBRoutePlanner/raw/master/screenshots/5.png
[img6]: https://github.com/Squiddymabob/GBRoutePlanner/raw/master/screenshots/6.png
[img7]: https://github.com/Squiddymabob/GBRoutePlanner/raw/master/screenshots/7.png
[img8]: https://github.com/Squiddymabob/GBRoutePlanner/raw/master/screenshots/8.png
[img9]: https://github.com/Squiddymabob/GBRoutePlanner/raw/master/screenshots/9.png
[img10]: https://github.com/Squiddymabob/GBRoutePlanner/raw/master/screenshots/10.png

### To Improve

If I were to continue development/remake this app I would:
  * Certainly make the line that is drawn for the road route between two points much clearer - easier to see over the all the Google Maps road information.
  * Add the ability to select to delete a single marker from the database instead of all at once.
  * Add a calculation for the distance for the route drawn on the map instead of the direct route.
  * Add the time it would take to drive to a location instead of walk.
