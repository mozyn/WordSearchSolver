import java.io.IOException;
import java.util.Stack;

public class Grid {
	private char[][] grid;
	private Stack<Position> positions;
	
	private enum Direction {
		RIGHT, DOWN, LEFT, UP, ALL
	}
	

	public Grid(int rows, int cols) {
		grid = new char[rows][cols];
	}

	public char getCharacter(int row, int col) {
		return grid[row][col];
	}
	
	public void setCharacter(int row, int col, char character) {
		grid[row][col] = character;
	}
	
	public void print() {
		for(int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				System.out.print(grid[i][j] + (j < grid[i].length - 1 ? " " : "\n"));
			}
		}
	}
	
	public void printToFile() throws IOException {
		for(int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				Main.outputFile.append(grid[i][j] + (j < grid[i].length - 1 ? " " : "\n"));
			}
		}
		Main.outputFile.append("\n");
	}
	
	
    private boolean search(int row, int col, int wordIndex, int characterIndex, String[] words, Direction direction, Position position) throws IOException  
    {  
    	
        boolean isOutOfBounds = row < 0 || row >= grid.length || col< 0 || col >= grid[row].length;
		int prevRow = row;
		int prevCol = col;
		switch (direction) {
		case ALL:
			break;
		case RIGHT:
			prevCol--;
			break;
		case DOWN:
			prevRow--;
			break;
		case LEFT:
			prevCol++;
			break;
		case UP:
			prevRow++;
			break;
		default:
		}
    	// when reached outside the grid or character does not match word at characterIndex
        if (isOutOfBounds || grid[row][col] != Character.toLowerCase(words[wordIndex].charAt(characterIndex))) {
        	// If checking final direction (up) then no match is found through this path

        	if (characterIndex == 0 && direction != Direction.ALL) {
        		Main.outputFile.append("Start (" + prevRow + "," + prevCol + ")" + " look " + direction + " for '" + words[wordIndex].charAt(characterIndex) + "' - not found\n");
        	}
        	if (characterIndex > 0) {
        		Main.outputFile.append("'" + words[wordIndex].charAt(characterIndex - 1) + "' found at (" + prevRow + "," + prevCol + ") move " + direction  + " for '" + words[wordIndex].charAt(characterIndex) + "' - not found\n");
        	}
        	if (wordIndex == 0 && direction == Direction.UP) {
        		Main.outputFile.append("ALL FOUR DIRECTIONS CHECKED - search for next '" + words[0].charAt(0) + "'\n");
        	}
        	if (direction == Direction.UP && !positions.isEmpty()) {
        		Position prevPosition = positions.pop();
        		Main.outputFile.append("BACKTRACK to '" + prevPosition.getWord().charAt(0) + "' at (" + prevPosition.getFrom().getRow() + "," + prevPosition.getFrom().getCol() + ")\n");
        	}
            return false;   
    	}
        
        boolean matchFound;
        
        // Change current matched character to upper case
        grid[row][col] = Character.toUpperCase(grid[row][col]);  
          
        // when word matched moved to next word or return if last word  
        if(characterIndex == words[wordIndex].length() - 1) {
    		Main.outputFile.append("'" + words[wordIndex].charAt(characterIndex - 1) + "' found at (" + prevRow + "," + prevCol + ") move " + direction  + " for '" + words[wordIndex].charAt(characterIndex) + "' - found\n");
    		Main.outputFile.append("'" + words[wordIndex].charAt(characterIndex) + "' found at (" + row + "," + col + ") word " + (wordIndex + 1) + " \"" + words[wordIndex] + "\" found\n");
        	position.setTo(new Coordinate(row, col));
        	positions.push(position);
        	if (wordIndex == words.length - 1) {
        		print();
        		for (int i = 0; i < positions.size(); i++) {
        			System.out.println(positions.get(i));
        		}
        		// change current grid character back to lower case
        		grid[row][col] = Character.toLowerCase(grid[row][col]); 
        		//Main.outputFile.append("ALL WORDS OF THE PHRASE WERE FOUND\n");
        		return true;
        	}
        	Main.outputFile.append("Look for word " + (wordIndex + 2) + ": "  + words[wordIndex + 1] + "\n");
            matchFound = search(row, col + 1, wordIndex + 1, 0, words, Direction.RIGHT, new Position(words[wordIndex + 1], new Coordinate(row, col + 1))) ||  
                    search(row + 1, col, wordIndex + 1, 0, words, Direction.DOWN, new Position(words[wordIndex + 1], new Coordinate(row + 1, col))) ||  
                    search(row, col - 1, wordIndex + 1, 0, words, Direction.LEFT, new Position(words[wordIndex + 1], new Coordinate(row, col - 1))) ||  
                    search(row - 1, col, wordIndex + 1, 0, words, Direction.UP, new Position(words[wordIndex + 1], new Coordinate(row - 1, col)));
        }
        else {
        	if (characterIndex == 0) {
        		Main.outputFile.append("Start (" + row + "," + col + ")" + " look for '" + words[wordIndex].charAt(characterIndex) + "' - found\n");
        	}

    		if (characterIndex > 0 && direction != Direction.ALL) {
        		Main.outputFile.append("'" + words[wordIndex].charAt(characterIndex - 1) + "' found at (" + prevRow + "," + prevCol + ") move " + direction  + " for '" + words[wordIndex].charAt(characterIndex) + "' - found\n");
    		}
        	//check all directions if we're at the beginning of the phrase
        	switch (direction) {
        	case ALL: 
        		matchFound = search(row, col + 1, wordIndex, characterIndex + 1, words, Direction.RIGHT, position) ||
        					 search(row + 1, col, wordIndex, characterIndex + 1, words, Direction.DOWN, position) ||
        					 search(row, col - 1, wordIndex, characterIndex + 1, words, Direction.LEFT, position) ||
        					 search(row - 1, col, wordIndex, characterIndex + 1, words, Direction.UP, position);
        		break;
        	case RIGHT:
        		matchFound = search(row, col + 1, wordIndex, characterIndex + 1, words, direction, position);
        		break;
        	case DOWN:
        		matchFound = search(row + 1, col, wordIndex, characterIndex + 1, words, direction, position);
        		break;
        	case LEFT:
        		matchFound = search(row, col - 1, wordIndex, characterIndex + 1, words, direction, position);
        		break;
        	case UP:
        		matchFound = search(row - 1, col, wordIndex, characterIndex + 1, words, direction, position);  
        		break;
        	default:
        		matchFound = false;
        	}
        }

        // Change character in grid back to lower case
        grid[row][col] = Character.toLowerCase(grid[row][col]); 
        return matchFound;  
    }  
    
    
    public boolean search(String[] words) throws IOException  
    {  
    	positions = new Stack<Position>();
        Main.outputFile.append("Search: \n");
    	Main.outputFile.append("Look for word 1: " + words[0] + "\n");
        for (int i = 0; i < grid.length; i++)  
        {  
            for (int j = 0; j < grid[i].length; j++)  
            {  
                if (search(i, j, 0, 0, words, Direction.ALL, new Position(words[0], new Coordinate(i, j))))  
                    return true;
            }  
        }  
        Main.outputFile.append("NO MORE '" + words[0].charAt(0) + "' values\n");
        return false;  
    }  

}
