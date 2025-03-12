# Self-study
Self-study is a quiz app that tracks your learning progress. The user answers a series of questions (via text or voice), then checks the correctness of the answer himself. The app keeps statistics and can ask understudied questions first. The user can create, import and edit their own question packs.
<h3>Areas of use:</h3>

- Learning foreign words;
- Learning any material in a question-answer format;
- Practicing pronunciation through tongue-twisters. The user is prompted to record a tongue-twister through a voice recorder and then check the quality of pronunciation.

<p align = "center">
  <img src="https://user-images.githubusercontent.com/116489108/197391567-fce06072-88ed-40bd-ba4c-c4c143ad2586.gif" width="250">
  <img src="https://user-images.githubusercontent.com/116489108/197391577-7e88036a-c650-4d51-8920-e85a84779fef.png" width="250">
  <img src="https://user-images.githubusercontent.com/116489108/197391574-fb93484f-4cc3-4dd5-a0db-ec9122466769.png" width="250">
</p>
<p align = "center">
  <img src="https://user-images.githubusercontent.com/116489108/197391576-eee51abe-b926-4769-8221-38c3102bf3be.png" width="250">
  <img src="https://user-images.githubusercontent.com/116489108/197391579-b9e489c1-7edc-4f56-8fe7-cda2fdd01315.png" width="250">
  <img src="https://user-images.githubusercontent.com/116489108/197391580-00e46483-5d42-41ba-bdb1-427c6ec55644.png" width="250">
</p>

# Architecture
The application is built on MVVM architecture with 3 separate layers:
Data layer (SQL database with room library as an abstraction layer)
Domain layer (repositories and use-case)
UI layer (activity, fragments, dialogs)
<h3>The application is also built on:</h3>

- Kotlin Coroutines;
- Hilt dependency injection library;
- Jetpack's Navigation component with Safe Args Gradle plugin;
- DataBinding;
- Gradle’s Kotlin DSL;
- Room library for work with SQLite database;
- Exoplayer for work with audio;
- <p><a href="https://github.com/doyaaaaaken/kotlin-csv">Kotlin-csv library</a> for work with csv files;</p>
- Gson serialization/deserialization library;
- AndroidX SplashScreen library.
