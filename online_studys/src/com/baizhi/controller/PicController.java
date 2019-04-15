package com.baizhi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@Controller
@RequestMapping("file")
public class PicController {


    @RequestMapping("upload")
    public String upload(MultipartFile file)throws Exception{
        //1.获取源文件夹
        String oldName = file.getOriginalFilename();
        //2.将接收的文件复制到服务器上
        file.transferTo(new File("E:\\服务器\\"+oldName));

        System.out.println(getPrintSize(file.getSize()));

        return "redirect:/index.jsp";
    }

    @RequestMapping("download")
    public void download(String fileName, HttpServletResponse resp)throws Exception{
        //1.读取要下载的文件
        InputStream is = new FileInputStream("E:\\服务器\\"+fileName);

        //设置响应头   attachment(附件)
        String s1 = URLEncoder.encode(fileName,"UTF-8");  //对中文进行编码 汉子 --> %3d
        resp.setHeader("content-disposition","attachment;filename="+s1);

        //2.获取给浏览器的响应输出流
        OutputStream os = resp.getOutputStream();

        //3.边读边写，将读取的内容响应输出给浏览器
        while(true){
            int i = is.read();
            if(i == -1)
                break;
            os.write(i);
        }
    }














    public static String getPrintSize(long size) {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "GB";
        }
    }

}
