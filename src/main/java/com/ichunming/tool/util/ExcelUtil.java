/**
 * excel util
 * created 2016/09/07
 * by ming
 */
package com.ichunming.tool.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelUtil {
	private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
	
	// private constructor
	private ExcelUtil() {}

	public static HSSFWorkbook wb;
	
	public static Sheet sheet;
	
	// usable index
	private static final short[] indexs = {0x19, 0x1b, 0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27};
	
	private static int cursor = -1;
	
	public static void setTarget(HSSFWorkbook wb, Sheet sheet) {
		ExcelUtil.wb = wb;
		ExcelUtil.sheet = sheet;
		wb.setActiveSheet(wb.getSheetIndex(sheet));
	}
	
	/**
	 * Excel写入一行
	 * @param sheet
	 * @param line
	 * @param recode
	 */
	public static void writeLine(int line, Map<Integer, String> recode) {
		// 创建row
		sheet.createRow(line);
		// 单元格设值
		for(int column : recode.keySet()) {
			writeCell(line, column, recode.get(column));
		}
	}
	
	/**
	 * Excel写入单元格
	 * @param sheet
	 * @param rownum
	 * @param column
	 * @param value
	 */
	public static void writeCell(int rownum, int column, String value) {
		Cell cell = sheet.getRow(rownum).createCell(column);
		cell.setCellValue(value);
	}
	
	/**
	 * 设置行属性
	 * @param sheet
	 * @param line
	 * @param style
	 */
	public static void setRowStyle(int line, CellStyle style) {
		// 取得Row
		Row row = sheet.getRow(line);
		// 设置属性
		for(int column = row.getFirstCellNum(); column < row.getLastCellNum(); column++) {
			setCellStyle(line, column, style);
		}
	}
	
	/**
	 * 设置单元格属性
	 * @param sheet
	 * @param line
	 * @param column
	 * @param style
	 */
	public static void setCellStyle(int line, int column, CellStyle style) {
		// 取得单元格
		Cell cell = sheet.getRow(line).getCell(column);
		// 设置单元格属性
		cell.setCellStyle(style);
	}
	
	/**
	 * 设置列宽
	 * @param sheet
	 * @param column
	 * @param width
	 */
	public static void setColumnWidth(int column, int width) {
		sheet.setColumnWidth(column, 256 * width);
	}
	
	/**
	 * 设置默认列宽
	 * @param width
	 */
	public static void setDefaultColumnWidth(int width) {
		sheet.setDefaultColumnWidth(width);
	}
	
	/**
	 * 设置列宽
	 * @param sheet
	 * @param columns
	 * @param widths
	 */
	public static void setColumnsWidth(int[] widths) {
		for(int i = 0; i < widths.length; i++) {
			setColumnWidth(i, widths[i]);
		}
	}
	
	/**
	 * 设置列宽
	 * @param sheet
	 * @param column
	 * @param width
	 */
	public static void setColumnsWidth(int[] columns, int[] widths) {
		for(int i = 0; i < columns.length; i++) {
			setColumnWidth(columns[i], widths[i]);
		}
	}
	
	/**
	 * 设置行高
	 * @param sheet
	 * @param line
	 * @param height
	 */
	public static void setRowHeight(int line, int height) {
		sheet.getRow(line).setHeight((short)height);
	}
	
	/**
	 * 创建默认标题样式
	 * @param wb
	 * @return
	 */
	public static CellStyle createDefaultHeadStyle() {
		Font font = wb.createFont();
		font.setColor(Font.COLOR_NORMAL);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		// 字体：加粗
		CellStyle headStyle= wb.createCellStyle();
		headStyle.setFont(font);
		// 居中
		headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 自动换行
		headStyle.setWrapText(true);
		return headStyle;
	}

	/**
	 * 创建有背景色的样式
	 * @param wb
	 * @param red
	 * @param green
	 * @param blue
	 * @return
	 */
	public static CellStyle createStyleWithBGC(int red, int green, int blue) {
		// color 0~255
		red = red < 0 ? 0 : red;
		red = red > 255 ? 255 : red;
		green = green < 0 ? 0 : green;
		green = green > 255 ? 255 : green;
		blue = blue < 0 ? 0 : blue;
		blue = blue > 255 ? 255 : blue;
		
		CellStyle style = wb.createCellStyle();
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		HSSFPalette palette = wb.getCustomPalette();
		short index = getNextIndex();
		logger.debug("" + index);
	    palette.setColorAtIndex(index, (byte) red, (byte) green, (byte) blue);
	    style.setFillForegroundColor(index);
	    
	    return style;
	}
	
	/**
	 * 设置自动换行
	 * @param style
	 */
	public static void setWrapText(CellStyle style) {
		style.setWrapText(true);
	}
	
	/**
	 * 设置边框样式
	 * @param style
	 * @param border
	 */
	public static void setBorder(CellStyle style, short border, short color) {
		style.setBorderTop(border);
	    style.setBorderRight(border);
	    style.setBorderBottom(border);
	    style.setBorderLeft(border);
	    
	    style.setTopBorderColor(color);
	    style.setRightBorderColor(color);
	    style.setBottomBorderColor(color);
	    style.setLeftBorderColor(color);
	}
	
	/**
	 * 创建单元格样式
	 * @return
	 */
	public static CellStyle createStyle() {
		return wb.createCellStyle();
	}

	/**
	 * 设置字体样式
	 * @param style
	 * @param color
	 * @param weigth
	 */
	public static void setFont(CellStyle style, short color, short weigth) {
		Font font = wb.createFont();
		// 字体
		font.setColor(color);
		font.setBoldweight(weigth);
		style.setFont(font);
	}
	
	/**
	 * 合并单元格
	 * @param sheet
	 * @param firstRow
	 * @param lastRow
	 * @param firstCol
	 * @param lastCol
	 */
	public static void mergeRegion(int firstRow, int lastRow, int firstCol, int lastCol) {
		CellRangeAddress region = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
		sheet.addMergedRegion(region);
	}
	
	/**
	 * excel文件内容转换成对象list
	 * @param sheet
	 * @param clz
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static <T> List<T> convertToList(Class<T> clz) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		List<T> result = null; // result
		T obj = null; // object
		
		int rowFrom = sheet.getFirstRowNum() + 1;
		int rowTo = sheet.getLastRowNum();
		
		// target object information
		result = new LinkedList<T>();
		
		//convert to object
		for(int rownum = rowFrom; rownum <= rowTo; rownum++) {
			// new instance
			obj = convertToObj(clz, rownum);
			
			if(null == obj) {
				// convert fail
				logger.debug("convert fail[rownum:" + rownum + "]");
			}
			// add to list
			result.add(obj);
		}
		
		logger.debug("success to convert excel file to object list");
		return result;
	}
	
	/**
	 * excel中行记录转换成对象
	 * @param sheet
	 * @param clz
	 * @param rownum
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 */
	public static <T> T convertToObj(Class<T> clz, int rownum) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		T obj = null; // object
		Field[] fields = null; // fields
		String[] methods = null; // methods
		Row row = null; // row
		
		// excel information
		Row header = sheet.getRow(0);
		int colFrom = header.getFirstCellNum();
		int colTo = header.getLastCellNum();
		
		// target object information
		fields = clz.getDeclaredFields();
		methods = getMethods(fields, header);
		String param;
		
		//convert to object
		row = sheet.getRow(rownum);
		// new instance
		obj = clz.newInstance();
		for(int colnum = colFrom; colnum < colTo; colnum++) {
			// method
			Method method = clz.getMethod(methods[colnum], java.lang.String.class);
			// parameter
			switch(row.getCell(colnum).getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				// 数值型
				param = row.getCell(colnum).getNumericCellValue() + "";
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				// 布尔型
				param = row.getCell(colnum).getBooleanCellValue() ? "true" : "false";
				break;
			case Cell.CELL_TYPE_STRING:
				// 字符串型
				param = row.getCell(colnum).getStringCellValue();
				break;
			default:
				// 默认
				param = "";
			}
			// invoke
			method.invoke(obj, param);
		}
		
		logger.debug("success to convert excel file to object");
		return obj;
	}
	
	/**
	 * get methods
	 * @param fields
	 * @param header
	 * @param ignoreId
	 * @return
	 */
	private static String[] getMethods(Field[] fields, Row header) {
		// cloFrom, colTo
		int colFrom = header.getFirstCellNum();
		int colTo = header.getLastCellNum();
		
		String[] result = new String[colTo];
		StringBuilder sb = null;
		String property;
		String prefix = "set"; // prefix

		for(int colnum = colFrom; colnum < colTo; colnum++) {
			// header规则:类属性(大小写) + '_' + ' '混合
			property = header.getCell(colnum).getStringCellValue().trim().replace("_", "");
			for(Field field : fields) {
				if(!field.getName().toLowerCase().equals(property.toLowerCase())) {
					continue;
				} else {
					sb = new StringBuilder(field.getName());
					sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
					result[colnum] = prefix + sb.toString();
					break;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * color用，取得下一个index值
	 * @return
	 */
	private static short getNextIndex() {
		if(cursor < indexs.length - 1) {
			cursor++;
		} else {
			cursor = -1;
			return getNextIndex();
		}
		return indexs[cursor];
	}
}
