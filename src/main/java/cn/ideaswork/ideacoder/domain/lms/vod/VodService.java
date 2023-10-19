package cn.ideaswork.ideacoder.domain.lms.vod;

import cn.ideaswork.ideacoder.infrastructure.config.QcloudVodConfig;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.vod.v20180717.models.DescribeMediaInfosResponse;
import com.tencentcloudapi.vod.v20180717.models.MediaInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface VodService {

    public MediaInfo getVodInfo(String fileid) throws TencentCloudSDKException;

    public String getVideoPlayUrlWithKey(String fileid) throws TencentCloudSDKException;

    public String deleteVodFile(String fileid) throws TencentCloudSDKException;
}
