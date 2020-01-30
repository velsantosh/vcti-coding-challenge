package com.vcti.ct.SRVServices;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="apigateway")
@RibbonClient(name ="srvservice" )
public interface SrvServiceApplicationProxy {

}
