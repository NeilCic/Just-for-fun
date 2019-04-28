package nonSense;

import java.util.Random;

public class KnightsTour {
	private final int N;
	private final int[][] board;
	private int moveCounter = 1;
	private int currRow;
	private int currCol;
	private int[][] legalMoves = { { -2, -1 }, { -1, -2 }, { 1, 2 }, { 2, 1 }, { 1, -2 }, { 2, -1 }, { -1, 2 }, { -2, 1 } };
	int[][] heuristicBoard;
	long timeOut = System.currentTimeMillis();
	static final long TIMER = 4_000;
	
	public KnightsTour(int boardSize, int startPosition) {
		N = boardSize;
		currRow = getRow(startPosition);
		currCol = getCol(startPosition);

		board = new int[N][N];
		heuristicBoard = new int[N][N];
		for (int i = 0; i < N; ++i) {
			for (int j = 0; j < N; ++j) {
				board[i][j] = 0;

				//initializing heuristics board
				int heuristics = 0;
				for (int k = 0; k < legalMoves.length; ++k) {
					int[] currMove = legalMoves[k];
					if (i + currMove[0] > -1 && i + currMove[0] < N &&
							j + currMove[1] > -1 && j + currMove[1] < N) {
						++heuristics;
					}
				}
				heuristicBoard[i][j] = heuristics;
			}
		}
		
		board[currRow][currCol] = moveCounter;
	}

	private void printBoard() {
		System.out.println("Found solution:");
		for (int i = 0; i < N; ++i) {
			for (int j = 0; j < N; ++j) {
				System.out.print(board[i][j] + "	");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	private int getRow(int position) {
		return position / N;
	}

	private int getCol(int position) {
		return position % N;
	}

	private void execute() {
		// stop condition
		if (moveCounter == N * N) {
			printBoard();
			return;
		}
//***********************************************************************************************
		// heuristics and timeout
		sortLegalMoves();
		if (System.currentTimeMillis() - timeOut > TIMER) {
			return;
		}
//***********************************************************************************************
		// actual work
		for (int i = 0; i < legalMoves.length; ++i) {
			int[] currMove = legalMoves[i];
			if (currRow + currMove[0] < N && currRow + currMove[0] > -1 &&
					currCol + currMove[1] < N && currCol + currMove[1] > -1 &&
					board[currRow + currMove[0]][currCol + currMove[1]] == 0) {
				
				currRow += currMove[0];
				currCol += currMove[1];
				board[currRow][currCol] = ++moveCounter;
				
				execute();
				if (moveCounter == N * N) {
					return;
				}
				
				board[currRow][currCol] = 0;
				--moveCounter;
				currRow -= currMove[0];
				currCol -= currMove[1];
			}
		}
		
		if (moveCounter == 1) {
			System.out.println("No solution");
		}
	}
	
	private int[] getHeuristicsResults() {
		int[] heuristicResults = new int[legalMoves.length];
		for (int i = 0; i < legalMoves.length; ++i) {
			int[] currMove = legalMoves[i];
			if (currRow + currMove[0] < N && currRow + currMove[0] > -1 &&
					currCol + currMove[1] < N && currCol + currMove[1] > -1) {
				heuristicResults[i] = heuristicBoard[currRow + currMove[0]][currCol + currMove[1]];
			}
		}
		
		return heuristicResults;
	}
	
	private void sortLegalMoves() {
		int[] heuristicResults = getHeuristicsResults();
		
		int[] tempPair = new int[2];
		int temp = 0;
		
		for (int i = 0; i < heuristicResults.length; ++i) {
			int minIndex = i;
			for (int j = i + 1; j < heuristicResults.length; ++j) {
				if (heuristicResults[j] < heuristicResults[minIndex]) {
					minIndex = j;
				}
			}
			
			if (heuristicResults[minIndex] < heuristicResults[i]) {
				temp = heuristicResults[minIndex];
				tempPair = legalMoves[minIndex];
				
				heuristicResults[minIndex] = heuristicResults[i];
				heuristicResults[i] = temp;
				
				legalMoves[minIndex] = legalMoves[i]; 
				legalMoves[i] = tempPair;
			}
		}
	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		Random rand = new Random();
		int boardSize = 8;
		int startPosition = rand.nextInt(boardSize * boardSize);
		System.out.println("Start position: " + startPosition);
		KnightsTour test1 = new KnightsTour(boardSize, startPosition);
		test1.execute();
		Double elapsedTime = (System.currentTimeMillis() - startTime) / 1000D;
		System.out.println(elapsedTime);
	}
}
