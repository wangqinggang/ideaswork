package cn.ideaswork.ideacoder.domain.lms.oss;

import cn.ideaswork.ideacoder.infrastructure.config.QcloudVodConfig;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.Bucket;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.CreateBucketRequest;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.DescribeMediaInfosRequest;
import com.tencentcloudapi.vod.v20180717.models.DescribeMediaInfosResponse;
import com.tencentcloudapi.vod.v20180717.models.MediaInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.util.List;

@Service
public class OssServiceImpl implements OssService {

    @Autowired
    QcloudVodConfig qcloudVodConfig;

    @Override
    public MediaInfo getVodInfo(String fileid) throws TencentCloudSDKException {
        // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
        // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
        Credential cred = new Credential(qcloudVodConfig.secretId, qcloudVodConfig.secretKey);
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("vod.tencentcloudapi.com");
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        VodClient client = new VodClient(cred, "", clientProfile);
        // 实例化一个请求对象,每个接口都会对应一个request对象
        DescribeMediaInfosRequest req = new DescribeMediaInfosRequest();
        String[] fileIds1 = {fileid};
        req.setFileIds(fileIds1);

        // 返回的resp是一个DescribeMediaInfosResponse的实例，与请求对象对应
        DescribeMediaInfosResponse resp = client.DescribeMediaInfos(req);

        MediaInfo[] mediaInfoSet = resp.getMediaInfoSet();
        MediaInfo mediaInfo = mediaInfoSet[0];

        // 输出json格式的字符串回包
//        System.out.println(DescribeMediaInfosResponse.toJsonString(resp));
        return mediaInfo;
    }

    @Override
    public COSClient initOssClient() throws TencentCloudSDKException {

        // 1 初始化用户身份信息（secretId, secretKey）。
        // SECRETID和SECRETKEY请登录访问管理控制台 https://console.cloud.tencent.com/cam/capi 进行查看和管理
        String secretId = qcloudVodConfig.secretId;
        String secretKey = qcloudVodConfig.secretKey;
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);

        // 2 设置 bucket 的地域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region("ap-beijing"); // TODO 此处默认 bucket 的存储区域为北京
        ClientConfig clientConfig = new ClientConfig(region);

        // 这里建议设置使用 https 协议
        // 从 5.6.54 版本开始，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.https);

        // 3 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);
        return cosClient;
    }

    @Override
    public Bucket createOssBucket(String bucketName) throws TencentCloudSDKException {
        String bucket = bucketName + "-" + qcloudVodConfig.appid; //存储桶名称，格式：BucketName-APPID
        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucket);
        // 设置 bucket 的权限为 Private(私有读写)、其他可选有 PublicRead（公有读私有写）、PublicReadWrite（公有读写）
        createBucketRequest.setCannedAcl(CannedAccessControlList.Private);
        COSClient cosClient = this.initOssClient();
        Bucket bucketResult = cosClient.createBucket(createBucketRequest);
        // 关闭客户端(关闭后台线程)
        cosClient.shutdown();
        return bucketResult;
    }

    @Override
    public List<Bucket> getBucketList() throws TencentCloudSDKException {
        COSClient cosClient = this.initOssClient();
        List<Bucket> buckets = cosClient.listBuckets();
        // 关闭客户端(关闭后台线程)
        cosClient.shutdown();
        return buckets;
    }

    @Override
    public PutObjectRequest uploadFile(File file, String folderName, String bucketName) throws TencentCloudSDKException {
        // 指定文件将要存放的存储桶
        bucketName = bucketName + "-" + qcloudVodConfig.appid;
        // 指定文件上传到 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName, file);
        COSClient cosClient = this.initOssClient();
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        // 关闭客户端(关闭后台线程)
        cosClient.shutdown();
        return putObjectRequest;
    }

    @Override
    public URL getObjectUrl(String bucketName, String folderName, String fileName) throws TencentCloudSDKException {
        // 存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
        bucketName = bucketName + "-" + qcloudVodConfig.appid;
        // 对象键(Key)是对象在存储桶中的唯一标识。详情请参见 [对象键](https://cloud.tencent.com/document/product/436/13324)
        String key = folderName + "/" + fileName;
        COSClient cosClient = this.initOssClient();
        System.out.println(cosClient.getObjectUrl(bucketName, key));
        URL objectUrl = cosClient.getObjectUrl(bucketName, key);
        // 关闭客户端(关闭后台线程)
        cosClient.shutdown();
        return objectUrl;
    }

    @Override
    public void deleteFileInBucket(String bucketName, String filefolderName, String fileName) throws TencentCloudSDKException {
        // Bucket的命名格式为 BucketName-APPID ，此处填写的存储桶名称必须为此格式
        bucketName = bucketName + "-" + qcloudVodConfig.appid;
        // 指定被删除的文件在 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示删除位于 folder 路径下的文件 picture.jpg
        String key = filefolderName + "-" + fileName;
        COSClient cosClient = this.initOssClient();
        cosClient.deleteObject(bucketName, key);
        // 关闭客户端(关闭后台线程)
        cosClient.shutdown();
    }


}
