package com.imooc.o2o.web.shopAdmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/frontend")
public class FrontendController {

    @RequestMapping(value="/index")
    public String index() {
        return "frontend/index";
    }

    @RequestMapping(value="/shoplist")
    public String shopList() {
        return "frontend/shoplist";
    }
}
