package com.zhangqtest.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * java类简单作用描述
 *
 * @ProjectName: zhangqiang-es-api
 * @Package: com.zhangqtest.config
 * @ClassName: dasf
 * @Description: java类作用描述
 * @Author: zhangq
 * @CreateDate: 2020-10-29 15:37
 * @UpdateUser: zhangq
 * @UpdateDate: 2020-10-29 15:37
 * @UpdateRemark: The modified content
 * @Version: 1.0 *
 */

@RestController
public class dasf {

  @RequestMapping("/a")
  public String te(){
    System.out.println("bbb");
    return "fdsa";
  }
}
