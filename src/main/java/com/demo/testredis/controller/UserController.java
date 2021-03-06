package com.demo.testredis.controller;

import com.demo.testredis.entity.User;
import com.demo.testredis.service.UserService;
import com.demo.testredis.utils.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Auther: scp
 * @Date: 2018/12/4 11:35
 * @Description:
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private RedisTemplate redisTemplate;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    /**
     * 缓存用户信息
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getUser/{userId}",method = RequestMethod.GET)
    public User getUser(@PathVariable(value = "userId")Long userId){
//        System.out.println("ip:1110");
        try {
            if (redisService.get("userInfo"+userId)==null){
                User user=userService.getUser(userId);
                redisService.set("userInfo"+userId,user);
                log.info("用户信息存入redis,userId--"+userId);
                return user;
            }else{
                log.info("从redis中获取数据");
                return (User)redisService.get("userInfo"+userId);
            }
        } catch (Exception e) {
            return userService.getUser(userId);
        }

    }

    /**
     * 计数器
     * @return
     */
    @RequestMapping(value = "/visitCount",method = RequestMethod.GET)
    public String getVisitCount(){
        if (redisService.get("visitCount")==null){
            redisService.set("visitCount",1);
            return "访问量:"+redisService.get("visitCount");
        }else{
            redisService.incr("visitCount",1);
            return "访问量:"+redisService.get("visitCount");
        }

    }

    /**
     * 访问限制
     * @return
     */
    @RequestMapping(value = "/rateLimiting",method = RequestMethod.GET)
    public String rateLimiting() {
        String ip = request.getRemoteAddr();
        Integer count = (Integer) redisService.get(ip);
        if (count == null) {
            redisService.set(ip, 1, 5);
        } else if (count >= 5) {
            return "访问频率过快";
        } else {
            redisService.incr(ip, 1);
        }
        log.info("5秒内的访问次数:"+count);
        return "welcom!";
    }

    @RequestMapping(value = "/saveMap/{id}",method = RequestMethod.GET)
    public String saveMap(@PathVariable int id){
        System.out.println(id+"  00");
        Map<String,Object>map=new HashMap<>();
        map.put("name","Tom");
        map.put("age","18");
        map.put("x",12424.6);
        map.put("y",43434.1);
        redisService.hmset("user-"+id,map,600);
        return "用户-"+id+"存储成功";
    }

    @RequestMapping(value = "/keys",method = RequestMethod.GET)
    public List<Map<Object,Object>> keys(){
        Set keys = redisTemplate.keys("user-" + "*");//模糊匹配
        List<Map<Object,Object>>result=new ArrayList<>();
        for (Object k:keys) {
            Map<Object, Object> map = redisService.hmget(k.toString());
            result.add(map);
        }
        return result;
    }


}
