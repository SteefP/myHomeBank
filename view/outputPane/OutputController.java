package view.outputPane;

import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;

import controller.BankControllerInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import model.Category;
import model.SubCategory;
import view.FXML.MainController;
import view.FXML.MockCategory;

public class OutputController implements Initializable {
	public BankControllerInterface controller;
	
	@FXML DatePicker dpChartStart;
	@FXML DatePicker dpChartEnd;
	
	public void addBankController(BankControllerInterface controller){
		this.controller = controller;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("initializing main controller");
			initDateValues();
		
		
	}
	public void initDateValues(){
		dpChartStart.setValue(LocalDate.of(2016, 6, 1));
		dpChartEnd.setValue(LocalDate.of(2017, 6, 1));
	}
	
	@FXML StackedBarChart sBChart;

	public void initchart(){

		LocalDate chartStart = dpChartStart.getValue();
		LocalDate chartEnd = dpChartEnd.getValue();

		sBChart.getData().clear();
		ObservableList<XYChart.Series<String, Number>> dataNumber = FXCollections.observableArrayList();

		for (Category c : controller.getCategories()){
			if(c.getDebetOrCredit().equals("D")){
				XYChart.Series<String, Number> tempSeries = new XYChart.Series<String, Number>();
				tempSeries.setName(c.getName());


				for (LocalDate i = chartStart; i.isBefore(chartEnd); i=i.plusMonths(1)){

					tempSeries.getData().add(
							new XYChart.Data<String, Number>
							(i.toString(),controller.categoryTotal
									(c.getName(), "D", 
											LocalDate.of(i.getYear(), i.getMonth() , 1), 
											LocalDate.of(i.plusMonths(1).getYear(), i.getMonth().plus(1) , 1))));

				}
				dataNumber.add(tempSeries);
			}
		}

		sBChart.getData().addAll(dataNumber);

		initchartnormal();
		initOutputTable();
	}
	
	@FXML BarChart bChart;

	public void initchartnormal(){  // to be adjusted 
		LocalDate chartStart = dpChartStart.getValue();
		LocalDate chartEnd = dpChartEnd.getValue();

		bChart.getData().clear();

		ObservableList<XYChart.Series<String, Number>> dataNumber = FXCollections.observableArrayList();

		XYChart.Series<String, Number> tempSeries1 = new XYChart.Series<String, Number>();
		XYChart.Series<String, Number> tempSeries2 = new XYChart.Series<String, Number>();
		tempSeries1.setName("Credit"); 
		tempSeries2.setName("Debet");

		for (LocalDate i = chartStart; i.isBefore(chartEnd); i=i.plusMonths(1)){
			System.out.println("C start date: "+LocalDate.of(i.getYear(), i.getMonth() , 1)+ "end date: "+LocalDate.of(i.plusMonths(1).getYear(), i.getMonth().plus(1) , 1));
			tempSeries1.getData().add(
					new XYChart.Data<String, Number>
					(i.toString(),controller.generalTotal("C", 
							LocalDate.of(i.getYear(), i.getMonth() , 1), 
							LocalDate.of(i.plusMonths(1).getYear(), i.getMonth().plus(1) , 1))));
			
			
		}
		dataNumber.add(tempSeries1);
		for (LocalDate i = chartStart; i.isBefore(chartEnd); i=i.plusMonths(1)){
			System.out.println("D start date: "+LocalDate.of(i.getYear(), i.getMonth() , 1)+ "end date: "+LocalDate.of(i.plusMonths(1).getYear(), i.getMonth().plus(1) , 1));
			tempSeries2.getData().add(
					new XYChart.Data<String, Number>
					(i.toString(), controller.generalTotal("D", 
							LocalDate.of(i.getYear(), i.getMonth() , 1), 
							LocalDate.of(i.plusMonths(1).getYear(), i.getMonth().plus(1) , 1))));
		}
		dataNumber.add(tempSeries2);


		bChart.getData().addAll(dataNumber);
	}

	public void about(){

	}
/*
	public void ruleCheckboxChecked(){    // nog afmaken met vervolg
		if(state != state.BACKSEARCH){
			state = state.ASSIGNFEEDBACK;
			refreshList();
		}
	}
	*/
	
	@FXML public TreeTableView<MockCategory> outputTable;
	@FXML public TreeTableColumn<MockCategory,String> outputCol1;
	@FXML public TreeTableColumn<MockCategory,Number> outputCol2;
	TreeItem<MockCategory> outputRoot;
	
	
	public void initOutputTable(){
		outputCol1.setCellValueFactory(   
				(TreeTableColumn.CellDataFeatures<MockCategory, String> param)   -> param.getValue().getValue().getName());
		outputCol2.setCellValueFactory(   
				(TreeTableColumn.CellDataFeatures<MockCategory, Number> param)   -> param.getValue().getValue().getAmount());
		
		
		refreshOutputTable();
		initOutputTableC();
	}

	private void refreshOutputTable() {
		LocalDate tableStart = dpChartStart.getValue();
		LocalDate tableEnd = dpChartEnd.getValue();
		Period p = Period.between(tableStart, tableEnd);
		int months = p.getMonths()+ p.getYears()*12;
		int tempGenTotal = (int )controller.generalTotal("C", tableStart, tableEnd)/months;


		outputRoot= new TreeItem<>(new MockCategory( tempGenTotal,"Inkomsten"));


		for(Category c : controller.getCategories()){

			if(c.getdebetOrCredit().equals("C")){
				int tempCAmount = (int) controller.categoryTotal(c.getName(), "C", tableStart, tableEnd)/months;
				TreeItem<MockCategory> tempCTreeItem= new TreeItem<MockCategory>(new MockCategory(tempCAmount, c.getName()));
				for(SubCategory sc : c.getSubCategories()){
					int tempScAmount = (int) controller.subCategoryTotal(sc.getName(), tableStart, tableEnd)/months;




					tempCTreeItem.getChildren().add(new TreeItem<MockCategory>(new MockCategory(tempScAmount, sc.getName())));
				}

				outputRoot.getChildren().add(tempCTreeItem );
			}

		}
		outputTable.setRoot(outputRoot);
		outputRoot.setExpanded(true);
	}
	
	@FXML public TreeTableView<MockCategory> outputTableC;
	@FXML public TreeTableColumn<MockCategory,String> outputColC1;
	@FXML public TreeTableColumn<MockCategory,Number> outputColC2;
	TreeItem<MockCategory> outputRootC;
	
	
	public void initOutputTableC(){
		outputColC1.setCellValueFactory(   
				(TreeTableColumn.CellDataFeatures<MockCategory, String> param)   -> param.getValue().getValue().getName());
		outputColC2.setCellValueFactory(   
				(TreeTableColumn.CellDataFeatures<MockCategory, Number> param)   -> param.getValue().getValue().getAmount());
		
		
		refreshOutputTableC();
	}
	
	private void refreshOutputTableC() {
		LocalDate tableStart = dpChartStart.getValue();
		LocalDate tableEnd = dpChartEnd.getValue();
		Period p = Period.between(tableStart, tableEnd);
		int months = p.getMonths()+ p.getYears()*12;
		int tempGenTotal = (int )controller.generalTotal("D", tableStart, tableEnd)/months;
			
		
			outputRoot= new TreeItem<>(new MockCategory( tempGenTotal,"Uitgaven"));
		
		
		for(Category c : controller.getCategories()){
		
				if(c.getdebetOrCredit().equals("D")){
					int tempCAmount = (int) controller.categoryTotal(c.getName(), "D", tableStart, tableEnd)/months;
					
					
					
					TreeItem<MockCategory> tempCTreeItem= new TreeItem<MockCategory>(new MockCategory(tempCAmount, c.getName()));
					
					
					for(SubCategory sc : c.getSubCategories()){
						int tempScAmount = (int) controller.subCategoryTotal(sc.getName(), tableStart, tableEnd)/months;
						
						
						
						
						tempCTreeItem.getChildren().add(new TreeItem<MockCategory>(new MockCategory(tempScAmount, sc.getName())));
					}
					
					outputRoot.getChildren().add(tempCTreeItem );
				}
			
		}
		outputTableC.setRoot(outputRoot);
		outputRoot.setExpanded(true);
		initchart3();
	}
	
	@FXML SplitPane OutputsSplit;


	private boolean family = true;
	@FXML RadioButton btnSingle;
	@FXML RadioButton btnCouple;
	@FXML RadioButton btnFamily;
	
	public ToggleGroup nibudChoice;
	
	@FXML StackedBarChart NChart;
	
	public void initchart3(){
		nibudChoice = new ToggleGroup();
		btnSingle.setToggleGroup(nibudChoice);
		btnCouple.setToggleGroup(nibudChoice);
		btnFamily.setToggleGroup(nibudChoice);
		
		LocalDate chartStart = dpChartStart.getValue();
		LocalDate chartEnd = dpChartEnd.getValue();
		Period p = Period.between(chartStart, chartEnd);
		int months = p.getMonths()+ p.getYears()*12;

		NChart.getData().clear();
		int [] nibud = {660,156,163,103,0,0,165,56,98,44,88,271};
		
		
		int woning =0; 
		int Energie=156; 
		int Verzekeringen=163; 
		int Abonnementen=103; 
		int Onderwijs=0; 
		int Overige_vaste_lasten=0; 
		int Vervoer=165; 
		int Kleding=56; 
		int Inventaris=98; 
		int Niet_vergoede_ziektekosten=44; 
		int Vrijetijdsuitgaven=88; 
		int Huishoudelijk_uitgaven=271; 
		
		if(family){
			nibud[0] =800; 
			nibud[1]=197; 
			nibud[2]=315; 
			nibud[3]=158; 
			nibud[4]=4; 
			nibud[5]=0; 
			nibud[6]=164; 
			nibud[7]=152; 
			nibud[8]=132; 
			nibud[9]=99; 
			nibud[10]=126; 
			nibud[11]=539; 
		}
		
		
		ObservableList<XYChart.Series<String, Number>> dataNumber = FXCollections.observableArrayList();

		for (Category c : controller.getCategories()){
			if(c.getDebetOrCredit().equals("D")){
				XYChart.Series<String, Number> tempSeries = new XYChart.Series<String, Number>();
				tempSeries.setName(c.getName());
				for (int i=0; i<26; i++){
					
					if(i>0 && i<13){

						if(c.getName().equals(controller.getCategories().get(i+6).getName())){

							tempSeries.getData().add(
									new XYChart.Data<String, Number>
									(c.getName(), (controller.categoryTotal(c.getName(), "D", chartStart,chartEnd)/months)-nibud[i-1]) 
									);
						}
					}
				}
				dataNumber.add(tempSeries);
			}
		}
		NChart.getData().addAll(dataNumber);
		initchart4();
		
	}
@FXML StackedBarChart NChart2;
	
	public void initchart4(){
		
		LocalDate chartStart = dpChartStart.getValue();
		LocalDate chartEnd = dpChartEnd.getValue();
		Period p = Period.between(chartStart, chartEnd);
		int months = p.getMonths()+ p.getYears()*12;

		NChart2.getData().clear();
		
		
		int [] nibud = {600,156,163,103,0,0,165,56,98,44,88,271};
		if(family){
			nibud[0] =800; 
			nibud[1]=197; 
			nibud[2]=315; 
			nibud[3]=158; 
			nibud[4]=4; 
			nibud[5]=0; 
			nibud[6]=164; 
			nibud[7]=152; 
			nibud[8]=132; 
			nibud[9]=99; 
			nibud[10]=126; 
			nibud[11]=539; 
		}


		ObservableList<XYChart.Series<String, Number>> dataNumber = FXCollections.observableArrayList();
		int i = 0;
		for (Category c : controller.getCategories()){
			if(c.getDebetOrCredit().equals("D")){
				XYChart.Series<String, Number> tempSeries = new XYChart.Series<String, Number>();
				XYChart.Series<String, Number> tempSeries2 = new XYChart.Series<String, Number>();
				if (i<12) {
					tempSeries.getData().add(
							new XYChart.Data<String, Number>
							("Nibud", nibud[i]));
					System.out.println(nibud[i]);
					tempSeries.getData().add(
							new XYChart.Data<String, Number>
							("You", controller.categoryTotal(c.getName(), "D", chartStart,chartEnd)/months) 
							);
					
					dataNumber.add(tempSeries);
				}
				
				i++;
			}
		}
		NChart2.getData().addAll(dataNumber);

	}
	
	public void radioToggled(ActionEvent e){
		if(nibudChoice.getSelectedToggle()== btnFamily){
			family = true;
		} else {
			family = false;
		}
		initchart3();
			
	}





	
	
	
}
