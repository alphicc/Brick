![MAVEN](https://img.shields.io/badge/Maven-v1.0.0--alpha02-blue) ![API](https://img.shields.io/badge/API-21-brightgreen?color=brightgreen)
<h1 align="center">
    <img height="300" src="https://raw.githubusercontent.com/alphicc/Brick/develop/media/logo.png?token=ALZGDT53SYM5UW6JIH5ORMDBVZQDA"/>
    <br>
	    Brick
    </br>
    <br>
		<em>
			Take control of your apps
		</em>
    </br>
</h1>

Brick is a lightweight library to take completely control of your app.

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
## Samples
<table align="center">
    <tr>
        <td>
            <img src="https://raw.githubusercontent.com/alphicc/Brick/develop/media/small%20sample.gif?token=ALZGDT4V54JKWH2VXD66KPDBVZQDK" width="256"/>
        </td>
        <td>
            <img src="https://raw.githubusercontent.com/alphicc/Brick/develop/media/large%20sample.gif?token=ALZGDTYTZYYSJY54OXTWS3TBVZQD4" width="256"/>
        </td>
    </tr>
    <tr>
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
    implementation("")
}
```
Thats all!
## Usage
1. Create **Router** in **any** place of your project (**Note: router contains all information about screen. Router destroyed == all navigation/screens data destroyed**).
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
    <img height="500" src="https://raw.githubusercontent.com/alphicc/Brick/develop/media/integration%20schema.png?token=ALZGDT6MAZUEFEK5A6B5PCDBVZQLW"/>
</h1>

### Work schema
<h1 align="center">
    <img height="500" src="https://raw.githubusercontent.com/alphicc/Brick/develop/media/work%20schema.png?token=ALZGDT7PXMK4OWBY6ZASBF3BVZQQE"/>
</h1>

### Lifecycle sample
<h1 align="center">
    <img height="600" src="https://raw.githubusercontent.com/alphicc/Brick/develop/media/lifecycle.png?token=ALZGDTY3YIFA7OJAVZYYGJTBVZQVS"/>
</h1>

## Multistack navigation
1. Create nested router using method **branch**.
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
    <img height="600" src="https://raw.githubusercontent.com/alphicc/Brick/develop/media/multistack%20graph%20short.png?token=ALZGDT7PKA4PDTQNG72D7GDBVZSLK"/>
</h1>

### Multistack graph sample (extended)
<h1 align="center">
    <img height="600" src="https://raw.githubusercontent.com/alphicc/Brick/develop/media/multistack%20graph%20full.png?token=ALZGDT3GUAL6Y4P3H2OIGK3BVZSKS"/>
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