package com.remoteLaboratory.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * srs代理工具类
 *
 * @Author: yupeng
 */
public class SrsAgentUtil {
    private static Logger log = LoggerFactory.getLogger(SrsAgentUtil.class);

    // 调用SRS代理客服端更新配置
    public void refreshConfig(String url) {
        url += "/agent/api/v1/refreshconfig";
        RefreshConfigThread refreshConfigThread = new RefreshConfigThread(url);
        refreshConfigThread.start();
    }

    class RefreshConfigThread extends Thread {
        String url;
        public RefreshConfigThread(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            // 等入库成功了再刷新配置
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            HttpUtil httpUtil = new HttpUtil();
            JSONObject result = httpUtil.doPostResult(this.url, "{}", "");
            if(result != null) {
                log.info("videoClipDelete result: ", result.toJSONString());
            }
        }
    }
}
