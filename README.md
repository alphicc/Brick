![MAVEN](https://img.shields.io/badge/Maven-v2.0.0-blue) ![Platform](https://img.shields.io/badge/platform-android-green?color=lightgray) ![API](https://img.shields.io/badge/API-21-brightgreen?color=brightgreen) ![Platform](https://img.shields.io/badge/platform-desktop-green?color=lightgray)
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

+ Framework free (Router can be injected in any layer of project. Navigate from any place you want. Example:
  UI-navigation or business logic navigation)
+ Child component navigation (easy BottomSheet navigation, Tab navigation, Dialogs navigation)
+ Composite navigation (build component from many mini-components.)
+ Decompose everything (Decompose component by buttons, text fields etc.!)
+ Nested navigation
+ Lifecycle
+ Multi-module navigation (provide router instance from any module to any module or just implement all app navigation
  inside one module or something else)
+ Deep-link support
+ Arguments support
+ components communication support
+ Current component check
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

<table align="center">
    <tr>
        <td>
            <img src="https://raw.githubusercontent.com/alphicc/Brick/main/media/desktop%20sample.gif"/>
        </td>
    </tr>
    <tr>
        <td align="center">
            Desktop sample
        </td>
    </tr>
</table>

## Installation

Add repository in your project

```kotlin
repositories {
    mavenCentral()
    maven {
        url "https://maven.pkg.jetbrains.space/public/p/compose/dev"
    }
}
```

Add the dependency in your build.gradle

```kotlin
dependencies {
    //Brick
    implementation 'io.github.alphicc:brick:2.0.0'
}
```

Android: Set jvmTarget in your build.gradle

```kotlin
kotlinOptions {
    jvmTarget = '11'
}
```

Thats all!

## Usage

1. Create **Router** in **any** place of your project (**Note: router contains all information about component. Router
   destroyed = all navigation/components data destroyed**).

```kotlin
val router: TreeRouter = TreeRouter.new()
```

2. Create **Component**. **Component != UI**. Component has lifecycle, channels to communicate between other components.
   Component can live without UI. UI - part of Component.

```kotlin
val component1 = Component<Unit>(
    key = "1",
    content = { _, _ -> SimpleComponent(1, "new") { smallSampleRouter.addComponent(component2) } } // content - ui
)
```

3. Provide **ContainerConnector** to **%PLATFORM%AnimatedComponentsContainer** or  **%PLATFORM%ComponentsContainer** .
   Your Router implements **ContainerConnector**.

```kotlin
class SmallSampleActivity : ComponentActivity() {

    val containerConnector: ContainerConnector = ... //inject or provide from application class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AndroidComponentsContainer(containerConnector) {
                //on router empty callback
            }
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
//Components.bottomMenuComponent.key - component key that contains nested container
private val firstMenuRouter = mainRouter.branch(Components.bottomMenuComponent.key).apply {
    addComponent(Components.innerNavigationComponent, this)//initial navigation sample
}
```

2. Pass created nested router to your nested **%PLATFORM%ComponentsContainer**.

```kotlin
//inside your composable function
%PLATFORM % AnimatedComponentsContainer(firstMenuRouter)
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

## Composite navigation

Starting with version 2.* and above, you can extract small UI components to the **Component** and paste this small
components in any place of other **Component** you want. Thus making them reusable. It also makes it possible to further
decompose work when working in the team. Allowing you to work on one small UI component (like button, text field etc.)
without affecting other components.

Screen sample
<h1 align="center">
    <img height="600" src="https://raw.githubusercontent.com/alphicc/Brick/main/media/composite%20screen.png"/>
</h1>

Decompose your screen by components

<h1 align="center">
    <img height="600" src="https://raw.githubusercontent.com/alphicc/Brick/main/media/screen%20parts.png"/>
</h1>

**Code sample**

Main screen component

```kotlin
val compositeScreen = Component<Unit>(
    key = "CompositeScreen",
    content = { _, compositeContainer ->
        Box(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.align(Alignment.TopCenter)) {
                compositeContainer.place(component1.key)//use place method to define position of your component
            }

            Box(modifier = Modifier.align(Alignment.Center)) {
                compositeContainer.place(component3.key)
            }

            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                compositeContainer.place(component2.key)
            }
        }
    }
)
```

Components.
```kotlin
val component1 = Component<Unit>(
    key = "CompositeScreenInternal 1",
    onCreate = { _, _ -> }, lifecycle of your componentg
    onDestroy = { _ -> },
    content = { _, _ -> Text("CompositeScreenInternal 1") }
)

val component2 = Component<Unit>(
    key = "CompositeScreenInternal 2",
    content = { _, _ -> Text("CompositeScreenInternal 2") }
)

val component3 = Component<Unit>(
    key = "CompositeScreenInternal 3",
    content = { _, _ ->
        Button({}) {
            Text("CompositeScreenInternal 3")
        }
    }
)
```

Attach your components to main component (screen)
```kotlin
compositeSampleRouter.addComponent(compositeScreen) // default navigation method. like addScreen method in 1.*.*
compositeSampleRouter.attachCompositeComponent(component1, "321") // attach component into compositeScreen
compositeSampleRouter.attachCompositeComponent(component2) //composite components will be attached to current main component (screen)
compositeSampleRouter.attachCompositeComponent(component3)
compositeSampleRouter.detachCompositeComponent(component3) // use to detach composite component from main component
```

**Note: composite components are shared between the component they are added to and its child components**

## Communicate between components

**Pass data**
```kotlin
// router - is your TreeRouter
// Screens.channelArgumentReceiveComponent.key - key of your destination component
// counter - argument
router.passArgument(Screens.channelArgumentReceiveComponent.key, counter)
```

**Receive data**
```kotlin
...
onCreate = { channel, _ -> return@Component ChannelArgumentReceiveViewModel(channel) }, //channel it is a SharedFlow that located in component onCreate lifecycle method
...

// Use method get<T> to receive your data
channel.onEach { _count.value = it.get() }.launchIn(scope)
```

## Navigation methods list

+ currentComponentKey
+ backComponent
+ backToComponent
+ replaceComponent
+ addComponent
+ lastChildKey
+ backChild
+ backToChild
+ replaceChild
+ addChild
+ cleanRouter
+ setOverlay
+ removeOverlay
+ newRootComponent
+ attachCompositeComponent
+ detachCompositeComponent

## Migrate from 1.* to 2.* version
+ Rename **AndroidAnimatedScreensContainer** to **AndroidAnimatedComponentsContainer**
+ Rename **AndroidScreensContainer** to **AndroidComponentsContainer**
+ Rename **DesktopAnimatedScreensContainer** to **DesktopAnimatedComponentsContainer**
+ Rename **DesktopScreensContainer** to **DesktopComponentsContainer**
+ To handle router empty action provide **onRouterEmpty** callback into **%PLATFORM%ComponentsContainer**
+ Rename **Screen\<T>** to **Component\<T>**
+ Inside **Component\<T>** change
```kotlin
...
content = { dataContainer ->  }
...
//to
...
content = { dataContainer, compositeContainer ->  }
...
```
+ Rename **currentScreenKey** to **currentComponentKey**
+ Rename **backScreen** to **backComponent**
+ Rename **backToScreen** to **backToComponent**
+ Rename **replaceScreen** to **replaceComponent**
+ Rename **addScreen** to **addComponent**
+ Rename **newRootScreen** to **newRootComponent**
+ To fix other package changes just reimport