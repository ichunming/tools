/**
 * @author ming
 * @date 2017年3月6日 下午6:06:11
 */
package com.ichunming.tool.excel;

import java.io.FileInputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ichunming.tool.entity.SHLocation;
import com.ichunming.tool.util.ExcelUtil;

public class SHLocation2DB {
	private static final Logger logger = LoggerFactory.getLogger(SHLocation2DB.class);
	
	public static void main(String[] args) throws Exception {
		String filePath = "D:/Share/ksxt/SHLocation.xls";

		HSSFWorkbook wb = null; // Workbook
		POIFSFileSystem fs = null; // file
		Sheet sheet = null; // excel sheet
		
		// 文档打开
        fs = new POIFSFileSystem(new FileInputStream(filePath));
        // excel工作簿创建
        wb = new HSSFWorkbook(fs);
        // excel sheet取得
		sheet = wb.getSheetAt(0);
		// 设置ExcelUtil
		ExcelUtil.setTarget(wb, sheet);
		
		List<SHLocation> shLocations = ExcelUtil.convertToList(SHLocation.class);
		
        logger.debug("" + shLocations.size());
		
        logger.info("Success.");
	}
}
