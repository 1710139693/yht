package common;

/**
 * Created by 秋月 on 2018/5/22.
 */
import net.sourceforge.tess4j.Tesseract;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Point;

import javax.imageio.ImageIO;


public class loginComposer {

    /**
     * 平台登录
     * @param driver
     * @throws InterruptedException
     */
    public static void onLoginInfo(WebDriver driver) {

        //获取浏览器cookie
        /*HttpServletRequest request = ServletActionContext.getRequest();
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for(Cookie cookie : cookies){
                System.out.println("name:"+cookie.getName()+", value:"+ cookie.getValue());
            }
        }*/
        baseComposer base=new baseComposer();
        String[] file_content = onLoginAccount();

        //最大化浏览器
        driver.manage().window().maximize();
        // 让浏览器访问 url
        driver.get(file_content[2]);

        try {
            Thread.sleep(1000);
            WebElement user_login = driver.findElement(By.className("user-login"));
            user_login.click();
            Thread.sleep(1000);
            WebElement user_name = driver.findElement(By.name("user-name"));
            user_name.sendKeys(file_content[0]);
            Thread.sleep(1000);
            WebElement password = driver.findElement(By.name("password"));
            password.sendKeys(file_content[1]);

            boolean codeFlag=true;
            for (int i=1;codeFlag;i++){
                //获取验证码
                String codestr=getValidCode(driver);
                System.out.println("======================"+codestr);

                WebElement valid_code=driver.findElement(By.xpath("//input[@name='valid-code']"));
                valid_code.clear();
                Thread.sleep(1000);
                valid_code.sendKeys(codestr);
                Thread.sleep(2000);

                //?????
                //WebElement login_form = driver.findElement(By.xpath("//form[contains(@class,'login-form')]"));
               // WebElement btn_login=driver.findElement(By.xpath("//input[contains(@id,'login') and @type='submit']"));
                WebElement btn_login=driver.findElement(By.cssSelector("#btn-login"));
                //WebElement btn_login=driver.findElement(By.id("btn-login"));
                btn_login.click();
                Thread.sleep(5000);

                By lactor=By.className("user-login");
                boolean flag=base.isElementExsit(driver,lactor,null);
                System.out.println("*************************"+flag);
                if(flag){
                    //捕获验证码图片元素
                    WebElement valid_img_div=driver.findElement(By.xpath("//div[@class='ib-column valid-img']"));
                    WebElement valid_img=valid_img_div.findElement(By.tagName("img"));
                    valid_img.click();
                    Thread.sleep(2000);
                    System.out.println("===========++++==========="+i);
                   /* WebElement valid_msg=driver.findElement(By.className("valid-msg"));
                    String text=valid_msg.getText();
                    System.out.println("++++++++"+text);
                    if(valid_msg.getText().equals("图片验证码错误")){
                        System.out.println("=============图片验证码错误");
                    }*/
                }else{
                    System.out.println("=============sss========="+i);
                    //System.out.println("=============登录成功");
                    codeFlag=false;
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取验证码
     * @return
     */
    public static String getValidCode(WebDriver driver){

        String result=null;
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        try {

            //捕获验证码图片元素
            WebElement valid_img_div=driver.findElement(By.xpath("//div[@class='ib-column valid-img']"));
            WebElement valid_img=valid_img_div.findElement(By.tagName("img"));

            Point location=valid_img.getLocation();
            int width=valid_img.getSize().getWidth();
            int height=valid_img.getSize().getHeight();
            //Rectangle rect = new Rectangle(width,height);

            BufferedImage img= ImageIO.read(scrFile);
            BufferedImage dest = img.getSubimage(location.getX(),location.getY(),width,height);
            ImageIO.write(dest,"png",scrFile);
            Thread.sleep(1000);

            File codePng=new File("D:/IdeaProject/pic/code.png");
            if (codePng.exists()){
                codePng.delete();
            }

            FileUtils.copyFile(scrFile, codePng);

            Tesseract instance = new Tesseract();

            //将验证码图片的内容识别为字符串
            result = instance.doOCR(codePng);

        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }




    /**
     * 读取登录平台信息（账号和地址）
     * @return
     */
    public static String[] onLoginAccount() {

        String[] cells=null;
        Workbook workbook = null;
        try {

            String loginFilePath=baseComposer.proj_path + "/loginInfo.xlsx";
            File loginFile=new File(loginFilePath);
            String fileName=loginFile.getName();
           //String prefix=fileName.substring(fileName.lastIndexOf(".")+1);
            InputStream instream = new FileInputStream(loginFilePath);

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

                    for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
                        Row row = sheet.getRow(rowNum);
                        if (row == null) {
                            continue;
                        }
                        //获得当前行的开始列
                        int firstCellNum = row.getFirstCellNum();
                        //获得当前行的列数
                        int lastCellNum = row.getPhysicalNumberOfCells();
                        cells = new String[row.getPhysicalNumberOfCells()];
                        for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {

                            Cell cell = row.getCell(cellNum);
                            cells[cellNum] = getCellValue(cell);
                        }
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
