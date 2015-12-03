# MAPP-Warehouse-Butlers
To Run the code simply import the project into eclipse and run the java applet in com.ai.major.wrapper

## Creating Custom Maps
One can create custom maps by either editing the current warehouses or adding new ones. A custom warehouse is created by extending the Warehouse class and defining the Warehouse in the String Array MazeDefine:
- 'X' represents walls
- 'O' represents items
- 'W' represents workstations
- 'G' represents conveyor belts //Not used right now.
- '_' or Blank Space for Blank.

Example:
* String MazeDefine=
 * {
  * "XXXXXXXXXXXXXXXXXXXXX",	// 1
  * "XO________________OOX",	// 2
  * "X__X__X__X__X__X__X_X",	// 3
  * "X__X__X__XOOX__X__X_X",	// 4
  * "X__X__X__XOOX__X__X_X",	// 5
  * "X__X__X__X__X__X__X_X",	// 6
  * "X__X__X__X__X__X__X_X",	// 7
  * "X__X__X__X__X__X__X_X",	// 8
  * "XO_X__X__X__X__X__X_X",	// 9
  * "X__X__X__X__X__X__X_X",	// 10
  * "X___________________X",	// 11
  * "X___________________X",	// 12
  * "X___________________X",	// 13
  * "X___________________X",	// 14
  * "XWWWWWWW____GGGGGGGGX",	// 15
  * "XXXXXXXXXXXXXXXXXXXXX",	// 16
 * };