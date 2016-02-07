// This program will create a .ppm type picture of size 200 pixels by 160 pixels. It will also now modify a ppm file
// by flipping it, contrasting it, putting it in negative and several more options.

import java.util.*;
import java.io.*;

public class ImageEdit
{
	private Pixel [][] imagearray; 
	private String inFile, outFile;
	private int height, width, maxValue;

	//int counter;

	public ImageEdit() 
	{
		inFile = new String("picard.ppm");	// default constructor that opens picard.ppm and prints to out.ppm
		outFile = new String("out.ppm");
	}
	public ImageEdit(String inF, String outF) 
	{
		inFile = inF;						// if the user entered arguments then they are set as the file to be written to and read from.	
		outFile = outF;
	}

	public static void main(String[] args)
	{
		ImageEdit picture;
		try // tries get the first arguments entered by the user and sets it as the inFile and outFile.
		{
			picture = new ImageEdit(args[0], args[1]); 
		}
		catch(ArrayIndexOutOfBoundsException e) // if no input is given then the program defaults to use picard.ppm and out.ppm
		{
			picture = new ImageEdit();
		}
		picture.execute(); // calls the method that runs the rest of the program.
	}
	public void execute()
	{
		loadPic(); // reads in the ppm file.
		greetingAndMenuList(); // prints what the user should consider doing to alter the image.
		promptUser(); // promts the user to choose what way to alter the image.
		writeToFile(); // writes the new ppm to the outFile with proper formatting.
		exitMessage(); // lets the user know that the program has finished altering the image and it is ready to view.
	}

	public void loadPic()
	{
		Scanner file = OpenFile.openToRead(inFile);
		file.nextLine(); // skips "P3"
		// width and height are at the top of all P3 files.
		width = file.nextInt();
		height = file.nextInt();
		maxValue = file.nextInt();
		imagearray = new Pixel[height][width];
		for(int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
				imagearray[i][j] = new Pixel(file.nextInt(), file.nextInt(), file.nextInt()); // fills the 2D array with the values of each pixel from the ppm.
		}
	}

	public void greetingAndMenuList ()
	{
		System.out.println("\n\n\n");			// opening statements for the program
		System.out.println("WELCOME TO THE PPM IMAGE EDITOR");
		System.out.println("\n\n      Consider the menu of choices below.");
		System.out.println("      (1)Flip Image Horizontally");
		System.out.println("      (2)Flip Image Vertically");
		System.out.println("      (3)Convert Image to Grayscale");
		System.out.println("      (4)Convert Image to Negative");
		System.out.println("      (5)Convert to Extreme Contrast\n\n");
	}
	
	public void promptUser()
	{
		char choice = 'a';
		while(choice != 'y' && choice != 'n')
			choice = Prompt.getString("Do you want to (1)Flip the image horizontally? (y/n)      : ").toLowerCase().charAt(0);
		if(choice == 'y') // checks if the user entered 'y' or 'Y' if not then nothing happens and the next prompt is printed, otherwise the picture is altered.
			horizontalFlip();
		choice = 'a';
		while(choice != 'y' && choice != 'n')
			choice = Prompt.getString("Do you want to (2)Flip the image vertically? (y/n)        : ").toLowerCase().charAt(0);
		if(choice == 'y')	// similar checking method
			verticalFlip();
		choice = 'a';
		while(choice != 'y' && choice != 'n')
			choice = Prompt.getString("Do you want to (3)Convert the image to grayscale? (y/n)   : ").toLowerCase().charAt(0);
		if(choice == 'y')
			grayscale();
		choice = 'a';
		while(choice != 'y' && choice != 'n')
			choice = Prompt.getString("Do you want to (4)Convert the image to negative? (y/n)    : ").toLowerCase().charAt(0);
		if(choice == 'y')
			negative();
		choice = 'a';
		while(choice != 'y' && choice != 'n')
			choice = Prompt.getString("Do you want to (5)Convert image to extreme contrast? (y/n): ").toLowerCase().charAt(0);
		if(choice == 'y')
			extremeContrast();
	}

	public void horizontalFlip()
	{
		Pixel temp; // temp Pixel instance that is going to be used to get the imagearray at each point in the Pixel 2D array and flip them to their corresponding
					//spot on the other side of the array.
		for(int i = 0; i < imagearray.length; i++)
		{
			for(int j = 0; j < imagearray[i].length / 2; j++)
			{
				temp = imagearray[i][j];
				imagearray[i][j] = imagearray[i][imagearray[i].length - j - 1];
				imagearray[i][imagearray[i].length - j - 1] = temp;
			}
		}
	}
	public void verticalFlip()
	{
		Pixel[] temp;
		for(int i = 0; i < imagearray.length / 2; i++)
		{
			temp = imagearray[i]; 		// takes the array at each line and moves it to its corresponding spot on the other side of the array.
			imagearray[i] = imagearray[imagearray.length - i - 1];		// swaps row 1 with last row, row 2 with second last row, etc...
			imagearray[imagearray.length - i - 1] = temp;
		}
	}

	public void grayscale()
	{
		Pixel[][] temp = new Pixel [height][width];
		int average;
		for(int i = 0; i < imagearray.length; i++)
		{
			for(int j = 0; j < imagearray[i].length; j++)
			{
				average = ((imagearray[i][j].getRed() + imagearray[i][j].getGreen() + imagearray[i][j].getBlue())/3);	// creates the average of 3 colors
				temp[i][j] = new Pixel(average, average, average);		// temp stores the average
			}
		}
		imagearray = temp;			//updates the array to the new image
	}	

	public void negative()
	{
		Pixel[][] temp = new Pixel[height][width];
		int red, green, blue;
		for(int i = 0; i < imagearray.length; i++)
		{
			for(int j = 0; j < imagearray[i].length; j++)
			{
				red = maxValue - imagearray[i][j].getRed();		// making each of the values max - the current value
				green = maxValue - imagearray[i][j].getGreen();
				blue = maxValue - imagearray[i][j].getBlue();
				temp[i][j] = new Pixel(red, green, blue);			// making the temp array with these new colors
			}
		}
		imagearray = temp; // updates the array to the new image.
	}

	public void extremeContrast()
	{
		Pixel[][] temp = new Pixel[height][width];
		int red, green, blue;
		for(int i = 0; i < imagearray.length; i++)
		{
			for(int j = 0; j < imagearray[i].length; j++)
			{
				if(imagearray[i][j].getRed() <= (maxValue / 2)) // if the value is less than half than the value is brought to the min.
					red = 0;
				else // other wise it is brought to the max.
					red = maxValue;
				if(imagearray[i][j].getGreen() <= (maxValue / 2))
					green = 0;
				else
					green = maxValue;
				if(imagearray[i][j].getBlue() <= (maxValue / 2))
					blue = 0;
				else
					blue = maxValue;
				temp[i][j] = new Pixel(red, green, blue);
			}
		}
		imagearray = temp; // updates the Pixel array.
	}

	public void writeToFile()
	{
		PrintWriter writer = OpenFile.openToWrite(outFile);		// opening the file to write the changed image to
		writer.print("P3");
		writer.print("\r\n");
		writer.println(width + "   " + height);
		writer.println("\r\n" + maxValue + "\r\n");
		for(int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				writer.print(imagearray[i][j] + " "); // prints the new pixel values in the ppm.
				if((j + 1) % 7 == 0) //prints a new line after every 7 pixel combinations 
					writer.print("\r\n");
			}
			writer.println("\r\n");
		}
		writer.close();
	}

	public void exitMessage()
	{
		System.out.println("\n\n\nYour edited image has been saved to: " + outFile);	// ending messages
		System.out.println("THANK YOU FOR USING THE PPM IMAGE EDITOR" + "\n\n\n");
	}

}
