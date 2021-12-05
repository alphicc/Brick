![MAVEN](https://img.shields.io/badge/Maven-v1.0.0--alpha02-blue) ![API](https://img.shields.io/badge/API-21-brightgreen?color=brightgreen)
<h1 align="center">
    <img height="300" src="https://raw.githubusercontent.com/alphicc/Brick/develop/media/logo.png?token=ALZGDT2QD7AXNL3S4OT6GPTBVUJ7S"/>
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
            <img src="https://raw.githubusercontent.com/alphicc/Brick/develop/media/small%20sample.gif?token=ALZGDT264ED4VX3UUG64IBLBVUJ6Q" width="256"/>
        </td>
        <td>
            <img src="https://raw.githubusercontent.com/alphicc/Brick/develop/media/large%20sample.gif?token=ALZGDTZDSYETJDFHYZW4PKLBVUJ3Y" width="256"/>
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