/**
 * 通过建表语句（sql文）
 * 创建相应表头信息的Excel文件
 */
package com.ichunming.tool.excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ichunming.tool.util.ExcelUtil;

public class Sql2Excel {
	
	private static final Logger logger = LoggerFactory.getLogger(Sql2Excel.class);
	public static final String TABLE_NAME_REG = "`(.)*`";
	public static final String ITEM_NAME_REG = "`(.)*`";
	
	public static void main(String[] args) throws Exception {
		String sqlFilePath = "D:/doc/table_sql/sys_message.sql";
		String saveFilePath = "D:/doc/table_sql/sys_message.xls";
		
		File sqlFile = new File(sqlFilePath);
		BufferedReader br = new BufferedReader(new FileReader(sqlFile));
		
		String lineStr = null;
		String tblName = null;
		List<String> items = new ArrayList<String>();
		String item = null;
		HSSFWorkbook wb = null; // Workbook
		Sheet sheet = null; // excel sheet
		FileOutputStream fos = null; // 文档输出流
		Map<Integer, String> excelRecode = new HashMap<Integer, String>(); // excel文件行记录
		int line = 0; // 记录指针
		
		// 文档打开
        File saveFile = new File(saveFilePath);
        
        // excel工作簿创建
        wb = new HSSFWorkbook();
        // excel sheet创建
		sheet = wb.createSheet("tables");
		// 设置ExcelUtil
		ExcelUtil.setTarget(wb, sheet);
		
		// 表名单元格格式
		CellStyle tblStyle= ExcelUtil.createStyle();
		ExcelUtil.setFont(tblStyle, Font.COLOR_NORMAL, Font.BOLDWEIGHT_BOLD);
		
		// 项目名单元格格式
		CellStyle itemStyle = ExcelUtil.createStyleWithBGC(146, 208, 80);
	    ExcelUtil.setBorder(itemStyle, HSSFCellStyle.BORDER_THIN, HSSFColor.BLACK.index);
		// 设置列宽
		ExcelUtil.setDefaultColumnWidth(12);
		
		while(null != (lineStr = br.readLine())) {
			if(lineStr.toLowerCase().contains("create table")) {
				
				// clear
				items.clear();
				// pickup table name
				Matcher m = Pattern.compile(TABLE_NAME_REG).matcher(lineStr);
				if(m.find()) {
					tblName = lineStr.substring(m.start() + 1, m.end() - 1);
					logger.debug("TABLE: " + tblName);
				} else {
					logger.error("error occurred!!!");
					return;
				}
				
				// next N line, pickup items
				while(null != (lineStr = br.readLine()) && lineStr.trim().startsWith("`")) {
					m = Pattern.compile(ITEM_NAME_REG).matcher(lineStr);
					if(m.find()) {
						item = lineStr.substring(m.start() + 1, m.end() - 1);
						logger.debug(item);
						items.add(item);
					}
				}

				// 表名
				excelRecode.put(0, "TABLE");
				excelRecode.put(1, tblName);
				// 写入excel
				ExcelUtil.writeLine(line, excelRecode);
				// 设置标题属性
				ExcelUtil.setRowStyle(line, tblStyle);
				line++;
				excelRecode.clear();
				
				// 项目名
				for(int i = 0; i < items.size(); i++) {
					excelRecode.put(i, items.get(i));
				}
				// 写入excel
				ExcelUtil.writeLine(line, excelRecode);
				// 设置标题属性
				ExcelUtil.setRowStyle(line, itemStyle);
				line++;
				excelRecode.clear();
			}
		}
		
		// excel 保存
        logger.info("excel文档保存");
        fos = new FileOutputStream(saveFile);
        wb.write(fos);
        
        // 文档输出流关闭
        logger.info("文档输出流关闭");
        fos.close();
        br.close();
        
        logger.info("Success.");
	}
}
