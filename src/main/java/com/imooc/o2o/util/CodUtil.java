package com.imooc.o2o.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 判断输入的验证码是否正确
 */
public class CodUtil {
    public static boolean checkVerifyCode(HttpServletRequest request){
        //实际工具给生成的验证码
        String verifyCodeExpected = (String)request.getSession().getAttribute(
                com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        //页面上人录入的验证码
        String verifyCodeActual = HttpServletRequestUtil.getString(request,"verifyCodeActual");
        if(verifyCodeActual == null || !verifyCodeActual.equals(verifyCodeExpected)){
            return false;
        }
        return true;
    }
}
