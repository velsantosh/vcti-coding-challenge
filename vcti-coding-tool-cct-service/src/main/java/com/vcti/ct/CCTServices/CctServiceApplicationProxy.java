package com.vcti.ct.CCTServices;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(name="apiGateway")
@RibbonClient(name ="cctService" )
public interface CctServiceApplicationProxy {

}
