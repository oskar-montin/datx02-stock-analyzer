	package util;

	import  java.io.*;

    import  org.apache.poi.hssf.usermodel.HSSFSheet;
    import  org.apache.poi.hssf.usermodel.HSSFWorkbook;
    import  org.apache.poi.hssf.usermodel.HSSFRow;
import  org.apache.poi.hssf.usermodel.HSSFCell;

    public class ExcelWriter{
    	HSSFWorkbook workbook;
        HSSFSheet sheet;
    	
    	public ExcelWriter() {
    		workbook=new HSSFWorkbook();
    		this.sheet=workbook.createSheet("FirstSheet");  
    	}
    	
    	public void addRow(Double[] input,int index) {
    		HSSFRow row=   sheet.createRow((short)index);
            row.createCell((short) 0).setCellValue(""+(index-1));
            short i = 1;
            for(Double e:input) {
            	row.createCell(i).setCellValue(e);
            	i++;
            }
    	}
    	
    	public void addRow(String[] input,int index) {
    		HSSFRow row=   sheet.createRow((short)index);
            row.createCell((short) 0).setCellValue("");
            short i = 1;
            for(String e:input) {
            	row.createCell(i).setCellValue(e);
            	i++;
            }
    	}
    	
    	public void save(String fileName) {
    		try{
	    		File file = new File(fileName);
	            FileOutputStream fileOut =  new FileOutputStream(file);
	            workbook.write(fileOut);
	            fileOut.close();
	            System.out.println("Your excel file has been generated!");
            } catch ( Exception ex ) {
                System.out.println(ex);

            }
    	}
    	
        public static void main(String[]args){
        	
        try{
        String filename="NewExcelFile.xls" ;
        HSSFWorkbook workbook=new HSSFWorkbook();
        HSSFSheet sheet =  workbook.createSheet("FirstSheet");  

        HSSFRow rowhead=   sheet.createRow((short)0);
        rowhead.createCell((short) 0).setCellValue("No.");
        rowhead.createCell((short) 1).setCellValue("Name");
        rowhead.createCell((short) 2).setCellValue("Address");
        rowhead.createCell((short) 3).setCellValue("Email");

        HSSFRow row=   sheet.createRow((short)1);
        row.createCell((short) 0).setCellValue("1");
        row.createCell((short) 1).setCellValue("Sankumarsingh");
        row.createCell((short) 2).setCellValue("India");
        row.createCell((short) 3).setCellValue("sankumarsingh@gmail.com");
        File file = new File(filename);
        FileOutputStream fileOut =  new FileOutputStream(file);
        workbook.write(fileOut);
        fileOut.close();
        System.out.println("Your excel file has been generated!");

        } catch ( Exception ex ) {
            System.out.println(ex);

        }
           }
       }