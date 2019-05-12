# IA-PacMan

## Launch of the program


## Source code
The opensource code was provided by Leonardo Ono. It is a simplified version of the famous video game: PacMan

The program is divided in two main branches : the actor elements and its infrastructure. Most of the classes inherite from PacmanActor. The different actors are for example ghosts, food balls, powerballs (which make PacMan able to eat the ghosts) and all the elements displayed on the screen (title, game over,...)

One of the first difficulty we met was the implementation of the game by Leonardo Ono. His programm wasn’t faithful to the original game and we spent some time commenting and rewriting some classes, especially the ghosts. PacMan’s ghosts have a very specific behaviour depending on their color.
In the original game, the ghosts’ NormalMode is :
* the red one, Blinky, tries to catch PacMan
* the pink one, Pinky, anticipates its movements, it tries to reach 4 tiles ahead of PacMan
* the blue one, Inky, tries to reach a specific tile : the case is determined by tracing twice the vector between the position of Blinky and two tiles ahead of PacMan. From an external point of view, it looks like Inky’s movements are random
* the orange one, Clyde, tries to reach PacMan while they are at least 8 tiles apart. If it gets closer, Clyde enters its ‘ScatterMode’

ScatterMode is a mode in which the ghosts don’t try to catch PacMan but instead try to reach one of the 4 corners of the map. When they come out of their cage (at the beginning or after being eaten), they stay in ScatterMode for 7s. At the beginning, the ghosts don’t get out of their cage at the same time: the red one starts outside the cage, the pink one gets out immediately, the blue one waits until PacMan ate at least 30 food balls and the orange one waits until it ate a third of the total food.

Among those features, not **one of them** were implemented by the developer of this game in Java. We couldn’t implement them all either, sometimes because they weren’t compatible with the actual code. And we did not have the time to recode everything ourselves.

Our ghosts act like in the original game, with some exceptions :
* the pink ghost anticipates 2 tiles ahead instead of 4
* a graphic bug happens sometimes : when PacMan eats a Powerball while the orange ghost is still in its cage, it stays displayed as ‘vulnerable’ (its frame is blue and scared) but its actual mode is not affected
* the blue ghost only has a random behaviour, its true behaviour wasn’t compatible with the actual code
* ghosts are supposed to be slower when chased by PacMan after eating a Powerball, but it wasn’t compatible with the actual code
* ghosts are supposed to visually return to their cage after being eaten, but they only teleport themselves in the cage : it wasn’t compatible with the actual code

Once the code was modified, we could start to code some AI algorithms.

## Algorithms and AI

### EatMax

The first algorithm we tried to implement was a simplified version of the MinMax algorithm (EatMax). It uses a search tree to explore the different possibilities of progression of PacMan 8 squares ahead, with the objective to maximize its score. It consequently choses the most favorable direction, where there is food.
The search tree is implemented in the PathFindTree.java class, and mostly takes into account the walls and PacMan’s possibility of movement.


Therefore, the first version of the algorithm only seeked to maximize its score, without trying to predict the moves of the ghosts. It is the ‘max’ part of a ‘MinMax’, in this case the ‘EatMax’ algorithm, which uses the GameState class. However, with this implementation, PacMan often took the ghosts into account only when it was to late. It is a very naive algorithm that allowed us to see what our PacMan lacked in order to become better. Moreover, we had for a long time a problem concerning the tunnel connecting the left and right sides of the map: PacMan didn’t understand that it could be interesting for him to cross, and so he hardly ever used it, even if it meant being caught by a ghost. It was all the more annoying that it meant that PacMan almost never got the food that was in the entrance of the tunnel.

To improve our code we implemented a more efficient method to go over all the possibilities: the FloydWarshall.java class. This class allows us to compute more easily the distances between all the points of the map, thus it allows us to improve our algorithm and to then implement the ‘min’ part of ‘MinMax’.

### MinMax

We then implemented the MinMax algorithm, where PacMan thinks all ghosts are out to get him and takes his decisions accordingly.
The AI still uses the search tree, but instead of only considering its own moves, it has both itself and the ghosts play one after the other, thinking that the ghosts will always move in the direction that will bring them closer to PacMan in normal mode, and in the direction that will take them away from him when they are in vulnerable mode.
We kept the same base as in the EatMax algorithm, but our PacMan uses the GameStatePlus class instead of the GameState class, and in particular the functions moveGhosts and shortestPathToPacman.
From now on, at the beginning of each game the AI uses the Floyd-Warshall algorithm in order to fill a matrix containing the distances between all the squares of the map, and a matrix containing lists of the possible directions from each square (possibleMoves). The functions moveGhosts and shortestPathToPacman use those matrixes to compute the best moves for the four ghosts in the ‘min’ part of the algorithm.

There are also some other differences between the implementation of EatMax and MinMax, but they are not actually linked to the change in algorithm, just to a global improvement in our code. Thus the possibleMoves matrix is reused in order to give the possible following states of PacMan. The distance matrix also allowed us to fix the tunnel bug.

This algorithm achieves better results than the  previous one.
The main issue with it is that it doesn’t take into account the real behavior of the ghosts, which sometimes move in a random way (ScatterMode), or don’t always try to catch PacMan regardless of their mode (blue ghost).


### ExpectiMax (work in progress)
We also wanted to code an Expectimax algorithm, for PacMan to anticipate ghosts movements (instead of thinking they were just aggressive like the red one). We had little time for coding this algorithm and the way the game was implemented in Java made the thing harder. This algorithm is not finished yet, in Expectimax.java (the code is commented to avoid error messages). Expectimax is similar to MinMan algorithm, but the ‘min’ is calculated based on the theoric behaviour of the ghosts. The red ghost is aggressive (aggressiveGhostChoice), the pink one anticipates the trajectory (trickyGhostChoice) and blue and orange ghosts act randomly (randomGhostChoice). If they are in Vulnerable mode, they run away from Pacman (scaredGhostChoice). PacMan’s choice of direction is then calculated depending on the game state after the theoric ghosts’ movements.

## Arbitrage vidéo
In this video we will show the strength and weakness of PacMan with the MinMax algorithm. It doesn't win every game it plays but with few tries it can finish the first level.

# Launching the Game
## Execution
Either :
	- Go in your IDE and launch the main() from the Main class
	- Go to Executable/ and execute Piacman.jar
		- On Windows, a simple double-click is enough
		- On Linux, sometimes due to problems with intelliJ .jar building, a double-click doesn't work, in this case, you may use ```java -jar Piacman.jar```
The folder still contains the original JavaPacman.jar, in any need of comparison

## Switching Modes
On the Title Screen, you can see on top-right that by default the game is played by MinMax.
On the Title Screen (or at any time during play) you can press M to go to Manual mode or K to go to EatMax, you can go back to MinMax by pressing L
