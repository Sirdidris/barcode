package application;
	
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Callback;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class Main extends Application {
	
	// desktop class to use function of native system
	private Desktop desktop = Desktop.getDesktop();
	//to save to choosen file
	String path ="";
	int choice = 5;
	String[][] mainBarcode;
	boolean isButtonPressed = true;
	ObservableList<Barcode> myList;
	final double MAX_WIDTH = 130;
	
	List<Barcode> list = new ArrayList<Barcode>();
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			//creates label with specified name
			final Label label1 = new Label("Rinkites tipà:");
			
			//ChoiceBox with list of choices, default is none.
			String[] options = {"Ermitaþas", "Bikuva/Statyk Pats", "Padaryk Pats"};
			final ChoiceBox<String> typeOfBarcode = new ChoiceBox<>(FXCollections.observableArrayList(options));
			
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
			final Button stopPrint = new Button ("Sustabdyti!");
			final Button updateTable = new Button("Atnaujinti lentelæ");
			
			printButton.setMaxWidth(MAX_WIDTH);
			stopPrint.setMaxWidth(MAX_WIDTH);
			updateTable.setMaxWidth(MAX_WIDTH);
			
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
									mainBarcode = f3.printAmount(barcodes);
									}
								break;
								case 1: 
									{
										f1.scanBik(path);
										mainBarcode = f1.getArray();
									}
								break;
								case 2: 
									{
										f1.scanPadaryk(path);
										mainBarcode = f1.getArray();
									}
								break;
								default: System.out.println("Turite pasirinkti tipà!");
								break;
								}
							}
						}
					});

			//observable list to update table
			myList = FXCollections.observableArrayList(list);
			
			final TableView<Barcode> table = new TableView<Barcode>(myList);
			
			table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			
			table.setEditable(true);
			
			//first barcode and lets in a cell to input new information
			TableColumn<Barcode, String> firstColumn = new TableColumn<Barcode, String>("Barkodas");
			firstColumn.setMinWidth(100);
			firstColumn.setMaxWidth(150);
			firstColumn.setSortable(false);
			firstColumn.setCellValueFactory(new PropertyValueFactory<Barcode, String>("name"));
			firstColumn.setCellFactory(TextFieldTableCell.forTableColumn());
			firstColumn.setOnEditCommit (
					new EventHandler<CellEditEvent<Barcode, String>>(){
						@Override
						public void handle(CellEditEvent<Barcode, String> t) {
							((Barcode) t.getTableView().getItems().get(
									t.getTablePosition().getRow())
									).setName(t.getNewValue());
						}
					}
					);
			
			//contains amount and let in cell input new information
			TableColumn<Barcode, Double> secondColumn = new TableColumn<Barcode, Double>("Kiekis");
			secondColumn.setMinWidth(50);
			secondColumn.setMaxWidth(50);
			secondColumn.setSortable(false);
			secondColumn.setCellValueFactory(new PropertyValueFactory<Barcode, Double>("amount"));
			secondColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
			secondColumn.setOnEditCommit(
					new EventHandler<CellEditEvent<Barcode, Double>>(){
						@Override
						public void handle(CellEditEvent<Barcode, Double> t) {
							((Barcode) t.getTableView().getItems().get(t.getTablePosition().getRow())
									).setAmount(t.getNewValue());
						}
					}
					);
			
			TableColumn<Barcode, Boolean> actionColumn = new TableColumn<Barcode, Boolean>("Trinti");
			actionColumn.setMinWidth(75);
			actionColumn.setMaxWidth(75);
			actionColumn.setSortable(false);
			actionColumn.setCellValueFactory(
					new Callback<TableColumn.CellDataFeatures<Barcode, Boolean>, 
	                ObservableValue<Boolean>>() {

	            @Override
	            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Barcode, Boolean> p) {
	                return new SimpleBooleanProperty(p.getValue() != null);
	            }
	});
			actionColumn.setCellFactory(
	                new Callback<TableColumn<Barcode, Boolean>, TableCell<Barcode, Boolean>>() {

	                    @Override
	                    public TableCell<Barcode, Boolean> call(TableColumn<Barcode, Boolean> p) {
	                        return new ButtonCell();
	                    }
	                
	        });
					
			table.setItems(myList);
			
			table.getColumns().addAll(firstColumn, secondColumn, actionColumn);
			
			updateTable.setOnAction(new EventHandler<ActionEvent>()
					{
						@Override
						public void handle (final ActionEvent e) {
							
							myList.clear();
							
							for (int i = 0; i < mainBarcode.length; i++)
							{
								Barcode temp = new Barcode(mainBarcode[i][0], Double.parseDouble(mainBarcode[i][1]));
								myList.add(temp);
							}
						}
					}
					);
			
			//new textfields to write information about new table line
			final TextField addBarcode = new TextField();
			addBarcode.setPromptText("Barcodas");
			addBarcode.setMaxWidth(100);
			final TextField addAmount = new TextField();
			addAmount.setPromptText("kiekis");
			addAmount.setMaxWidth(secondColumn.getPrefWidth());
			
			// button to add new data into Table
			final Button addButton = new Button("Pridëti");
			addButton.setOnAction(new EventHandler<ActionEvent>()
			{
				@Override
				public void handle (ActionEvent e) {
					myList.add((new Barcode(
							addBarcode.getText(),
							Double.parseDouble(addAmount.getText()))));
					addBarcode.clear();
					addAmount.clear();
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
										switch(choice)
										{
										case 0:
											{
												List<Barcode> forUsing = table.getItems();	
												runtime f2 = new runtime();
												int counter = 0;
												for ( int i = 0; i < forUsing.size(); i++)
												{
													//checks if button stop is pressed
													if (isButtonPressed)
													{
														f2.RunBabyRun(forUsing.get(i));
														counter++;
													}
												}
												//restarts the button stop so would work again
												isButtonPressed = true;
												printButton.setDisable(false);
												myList.remove(0, counter);
											}
										break;
										case 1: 
											{
												List<Barcode> forUsing = table.getItems();
												runtime f2 = new runtime();
												int counter = 0;
												for ( int i = 0; i < forUsing.size(); i++)
												{
													if (isButtonPressed)
													{
														f2.RunBabyRun(forUsing.get(i));
														counter++;
													}
												}
												isButtonPressed = true;
												printButton.setDisable(false);
												myList.remove(0, counter);						
											}
										break;
										case 2: 
											{
												List<Barcode> forUsing = table.getItems();
												runtime f2 = new runtime();
												int counter = 0;
												for ( int i = 0; i < forUsing.size(); i++)
												{
													if (isButtonPressed)
													{
														f2.RunBabyRun(forUsing.get(i));
														counter++;
													}
												}
												isButtonPressed = true;
												printButton.setDisable(false);
												myList.remove(0, counter);
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
			
			stopPrint.setOnAction(new EventHandler<ActionEvent>()
			{
				@Override
					public void handle (final ActionEvent e)
				{
					isButtonPressed = false;
				}
				
			});
			
			HBox hbox2 = new HBox(8);
			hbox2.getChildren().addAll(addBarcode, addAmount, addButton);
			
			VBox vbox = new VBox(15);
			vbox.getChildren().addAll(updateTable, printButton, stopPrint);
			
			//creates grid for scene for components to be put
			GridPane grid = new GridPane();
			grid.setVgap(5);
			grid.setHgap(6);
			grid.setPadding(new Insets(5, 5, 5, 5));
			grid.add(label1, 0, 0);
			grid.add(typeOfBarcode, 0, 2);
			grid.add(openButton, 1, 0);
			grid.add(fileName, 1, 2);
			grid.add(filePath, 1, 4);
			grid.add(hbox2, 0, 10);
			grid.add(table, 0, 8);
			grid.add(vbox, 1, 8);
			
			//creates new window
			Scene scene = new Scene(new Group(), 600, 650);
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
	
	public class ButtonCell extends TableCell<Barcode, Boolean> {
		final Button cellButton = new Button("Trinti");
		
		ButtonCell(){
			//when button is pressed
			cellButton.setOnAction(new EventHandler<ActionEvent>()
			{
				
				@Override
				public void handle(ActionEvent t)
				{
					Barcode currentBarcode = (Barcode) ButtonCell.this.getTableView().getItems().get(ButtonCell.this.getIndex());
					myList.remove(currentBarcode);
				}
			});
		}
			//If row is not empty dispaly button
			@Override
			protected void updateItem (Boolean item, boolean empty)
			{
				super.updateItem(item, empty);
				
				if (empty)
				{
					setText(null);
					setGraphic(null);
				}
				else
				{
					setGraphic(cellButton);
				}
			}

		}
	}
