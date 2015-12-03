# MAPP-Warehouse-Butlers
To run the code simply import the project into eclipse and run the java applet in com.ai.major.wrapper

## Citations
 - This is based on the algorithm proposed by Ko-Hsin Cindy Wang and Adi Botea in their paper "MAPP: a Scalable Multi-Agent Path Planning Algorithm with Tractability and Completeness Guarantees" in "Journal of Artificial Intelligence Research" in 2011.
 - The UI has been constructed by taking apart the "Capstone Pacman" code from "https://github.com/rdkral/Capstone-Pacman"

## Creating Custom Maps
One can create custom maps by either editing the current warehouses or adding new ones. A custom warehouse is created by extending the Warehouse class and defining the Warehouse in the String Array MazeDefine:
- 'X' represents walls
- 'O' represents items
- 'W' represents workstations
- 'G' represents conveyor belts //Not used right now.
- '_' or Blank Space for Blank.

One can then add this warehouse to the java applet easily by following the example of other added warehouses in the applet file.

Example:
![Code](https://github.com/altercation/solarized/raw/master/code.png)
![Snap](https://github.com/altercation/solarized/raw/master/snap.png)


