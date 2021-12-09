//Program Name: Program 5 CIS 350
//Programmer Name: Mohamed Al-Omairi
//Description: Project 5
//Date Created: 8/14/21


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Scanner;

public class Main {
	
	public static FileWriter outputFile; 
	

	public static void main(String[] args) {
		Scanner userInput = new Scanner(System.in);
		Scanner inputFile = null;
		String inputFileName;
		String outputFileName;
		
		try {
			System.out.print("Please enter grid filename: ");
			inputFileName = userInput.nextLine();
			
			System.out.print("Please enter an output file name: ");
			outputFileName = userInput.nextLine();
			outputFile = new FileWriter(outputFileName);
			
			outputFile.write("Grid from: " + inputFileName + "\n");
			
			inputFile = new Scanner(new File(inputFileName));
			String line = inputFile.nextLine();
			String[] dimensions = line.split("\s+");
			int rows = Integer.parseInt(dimensions[0]);
			int cols = Integer.parseInt(dimensions[1]);
			if (rows <= 0 ) { 
				throw new Exception("[ERROR] Input Grid Row Specs Invalid: " + rows);	
			}
			else if (cols <= 0) { 
				throw new Exception("[ERROR] Input Grid Column Specs Invalid: " + cols);
			}
			System.out.println("\n" + rows + " rows of " + cols + " characters");
			outputFile.append(rows + " rows of " + cols + " characters\n");
			
		
			Grid grid = new Grid(rows, cols);
			for (int i = 0; i < rows; i++) {
				line = inputFile.nextLine().trim(); 
				for (int j = 0; j < cols; j++) {
					if (Character.isUpperCase(line.charAt(i))) { 
						System.out.println("[WARNING] - Uppercase letter detected: " + line.charAt(i) +  " at position [" + i + "," + j + "] Converted to lowercase...");
					}
					else if (Character.isUpperCase(line.charAt(j))) { 
						System.out.println("[WARNING] - Uppercase letter detected: " + line.charAt(j) + " at position [" + i + "," + j + "] Converted to lowercase...");
					}
					if (!Character.isAlphabetic(line.charAt(i))) { 
						throw new Exception("[ERROR] Input Grid Error! Character: " + line.charAt(i) + " at position [" + i + "," + j + "] is invalid!");
					}
					else if (!Character.isAlphabetic(line.charAt(j))) { 
						throw new Exception("[ERROR] Input Grid Error! Character: " + line.charAt(j) + " at position [" + i + "," + j + "] is invalid!");
					}
					grid.setCharacter(i, j, Character.toLowerCase(line.charAt(j)));
				}
			}
	
			inputFile.close();
			System.out.print("\n");
			System.out.println("Puzzle Layout\n");
			grid.print();
			System.out.print("\n");
			outputFile.append("Puzzle Layout\n");
			grid.printToFile();
			
			
			
			boolean matchFound;
			boolean invalid;
			String[] words;
			while (!line.isEmpty()) {
				invalid = false;
				System.out.print("Phrase to search (Leave blank and press enter to Quit): ");
				line = userInput.nextLine();
				System.out.print("\n");
				if (line.isEmpty()) { 
					continue;
				}
				for (int i = 0; i < line.length(); i++) { 
					if (!Character.isAlphabetic(line.charAt(i)) && !Character.isSpaceChar(line.charAt(i))) { 
						invalid = true;
					}
				}
				if (invalid) {
					System.out.println("[ERROR] Invalid word/phrase: '" + line + "' Re-enter word/phrase");
					continue;
				}
				words = line.split("\\s+");
				System.out.println("Looking for: " + line);
				outputFile.append("Looking for: " + line + "\n");
				System.out.println("Phrase contains " + words.length + " words");
				outputFile.append("Phrase contains " + words.length + " words\n");
				matchFound = grid.search(words);
				
				// if matchFound is false show message here
				System.out.println("Phrase '" + line + "' was "  + (matchFound ? "FOUND" : "NOT FOUND"));
				outputFile.append("\nPhrase '" + line + "' "  + (matchFound ? "FOUND\n\n" : "NOT FOUND\n\n"));
			}
		}
		
		catch(FileNotFoundException e) {
			// do something if file wasn't found
			System.out.println(e.getMessage());
			try { outputFile.append(e.getMessage()); } catch (Exception ex) { /* ignore */ }
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
			try { outputFile.append(e.getMessage()); } catch (Exception ex) { /* ignore */ }
		}
		catch(Exception e) {
			//backup exception
			System.out.println(e.getMessage());
			try { outputFile.append(e.getMessage()); } catch (Exception ex) { /* ignore */ }
		}
		//universal exception
		finally {
			try {
				userInput.close();
				inputFile.close();
				outputFile.close();
			}
			catch (Exception e) {/* ignore */}
		}
		
		System.out.println("Shutting down...");
		System.out.println("Thanks for running the Word Search!");

	}
}
