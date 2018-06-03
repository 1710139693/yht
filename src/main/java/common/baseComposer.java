package common;

/**
 * Created by 秋月 on 2018/5/22.
 */
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;


public class baseComposer {

    public static String proj_path = System.getProperty("user.dir");
    public static String logFileAddr = proj_path + "/log/";
    public static String picFileAddr=proj_path + "/pic";
    public static String loginFileAddr=proj_path + "/loginInfo.xlsx";
    public static String goodsIDFileAddr=proj_path+"/goods.xlsx";
    public static String goodsUrlBase="https://www.1haitao.com/details/";
    public static String yhtURL="https://www.1haitao.com";

    /**
     * 打开浏览器,输入请求地址
     * @param url
     * @return
     * @throws InterruptedException
     */
    public  WebDriver openBrowserWithUrl(String url) throws InterruptedException{
        WebDriver driver=openBrowser();
        // 让浏览器访问 url
        driver.get(url);
        Thread.sleep(1000);
        return driver;
    }

    public  WebDriver openBrowser() throws InterruptedException{
        //获取浏览器cookie
        /*HttpServletRequest request = ServletActionContext.getRequest();
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for(Cookie cookie : cookies){
                System.out.println("name:"+cookie.getName()+", value:"+ cookie.getValue());
            }
        }*/

        //System.setProperty("webdriver.gecko.driver", geckodriver);

        // 如果你的 FireFox 没有安装在默认目录，那么必须在程序中设置
        //System.setProperty("webdriver.firefox.bin","D:\\Program Files\\Mozilla Firefox\\firefox.exe");

        // 创建一个 FireFox 的浏览器实例
        WebDriver driver = new FirefoxDriver();
        //最大化浏览器
        driver.manage().window().maximize();
        Thread.sleep(1000);
        return driver;
    }

    /**
     * 判断元素/对象是否存在
     * @param driver
     * @param selector
     * @return
     */
    public boolean isElementExsit(WebDriver driver, By selector, String res_txt) {
        boolean flag = false;
        try {
            WebElement element = driver.findElement(selector);
            flag = null != element;
            if (flag) {
                rwFile("元素/对象", res_txt, "存在");
            }else {
                rwFile("元素/对象", res_txt, "不存在");
            }
        } catch (Exception e) {
            rwFile("元素/对象", "抛异常", selector.toString());
        }
        return flag;
    }

    /**
     * 写入日志
     * @param cases
     * @param message
     * @param value
     */
    public void rwFile(String cases, String message, String value) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(logFileAddr + getFormatDateYMD(new Date()) + ".log", true);
            String valuestring = String.valueOf(value);
            fw.write(cases + " "+ message + " " + valuestring + "\r\n");
            fw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 时间格式化
     * @param date
     * @return
     */
    public String getFormatDateHms(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        return sdf.format(cl.getTime());
    }
    public String getFormatDateYMD(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        return sdf.format(cl.getTime());
    }
    public String getFormatDateYMDHMS(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        return sdf.format(cl.getTime());
    }


    /**
     * 读取Excel信息
     * @param filepath
     * @return
     */
    public static String[] readExcelInfo(String filepath) {

        String[] cells=null;
        Workbook workbook = null;
        try {
            File loginFile=new File(filepath);
            String fileName=loginFile.getName();
            //String prefix=fileName.substring(fileName.lastIndexOf(".")+1);
            InputStream instream = new FileInputStream(filepath);

            if(null==loginFile){
                System.out.println("===========文件不存在");
                throw new FileNotFoundException("文件不存在");

            }else if(fileName.endsWith("xls")) {
                workbook=new HSSFWorkbook(instream);

            }else if(fileName.endsWith("xlsx")){
                workbook=new XSSFWorkbook(instream);

            }else{
                System.out.println("========prefix=========不是Excel文件");
            }

            if(workbook != null) {
                for(int sheetNum = 0;sheetNum < workbook.getNumberOfSheets();sheetNum++) {
                    Sheet sheet = workbook.getSheetAt(0);
                    if(sheet == null){
                        continue;
                    }
                    //获得当前sheet的开始行
                    int firstRowNum = sheet.getFirstRowNum();
                    //获得当前sheet的结束行
                    int lastRowNum = sheet.getLastRowNum();

                    cells = new String[lastRowNum+1];
                    for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
                        Row row = sheet.getRow(rowNum);
                        if (row == null) {
                            continue;
                        }

                        Cell cell = row.getCell(0);
                        cells[rowNum] = getCellValue(cell);

                        /*//获得当前行的开始列
                        int firstCellNum = row.getFirstCellNum();
                        //获得当前行的列数
                        int lastCellNum = row.getPhysicalNumberOfCells();
                        //cells = new String[row.getPhysicalNumberOfCells()];
                        for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {

                            Cell cell = row.getCell(cellNum);
                            cells[cellNum] = getCellValue(cell);
                        }*/
                    }
                }
            }
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cells;
    }

    public static String getCellValue(Cell cell){
        String cellValue = "";
        if(cell == null){
            return cellValue;
        }
        //把数字当成String来读，避免出现1读成1.0的情况
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
        //判断数据的类型
        switch (cell.getCellType()){
            case Cell.CELL_TYPE_NUMERIC: //数字
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING: //字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA: //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_BLANK: //空值
                cellValue = "";
                break;
            case Cell.CELL_TYPE_ERROR: //故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }

}