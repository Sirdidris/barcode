package application;
import java.lang.Runtime;
import java.io.IOException;
import java.lang.Process;
import java.util.concurrent.TimeUnit;



public class runtime {

	public void RunBabyRun (String[][] barcodes, int arrayLength)
	{
		Runtime cmd = Runtime.getRuntime();
		Process pr = null;
		String path = new String ("C:\\Program Files\\Seagull\\BarTender Ultralite\\BarTend.exe ");
		for (int i = 0; i <arrayLength; i++)
		{
			//System.out.println("\"C:\\Program Files\\Seagull\\BarTender Ultralite\\Bartend.exe\" /F=C:\\"+barcodes[i][0]+".btw /C="+barcodes[i][1]+" /P");
			try {
				pr = cmd.exec(path+"/F=C:\\barkodai\\"+barcodes[i][0]+".btw /C="+barcodes[i][1]+" /P");
				try {
					TimeUnit.MILLISECONDS.sleep(7000+100*Long.parseLong(barcodes[i][1]));
				}
				catch (InterruptedException e){
					System.out.println("Sleep was interupted!");
				}
			} catch (IOException e) {
				System.out.println("Wrong path");
			}
		}
		
		try {
			pr = cmd.exec(path+" /CLOSE");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int exitVal = 1;
		try {
			exitVal = pr.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
