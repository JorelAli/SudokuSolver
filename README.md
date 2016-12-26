# SudokuSolver

This repository is an attempt of trying to find a new method of solving a sudoku puzzle.

## What's sudoku?
[Sudoku](https://en.wikipedia.org/wiki/Sudoku) is a logic based puzzle where you have to arrange numbers in a 9x9 grid, such that each row, column and 3x3 grid does not contain the numbers 1 to 9 more than once.

## How do computers solve sudoku?
Basically, in this repository I have two types of puzzles:
* Regular puzzles - These puzzles can be solved by a human by checking each row/column/box to find where the numbers go
* Advanced puzzles - These puzzles cannot be solved by a human using the regular method of checking the rows, columns and boxes and require guessing. Guessing is generally a bad thing to do in sudoku as if one of the numbers in the grid are incorrect, then every other number entered into the grid will also be incorrect. Therefore, we have this algorithm called [backtracking](https://en.wikipedia.org/wiki/Backtracking) which is a method of solving problems by trying possible solutions. If a solution fails, then you go back and try the other possible solutions until you succeed.

## Why even solve sudoku puzzles?
As it's a logical puzzle involving number placement, it can be used to model many other types of problems, such as protein folding.

# Why does this repository exist?
I'm trying to find a method better than backtracking, OR finding a method which requires the least amount of backtracking.
