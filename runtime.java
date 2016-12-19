import java.lang.Runtime;
import java.io.IOException;
import java.lang.Process;



public class runtime {

	public void RunBabyRun (){
		Runtime cmd = Runtime.getRuntime();
		Process pr = null;
		try {
			pr = cmd.exec("C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int exitVal = 1;
		try {
			exitVal = pr.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("here u go"+exitVal);
	}
}
