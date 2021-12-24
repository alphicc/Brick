![MAVEN](https://img.shields.io/badge/Maven-v1.0.0--alpha05-blue) ![API](https://img.shields.io/badge/API-21-brightgreen?color=brightgreen)
<h1 align="center">
    <img height="300" src="https://raw.githubusercontent.com/alphicc/Brick/main/media/logo.png"/>
    <br>
	    Brick
    </br>
    <br>
		<em>
			Take control of your apps
		</em>
    </br>
</h1>

Brick is a lightweight library to make navigation.

## Features
+ Framework free (Router can be injected in any layer of project. Navigate from any place you want. Example: UI-navigation or business logic navigation)
+ Child screen navigation (easy BottomSheet navigation, Tab navigation, Dialogs navigation)
+ Nested navigation
+ Lifecycle
+ Multi-module navigation (provide router instance from any module to any module or just implement all app navigation inside one module or something else)
+ Deep-link support
+ Arguments support
+ Screens communication support
+ Current screen check
+ Transition animations
+ Overlay navigation
## Samples
<table align="center">
    <tr>
        <td>
            <img src="https://raw.githubusercontent.com/alphicc/Brick/main/media/overlay%20sample.gif" width="256"/>
        </td>
        <td>
            <img src="https://raw.githubusercontent.com/alphicc/Brick/main/media/animations%20sample.gif" width="256"/>
        </td>
        <td>
            <img src="https://raw.githubusercontent.com/alphicc/Brick/main/media/small%20sample.gif" width="256"/>
        </td>
        <td>
            <img src="https://raw.githubusercontent.com/alphicc/Brick/main/media/large%20sample.gif" width="256"/>
        </td>
    </tr>
    <tr>
        <td align="center">
            Overlay sample
        </td>
        <td align="center">
            Transition Animation
        </td>
        <td align="center">
            Small sample
        </td>
        <td align="center">
            Large sample
        </td>
    </tr>
</table>

## Installation
Add repository in your project
```kotlin
repositories {
    mavenCentral()
}
```
Add the dependency in your build.gradle
```kotlin
dependencies {
    //Brick
    implementation 'io.github.alphicc:brick:1.0.0-alpha05'
}
```
Thats all!
## Usage
1. Create **Router** in **any** place of your project (**Note: router contains all information about screen. Router destroyed = all navigation/screens data destroyed**).
```kotlin
val router: TreeRouter = TreeRouter.new()
```
2. Create **Screen**. **Screen != UI**. Screen has lifecycle, channels to communicate between other screens. Screen can live without UI. UI (only android composable function for now &#128526;) - part of Screen.
```kotlin
val screen1 = Screen<Unit>(
  key = "1",
  content = { SimpleScreen(1, "new") { smallSampleRouter.addScreen(screen2) } } // content - ui
)
```
3. Provide **ContainerConnector** to **AnimatedScreensContainer** or  **ScreensContainer** . Your Router implements **ContainerConnector**.
```kotlin
class SmallSampleActivity : ComponentActivity() {

  val containerConnector: ContainerConnector = ... //inject or provide from application class

  override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)

      setContent {
          ScreensContainer(containerConnector)
      }
  }
}
```
4. Navigate!
## Work representation
### Integration schema
<h1 align="center">
    <img height="500" src="https://raw.githubusercontent.com/alphicc/Brick/main/media/integration%20schema.png"/>
</h1>

### Work schema
<h1 align="center">
    <img height="500" src="https://raw.githubusercontent.com/alphicc/Brick/main/media/work%20schema.png"/>
</h1>

### Lifecycle sample
<h1 align="center">
    <img height="600" src="https://raw.githubusercontent.com/alphicc/Brick/main/media/lifecycle.png"/>
</h1>

## Multistack navigation
1. Create nested router using **branch** method.
```kotlin
//Screens.bottomMenuScreen.key - screen key that contains nested container
private val firstMenuRouter = mainRouter.branch(Screens.bottomMenuScreen.key).apply {
  addScreen(Screens.innerNavigationScreen, this)//initial navigation sample
}
```

2. Pass created nested router to your nested **ScreensContainer**.
```kotlin
//inside your composable function
AnimatedScreensContainer(firstMenuRouter)
```
3. Use your nested router to make nested navigation!

### Multistack graph sample (simple)
<h1 align="center">
    <img height="600" src="https://raw.githubusercontent.com/alphicc/Brick/main/media/multistack%20graph%20short.png"/>
</h1>

### Multistack graph sample (extended)
<h1 align="center">
    <img height="600" src="https://raw.githubusercontent.com/alphicc/Brick/main/media/multistack%20graph%20full.png"/>
</h1>

## Navigation methods list
+ currentScreenKey
+ backScreen
+ backToScreen
+ replaceScreen
+ addScreen
+ lastChildKey
+ backChild
+ backToChild
+ replaceChild
+ addChild
+ cleanRouter
+ setOverlay
+ removeOverlay