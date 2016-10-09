package com.johan.jnet;

import com.johan.jnet.annotation.Get;
import com.johan.jnet.http.Call;

/**
 * Created by Administrator on 2016/10/8.
 */
public interface TestService {

    @Get("http://www.baidu.com")
    Call<String> share();

}
