package application;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.PrintWriter;
/**
 * @author Darijus
 *version: 0.5
 *
 *Scans file for barcodes number and amount associated with it. Outputs file with same name +"output". Stores same imformation in 2D array.
 */
public class scanFile {
	
	private String [][] barcodeArray = new String[1000][2];
	public int counter = 0;
	String fileName;
	public scanFile (String name) {
		fileName = name;
	}

	
	public void scanErmi (String fileName){
		
		File file = new File(fileName);
	
		// new scanner to read file
		Scanner barcodeFile = null;
		try {
			barcodeFile = new Scanner(file);
		} catch (FileNotFoundException e1) {
			System.out.println("Check your file name");
			//e1.printStackTrace();
		}
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(fileName+"ERMI.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String tempBarcode = "";
		double amount = 0;
		// looks for barcode in file
		while (barcodeFile.hasNextLine()){
			String barcode = barcodeFile.nextLine();
			Scanner line = new Scanner (barcode);
			while (line.hasNext())
			{
				String barcodes = line.next();
				int x = barcodes.length();
				if (x >= 3)
					{
					String z = barcodes.substring(x-3);
					// checks if next string is barcode and prints it to file
						if ((barcodes.startsWith("200") || barcodes.startsWith("209")) && x == 13)
							{
								if (!tempBarcode.equals(barcodes))
								{
									if (amount != 0)
										{
										writer.println(amount);
										//Puts String converted from int to array and removes ".0" from end
										barcodeArray[counter++][1] =String.valueOf(amount).substring(0, String.valueOf(amount).length()-2);
										}
									amount = 0;
									writer.print(barcodes+"\t");
									barcodeArray[counter][0] = barcodes;
									tempBarcode = barcodes;
								}
							}
						// looks for amount according to fact that before amount there is a counting unit
						else if (z.equalsIgnoreCase("VNT") || z.equalsIgnoreCase("PAK") || z.equalsIgnoreCase("KPL") || z.equalsIgnoreCase("M.B"))
						{
							while (line.hasNext())
							{
								String temp = line.next();
								boolean digit = false;
									// checks if string is longer then 4
									if (temp.length() > 4)
									{
										//removes .000
										temp = temp.substring(0, temp.length()-4);
										//checks if its a digit
										for (int i = 0; i < temp.length(); i++)
											{
												if (Character.isDigit(temp.charAt(i)))
													{
														digit = true;
													}
												else
													{
														digit = false;
														break;
													}
											}
									}
												
									if (digit)
										{
										amount += Integer.parseInt(temp);
										break;
										}
							}
						}
					}
			}

		}
		// prints last barcode amount
		writer.print(amount);
		barcodeArray[counter][1] = String.valueOf(amount).substring(0, String.valueOf(amount).length()-2);		
		//close both files
		barcodeFile.close();
		writer.close();
	}
	
	public String[][] getArray ()
	{
		return this.barcodeArray;
	}

	private double valueOf(String barcodes) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void scanBik (String fileName){
		
		File file = new File(fileName);
	
		// new scanner to read file
		Scanner barcodeFile = null;
		try {
			barcodeFile = new Scanner(file);
		} catch (FileNotFoundException e1) {
			System.out.println("Check your file name");
			//e1.printStackTrace();
		}
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(fileName+"BIK.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// looks for barcode in file
		while (barcodeFile.hasNextLine()){
			String barcode = barcodeFile.nextLine();
			Scanner line = new Scanner (barcode);
			while (line.hasNext())
			{
				String barcodes = line.next();
				int x = barcodes.length();
				if (x >= 3)
					{
					// checks if next string is barcode and prints it to file
						if ((barcodes.startsWith("000") || barcodes.startsWith("300") || barcodes.startsWith("999") || barcodes.startsWith("290") || barcodes.startsWith("590")) && x > 10)
							{
								String temp = onlyNumbers(barcodes, x);
								writer.println(temp+"\t2");
								barcodeArray[counter][0] = temp;
								barcodeArray[counter][1] = "2";
								counter++;
							}
					}
			}

		}
		//close both files
		barcodeFile.close();
		writer.close();
	}
	
	public void scanPadaryk(String fileName)
	{
		
		File file = new File(fileName);
	
		// new scanner to read file
		Scanner barcodeFile = null;
		try {
			barcodeFile = new Scanner(file);
		} catch (FileNotFoundException e1) {
			System.out.println("Check your file name");
			//e1.printStackTrace();
		}
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(fileName+"PP.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String tempBarcode = "";
		// looks for barcode in file
		while (barcodeFile.hasNextLine())
		{
			String barcode = barcodeFile.nextLine();
			Scanner line = new Scanner (barcode);
			while (line.hasNext())
			{
				String barcodes = line.next();
				int x = barcodes.length();
				if (x >= 3)
					{
					// checks if next string is barcode and prints it to file
						if ((barcodes.startsWith("200") || barcodes.startsWith("209")) && x > 10)
							{
							if (!tempBarcode.equals(barcodes))
								{
									String temp = onlyNumbers(barcodes, x);
									writer.println(temp+"\t2");
									barcodeArray[counter][0] = temp;
									barcodeArray[counter++][1] = "2";
									tempBarcode = temp;
								}
							}
					}
			}
		}
		//close files
		barcodeFile.close();
		writer.close();
	}
	
	// returns string that qualifies as barcode, in input gets number with non-numeric char
	public String onlyNumbers (String name, int length){
		int x = length;
		String y = name;
		if (!Character.isDigit(y.charAt(x-1)))
			{
				y = onlyNumbers(name.substring(0, length-1), length-1);
			}
		return y;
	}
	
}
