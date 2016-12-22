import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.PrintWriter;
import java.lang.Double;
/**
 * @author Darijus
 *version: 0.1
 *
 *Scans file for barcodes number and amount associated with it. Outputs file with same name +"output". Stores same imformation in 2D array.
 */
public class scanFile {
	
	private long [][] barcodeArray = new long[1000][2];
	public int counter = 0;
	String fileName;
	public scanFile (String name) {
		fileName = name;
	}

	
	public void scanIt (String fileName){
		
		File file = new File(fileName+".txt");
	
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
			writer = new PrintWriter(fileName+"output.txt");
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
										barcodeArray[counter++][1] = (long) amount;
										}
									amount = 0;
									writer.print(barcodes+"\t");
									barcodeArray[counter][0] = (long) Double.parseDouble(barcodes);
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
		writer.println(amount);
		barcodeArray[counter][1] = (long) amount;		
		//close both files
		//barcodeFile.close();
		writer.close();
	}
	
	public long[][] getArray ()
	{
		return this.barcodeArray;
	}

	private double valueOf(String barcodes) {
		// TODO Auto-generated method stub
		return 0;
	}
}
