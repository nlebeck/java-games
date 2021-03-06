# Games

This repository holds my game programming code.
The first project I'm working on is an action-adventure game (like Zelda) in Java.
Although I'm sure there are plenty of tools out there that I could be taking
advantage of, I plan to program most things from scratch, just to learn the
process. I hope to use open-source sprite collections like spritelib for art
or create my own sprites.

I based the main game loop in my Java game on the game loop from the online
draft of "Killer Game Programming in Java" by Andrew Davison, but aside from
that, I'm mostly just figuring things out on my own.

## Compiling and running the action-adventure game

The game engine and action-adventure game are in separate Maven repositories.
To compile the action-adventure game on the command line, run these commands
(they work on Windows and should work on Unix too):

    cd game-engine
    mvn install
    cd ..
    cd action-adventure
    mvn package

After that, you can run the game using the JAR
`action-adventure-1.0-SNAPSHOT-jar-with-dependencies.jar` in the `target`
directory.

You can also compile and run the game in Eclipse, which is the IDE that I use
to work on it. After importing the two Maven projects, run the commands above
once, and after that, you should be able to just click the "run" button in
Eclipse to compile and run the game.

I found that after changing code in the game engine project, I can just run
the game in Eclipse, and it will automatically reflect the game engine
changes without updating the version installed in the local Maven repository.
That behavior is perfect for development, but I don't fully understand it.

## Roadmap

Although my rate of progress on this game isn't very fast, I'm hoping to
continue poking away at it over time. These are the high-level things I plan
to work on:

1. *Building out a "vertical slice" of the game.* My first priority is to add
some more locations, objects, and behaviors to the game.

2. *Thinking about what belongs in the game engine vs. the game.* I've split
the code into a generic game engine and the specific code implementing my game.
It's hard to tell exactly what functionality belongs in the game engine, and
how high- or low-level that functionality should be, so I'm hoping that as I
add more functionality to the game, I'll get a better sense for what should go
where.

3. *Thinking about `GameLogic` and `GameScene` organization.* Right now, if
you have logic that accesses the current `GameScene` inside of a component that
is reusable across multiple scenes (e.g., a collision handler or a sprite's
`update()` method), you need to get the `GameScene` and then cast it to the
right subclass. It's definitely not ideal to read off the type of the current
`GameScene` and handle different subclasses on a case-by-case basis. Is it okay
to define a common interface that must be implemented by each possible
`GameScene` that could contain the component, and then cast to that common
type? Or is there a better design that would avoid the need to perform casts
entirely?

## Sprite class organization

These are the current principles for how a game should define types
representing its different "game objects:"

* All game objects ultimately inherit from the Sprite class.

* The Sprite class hierarchy is abstract except for the bottom level.

* Collision handlers are defined for pairs of concrete Sprite subclasses, i.e.,
pairs of bottom-level classes.

This way, there's no ambiguity about which collision handler applies to which
class. The downside is that there might be duplicated code if multiple classes
behave the same way when colliding with a given class, but my vision for the
collision handlers is that they will mostly just invoke methods on the
colliding objects, so in this case, if those multiple classes have a common
abstract superclass, it could just define one method that all of the collision
handlers invoke.

For now, I'm going to try to stick to only implementing the `update()`
method in concrete bottom-level classes and including any common update
functionality in separate methods, in order to make it easier to reason about
what's happening in a given bottom-level class's update method. Also, now that
`Behaviors` (described below) exist, I'm hoping to keep the `Sprite` class
hierarchy pretty shallow and use `Sprite` methods mainly to manage the
interaction between different `Behaviors`. Once I have a better sense for how
well `Behaviors` work, I might revise the principles above or add bullets.

## Behaviors and MoveBehaviors

As I implemented more game logic, I started to realize that without some
mechanism for re-using logic across different `Sprites`, there would either be
an unwieldy `Sprite` class hierarchy or a lot of duplicated code. As a result,
`Sprites` now contain `Behavior` and `MoveBehavior` objects. A `Behavior` is a
little package of private state and update logic. The game engine defines an
`update()` method for each `Behavior` and takes care of calling that method
every frame. If multiple `Behaviors` need to interact, code for that
interaction can go in the `Sprite`'s own `update()` method or other methods.
The idea is that `Sprite` code will now be used mainly to manage the
interaction between `Behaviors`.

`MoveBehaviors` are a special kind of `Behavior` that determine a `Sprite`'s
movement distance and direction each frame. Both `Behaviors` and
`MoveBehaviors` have timed variants that the game engine will stop updating
once they are done.

This design is definitely experimental, and as I implement more different
kinds of Sprites and game functionality, I'll see how well it works.

## GameLogic and GameScenes

When using this game engine, the game's state at any point in time is
partitioned between a `GameLogic` object, a `GameScene` object, and one or more
`Sprite` objects. Each object has an `update()` method that fires every frame.

The `GameLogic` object persists for the entirety of the game and holds global
game state.

Each `GameScene` object corresponds to a specific scene, or a contained area of
the game world populated with sprites. The current `GameScene` instance is
destroyed when the game engine transitions to the next scene, so a `GameScene`
object should hold only state relevant to the current scene.

The `GameLogic` instance and current `GameScene` instance can be accessed
inside any game code using game engine methods. My current development
philosophy is to have each object's update method touch only objects "more
global" than itself. In other words, a `GameLogic` object's update method only
accesses its own state, a `GameScene`'s update method calls methods of the
`GameLogic` if necessary, and a `Sprite`'s update method is free to call
methods of either object.

## Collisions, proximity events, and interactions

Currently, this game engine supports three different kinds of events.

* *Collisions* occur when one Sprite attempts to move into the space occupied
by another Sprite. The game engine stops the Sprite from moving and calls the
CollisionHandler registered for the pair of Sprite subclasses.

* *Proximity events* occur when one Sprite is within a certain distance from
another Sprite (as measured between their center points). Currently, proximity
events are defined between pairs of Sprite subclasses alongside of collisions
in the corresponding CollisionHandler class.

* *Interactions* occur when the player character is next to a Sprite and
presses the "interact button" (currently the Enter key). Currently,
interactions are defined by a per-Sprite InteractionHandler.

The current setup, with collisions and proximity events being defined per-class
and interactions being defined per-object, is not ideal. My current plan is to
add per-class interactions and per-object pair collisions and proximity events,
so that you can write either per-class or per-object logic for all three event
types. I might also add support for collisions and proximity events defined
between an object and a class, if it ends up being useful.

Niel Lebeck
