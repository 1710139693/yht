package common;

/**
 * Created by 秋月 on 2018/5/22.
 */
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class baseComposer {

    public static String proj_path = System.getProperty("user.dir");
    public static String exportFileAddr = proj_path + "/log/";
    public static String picFileAddr=proj_path + "/pic";


    /**
     * 账号登录
     * @throws InterruptedException
     */
    @Test
    public void onLoginInfo() throws InterruptedException{

        loginComposer.onLoginInfo();
    }

    /**
     * 打开浏览器,输入请求地址
     * @param url
     * @return
     */
    public  WebDriver openBrowser(String url) {
        //System.setProperty("webdriver.gecko.driver", geckodriver);

        // 如果你的 FireFox 没有安装在默认目录，那么必须在程序中设置
        //System.setProperty("webdriver.firefox.bin","D:\\Program Files\\Mozilla Firefox\\firefox.exe");

        // 创建一个 FireFox 的浏览器实例
        WebDriver driver = new FirefoxDriver();
        //最大化浏览器
        //driver.manage().window().maximize();
        // 让浏览器访问 url
        driver.get(url);
        return driver;
    }


    /**
     * 判断元素/对象是否存在
     * @param driver
     * @param locator
     * @return
     */
    public boolean isElementExsit(WebDriver driver, By locator, String res_txt) {
        boolean flag = false;
        try {
            WebElement element = driver.findElement(locator);
            flag = null != element;
            if (res_txt != null) {
                if (flag) {
                   rwFile("元素/对象", res_txt, "存在");
                } else {
                  rwFile("元素/对象", res_txt, "不存在");
                }
            }
        } catch (Exception e) {
            if (res_txt != null) {
                rwFile("元素/对象", "抛异常", locator.toString());
            }
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
            fw = new FileWriter(exportFileAddr + getFormatDateYMD(new Date()) + ".log", true);
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


}