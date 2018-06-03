package common;

/**
 * Created by 秋月 on 2018/5/22.
 */
import net.sourceforge.tess4j.Tesseract;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.openqa.selenium.*;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Point;
import javax.imageio.ImageIO;


public class loginComposer {

    /**
     * 平台登录
     * @throws InterruptedException
     */
    @Test
    public void onLoginInfo() throws InterruptedException{

        baseComposer base=new baseComposer();

        //读取Excel登录信息
        String[] file_content = base.readExcelInfo(base.loginFileAddr);

        WebDriver driver=base.openBrowserWithUrl(file_content[2]);
        base.rwFile("","=======================ready to login=====================","");

        try {
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
                System.out.println("==========codestr============"+codestr);
                base.rwFile("登录验证码","",codestr);

                //输入验证码
                WebElement valid_code=driver.findElement(By.xpath("//input[@name='valid-code']"));
                valid_code.clear();
                Thread.sleep(1000);
                valid_code.sendKeys(codestr);
                Thread.sleep(5000);

                //点击登录
                //WebElement btn_login=driver.findElement(By.xpath("//input[@value='登 录']"));
                WebElement btn_login=driver.findElement(By.cssSelector("#btn-login"));
                btn_login.click();
                Thread.sleep(5000);

                //判断登录是否存在，若不存在说明登录成功！
                By selector=By.className("user-login");
                boolean flag=base.isElementExsit(driver,selector,"头部登录按钮");

                if(flag){
                    //捕获验证码图片元素
                    WebElement valid_img_div=driver.findElement(By.xpath("//div[@class='ib-column valid-img']"));
                    WebElement valid_img=valid_img_div.findElement(By.tagName("img"));
                    valid_img.click();
                    Thread.sleep(3000);

                    base.rwFile("登录状态","第"+i+"次登录","fail");

                }else{
                    System.out.println("=============登录成功========="+i);
                    base.rwFile("登录状态","第"+i+"次登录","success");
                    codeFlag=false;
                }

                }

        } catch (Exception e) {
            base.rwFile("异常：","",e.toString());
            e.printStackTrace();
        }
        driver.quit();
    }

    /**
     * 获取验证码
     * @return
     */
    public  String getValidCode(WebDriver driver){

        String result=null;
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        try {

            //捕获验证码图片元素
            WebElement valid_img_div=driver.findElement(By.xpath("//div[@class='ib-column valid-img']"));
            WebElement valid_img=valid_img_div.findElement(By.tagName("img"));

            Point location=valid_img.getLocation();
            int width=valid_img.getSize().getWidth();
            int height=valid_img.getSize().getHeight();

            BufferedImage img= ImageIO.read(scrFile);
            BufferedImage dest = img.getSubimage(location.getX(),location.getY(),width,height);
            ImageIO.write(dest,"png",scrFile);
            Thread.sleep(1000);

            File codePng=new File(baseComposer.picFileAddr + "/code.png");
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




}
