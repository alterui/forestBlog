package com.lr.blog.controller.common;

import com.alibaba.fastjson.JSON;
import com.lr.blog.dto.ResultVO;
import com.lr.blog.dto.UploadFileVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by lr
 */
@Controller
@Slf4j
public class UploadFileController {



    /**
     * 上传文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/uploadFile",method = RequestMethod.POST)
    @ResponseBody
    public String uploadFile(@Param("file")MultipartFile file) {

        //本地使用,上传位置
//        String rootPath ="/www/uploads/";
        String rootPath ="D:/uploads/";

        //文件的完整名称,如spring.jpeg
        String filename = file.getOriginalFilename();
        //文件名,如spring
        String name = filename.substring(0,filename.indexOf("."));
        //文件后缀,如.jpeg
        String suffix = filename.substring(filename.lastIndexOf("."));

        //创建年月文件夹
        Calendar date = Calendar.getInstance();
        File dateDirs = new File(date.get(Calendar.YEAR)
                + File.separator + (date.get(Calendar.MONTH)+1));

        //目标文件
        File descFile = new File(rootPath+File.separator+dateDirs+File.separator+filename);
        int i = 1;
        //若文件存在重命名
        String newFilename = filename;
        while(descFile.exists()) {
            newFilename = name+"("+i+")"+suffix;
            String parentPath = descFile.getParent();
            descFile = new File(parentPath+File.separator+newFilename);
            i++;
        }
        //判断目标文件所在的目录是否存在
        if(!descFile.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            descFile.getParentFile().mkdirs();
        }

        //将内存中的数据写入磁盘
        try {
            file.transferTo(descFile);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传失败，cause:{}",e);
        }
        //完整的url
        String fileUrl =  "/uploads/"+dateDirs+ "/"+newFilename;

        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("成功");

        UploadFileVO uploadFileVO = new UploadFileVO();
        uploadFileVO.setTitle(filename);
        uploadFileVO.setSrc(fileUrl);
        resultVO.setData(uploadFileVO);
        return JSON.toJSONString(resultVO);
    }
}
