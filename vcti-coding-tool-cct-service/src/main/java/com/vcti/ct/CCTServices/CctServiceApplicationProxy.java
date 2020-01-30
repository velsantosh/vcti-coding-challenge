package com.vcti.ct.CCTServices;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(name="apigateway")
@RibbonClient(name ="cctservice" )
public interface CctServiceApplicationProxy {

}
