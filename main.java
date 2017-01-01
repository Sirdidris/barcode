import java.util.Scanner;

public class main {

	public static void main(String[] args) 
	{
		Scanner input = new Scanner (System.in);
		
		System.out.println("Specify your file name:");
		String fileName = input.next();
		scanFile f1 = new scanFile(fileName);
		System.out.println("ermi ar bik?");
		String next = input.next();
		if (next.equals("ermi"))
			{
				f1.scanErmi(fileName);
			}
		else
			{
				f1.scanBik(fileName);
			}
		long[][] barcodes= new long[f1.counter][2];
		barcodes = f1.getArray();
		barcodeCounter f3 = new barcodeCounter();
		barcodes = f3.printAmount(barcodes);
		
		runtime f2 = new runtime();
		f2.RunBabyRun(barcodes, f1.counter+1);
		
		input.close();
	}

}
