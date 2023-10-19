package cn.ideaswork.ideacoder.domain.lms.vod;

import cn.ideaswork.ideacoder.infrastructure.config.QcloudVodConfig;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.DeleteMediaRequest;
import com.tencentcloudapi.vod.v20180717.models.DeleteMediaResponse;
import com.tencentcloudapi.vod.v20180717.models.DescribeMediaInfosRequest;
import com.tencentcloudapi.vod.v20180717.models.DescribeMediaInfosResponse;
import com.tencentcloudapi.vod.v20180717.models.MediaInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VodServiceImpl implements VodService {

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
    public String getVideoPlayUrlWithKey(String fileid) throws TencentCloudSDKException {
        MediaInfo vodInfo = this.getVodInfo(fileid);
        String mediaUrl = vodInfo.getBasicInfo().getMediaUrl();


        return null;
    }

    @Override
    public String deleteVodFile(String fileid) throws TencentCloudSDKException {
        // 实例化要请求产品的client对象,clientProfile是可选的
        Credential credential = new Credential(qcloudVodConfig.secretId,qcloudVodConfig.secretKey);
        String region = "";
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("vod.tencentcloudapi.com");
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        VodClient client = new VodClient(credential,region,clientProfile);
        // 实例化一个请求对象,每个接口都会对应一个request对象
        DeleteMediaRequest req = new DeleteMediaRequest();
        req.setFileId(fileid);
        // 返回的resp是一个DeleteMediaResponse的实例，与请求对象对应
        DeleteMediaResponse resp = client.DeleteMedia(req);
        // 输出json格式的字符串回包
        System.out.println(DeleteMediaResponse.toJsonString(resp));
        return resp.getRequestId();
    }
}
