package io.github.skepter.sudokusolver;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Main {

	public static void main(String[] args) {
		new Main();
	}
	
	public Visualiser v;
	

	// Starting off with a general 9x9 grid. Will be customisable later
	public int[][] grid;
	public int iteration;
	public int changes;
	
	public static final long DELAY = 10;
	public static final int miniGridSize = 3;
	public static final int gridSize = miniGridSize * miniGridSize;
	
	public Main() {
				
		
		grid = generateSpecificSudokuGrid(1, new int[gridSize][gridSize]);
		iteration = 0;
		changes = 0;

//		grid[0][7] = 5;
//		grid[0][8] = 8;
//		grid[1][7] = 1;
//		grid[2][5] = 5;
		
//		grid[3][0] = 4;
//		grid[5][4] = 9;
//		grid[2][0] = 9;
//		grid[8][0] = 7;
		
		grid[2][7] = 6;
		
		v = new Visualiser(grid, gridSize, "Sudoku");
		
		
		new Visualiser(generateRelationshipTable(), gridSize, "Relationship table");

		
		solveByCalculation();
		//solveAdvancedByCalculation(false, -1);
		

//		System.out.println("Finding values for first 3x3 grid for index (0, 1):");
//		findBoxValues(0, 1).forEach(o -> System.out.print(o + ", "));
//		System.out.println("\nPrinting inverted values:");
//		invertValues(findBoxValues(0, 1)).forEach(o -> System.out.print(o + ", "));

//		System.out.println("Performing solve by calculation:");
//		
//		long before = System.currentTimeMillis();
//		solveByCalculation();
//		System.out.println("Program took: " + (System.currentTimeMillis() - before) + " milliseconds");
//		
//		solveByBacktracking();
	}
	
	/**
	 * Creates some kinda table thing which shows how many boxes
	 * the value of another box will affect
	 */
	public int[][] generateRelationshipTable() {
		int[][] relationships = new int[gridSize][gridSize];
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				if (grid[i][j] != 0) {
					//Represents an existing number
					relationships[i][j] = 0;
				} else {
					/*
					 * For all (i) current box possibilities:
					 * find all row/col/minigrid with the same number (i), count it up.
					 * that total FOR ALL (i) in current box possibilities = relationshipNumber.
					 * 
					 *  boxes with the greatest relationshipNumber are prioritised first.
					 */
					Set<Integer> possibleNumbers = getPossibleNumbers(i, j);
					for(int possibility : possibleNumbers) {
						
						//counter which counts relationshipNumber
						int count = 0;
						
						// Check current row (checking vertically)
						for (int row = 0; row < gridSize; row++) {
							if (row != i) {
								if (getPossibleNumbers(row, j).contains(possibility)) {
									count++;
								}
							}
						}
						
						// Check current column (checking horizontally)
						for (int col = 0; col < gridSize; col++) {
							if (col != j) {
								if (getPossibleNumbers(i, col).contains(possibility)) {
									count++;
								}
							}
						}
						
						// Check minigrid
						int row = (i / miniGridSize) * miniGridSize;
						int col = (j / miniGridSize) * miniGridSize;

						for (int r = 0; r < miniGridSize; r++) {
							for (int c = 0; c < miniGridSize; c++) {
								if (row != i && col != j) {
									if (getPossibleNumbers(row + r, col + c).contains(possibility)) {
										count++;
									}
								}
							}
						}
						
						relationships[i][j] = count;
					}
				}
			}
		}
		return relationships;
	}
	
	

	public int[][] generateSpecificSudokuGrid(int difficulty, int[][] grid) {
		switch(difficulty) {
		case 0:
			// Easy
			grid = new int[][] { 
				new int[] { 0, 0, 0, 2, 6, 0, 7, 0, 1 }, 
				new int[] { 6, 8, 0, 0, 7, 0, 0, 9, 0 }, 
				new int[] { 1, 9, 0, 0, 0, 4, 5, 0, 0 }, 
				new int[] { 8, 2, 0, 1, 0, 0, 0, 4, 0 }, 
				new int[] { 0, 0, 4, 6, 0, 2, 9, 0, 0 }, 
				new int[] { 0, 5, 0, 0, 0, 3, 0, 2, 8 }, 
				new int[] { 0, 0, 9, 3, 0, 0, 0, 7, 4 }, 
				new int[] { 0, 4, 0, 0, 5, 0, 0, 3, 6 }, 
				new int[] { 7, 0, 0, 0, 1, 8, 0, 0, 0 }, 
			};
			break;
		case 1:
			// Advanced
			grid = new int[][] { 
				new int[] { 2, 0, 0, 3, 0, 0, 0, 0, 0 }, 
				new int[] { 8, 0, 4, 0, 6, 2, 0, 0, 3 }, 
				new int[] { 0, 1, 3, 8, 0, 0, 2, 0, 0 }, 
				new int[] { 0, 0, 0, 0, 2, 0, 3, 9, 0 }, 
				new int[] { 5, 0, 7, 0, 0, 0, 6, 2, 1 }, 
				new int[] { 0, 3, 2, 0, 0, 6, 0, 0, 0 }, 
				new int[] { 0, 2, 0, 0, 0, 9, 1, 4, 0 }, 
				new int[] { 6, 0, 1, 2, 5, 0, 8, 0, 9 }, 
				new int[] { 0, 0, 0, 0, 0, 1, 0, 0, 2 }, 
			};
			break;
		case 2:
			// 1 Star difficulty
			grid = new int[][] {
				new int[] { 0, 0, 0, 0, 3, 1, 4, 0, 0 },
				new int[] { 0, 0, 0, 0, 8, 0, 0, 0, 7 },
				new int[] { 3, 0, 0, 7, 0, 0, 9, 0, 5 },
				new int[] { 1, 0, 4, 6, 0, 0, 0, 5, 0 },
				new int[] { 5, 0, 0, 0, 0, 7, 3, 2, 0 },
				new int[] { 0, 0, 0, 5, 0, 0, 7, 6, 4 },
				new int[] { 0, 2, 1, 0, 9, 8, 0, 0, 0 },
				new int[] { 8, 0, 0, 0, 0, 2, 0, 0, 0 },
				new int[] { 0, 4, 0, 0, 0, 0, 0, 7, 0 },
			};
			break;
		case 3:
			// 3 Star difficulty
			grid = new int[][] {
				new int[] { 2, 0, 3, 7, 1, 6, 0, 0, 0 },
				new int[] { 0, 0, 9, 0, 4, 3, 0, 1, 0 },
				new int[] { 0, 0, 6, 0, 0, 0, 8, 0, 0 },
				new int[] { 0, 0, 0, 0, 0, 0, 2, 0, 8 },
				new int[] { 0, 0, 0, 0, 3, 0, 0, 0, 0 },
				new int[] { 0, 1, 0, 0, 6, 0, 0, 5, 0 },
				new int[] { 0, 6, 0, 4, 0, 0, 7, 0, 0 },
				new int[] { 0, 5, 0, 3, 7, 0, 0, 0, 0 },
				new int[] { 8, 0, 0, 0, 0, 0, 9, 0, 5 },
			};
			break;
		case 4:
			// 5 Star difficulty
			grid = new int[][] {
				new int[] { 0, 0, 0, 0, 0, 0, 0, 1, 2 },
				new int[] { 0, 9, 0, 0, 0, 7, 0, 0, 3 },
				new int[] { 0, 0, 0, 6, 0, 0, 4, 7, 0 },
				new int[] { 0, 0, 0, 0, 4, 0, 6, 0, 8 },
				new int[] { 0, 6, 2, 0, 0, 0, 9, 0, 0 },
				new int[] { 0, 3, 0, 0, 5, 0, 0, 0, 0 },
				new int[] { 0, 1, 0, 7, 3, 5, 0, 8, 0 },
				new int[] { 4, 0, 0, 0, 8, 0, 0, 0, 0 },
				new int[] { 2, 0, 0, 0, 0, 0, 1, 0, 0 },
			};
			break;	
		}
		return grid;
	}

	/**
	 * Solves the sudoku grid using the backtracking algorithm. See the <a href=
	 * "https://en.wikipedia.org/wiki/Sudoku_solving_algorithms#Backtracking">Wikipedia</a>
	 * page for more info:
	 */
	public void solveByBacktracking() {
//		int[][] copy = grid;
//		for(int i = 0; i < 9; i++) {
//			for(int j = 0; j < 9; j++) {
//				if(copy[i][j] == 0) {
//					for(int k = 1; k <= 9; k++) {
//						boolean fillIn = true;
//						//check row
//						for(int row = 0; row < 9; row++) {
//							if(copy[row][j] == k) {
//								fillIn = false;
//							} 
//						}
//						for(int col = 0; col < 9; col++) {
//							if(copy[i][col] == k) {
//								fillIn = false;
//							} 
//						}
//						for(int val : findBoxValues(i, j)) {
//							if(val == k) {
//								fillIn = false;
//							}
//						}
//						if(fillIn) {
//							copy[i][j] = k;
//							break;
//						}
//					}
//				} else {
//					while(i < 9) {
//						i++;
//						if(i == 9) {
//							i = 1;
//							j++;
//							if(j == 9) {
//								//displayGrid(grid);
//							}
//						}
//					}
//				}
//			}
//		}
	}

	/**
	 * Inverts the current values. For example, a set of integers 1, 2, 3, 5
	 * will return 4, 6, 7, 8, 9
	 * 
	 * @param values The set of values to invert
	 * @return A set of inverted values
	 */
	private Set<Integer> invertValues(Set<Integer> values) {
		Set<Integer> set = new HashSet<Integer>();
		for (int i = 1; i <= gridSize; i++) {
			if (!values.contains(i))
				set.add(i);
		}
		return set;
	}
	
	/**
	 * Returns a 3x3 array of the values of the minigrid grid which contains the row and column
	 * @param row
	 * @param col
	 * @return
	 */
	protected int[][] getBox(int row, int col) {
		int[][] arr = new int[miniGridSize][miniGridSize];
		row = (row / miniGridSize) * miniGridSize;
		col = (col / miniGridSize) * miniGridSize;

		for (int r = 0; r < miniGridSize; r++) {
			for (int c = 0; c < miniGridSize; c++) {
				arr[r][c] = grid[row + r][col + c];
			}
		}
		return arr;
	}

	/**
	 * New method of getting values in the a 3x3 grid
	 * @param row
	 * @param col
	 * @return
	 */
	private Set<Integer> findMiniGridValues(int row, int col) {
		
		Set<Integer> values = new HashSet<Integer>();
		int newRow = (row / miniGridSize) * miniGridSize;
		int newCol = (col / miniGridSize) * miniGridSize;

		for (int r = 0; r < miniGridSize; r++) {
			for (int c = 0; c < miniGridSize; c++) {
				if(grid[newRow + r][newCol + c] != 0)
					values.add(grid[newRow + r][newCol + c]);
			}
		}
		return values;
	}


	private boolean analyseBox(int row1, int col1) {
		
		int row = (row1 / miniGridSize) * miniGridSize;
		int col = (col1 / miniGridSize) * miniGridSize;

		Set<Integer> possibleValuesForBox = getPossibleNumbers(row1, col1);
		
		Set<Integer> thingsToRemove = new HashSet<Integer>();
		for (int r = 0; r < miniGridSize; r++) {
			for (int c = 0; c < miniGridSize; c++) {
				if(grid[row + r][col + c] == 0 && ((row + r != row1) || (col + c != col1))) {
					thingsToRemove.addAll(getPossibleNumbers(row + r, col + c));	
				}
			}
		}
		possibleValuesForBox.removeAll(thingsToRemove);
			
		if(possibleValuesForBox.size() == 1) {
			System.out.println("Analysis: Setting value of index (" + row1 + ", " + col1 + ") to " + possibleValuesForBox.toArray()[0]);
			grid[row1][col1] = (int) possibleValuesForBox.toArray()[0];
			return true;
		}
		return false;
	}
	
	/**
	 * Solves the sudoku grid using a custom algorithm. This is my own algorithm
	 * which is based off of a method which humans use to solve sudoku problems.
	 * @return whether the sudoku puzzle is solvable
	 */
	public boolean solveByCalculation() {
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				if (grid[i][j] == 0) {

					Set<Integer> possibleNumbers = getPossibleNumbers(i, j);

					System.out.println("Index = (" + i + ", " + j + "): " + Arrays.toString(possibleNumbers.toArray()));

					if(possibleNumbers.size() == 0) {
						changes = 0;
						System.out.println("Sudoku puzzle is impossible (cannot be solved) - Like ACTUALLY impossible. Not even a computer can solve this");
						return false;
					}
					if (possibleNumbers.size() == 1) {
						changes++;
						System.out.println("Setting (" + i + ", " + j + ") to " + possibleNumbers.toArray()[0]);
						grid[i][j] = (int) possibleNumbers.toArray()[0];
					} else {
						if (analyseBox(i, j)) {
							changes++;
						}
					}

					if (changes != 0) {
						v.update(grid);
						try {
							Thread.sleep(DELAY);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		if (changes != 0) {
			iteration++;
			changes = 0;
			solveByCalculation();
		} else {
			for (int i = 0; i < gridSize; i++) {
				for (int j = 0; j < gridSize; j++) {
					if (grid[i][j] == 0) {
						v.update(grid);
						System.out.println("Sudoku puzzle cannot be solved (This is probably an advanced puzzle)");
						new Visualiser(generateRelationshipTable(), gridSize, "Relationship table after attempted solve");
						return false;
					}
				}
			}
			System.out.println("Iterations: " + iteration);
		}
		return true;
	}

	/*******************************************************************************/
	
	private Set<Integer> getPossibleNumbers(int i, int j) {
		if(grid[i][j] != 0) {
			return new HashSet<Integer>();
		}
		
		// Check row:
		Set<Integer> numbersItCannotBe = new HashSet<Integer>();
		for (int row = 0; row < gridSize; row++) {
			if (row != i && grid[row][j] != 0) {
				numbersItCannotBe.add(grid[row][j]);
			}
		}

		// Check column:
		for (int col = 0; col < gridSize; col++) {
			if (col != j && grid[i][col] != 0) {
				numbersItCannotBe.add(grid[i][col]);
			}
		}
		
		//Check mini grid:
		numbersItCannotBe.addAll(findMiniGridValues(i, j));
		return invertValues(numbersItCannotBe);
	}
	
	public int[][] initialGrid; 
	
	/**
	 * Tries to solve an advanced sudoku problem
	 */
	public void solveAdvancedByCalculation(boolean calculateAlternatives, int alternativeIndex) {	
		System.out.println("INDEX = " + alternativeIndex);
		//Set the initial grid
		if(iteration == 0) {
			initialGrid = new int[9][9];
			for(int i = 0; i < 9; i++) {
				for(int j = 0; j < 9; j++) {
					initialGrid[i][j] = grid[i][j];
				}
			}
		}
		
		iteration++;
		
		mainLoop:
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (grid[i][j] == 0) {
					
					Set<Integer> possibleNumbers = getPossibleNumbers(i, j);
					System.out.println("Index = (" + i + ", " + j + "): " + Arrays.toString(possibleNumbers.toArray()));
					
					System.out.println("Analysing box:");
					
					
					if(possibleNumbers.size() == 0) {
						System.out.println("Resetting grid!");
						for(int k = 0; k < 9; k++) {
							for(int l = 0; l < 9; l++) {
								grid[k][l] = initialGrid[k][l];
							}
						}
						v.update(grid);
						break mainLoop;
					}
					
					if(possibleNumbers.size() == 1) {
						changes++;
						System.out.println("Setting value of index (" + i + ", " + j + ") to " + possibleNumbers.toArray()[0]);
						grid[i][j] = (int) possibleNumbers.toArray()[0];
					} 
					
					if(calculateAlternatives) {
						if(possibleNumbers.size() > 1) {
							if((alternativeIndex >= possibleNumbers.size())) {
								alternativeIndex = 0;
							}
							
							changes++;
							System.out.println("Trying by setting value of index (" + i + ", " + j + ") to " + possibleNumbers.toArray()[alternativeIndex]);
							grid[i][j] = (int) possibleNumbers.toArray()[alternativeIndex];
							calculateAlternatives = false;
						}
					}
					
					v.update(grid);
					try {
						Thread.sleep(DELAY);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		System.out.println(changes + " changes were made");
		if(changes != 0) {
			changes = 0;
			solveAdvancedByCalculation(false, alternativeIndex);
		} else {
			checkingLoop: for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					if (grid[i][j] == 0) {
						System.out.println("No solutions for this grid, trying another solution...");
						break checkingLoop;
					}
				}
			}
			
			solveAdvancedByCalculation(true, alternativeIndex + 1);
			return;
		}
	}
	
}
