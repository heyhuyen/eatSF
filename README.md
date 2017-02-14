# eatSF
A basic implementation of displaying places to eat in SF on a map and in a list. 

## User Stories
Using the Google Place Search API, display restaurants in the city of San Francisco:
* On a map with markers.
* In a list, displaying the name, at least one photo (if available), and rating (if available) for each item

Tapping on a restaurant marker or list item should display the details of the selected restaurant:
1) Name
2) Formatted Address
3) A way to contact the restaurant (if available)
4) At least one photo (if available)
5) Rating (if available)
6) List of reviews (if available)

## Video Walkthrough
[Link to video walkthrough gif] (http://imgur.com/HsmBIIL.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Open-source libraries used
- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Android
- [ButterKnife](http://jakewharton.github.io/butterknife/) - Field and method binding for Android views

## Build
* Clone the repo
* Import the project into Android Studio
* Build and Run (optional: have Google Maps installed on your emulator or device for map intents from the detail view)

## License

    Copyright 2017 Huyen Tran

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
