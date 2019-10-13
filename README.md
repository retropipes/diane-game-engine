# Diane Java Game Engine / Framework

Diane is a 2D, top-down, tiled GUI framework for Java games. It was originally written in Java SE 6, but has been updated numerous times. Currently, it uses Java SE 12.

Diane gets its name from the initial letters of "*Drunken Input Atomic Nucleus Engine*", so derived from its most unique feature, drunk simulation, and its deep integration with games that use it.

Diane supports *editing* as well as *playing* game levels. Customization takes the form of hooks you can put your own logic in. You can also disable modules your game does not need or use.

Diane integrates natively with **macOS, Windows, and Linux** via the [Native Integration library](https://github.com/PuttySoftware/lib-java-native-integration) (included in static form), so it looks like other applications on the OS it is running on, and uses features like the Windows taskbar, the macOS Dock, and the native notification systems, if desired.

Ideas for expanding what Diane can do are welcomed!

Diane is quite incomplete right now... at the moment (v0.1.0-alpha), the loader API, direction utilities, and an object model all exist, which should now be enough to build games on top of. More additions are planned as Diane matures.
