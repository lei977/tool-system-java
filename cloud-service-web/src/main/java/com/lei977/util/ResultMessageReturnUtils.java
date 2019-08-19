package com.drore.cloud.tdp.common.util;

import com.drore.cloud.sdk.common.resp.RestMessage;
import org.springframework.stereotype.Component;

/**
 * Created by sunholdIng on 2017/7/4.
 */
@Component
public class ResultMessageReturnUtils {

    /**
     * 错误返回
     */
    public RestMessage getErrRestMessage(String message) {
        RestMessage restMessage = new RestMessage();
        restMessage.setSuccess(false);
        restMessage.setErrCode(8500);
        restMessage.setMessage(message);
        return restMessage;
    }

    /**
     * 正确返回
     */
    public RestMessage getSuccRestMessage(String message, Object data) {
        RestMessage restMessage = new RestMessage();
        restMessage.setSuccess(true);
        restMessage.setErrCode(8200);
        restMessage.setMessage(message);
        restMessage.setData(data);
        return restMessage;
    }
}
