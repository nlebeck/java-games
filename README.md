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

3. *Rethinking the Sprite class organization.* When I initially came up with
the Sprite class organization described below, I was inspired by the
object-oriented programming I had learned growing up, when Java was the hot new
programming language and inheritance was the law of the land. Now, it seems
like the consensus is that composition is superior to inheritance in most
circumstances. Once my game has more Sprites, I'd like to take a step back and
think about whether and how to change the Sprite class organization. Is it
better to switch from an inheritance-based system to a composition-based
system? Is it fine to continue using the concrete Sprite subclasses as
essentially tags for collision-handling? Can I just push enough functionality
into the game engine and have the actual Sprite subclasses be so small that it
doesn't really matter how they're organized?

4. *Thinking about `GameLogic` and `GameScene` organization.* Right now, if
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
what's happening in a given bottom-level class's update method. If this
principle seems to work well, maybe I'll promote it to being a bullet in the
list above.

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
