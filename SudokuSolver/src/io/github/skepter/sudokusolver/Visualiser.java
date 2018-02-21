package io.github.skepter.sudokusolver;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JToggleButton;

@SuppressWarnings("serial")
public class Visualiser extends JFrame {

	
	JToggleButton[][] view;
	private int gridSize;

	public Visualiser(int[][] grid, int gridSize, String title) {
		this.gridSize = gridSize;
		setLayout(new GridLayout(gridSize, gridSize));
		view = new JToggleButton[gridSize][gridSize];
		for (int row = 0; row < gridSize; row++) {
			for (int col = 0; col < gridSize; col++) {
				JToggleButton b = new JToggleButton();
				b.setFont(new Font("Arial", Font.BOLD, 18));
				view[row][col] = b;
				add(view[row][col]);
			}
		}
		setBounds(0, 0, 550, 500);
		setTitle(title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
		update(grid);
	}

	public void update(int[][] grid) {
		for (int row = 0; row < gridSize; row++) {
			for (int col = 0; col < gridSize; col++) {
				if (grid[row][col] != 0)
					view[row][col].setText(String.valueOf(grid[row][col]));
				else
					view[row][col].setText("");
			}
		}
	}
	
	/**
	 * Toggles the button in the visualizer
	 * @param row
	 * @param col
	 */
	public void toggle(int row, int col) {
		view[row][col].setSelected(!view[row][col].isSelected());
	}

}
