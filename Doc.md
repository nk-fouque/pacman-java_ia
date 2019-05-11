# IA-Pacman

## Lancement du programme


## Code
Le code provient d'un programme opensource d'un certain Leonardo Ono qui a fait une version simplifiée en java du célèbre jeu Pac-Man.

Le programme s'organise en deux branches importantes : les éléments acteurs du jeu et son infrastructure. La plupart des classes héritent donc de PacmanActor . Dans les acteurs on retrouve les fantômes, les boules de nourritures, les powerballs (qui permettent à Pacman de manger des fantômes) et tous les éléments s'affichant à l'écran (titre, game over,...).

Une des premières difficultés que nous avons rencontré est l’implémentation de PacMan par Leonardo Ono. Son implémentation ne correspondait pas exactement au fonctionnement original du jeu et nous avons passé un certain temps à commenter et recoder certaines classes, notamment les fantômes. Les fantômes de PacMan ont un comportement très spécifique en fonction de leur couleur. Dans le jeu original :
* le rouge, Blinky, cherchait à rattraper Pacman
* le rose, Pinky, anticipait ses mouvements en essayant d’atteindre un emplacement 4 cases devant Pacman
* le bleu, Inky, tentait de parvenir à une case qui correspondait au double du vecteur reliant le fantôme rouge à la 2e case devant Pacman. Ca donne l’impression qu’il agit aléatoirement mais ce n’est pas totalement vrai
* le orange, Clyde, cherchait à atteindre Pacman tant que celui-ci était à plus de 8 cases de lui. Sinon, il entrait en “Scatter Mode”. 

Le Scatter Mode est un mode de déplacement des fantômes où ils ne cherchent plus à attraper Pacman mais où ils tentent d’atteindre un des quatre coins de la zone  de jeu. C’est le mode dans lequel ils sont après être sortis de leur cage centrale, pendant 7 secondes. D’ailleurs, les fantômes ne sortent pas tous en même temps de la cage : le rouge est directement dehors, le rose sort immédiatement mais le bleu attend que Pacman ait mangé 30 éléments de nourriture et le orange un tier de la nourriture totale. Parmi toutes ces spécificités des fantômes, **pas une seule** n’avait été implémentée par le développeur du jeu en java. Nous n’avons pas pu toutes les implémenter non plus, parce que certaines fonctionnalités n’étaient pas compatibles avec l’état actuel du code, mais nous n’avions pas non plus le temps de recoder tout le jeu nous même. Nos fantômes agissent comme dans le jeu original, à quelques exceptions près : 
* le fantôme rose anticipe à 2 cases et non à 4
* un bug graphique arrive parfois lorsque Pacman mange une Powerball alors que le fantôme orange est dans la cage : il reste affiché comme vulnérable (bleu et effrayé) quand il en sort alors que son état est en Scattermode ou Normalmode
* le fantôme bleu a un comportement totalement aléatoire, l’implémentation de son véritable comportement n’est pas compatible avec le code qui l’entoure
* les fantômes sont censés aller plus lentement quand ils sont en mode “vulnérable”, mais cette implémentation n’est pas compatible avec le code qui l’entoure


Mais une fois le code modifié, nous pouvions faire quelques algorithmes.

## Algorithmes et IA

### EatMax

Le premier algorithme que nous avons cherché à mettre en place est un algorithme MinMax simplifié (EatMax). Il fait appel à un arbre de recherche qui explore les possibilités d'évolution de Pacman jusqu'à 8 cases devant lui avec comme objectif de maximiser son score. Il choisit donc la direction la plus favorable, là où il y a de la nourriture..
Cet arbre de recherche est programmé dans la classe PathfindTree.java et tient surtout compte des murs et des possibilités de mouvements de Pacman

La première version de l’algorithme ne cherchait qu’à maximiser le score, sans essayer de prévoir le mouvement des fantômes. C’est la partie “Max” d’un “MinMax”, il s’agit de l’algorithme “EatMax”, qui utilise la classe GameState. Cependant, Pacman prenait souvent les fantômes en compte trop tard. C’est un algorithme très naïf qui nous a permis de savoir ce qui manquait à notre Pacman pour devenir meilleur. De plus, nous avons pendant longtemps eu un problème au niveau du tunnel reliant les côtés gauche et droit de la carte : Pacman ne comprenait pas qu’il pourrait être intéressant pour lui de traverser, et ne les utilisait donc quasiment jamais, quitte à être attrapé par un fantôme. C’était d’autant plus embêtant que des boules de nourriture se trouvent à l’entrée des tunnels, et il n’allait quasiment jamais les chercher.

Pour améliorer notre code nous avons implémenté une méthode plus efficace pour parcourir les possibilités : la classe FloydWarshall.java. Cette classe permet de calculer plus facilement la distance entre tous les points de la zone de jeu. Elle nous permet d’améliorer l’algorithme et d’implémenter ensuite le “Min” de “MinMax”.


### MinMax

Nous avons donc mis en place un algorithme MinMax, où Pacman pense que tous les fantômes vont essayer de l’attraper, et va donc prendre cela en compte dans ses décisions. 
Il va ainsi continuer à faire un arbre de recherche, mais il fera jouer l’un après l’autre Pacman et les fantômes, en considérant que les fantômes vont toujours aller dans la direction qui les rapproche le plus de lui s’ils sont en mode normal, et qui les éloigne le plus s’ils sont en mode vulnérable.
Nous avons gardé la même base que EatMax, mais notre Pacman utilise la classe GameStatePlus, et en particulier les fonctions moveGhosts et shortestPathToPacman. 
Désormais, au début de chaque partie, notre IA utilise l’algorithme de Floyd-Warshall pour remplir une matrice contenant les distances entre tous les points de la carte, et une matrice contenant une liste des différentes directions possibles en partant de chaque case (possibleMoves). Les fonctions moveGhosts et shortestPathToPacman vont utiliser ces matrices pour calculer les meilleur mouvement des quatre fantômes dans la partie “min” de l’algorithme.

Il y a également quelques autres différences entre l’implémentation d’EatMax et de MinMax, qui ne sont pas liés au changement d’algorithme mais plutôt à une amélioration globale de notre code. Ainsi, la matrice possibleMoves est réutilisée pour la fonction donnant les états suivants possibles de Pacman. L’utilisation de la matrice de distances a également permis de régler le bug du tunnel.

Cet algorithme parvient à de meilleurs résultats que le précédent.
Le problème avec cet algorithme est qu’il ne prend pas en compte le comportement réel des fantômes, qui bougent parfois de façon aléatoire (ScatterMode), et qui pour certains n’essayent pas d’attraper Pacman quel que soit leur mode.

### ExpectiMax (work in progress)
Nous voulions aussi programmer un algorithme ExpectiMax pour que notre Pacman puisse anticiper les mouvements des fantômes (plutôt que de croire qu’ils étaient juste agressifs). Nous avions peu de temps pour développer cet algorithme, et la programmation originelle du jeu ne permettaient pas un implémentation facile. Le travail est donc inachevé, dans la classe Expectimax.java. Mais en terme d’algorithme, Expectimax est comme le MinMax, sauf que le “min” est calculé à partir du comportement théorique de chaque fantôme. Le fantôme rouge est agressif, le fantôme rose anticipe la trajectoire, et les deux autres ont un comportement semi-aléatoire. Le choix de PacMan est alors calculé en fonction de l’état du jeu après le mouvement théorique des fantômes. 

## Arbitrage vidéo
bravo pacmouille


