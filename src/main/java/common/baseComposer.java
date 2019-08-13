package common;

/**
 * Created by 秋月 on 2018/5/22.
 */
import com.sun.xml.internal.ws.api.ha.StickyFeature;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;
import sun.rmi.runtime.Log;
import java.util.List;

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
        /*WebDriver driver = new ChromeDriver();
        System.setProperty("webdriver.chrome.driver","D:\\yhtWord\\IdeaProject\\chromedriver.exe");*/
        //最大化浏览器
        driver.manage().window().maximize();
        Thread.sleep(1000);
        return driver;
    }

    /**
     * 使用谷歌浏览器模拟wap测试
     * @throws InterruptedException
     */
    @Test
    public void initdriver() throws InterruptedException{
        String devicesName="Galaxy S5";
        System.setProperty("webdriver.chrome.driver","D:\\yhtWord\\IdeaProject\\chromedriver.exe");
        Map<String, String> mobileEmulation=new HashMap<String, String>();
        //设置设备
        mobileEmulation.put("deviceName",devicesName);
        System.out.println("使用谷歌浏览器模拟手机设备为："+ devicesName);

        Map<String, Object> chromeOptions=new HashMap<String, Object>();
        chromeOptions.put("mobileEmulation",mobileEmulation);
        DesiredCapabilities capabilities=DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY,chromeOptions);
        try {
            WebDriver driver=new ChromeDriver(capabilities);
            driver.get("https://www.1haitao.com/website89.html");

            Thread.sleep(3000);
            //旋转屏幕
            /*WebDriver driver = new AndroidDriver();
            ((Rotatable)driver).rotate(ScreenOrientation.LANDSCAPE);
            driver.get("url");*/
            //触摸和滚动屏幕
            /*((Rotatable)driver).rotate(ScreenOrientation.PORTRAIT);
            TouchActions touch = new TouchActions(driver);
            touch.scroll(0, 560).build().perform();//向下滚动800像素
*/


            /*//滑动至特定元素处，方法一 该方法滑动后不停留，不利于截屏，不建议使用
            Actions action=new Actions(driver);
            action.moveToElement(element).perform();
            //滑动至特定元素处，方法二
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);*/



            //移动到元素element对象的“顶端”与当前窗口的“顶部”对齐  有效
            //((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
            //((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

//移动到元素element对象的“底端”与当前窗口的“底部”对齐
          //  ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", element);

//移动到页面最底部 有效
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");

//移动到指定的坐标(相对当前的坐标移动)
          //  ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 700)");

//结合上面的scrollBy语句，相当于移动到700+800=1600像素位置
          // ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 2000)");

//移动到窗口绝对位置坐标，如下移动到纵坐标1600像素位置
           // ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 1600)");

//结合上面的scrollTo语句，仍然移动到纵坐标1200像素位置
            //((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 800)");
            Thread.sleep(5000);
            List<WebElement> good_desc=driver.findElements(By.className("good-desc"));
            WebElement element=good_desc.get(0);
            WebElement element8=good_desc.get(8);

            int dd_size=good_desc.size();
            System.out.println("================+++++++"+dd_size);

        }catch (Exception e){
            e.printStackTrace();
        }

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