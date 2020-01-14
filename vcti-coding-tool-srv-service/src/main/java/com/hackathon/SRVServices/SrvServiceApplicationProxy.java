package com.hackathon.SRVServices;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="apiGateway")
@RibbonClient(name ="srvService" )
public interface SrvServiceApplicationProxy {

}
