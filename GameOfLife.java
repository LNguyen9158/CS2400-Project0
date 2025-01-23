/*
* File: GameOfLife
* Author: Lacey Nguyen
* Class: CS 1400-01 Intro Programming Prob Solving
* 
* Assignment: Program 6
* Date last modified: 12/4/2024
* 
* Purpose: This is the Program 6 assignment that is to be submitted on zyBooks that is like Conway's Game of Life.  
*/

import java.util.Scanner;
import java.io.*;

public class GameOfLife {
	Scanner dataFS = null;
	FileInputStream fbStream = null;
	private int numColumns; 
	private int numRows; 
	private int[][] gameBoard;
	private int genCount = 1;

	public void setCR(Scanner scnr) {
		numRows = scnr.nextInt();	//PRESUMABLY: first number in file
		numColumns = scnr.nextInt();	//PRESUMABLY: the second number in file
		//numRows = scnr.nextInt();	//PRESUMABLY: second number in file
	}

	public int getColumns() { 
		return numColumns; //numColumns of game board
	}

	public int getRows() { 
		return numRows;
	}

	public void setBoard(File inputFile) throws IOException, ArrayIndexOutOfBoundsException { //set up board and put into gameBoard
		char[][] board = new char[numRows][numColumns]; 
		gameBoard = new int[numRows][numColumns];
		fbStream = new FileInputStream(inputFile);
		dataFS = new Scanner(fbStream); 
		dataFS.nextLine();
		int tempInt;
		char tempChar;
		String tempString; 
		int row = -1;
		while (dataFS.hasNextLine()) {
			tempString = dataFS.nextLine();
			tempString = tempString.replaceAll(" ", "");		//take out whitespace
			char[] c = tempString.toCharArray(); 
			//System.out.println(tempString);
			row++;
			for (int k = 0; k < tempString.length(); k++) {
				try {
					board[row][k] = c[k];
				}
				catch (ArrayIndexOutOfBoundsException e) {
					//System.out.print(k);
					//continue;
				}
			} //System.out.println();
		}
		for (int i = 0; i < numRows; i++) { //copy and change char to int for usage
			for (int j = 0; j < numColumns; j++) {
				gameBoard[i][j] = board[i][j] - '0';
			}
		}
		//TO CHECK ARRAY BY PRINTING:
		/*for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				System.out.print(gameBoard[i][j] + " ");
			}
			System.out.println();
		}*/
		
	}

	public int[][] callBoard() {
		return gameBoard;
	}

	public int getCell(int column, int row) throws ArrayIndexOutOfBoundsException { //get value of cell at column and row
		callBoard();
		try {
			return gameBoard[row][column];	//value of cell at column and row 
		} //end of try
		catch (ArrayIndexOutOfBoundsException e) {
			return 0; //if column/row out of game board bounds
		}
	}

	public void setCell(int column, int row, int value) { //set value of cell at given column and row
		gameBoard[row][column] = value;
	}

	public int setCount(int count) {
		genCount = count;
		return genCount;
	}

	public int findNeighbors(int i, int j) {
		int neighbors = 0;
		boolean cellAlive = false;
		for (int k = i-1; k < i+2; k++) { //to count neighbors
			for (int l = j-1; l < j+2; l++) {
				if (getCell(l, k) == 1)
					neighbors += 1;
				if (getCell(j,i) == 1)
					cellAlive = true;
			}
		}
		if (cellAlive)
			neighbors -= 1;
		return neighbors;
	}

	public void computeNextGeneration(int generation) { 		//parameter: numGen to compute
		int[][] tempArray = new int[numRows+5][numColumns+5];	//create temp 2D array to compute next iteration of board
		int [][] copyBoard = new int[numRows+5][numColumns+5];
		
		callBoard();
		for (int i = 0; i < numRows; i++) { 
			for (int j = 0; j < numColumns; j++) {
				if (gameBoard[i][j] == 0 || gameBoard[i][j] == 1)
					copyBoard[i+3][j+3] = getCell(j,i);
			}
		}
		/*for (int i = 0; i < numRows+5; i++) { 			//check contents
			for (int j = 0; j < numColumns+5; j++) {
				System.out.print(copyBoard[i][j] + " ");
			}
			System.out.println();
		}*/
		if (generation == 1 && genCount > 1) { 				//base case to stop calls; if not first time
			for (int i = 0; i < numRows; i++) { 
				for (int j = 0; j < numColumns; j++) {
					int neighbors = findNeighbors(i,j);		//count each cell's neighbors 
					//neighbors = findNeighbors(i,j);
					if (gameBoard[i][j] == 1 && (neighbors < 2 || neighbors > 3)) //alive, <2 or >3 neighbors, dies 
						tempArray[i][j] = 0;
					else if (gameBoard[i][j] == 1 && (neighbors == 2 || neighbors == 3)) //alive w/2 or 3 neighbors, live
						tempArray[i][j] = 1;
					else if (gameBoard[i][j] == 0 && neighbors == 3) //dead and has 3 neighbors
						tempArray[i][j] = 1;
					else 
						tempArray[i][j] = 0;
				}//end of j for loop
			}
			for (int i = 0; i < numRows+5; i++) { 			//update board to represent next gen
				for (int j = 0; j < numColumns+5; j++) {
					copyBoard[i][j] = tempArray[i][j];
				}
			}
			for (int i = 0; i < numRows; i++) { 			//update board for printing
				for (int j = 0; j < numColumns; j++) {
					if ((tempArray[i][j] == 1 || tempArray[i][j] == 0) && (gameBoard[i][j] == 0 || gameBoard[i][j] == 1))
						gameBoard[i][j] = copyBoard[i][j];
				}
			}
			System.out.println("Generation " + genCount + "\n");
			print();
		}
		else if (generation == 1) {
			System.out.println("Generation " + genCount + "\n");
			print();
}
		else if (genCount == 1){ 				//Gen 1
			System.out.println("Generation " + genCount + "\n");
			print();
			System.out.println(); //MAY REMOVE; FORMATTING
			genCount+=1;
			computeNextGeneration(generation - 1); 
		}
		else { //make tempArray based on game rules
			for (int i = 0; i < numRows; i++) { 
				for (int j = 0; j < numColumns; j++) {
					int neighbors = 0;		//count each cell's neighbors 
					neighbors = findNeighbors(i,j);
					if (gameBoard[i][j] == 1 && (neighbors < 2 || neighbors > 3)) //alive, <2 or >3 neighbors, dies 
						tempArray[i][j] = 0;
					else if (gameBoard[i][j] == 1 && (neighbors == 2 || neighbors == 3)) //alive w/2 or 3 neighbors, live
						tempArray[i][j] = 1;
					else if (gameBoard[i][j] == 0 && neighbors == 3) //dead and has 3 neighbors
						tempArray[i][j] = 1;
					else 
						tempArray[i][j] = 0;
				}//end of j for loop
			}
			for (int i = 0; i < numRows; i++) { 			//update board to represent next gen
				for (int j = 0; j < numColumns; j++) {
					copyBoard[i][j] = tempArray[i][j];
				}
			}
			for (int i = 0; i < numRows; i++) { 			//update board for printing
				for (int j = 0; j < numColumns; j++) {
					if (tempArray[i][j] == 1 || tempArray[i][j] == 0 && (gameBoard[i][j] == 0 || gameBoard[i][j] == 1))
						gameBoard[i][j] = copyBoard[i][j];
				}
			}
			System.out.println("Generation " + genCount + "\n");	//printing board after updating
			print();
			System.out.println();	//MAY REMOVE; FORMATTING
			genCount += 1; //increments generation to be printed 
			computeNextGeneration(generation - 1); 			//recursive call until 0 gens to compute
		}
	}

	public void print() { //prints board to console
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				System.out.print(gameBoard[i][j]);
			}
			System.out.println();
		}
		//System.out.println(); //MAY REMOVE; FORMATTING
	}

	public static void main(String[] args) throws IOException, ArrayIndexOutOfBoundsException {
		Scanner scnr = new Scanner(System.in);
		
		System.out.print("Please enter a valid file name: ");
		String userFile = scnr.nextLine();	//get file name from user
		File fileName = new File(userFile);	//make a file
		if (!fileName.exists()) 		//check for file
			System.out.println("File does not exist.");
		else 
			System.out.print("How many generations to compute: ");
		int numGen;
		if (!scnr.hasNextInt()) {
			char userGen = scnr.next().charAt(0);
			numGen = userGen - '0'; 				//if input char, change to int
		}
		else { //if input is int
			numGen = scnr.nextInt();
		}
		System.out.println();						
		FileInputStream fbs = new FileInputStream(userFile);		//to read from file
		Scanner fileScnr = new Scanner(fbs);				//scans inputFile contents through fbStream
		GameOfLife theFool = new GameOfLife();
		theFool.setCR(fileScnr); 					//pass to setCR method w/file scanner to set columns and rows
		theFool.setBoard(fileName);
		theFool.computeNextGeneration(numGen);
		//System.out.println(theFool.findNeighbors(4,2));
	}

}