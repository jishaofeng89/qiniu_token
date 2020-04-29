package com.a360inhands.qiniu.controller;

import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {

    @RequestMapping("/list")
    @ResponseBody
    public Object get(HttpServletRequest req, HttpServletResponse resp) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Region.region1());
        //...其他参数参考类注释

        String accessKey = "5-QsjwcqbzJkIkYhXqiHrD-M0dRz31i-dFSqUHgy";
        String secretKey = "BhMikfDUxngRq54-ja9-vFsgb15WbOXxyLokJ70R";

        String bucket = "dynamic-app-public";

        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);

        //文件名前缀
        String prefix = "";
        //每次迭代的长度限制，最大1000，推荐值 1000
        int limit = 1000;
        //指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
        String delimiter = "";

        //列举空间文件列表
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucket, prefix, limit, delimiter);
        List<FileInfo> fileInfoList = new ArrayList<FileInfo>();
        while (fileListIterator.hasNext()) {
            //处理获取的file list结果
            FileInfo[] items = fileListIterator.next();
            for (FileInfo item : items) {
                System.out.println(item.key);
                System.out.println(item.hash);
                System.out.println(item.fsize);
                System.out.println(item.mimeType);
                System.out.println(item.putTime);
                System.out.println(item.endUser);
                fileInfoList.add(item);
            }
        }
        return fileInfoList;
    }
}
