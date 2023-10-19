package cn.ideaswork.ideacoder.domain.lms.oss;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.Bucket;
import com.qcloud.cos.model.PutObjectRequest;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.vod.v20180717.models.MediaInfo;

import java.io.File;
import java.net.URL;
import java.util.List;

public interface OssService {

    public MediaInfo getVodInfo(String fileid) throws TencentCloudSDKException;

    public COSClient initOssClient() throws TencentCloudSDKException;

    public Bucket createOssBucket(String bucketName) throws TencentCloudSDKException;

    public List<Bucket> getBucketList() throws TencentCloudSDKException;

    public PutObjectRequest uploadFile(File file, String folderName, String bucketName) throws TencentCloudSDKException;

    public URL getObjectUrl(String bucketName, String folderName, String fileName) throws TencentCloudSDKException;

    public void deleteFileInBucket(String bucketName, String filefolderName, String fileName) throws TencentCloudSDKException;
}
