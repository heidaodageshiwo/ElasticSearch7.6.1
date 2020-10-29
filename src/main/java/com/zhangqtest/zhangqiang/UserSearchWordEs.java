package com.zhangqtest.zhangqiang;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @PackageName com.zhangqtest.zhangqiang
 * @ClassName UserSearchWordEs
 * @Description TODO
 * @Author zhangqiang
 * @Date 2020/10/13 10:53
 * @Version 1.0
 **/
/*@Data
@AllArgsConstructor
@NoArgsConstructor*/
public class UserSearchWordEs {


  //id
  private String id;
  //用户id
  private String userid;
  //用户name
  private String username;
  //用户name
  private String searchword;
  //用户搜索时间戳格式
  private Long createtimestamp;
  //用户搜索时间格式化以后的时间
  private String createtime;


  public UserSearchWordEs(String id, String userid, String username, String searchword,
      Long createtimestamp, String createtime) {
    this.id = id;
    this.userid = userid;
    this.username = username;
    this.searchword = searchword;
    this.createtimestamp = createtimestamp;
    this.createtime = createtime;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserid() {
    return userid;
  }

  public void setUserid(String userid) {
    this.userid = userid;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getSearchword() {
    return searchword;
  }

  public void setSearchword(String searchword) {
    this.searchword = searchword;
  }

  public Long getCreatetimestamp() {
    return createtimestamp;
  }

  public void setCreatetimestamp(Long createtimestamp) {
    this.createtimestamp = createtimestamp;
  }

  public String getCreatetime() {
    return createtime;
  }

  public void setCreatetime(String createtime) {
    this.createtime = createtime;
  }
}
