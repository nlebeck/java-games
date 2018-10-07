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

## Sprite class organization

In a real game engine, there are lots of interesting questions about how to
design a system for defining different types of "game objects" in a way that
is flexible and can be easily extended. These questions include whether to use
composition or inheritance for defining new game objects, and things like that.
Because my ambitions are modest for now, I'm hoping to sidestep those questions
with the following simple system:

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

Niel Lebeck
