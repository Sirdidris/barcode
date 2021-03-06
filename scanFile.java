import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.PrintWriter;
import java.lang.Double;
/**
 * @author Darijus
 *version: 0.1
 *
 *Scans file for barcodes number
 */
public class scanFile {
	
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
										}
									amount = 0;
									writer.print(barcodes+"\t");
									tempBarcode = barcodes;
								}
							}
						// looks for amount according to fact that before amount there is a counting unit
						else if (z.equalsIgnoreCase("VNT") || z.equalsIgnoreCase("PAK") || z.equalsIgnoreCase("KPL") || z.equalsIgnoreCase("M.B"))
						{
							while (line.hasNext()){
								while (!line.hasNextDouble())
									{
										line.next();
									}
								amount += line.nextDouble();
								break;
							}
						}
					}
			}

		}
		// prints last barcode amount
		writer.println(amount);
		//close both files
		barcodeFile.close();
		writer.close();
	}

	private double valueOf(String barcodes) {
		// TODO Auto-generated method stub
		return 0;
	}
}
