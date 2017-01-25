package application;
import java.lang.Runtime;
import java.io.IOException;
import java.lang.Process;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;



public class runtime {

	public void RunBabyRun (Barcode barcodes)
	{
		Runtime cmd = Runtime.getRuntime();
		Process pr = null;
		String path = new String ("C:\\Program Files\\Seagull\\BarTender Ultralite\\BarTend.exe ");
			//System.out.println("\"C:\\Program Files\\Seagull\\BarTender Ultralite\\Bartend.exe\" /F=C:\\"+barcodes.getName()+".btw /C="+barcodes.getAmount()+" /P");
			try {
				pr = cmd.exec(path+"/F=C:\\barkodai\\"+barcodes.getName()+".btw /C="+barcodes.getAmount()+" /P");
				try {
					TimeUnit.MILLISECONDS.sleep(7000+100*(long)barcodes.getAmount());
				}
				catch (InterruptedException e){
					System.out.println("Sleep was interupted!");
				}
			} catch (IOException e) {
				System.out.println("Wrong path");
			}
		
		
		try {
			pr = cmd.exec(path+" /CLOSE");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			int exitVal = pr.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
