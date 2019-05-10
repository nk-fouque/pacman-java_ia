# IA-Pacman

## Lancement du programme


## Code
Le code provient d'un programme opensource d'un certain Leonardo Ono qui a fait une version simplifiée 
en java du célèbre jeu Pac-Man.

Le programme s'organise en deux branches importantes : les éléments acteurs du jeu et son infrastructure. La 
plupart des classes héritent donc de PacmanActor . Dans les acteurs on retrouve les
fantômes, les boules de nourritures, les powerballs (qui permettent à Pacman de manger des fantômes) 
et tous les éléments s'affichant à l'écran (titre, game over,...).

## Algorithmes et difficultés
Le premier algorithme que nous avons cherché à mettre en place est un algorithme MinMax simplifié. 

Avant de mettre en place cet algorithme, nous devions nous assurer de comprendre le code, qu'il 
correspondait à ce dont on avait besoin. Nous avons perdu du temps à commenter ce qui ne l'était 
pas et à recoder certaines classes (notamment les fantômes) qui étaient trop éloignées du mode de 
fonctionnement classique du jeu.


Une fois le code modifié, nous avons commencé à faire l'algorithme MinMax. Il fait appel à un arbre de recherche
 qui explore les possibilités d'évolution de Pacman jusqu'à 8 cases devant lui avec comme objectif de maximiser
 son score. Il choisit donc la direction la plus favorable, imaginant que se faire manger par les fantômes ferait
 chûter son score à 0.
Cet arbre de recherche est programmé dans la classe PathfindTree.java. 

## Arbitrage vidéo
bravo pacmouille