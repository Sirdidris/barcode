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
	
	public void scanIt (){
		
		File file = new File("dada.txt");
	
		// new scanner to read file
		Scanner barcodeFile = null;
		try {
			barcodeFile = new Scanner(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("output.txt");
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
						if (barcodes.startsWith("200") && x == 13)
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
						else if (z.equalsIgnoreCase("VNT") || z.equalsIgnoreCase("PAK") || z.equalsIgnoreCase("KPL") || z.equalsIgnoreCase("M.B"))
						{
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
		writer.println(amount);
		barcodeFile.close();
		writer.close();
	}

	private double valueOf(String barcodes) {
		// TODO Auto-generated method stub
		return 0;
	}
}
