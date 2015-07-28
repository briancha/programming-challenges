/*
 * Time Clock: allows employees to clock in and clock out using their own 4-digit PIN. 
 * Records their start and end times on an Excel workbook, whose title is based on the month, year, and pay period.
 * 
 * The workbook is split into different sheets for each employee.
 * Each sheet totals up each employee's time worked and displays it.
 * 
 * Features: 
 * Program stores its data on spreadsheet when it is not running
 * Must continuously run once the first employee clocks in, until the last employee clocks out for the day 
 * Uses Apache POI (http://poi.apache.org/) to work with Excel
 */

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
// add apache library to eclipse: 
// http://stackoverflow.com/questions/605181/how-can-i-add-the-apache-poi-library-in-and-eclipse-project
// Apache POI: http://poi.apache.org/spreadsheet/quick-guide.html

public class TimeClock extends JFrame implements KeyListener {
	
	// JFrame elements
	JMenuBar heading = new JMenuBar();
	JPanel display = new JPanel();
	
	Font large = new Font("Arial", Font.BOLD, 30);
	Font medium = new Font("Arial", Font.BOLD, 15);
	
	JLabel title = new JLabel("Welcome to XYZ Company");
	JLabel description = new JLabel("Enter your PIN: ");
	JPasswordField pinField = new JPasswordField("", 8);
	JLabel welcome = new JLabel("");
	JLabel clockInOut = new JLabel("");
	
	// employees and their PINs
	String[] employees = {"John Smith", "Susie Fauske", "Saran Lusk", "Dennis Routt"};
	int[] pins = {1111, 2222, 3333, 4444};
	
	// # employees
	int size = employees.length;
	
	// stores start/end times and time worked in milliseconds
	long[] compStartTimes = new long[size];
	long[] compEndTimes = new long[size];
	long[] compTimeWorked = new long[size];
	
	// stores most recent row for each employee
	// for figuring out where to place the end time and time worked
	int[] mostRecentRow = new int[size];
	
	// uses more human readable formats to store start/end times (8:01 AM) and time worked (8h 32m)
	String[] humanStartTimes = new String[size];
	String[] humanEndTimes = new String[size];
	String[] humanTimeWorked = new String[size];
	long hours = 0, minutes = 0;
	
	// records the number of times each employee has clocked in and out
	// if even, they need to clock in
	// if odd, they need to clock out
	int[] numEntries = new int[size];
	
	// mm/dd/yy date format
	SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
	
	// date format with only the day
	SimpleDateFormat day = new SimpleDateFormat("dd");	
	
	// time format, i.e. 8:42 AM
    SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
    
    // date format: mm-yyyy
    // for names of files
    SimpleDateFormat fileFormat = new SimpleDateFormat("MM-yyyy");
    HSSFWorkbook workbook;
    HSSFSheet sheet;
    HSSFRow rowhead, row;
    Sheet timeSheet;
    FileOutputStream outputStream;
    File excelSpreadsheet;
    
    // allows you to limit textfield input to 4 characters
    // http://stackoverflow.com/questions/3519151/how-to-limit-the-number-of-characters-in-jtextfield
    class JTextFieldLimit extends PlainDocument {
	  private int limit;

	  JTextFieldLimit(int limit) {
	   super();
	   this.limit = limit;
	   }

	  public void insertString( int offset, String  str, AttributeSet attr ) throws BadLocationException {
	    if (str == null) return;

	    if ((getLength() + str.length()) <= limit) {
	      super.insertString(offset, str, attr);
	    }
	  }
	}    
    
    // TimeClock constructor
	public TimeClock() {
		// sets title and layout
		setTitle("Time Clock"); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout()); // layout
		
		setJMenuBar(heading); // sets top JMenuBar
		
		add(display); // JPanel
		// sets layout to grid
		display.setLayout(new GridLayout(5,1));
		
		// adds JLabels and sets font
		display.add(title);
		title.setFont(large);
		
		display.add(description);
		description.setFont(medium);
		
		// PIN text field
		display.add(pinField);
		pinField.addKeyListener(this);
		// limits input to 4 characters
		pinField.setDocument(new JTextFieldLimit(4));
     
		// adds welcome and clocked in or out
		display.add(welcome);
		display.add(clockInOut);
		
		// outputs a warning if the # employees is different from the # PINs
		if (employees.length != pins.length) {
			System.out.println("Make sure # employees is equal to # pins.");
		}
		
		long time = System.currentTimeMillis();
		
		// creates file based on the month and year, i.e. 07-2015.xls
		String fileName = fileFormat.format(time);
		
		// get the day of the month
		String dayOfMonthStr = day.format(time);
		int dayOfMonth = Integer.parseInt(dayOfMonthStr);
		
		System.out.println("Day of month " + dayOfMonth);
		
		// changes the name of the file based on whether it is the 1st pay period (1st - 15th) or 2nd (16th - 30th)
		if (dayOfMonth <= 15) {
			fileName += " (1st - 15th)";
		} else {
			fileName += " (16th - 30th)";
		}
		
		// file location
		fileName = "D:/Downloads/TimeSheets/" + fileName + ".xls";		
		excelSpreadsheet = new File(fileName);
		
		// If there is an existing Excel file, the program reads that file		
		if (excelSpreadsheet.exists()) {
			try {
				// Found this way to read existing file on: 
				// http://svn.apache.org/repos/asf/poi/trunk/src/examples/src/org/apache/poi/hssf/usermodel/examples/HSSFReadWrite.java
				// Found under "Reading or modifying an existing file": http://poi.apache.org/spreadsheet/how-to.html
				workbook = new HSSFWorkbook(new FileInputStream(fileName)); 
				// previous method using HSSFReadWrite.java: HSSFReadWrite.readFile(fileName);
				System.out.println("Reading existing file");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			// If there is no existing Excel spreadsheet
			if (!excelSpreadsheet.exists()) {
				// creates new workbook: http://stackoverflow.com/questions/1176080/create-excel-file-in-java 
				workbook = new HSSFWorkbook();
				
				// iterates through employees array
				for (int i = 0; i < employees.length; i++) {
					System.out.println("Creating new Excel spreadsheet");
					
					// creates a sheet in the workbook for every employee
					sheet = workbook.createSheet(employees[i]);
					System.out.println("Created new sheet for " + employees[i]);
					
					// creates the 1st row and sets the value of the 1st cell to the employee's name
					rowhead = sheet.createRow(0);
					rowhead.createCell(0).setCellValue(employees[i]);
					
					// merges cell A1 and B1 for the employee's name
					sheet.addMergedRegion(new CellRangeAddress(
				            0, //first row (0-based)
				            0, //last row  (0-based)
				            0, //first column (0-based)
				            1  //last column  (0-based)
				    ));
				
					// creates 2nd row
					row = sheet.createRow(1);
				
					// sets the value of the cells to Date, Start Time, End Time, Time Worked, and Time Worked (in ms)
					// auto sizes the Excel columns
					row.createCell(0).setCellValue("Date");
					sheet.autoSizeColumn(0);
	            
	            	row.createCell(1).setCellValue("Start");
	            	sheet.autoSizeColumn(1);
	            
	            	row.createCell(2).setCellValue("End");
	            	sheet.autoSizeColumn(2);
	            
	            	row.createCell(3).setCellValue("Total");
	            	sheet.autoSizeColumn(3);
	            	
	            	row.createCell(4).setCellValue("ms");
	            	sheet.autoSizeColumn(4);
	            	
	            	// makes column F wider to provide a buffer 
	            	sheet.setColumnWidth(5, 1000);
	            	
	            	// descriptors for total time worked
	            	sheet.getRow(0).createCell(6).setCellValue("Total (ms)");
	            	sheet.getRow(1).createCell(6).setCellValue("Total"); 	
				}
			}
			
			// writes these changes to the Excel spreadsheet
			outputStream = new FileOutputStream(excelSpreadsheet);
			workbook.write(outputStream);
			outputStream.close();
		} catch (IOException e) {}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// once a key is pressed, sets both welcome and clocked in or out label to invisible
		welcome.setVisible(false);
		clockInOut.setVisible(false);
		
		// stores key value that is entered
		int key = e.getKeyCode();
		
		// confirms that Enter key was pressed
		if (key == KeyEvent.VK_ENTER) {			
			
			// holds PIN that is entered
			char[] enteredChar = pinField.getPassword();
			// converts array of char to string
			String enteredString = new String(enteredChar);
			
			int enteredPin = -1;
			try {
				// parses the string for a number
				enteredPin = Integer.parseInt(enteredString);
			} 
			
			// if the string cannot be converted to a number, prompts user to enter a valid PIN
			catch (NumberFormatException error) {
				welcome.setText("Please enter a valid PIN.");
				clockInOut.setText("");
			}
			
			boolean found = false;
			// for loop: iterates through the PINs array 
			for (int i = 0; i < pins.length; i++) {
				// if a match is found
				if (enteredPin == pins[i]) {
					found = true;					
					
					// says hello to that employee
					welcome.setText("Hello, " + employees[i] + ".");
					
					
					// find the worksheet that matches the employee's name
					sheet = workbook.getSheet(employees[i]);
					
					// gets the time
					long time = System.currentTimeMillis();
					// formats time into mm/dd/yy
					String date = dateFormat.format(time);
					
					// if the number of entries is even, assumes employee is clocking in
					if (numEntries[i] % 2 == 0) {
						
						// gets the current time in milliseconds since 1/1/1970, and stores it at the corresponding position in compStartTimes array
						compStartTimes[i] = time;
						// formats that time in a more readable format, i.e. 8:32 AM
						humanStartTimes[i] = timeFormat.format(time);
						// outputs that the employee has been clocked in, as well as the current time
						clockInOut.setText("You have been clocked in. It is " + humanStartTimes[i] + ".");
						
						// output to console: for debugging
						System.out.println(employees[i] + " clocked in " + humanStartTimes[i]);
						
						// finds an empty row to store the start time
						// saves this row value into an array, so the end time can be stored in the same row as well
						row = sheet.getRow(mostRecentRow[i]);
						while (row != null) {							
							mostRecentRow[i]++;
							row = sheet.getRow(mostRecentRow[i]);
						}
						
						try {
							// fills in the next empty row with the date and start time
							row = sheet.createRow(mostRecentRow[i]);
							row.createCell(0).setCellValue(date);
							row.createCell(1).setCellValue(humanStartTimes[i]);	
							
							// autosize newly filled columns
							sheet.autoSizeColumn(0);
							sheet.autoSizeColumn(1);							
							
							// writes these changes to the spreadsheet
							outputStream = new FileOutputStream(excelSpreadsheet);
							workbook.write(outputStream);
							outputStream.close();
						} catch (FileNotFoundException e1) {
							welcome.setText("Please close the Excel time sheet for this pay period.");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 
					} 
					
					// if the number of entries is odd, assumes employee is clocking out
					else {
						// gets the current time in milliseconds since 1/1/1970, and stores it at the corresponding position in compEndTimes array
						compEndTimes[i] = time;
						// formats that time in a more readable format, i.e. 8:32 AM
						humanEndTimes[i] = timeFormat.format(time);
						// outputs that the employee has been clocked out, as well as the current time
						clockInOut.setText("You have been clocked out. It is " + humanEndTimes[i] + ".");
						// output to console: for debugging
						System.out.println(employees[i] + " clocked out " + humanEndTimes[i]);
						
						// calculates time worked in milliseconds: end time - start time, and stores it at the corresponding position in compTimeWorked array
						compTimeWorked[i] = compEndTimes[i] - compStartTimes[i];
						// formats this number of milliseconds into hours and minutes, and stores it at the corresponding position in humanTimeWorked array
						// http://stackoverflow.com/questions/625433/how-to-convert-milliseconds-to-x-mins-x-seconds-in-java
						int minutes = (int) ((compTimeWorked[i] / (1000*60)) % 60);
						int hours   = (int) ((compTimeWorked[i] / (1000*60*60)) % 24);
						humanTimeWorked[i] = String.format("%dh %dm", hours, minutes);
						
						try {
							// gets the most recently used row (for start time)
							row = sheet.getRow(mostRecentRow[i]);
							// stores end time, time worked, and time worked (in ms) for later calculation
							row.createCell(2).setCellValue(humanEndTimes[i]);
							row.createCell(3).setCellValue(humanTimeWorked[i]);						
							row.createCell(4).setCellValue(compTimeWorked[i]);							
							
							// sets formula totaling the number of milliseconds worked in cell H1
							// setCellFormula: http://poi.apache.org/spreadsheet/formula.html
							Cell totalMs = sheet.getRow(0).createCell(7);
							totalMs.setCellFormula("sum(E:E)");
							
							// evaluate the value of a cell that has a formula (in this case, cell H1)
							// in order to get the total milliseconds and convert it to hours and minutes
							// http://poi.apache.org/spreadsheet/eval.html
							FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
							CellValue totalMillisCell = evaluator.evaluate(totalMs);
							double totalMillis = totalMillisCell.getNumberValue();
							System.out.println("Sum: " + totalMillis);
							
							// gets # minutes and hours from total milliseconds
							minutes = (int) ((totalMillis / (1000*60)) % 60);
							hours   = (int) ((totalMillis / (1000*60*60)) % 24);
							
							// sets cell H2 to a string with # hours and minutes, i.e. 20h 23m
							sheet.getRow(1).createCell(7).setCellValue(hours + "h " + minutes + "m");
							
							// autosize newly filled columns
							sheet.autoSizeColumn(2);
							sheet.autoSizeColumn(3);
							sheet.autoSizeColumn(4);
							sheet.autoSizeColumn(7);

							// writes this new information to the spreadsheet
							outputStream = new FileOutputStream(excelSpreadsheet);
							workbook.write(outputStream);
							outputStream.close();
						} catch (FileNotFoundException e1) {
							welcome.setText("Please close the Excel time sheet for this pay period.");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						
						
						// output to console arrays the start/end times and time worked. for debugging
						System.out.println("Start " + humanStartTimes[i] + ", " + compStartTimes[i]);						
						System.out.println("End " + humanEndTimes[i] + ", " + compEndTimes[i]);
						System.out.println("Time Worked " + humanTimeWorked[i] + ", " + compTimeWorked[i]);
					}
					
					// increments # entries that specific employee has made
					numEntries[i]++;
				}
			}

			// if what was entered does not match any of the PINs
			// prompts user to enter a valid PIN
			if (!found) {
				welcome.setText("Please enter a valid PIN.");
				clockInOut.setText("");
			}
			
			// removes all text from PIN field
			pinField.setText("");
			
			// sets welcome and clocked in or out message to visible
			welcome.setVisible(true);
			clockInOut.setVisible(true);
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
	
	public static void main(String[] args) {
		// creates object, sets size and visibility
		TimeClock window = new TimeClock();
		window.setSize(500, 500);
		window.setVisible(true);
	}

}
