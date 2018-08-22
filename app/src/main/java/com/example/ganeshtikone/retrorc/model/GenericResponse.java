package com.example.ganeshtikone.retrorc.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ganesh Tikone on 7/5/18. Company: Silicus Technologies Pvt. Ltd. Email:
 * ganesh.tikone@silicus.com Class:
 */
public class GenericResponse {

  @SerializedName("status")
  @Expose
  private Integer status;
  @SerializedName("data")
  @Expose
  private String data;
  @SerializedName("msg")
  @Expose
  private String msg;

  /**
   * No args constructor for use in serialization
   *
   */
  public GenericResponse() {
  }

  /**
   *
   * @param status
   * @param data
   * @param msg
   */
  public GenericResponse(Integer status, String data, String msg) {
    super();
    this.status = status;
    this.data = data;
    this.msg = msg;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  @Override
  public String toString() {
    return new StringBuilder().append("status:"+ status).append("data:"+ data).append("msg:"+ msg).toString();
  }

}