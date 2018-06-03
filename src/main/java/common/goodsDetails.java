package common;

/**
 * Created by pc on 2018/5/30.
 */

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
;
import org.testng.annotations.Test;

import java.util.List;

public class goodsDetails {

    /**
     * 单品详情中间页
     * @throws InterruptedException
     */
    @Test
    public void detailsPage() throws InterruptedException{
        baseComposer base=new baseComposer();

        //从文件中读取商品ID
       String[] goodsIds=base.readExcelInfo(base.goodsIDFileAddr);
       int goodsLength=goodsIds.length;
       base.rwFile("","=======================goods detailpage check=====================","");

        try {
            WebDriver driver=base.openBrowser();

            for(int num=0;num<goodsLength;num++){
                Thread.sleep(1000);
                driver.get(base.goodsUrlBase+goodsIds[num]+".html");
                System.out.println("=========file_content=========="+ goodsIds[num]);
                Thread.sleep(5000);

                //判断爬虫信息是否存在
                //By selector=By.className("msg black");
                By selector=By.cssSelector("#page > div.detail > div.wrap > div.detail-header > div.right > div.detail-sku.cf > div.msg.black");
                boolean flag=base.isElementExsit(driver,selector,"爬虫错误信息");
                System.out.println("==============flag========"+flag);

                if(flag){
                    WebElement msg=driver.findElement(selector);
                    //String msgStr=msg.getAttribute("textContent");
                    String msgStr=msg.getText();
                    System.out.println("=========msgStr============="+msgStr);
                    base.rwFile("商品详情",msgStr,"goodsID为：" + goodsIds[num]);

                    /* String urlStr=driver.getCurrentUrl();
                    int left=urlStr.lastIndexOf("/");
                    int right=urlStr.lastIndexOf(".");
                    String goodsID=urlStr.substring(left+1,right);*/

                }else{
                    System.out.println("===========page correct===========");
                    base.rwFile("商品详情","page correct","");
                }

                //判断满折是否有两个
                By lactor=By.xpath("//span[contains(text(),'满折') and @class='active']");
                boolean activeflag=base.isElementExsit(driver,lactor,"满折");
                if (activeflag){
                    List<WebElement> active_item = driver.findElements(lactor);

                    if(active_item.size()>1){
                        base.rwFile("促销活动检测","满折重复"+ active_item.size(),"goodsID为：" + goodsIds[num] );
                    }
                }else {
                    base.rwFile("促销活动检测","无满折","");
                }

                //判断图片是否存在
                WebElement goods_img_divg=driver.findElement(By.className("goods-img"));
                WebElement goods_img=goods_img_divg.findElement(By.tagName("img"));
                int width=goods_img.getSize().getWidth();
                int height=goods_img.getSize().getHeight();
                System.out.println("=============width========="+width+"==========height======="+height);
                if(width>278){
                    base.rwFile("单品图片检测","图片为空","goodsID为：" + goodsIds[num]);
                }


            }

        driver.quit();
        } catch (Exception e) {
            base.rwFile("异常：","",e.toString());
            e.printStackTrace();
        }

    }


}

