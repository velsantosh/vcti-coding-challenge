package com.vcti.ct.AAServices;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="apigateway")
@RibbonClient(name ="aaservice" )
public interface AaServicesApplicationProxy {

}
