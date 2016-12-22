import java.lang.Runtime;
import java.io.IOException;
import java.lang.Process;



public class runtime {

	public void RunBabyRun (long[][] barcodes, int arrayLength)
	{
		Runtime cmd = Runtime.getRuntime();
		Process pr = null;
		String path = new String ("C:\\Program Files\\Seagull\\BarTender Ultralite\\BarTend.exe ");
		for (int i = 0; i <arrayLength; i++)
		{
			try {
				pr = cmd.exec(path+"/F=C:\\"+barcodes[i][0]+".btw /C="+barcodes[i][1]+" /P");
				//System.out.println("\"C:\\Program Files\\Seagull\\BarTender Ultralite\\Bartend.exe\" /F=C:\\"+barcodes[i][0]+".btw /C="+barcodes[i][1]+" /P");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
