/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.services;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import co.yixiang.api.YshopException;
import co.yixiang.common.util.IpUtil;
import co.yixiang.common.util.JwtToken;
import co.yixiang.constant.ShopConstants;
import co.yixiang.enums.AppFromEnum;
import co.yixiang.modules.auth.param.LoginParam;
import co.yixiang.modules.auth.param.RegParam;
import co.yixiang.modules.user.domain.YxUser;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.service.dto.WechatUserDto;
import co.yixiang.modules.user.vo.OnlineUser;
import co.yixiang.mp.config.WxMpConfiguration;
import co.yixiang.utils.EncryptUtils;
import co.yixiang.utils.RedisUtils;
import co.yixiang.utils.ShopKeyUtils;
import co.yixiang.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.vdurmont.emoji.EmojiParser;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @ClassName 登陆认证服务类
 * @Author hupeng <610796224@qq.com>
 * @Date 2020/6/14
 **/
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthService {

    private final YxUserService userService;
    private final RedisUtils redisUtil;
    private final WxMaService wxMaService;
    private final RedisUtils redisUtils;

    private static Integer expiredTimeIn;


    @Value("${yshop.security.token-expired-in}")
    public void setExpiredTimeIn(Integer expiredTimeIn) {
        AuthService.expiredTimeIn = expiredTimeIn;
    }




    /**
     * 小程序登陆
     * @param loginParam loginParam
     * @return long
     */
    @Transactional
    public YxUser wxappLogin(LoginParam loginParam) {
        String code = loginParam.getCode();
        String encryptedData = loginParam.getEncryptedData();
        String iv = loginParam.getIv();
        String spread = loginParam.getSpread();
        try {
            //读取redis配置
            String appId = redisUtil.getY(ShopKeyUtils.getWxAppAppId());
            String secret = redisUtil.getY(ShopKeyUtils.getWxAppSecret());
            if (StrUtil.isBlank(appId) || StrUtil.isBlank(secret)) {
                throw new YshopException("请先配置小程序");
            }
            WxMaDefaultConfigImpl wxMaConfig = new WxMaDefaultConfigImpl();
            wxMaConfig.setAppid(appId);
            wxMaConfig.setSecret(secret);

            wxMaService.setWxMaConfig(wxMaConfig);
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            WxMaUserInfo wxMpUser = wxMaService.getUserService()
                    .getUserInfo(session.getSessionKey(), encryptedData, iv);
            String openid = wxMpUser.getOpenId();
            YxUser returnUser = null;
            //如果开启了UnionId
            if (StrUtil.isNotBlank(wxMpUser.getUnionId())) {
                openid = wxMpUser.getUnionId();
            }

            YxUser yxUser = userService.getOne(Wrappers.<YxUser>lambdaQuery()
                    .eq(YxUser::getUsername, openid),false);

            if (ObjectUtil.isNull(yxUser)) {


                //过滤掉表情
                String nickname = EmojiParser.removeAllEmojis(wxMpUser.getNickName());
                String ip = IpUtil.getRequestIp();
                YxUser user = YxUser.builder()
                        .username(openid)
                        .nickname(nickname)
                        .avatar(wxMpUser.getAvatarUrl())
                        .addIp(ip)
                        .lastIp(ip)
                        .userType(AppFromEnum.ROUNTINE.getValue())
                        .build();

                //构建微信用户
                WechatUserDto wechatUserDTO = WechatUserDto.builder()
                        .nickname(nickname)
                        .routineOpenid(wxMpUser.getOpenId())
                        .unionId(wxMpUser.getUnionId())
                        .sex(Integer.valueOf(wxMpUser.getGender()))
                        .language(wxMpUser.getLanguage())
                        .city(wxMpUser.getCity())
                        .province(wxMpUser.getProvince())
                        .country(wxMpUser.getCountry())
                        .headimgurl(wxMpUser.getAvatarUrl())
                        .build();

                user.setWxProfile(wechatUserDTO);

                userService.save(user);

                returnUser = user;

            } else {
                returnUser = yxUser;
                WechatUserDto wechatUser =yxUser.getWxProfile();
                if ((StrUtil.isBlank(wechatUser.getRoutineOpenid()) && StrUtil.isNotBlank(wxMpUser.getOpenId()))
                        || (StrUtil.isBlank(wechatUser.getUnionId()) && StrUtil.isNotBlank(wxMpUser.getUnionId()))) {
                    wechatUser.setRoutineOpenid(wxMpUser.getOpenId());
                    wechatUser.setUnionId(wxMpUser.getUnionId());

                    yxUser.setWxProfile(wechatUser);

                }
                yxUser.setUserType(AppFromEnum.ROUNTINE.getValue());
                userService.updateById(yxUser);
            }
            userService.setSpread(spread, returnUser.getUid());
            return returnUser;
        } catch (WxErrorException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new YshopException(e.toString());
        }


    }

    /**
     * 公众号登陆
     * @param code code码
     * @param spread 上级用户
     * @return uid
     */
    @Transactional
    public YxUser wechatLogin(String code,String spread){
        try {
            WxMpService wxService = WxMpConfiguration.getWxMpService();
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxService.oauth2getAccessToken(code);
            WxMpUser wxMpUser = wxService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
            String openid = wxMpUser.getOpenId();

            //如果开启了UnionId
            if (StrUtil.isNotBlank(wxMpUser.getUnionId())) {
                openid = wxMpUser.getUnionId();
            }

            YxUser yxUser = userService.getOne(Wrappers.<YxUser>lambdaQuery()
                    .eq(YxUser::getUsername,openid),false);

            //long uid = 0;
            YxUser returnUser = null;
            if(yxUser == null){
                //过滤掉表情
                String nickname = EmojiParser.removeAllEmojis(wxMpUser.getNickname());
                log.info("昵称：{}", nickname);
                //用户保存
                String ip = IpUtil.getRequestIp();
                YxUser user = YxUser.builder()
                        .username(openid)
                        .nickname(nickname)
                        .avatar(wxMpUser.getHeadImgUrl())
                        .addIp(ip)
                        .lastIp(ip)
                        .userType(AppFromEnum.WECHAT.getValue())
                        .build();

                //构建微信用户
                WechatUserDto wechatUserDTO = WechatUserDto.builder()
                        .nickname(nickname)
                        .openid(wxMpUser.getOpenId())
                        .unionId(wxMpUser.getUnionId())
                        .sex(wxMpUser.getSex())
                        .language(wxMpUser.getLanguage())
                        .city(wxMpUser.getCity())
                        .province(wxMpUser.getProvince())
                        .country(wxMpUser.getCountry())
                        .headimgurl(wxMpUser.getHeadImgUrl())
                        .subscribe(wxMpUser.getSubscribe())
                        .subscribeTime(wxMpUser.getSubscribeTime())
                        .build();

                user.setWxProfile(wechatUserDTO);
                userService.save(user);

                returnUser = user;
            }else{
                returnUser = yxUser;
                WechatUserDto wechatUser = yxUser.getWxProfile();
                if((StrUtil.isBlank(wechatUser.getOpenid()) && StrUtil.isNotBlank(wxMpUser.getOpenId()))
                        || (StrUtil.isBlank(wechatUser.getUnionId()) && StrUtil.isNotBlank(wxMpUser.getUnionId()))){
                    wechatUser.setOpenid(wxMpUser.getOpenId());
                    wechatUser.setUnionId(wxMpUser.getUnionId());

                    yxUser.setWxProfile(wechatUser);
                }

                yxUser.setUserType(AppFromEnum.WECHAT.getValue());
                userService.updateById(yxUser);

            }

            userService.setSpread(spread,returnUser.getUid());

            log.error("spread:{}",spread);

            return returnUser;

        } catch (WxErrorException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new YshopException(e.toString());
        }
    }


    /**
     * 注册
     * @param param RegDTO
     */
    @Transactional
    public void register(RegParam param){
        String account = param.getAccount();
        String ip = IpUtil.getRequestIp();
        YxUser user = YxUser.builder()
                .username(account)
                .nickname(account)
                .password(SecureUtil.md5(param.getPassword()))
                .phone(account)
                .avatar(ShopConstants.YSHOP_DEFAULT_AVATAR)
                .addIp(ip)
                .lastIp(ip)
                .userType(AppFromEnum.H5.getValue())
                .build();

        userService.save(user);

        userService.setSpread(param.getSpread(),user.getUid());

    }


    /**
     * 保存在线用户信息
     * @param yxUser /
     * @param token /
     * @param request /
     */
    public void save(YxUser yxUser, String token, HttpServletRequest request){
        String job = "yshop开发工程师";
        String ip = StringUtils.getIp(request);
        String browser = StringUtils.getBrowser(request);
        String address = StringUtils.getCityInfo(ip);
        OnlineUser onlineUser = null;
        try {
            onlineUser = new OnlineUser(yxUser.getUsername(), yxUser.getNickname(), job, browser ,
                    ip, address, EncryptUtils.desEncrypt(token), new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        redisUtils.set(ShopConstants.YSHOP_APP_LOGIN_USER + token, onlineUser, AuthService.expiredTimeIn);
    }

    /**
     * 检测用户是否在之前已经登录，已经登录踢下线
     * @param userName 用户名
     */
    public void checkLoginOnUser(String userName, String igoreToken){
        List<OnlineUser> onlineUsers = this.getAll(userName);
        if(onlineUsers ==null || onlineUsers.isEmpty()){
            return;
        }
        System.out.println("onlineUsers:"+onlineUsers);
        for(OnlineUser onlineUser:onlineUsers){
            if(onlineUser.getUserName().equals(userName)){
                try {
                    String token = EncryptUtils.desDecrypt(onlineUser.getKey());
                    if(StringUtils.isNotBlank(igoreToken)&&!igoreToken.equals(token)){
                        this.kickOut(onlineUser.getKey());
                    }else if(StringUtils.isBlank(igoreToken)){
                        this.kickOut(onlineUser.getKey());
                    }
                } catch (Exception e) {
                    log.error("checkUser is error",e);
                }
            }
        }
    }

    /**
     * 踢出用户
     * @param key /
     */
    public void kickOut(String key) throws Exception {
        key = ShopConstants.YSHOP_APP_LOGIN_USER + EncryptUtils.desDecrypt(key);
        redisUtils.del(key);

    }

    /**
     * 退出登录
     * @param token /
     */
    public void logout(String token) {
        String key = ShopConstants.YSHOP_APP_LOGIN_USER + token;
        redisUtils.del(key);
    }

    /**
     * 查询全部数据，不分页
     * @param filter /
     * @return /
     */
    private List<OnlineUser> getAll(String filter){
        List<String> keys = null;
        keys = redisUtils.scan(ShopConstants.YSHOP_APP_LOGIN_USER + "*");

        Collections.reverse(keys);
        List<OnlineUser> onlineUsers = new ArrayList<>();
        for (String key : keys) {
            OnlineUser onlineUser = (OnlineUser) redisUtils.get(key);
            if(StringUtils.isNotBlank(filter)){
                if(onlineUser.toString().contains(filter)){
                    onlineUsers.add(onlineUser);
                }
            } else {
                onlineUsers.add(onlineUser);
            }
        }
        onlineUsers.sort((o1, o2) -> o2.getLoginTime().compareTo(o1.getLoginTime()));
        return onlineUsers;
    }



}
