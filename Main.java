package application;
	
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;


public class Main extends Application {
	// desktop class to use function of native system
	private Desktop desktop = Desktop.getDesktop();
	//to save to choosen file
	String path ="";
	int choice = 5;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			//creates label with specified name
			final Label label1 = new Label("Rinkites tipà:");
			
			//ChoiceBox with list of choices, default is none.
			String[] options = {"Ermitaþas", "Bikuva/Statyk Pats", "Padaryk Pats"};
			final ChoiceBox<String> typeOfBarcode = new ChoiceBox(FXCollections.observableArrayList(options));
			
			typeOfBarcode.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>()
				{
					public void changed(ObservableValue ov, Number value, Number new_value)
					{
						choice = new_value.intValue();
					}
				
				});
			
			final FileChooser fileChooser = new FileChooser();
			
			final Button openButton = new Button ("Pasirinkite failà...");
			
			final Button printButton = new Button ("Spausdinti barkodus");
			
			//creates new label and text box to show user selected file
			final Label fileName = new Label("Jûsø pasirinktas failas:");
			final TextField filePath = new TextField();
			filePath.setPrefWidth(290);
			
			//associates button with event and on registering event opens fileChooser to choose file and saves files place.
			openButton.setOnAction(new EventHandler<ActionEvent>()
					{
						@Override
						public void handle (final ActionEvent e) {
							configureFileChooser(fileChooser);
							File file = fileChooser.showOpenDialog(primaryStage);
							if (file != null)
							{
								path = file.getAbsolutePath();
								filePath.setText(path);
							}
						}
					});
			
			printButton.setOnAction(new EventHandler<ActionEvent>()
					{
						@Override
						public void handle (final ActionEvent e) {
							printButton.setDisable(true);
							
							// task to print barcode
							Task<Void> barcodePrinting = new Task<Void>(){
								@Override protected Void call() throws Exception
									{
										// depending on choosen type prints barcodes
										scanFile f1 = new scanFile(path);
										switch(choice)
										{
										case 0:
											{
											f1.scanErmi(path);
											String[][] barcodes= new String[f1.counter][2];
											barcodes = f1.getArray();
											barcodeCounter f3 = new barcodeCounter();
											barcodes = f3.printAmount(barcodes);
			
											runtime f2 = new runtime();
											f2.RunBabyRun(barcodes, f1.counter+1);
											}
										break;
										case 1: 
											{
												f1.scanBik(path);
												String[][] barcodes= new String[f1.counter][2];
												barcodes = f1.getArray();
												runtime f2 = new runtime();
												f2.RunBabyRun(barcodes, f1.counter+1);								
											}
										break;
										case 2: 
											{
												f1.scanPadaryk(path);
												String[][] barcodes= new String[f1.counter][2];
												barcodes = f1.getArray();
												runtime f2 = new runtime();
												f2.RunBabyRun(barcodes, f1.counter+1);
											}
										break;
										default: System.out.println("Turite pasirinkti tipà!");
										break;
										}
										return null;
									}
							};
							//when task succeed then button is activated again.
							barcodePrinting.setOnSucceeded(event ->{
								printButton.setDisable(false);
							});
							// Start task in new thread
							new Thread (barcodePrinting).start();
						}
					});
			
			//creates grid for scene for components to be put
			GridPane grid = new GridPane();
			grid.setVgap(5);
			grid.setHgap(6);
			grid.setPadding(new Insets(5, 5, 5, 5));
			grid.add(label1, 0, 0);
			grid.add(typeOfBarcode, 1, 0);
			grid.add(openButton, 0, 2);
			grid.add(fileName, 0, 4);
			grid.add(filePath, 1, 4);
			grid.add(printButton, 0, 6);
	
			//creates new window
			Scene scene = new Scene(new Group(), 450, 200);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			Group root = (Group)scene.getRoot();
			root.getChildren().add(grid);
			
			//initiate scene with name.
			primaryStage.setScene(scene);
			primaryStage.setTitle("Barkodø Spausdinimas");
					
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		

	}

	// method to open file and checks for exception to get a user Level of "igaliojimu"
	protected void openFile(File file) {
		try {
			desktop.open(file);
		} catch (IOException ex) {
			Logger.getLogger(
				Main.class.getName()).log(
					Level.SEVERE, null, ex
				);
		}
		
	}

	// configure fileChooser to start at home directory and look for txt files.
	private static void configureFileChooser(final FileChooser fileChooser) {
		fileChooser.setTitle("Rinkites sàskaitos failà");
		fileChooser.setInitialDirectory(
				new File(System.getProperty("user.home"))
		);
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("TXT", "*.txt")
				);
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
