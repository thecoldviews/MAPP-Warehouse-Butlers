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
  * "XO&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;OOX",	// 2
  * "X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;X",	// 3
  * "X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;&nbsp;XOOX&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;X",	// 4
  * "X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;&nbsp;XOOX&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;X",	// 5
  * "X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;X",	// 6
  * "X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;X",	// 7
  * "X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;X",	// 8
  * "XO&nbsp;X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;X",	// 9
  * "X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;&nbsp;X&nbsp;X",	// 10
  * "X&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;X",	// 11
  * "X&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;                   X",	// 12
  * "X&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;                   X",	// 13
  * "X&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;                   X",	// 14
  * "XWWWWWWW&nbsp;&nbsp;&nbsp;&nbsp;GGGGGGGGX",	// 15
  * "XXXXXXXXXXXXXXXXXXXXX",	// 16
 * };