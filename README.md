# MAPP-Warehouse-Butlers
To Run the code simply import the project into eclipse and run the java applet in com.ai.major.wrapper

## Creating Custom Maps
One can create custom maps by either editing the current warehouses or adding new ones. A custom warehouse is created by extending the Warehouse class and defining the Warehouse in the String Array MazeDefine:
- 'X' represents walls
- 'O' represents items
- 'W' represents workstations
- 'G' represents conveyor belts //Not used right now.

Example:
* String MazeDefine=
 * {
  * "XXXXXXXXXXXXXXXXXXXXX",	// 1
  * "XO                OOX",	// 2
  * "X  X  X  X  X  X  X X",	// 3
  * "X  X  X  XOOX  X  X X",	// 4
  * "X  X  X  XOOX  X  X X",	// 5
  * "X  X  X  X  X  X  X X",	// 6
  * "X  X  X  X  X  X  X X",	// 7
  * "X  X  X  X  X  X  X X",	// 8
  * "XO X  X  X  X  X  X X",	// 9
  * "X  X  X  X  X  X  X X",	// 10
  * "X                   X",	// 11
  * "X                   X",	// 12
  * "X                   X",	// 13
  * "X                   X",	// 14
  * "XWWWWWWW    GGGGGGGGX",	// 15
  * "XXXXXXXXXXXXXXXXXXXXX",	// 16
 * };