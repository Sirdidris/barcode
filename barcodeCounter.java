package application;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;

public class barcodeCounter {
	
	// list of Files name stored in array
	public String listOfFiles = "barkodu sarasas";
	String[][] arrayList;
	
	public String[][] printAmount (String[][] barcodeList)
	{
		arrayList = smallerArray(barcodeList);
		
		File file = new File (listOfFiles+".txt");
		
		// scans file to get a min and max values for barcodes so actual could be compared.
		Scanner listToCompare = null;
		try 
			{
				listToCompare = new Scanner(file);
			}
		catch (FileNotFoundException e1)
			{
				System.out.println("Did you change file name?");
			}
		String[][] barcodeKey = new String[2000][2];
		int counter = 0;
		
		while (listToCompare.hasNextLine())
			{
				String barcodeWithValue = listToCompare.nextLine();
				Scanner line = new Scanner (barcodeWithValue);
				while (line.hasNextLong())
					{
						barcodeKey[counter][0] = line.next();
						barcodeKey[counter][1] = line.next();
						counter++;
					}
				line.close();
			}
		
		for (int i =0; i < arrayList.length; i++)
			{
				for (int z = 0; z < counter+1; z++)
				{
					if ( arrayList[i][0].equals(barcodeKey[z][0]) )
						{
							switch (Integer.parseInt(barcodeKey[z][1]))
							{
								case 0: arrayList[i][1] = String.valueOf(2);
								break;
								case 1: arrayList[i][1] = String.valueOf(evenOrNot(Long.parseLong(arrayList[i][1])));
								break;
								case 2: arrayList[i][1] = String.valueOf(evenOrNot(Long.parseLong(arrayList[i][1])/3));
							}
						}
				}
			}
		//return updated array with amounts to print
		return arrayList;
	}

	public long evenOrNot (long z)
	{
		if (z%2 == 0)
		{
			z = z+2;
		}
		else
		{
			z=z+1;
		}
		return z;
	}
	
	private String[][] smallerArray (String[][] array){
		int counter = 0;
		while (array[counter][0] != null)
			{
				counter++;
			}
		String[][] shortArray = new String[counter][2];
		
		for (int i = 0; i < counter; i++)
		{
			shortArray[i][0] = array[i][0];
			shortArray[i][1] = array[i][1];
		}
		return shortArray;
	}
}
