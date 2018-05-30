package common;

/**
 * Created by pc on 2018/5/30.
 */

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import common.baseComposer;
import org.testng.annotations.Test;

public class goodsDetails {
    //String goodsAdrr=null;

    @Test
    public void detailsPage(){
        baseComposer base=new baseComposer();
        //从文件中读取商品ID

        try {
            WebDriver driver=base.openBrowser("https://www.1haitao.com/details/269156482.html");
            Thread.sleep(5000);

            //By lactor=By.className("msg black");
            By lactor=By.xpath("//div[contains(@class,'msg')]");

            boolean flag=base.isElementExsit(driver,lactor,null);
            System.out.println("==============flag========"+flag);
            if(flag){
                WebElement msg=driver.findElement(lactor);
                //String msgStr=msg.getAttribute("textContent");
                String msgStr=msg.getText();
                System.out.println("=========msgStr============="+msgStr);
                String urlStr=driver.getCurrentUrl();
                System.out.println("=========urlStr============="+urlStr);
                int left=urlStr.lastIndexOf("/");
                int right=urlStr.lastIndexOf(".");
                String goodsID=urlStr.substring(left+1,right);
                System.out.println("=========goodsID============="+goodsID);
                base.rwFile("商品详情检测",msgStr,"ID号为：" + goodsID);

            }else{
                System.out.println("===========page correct===========");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

