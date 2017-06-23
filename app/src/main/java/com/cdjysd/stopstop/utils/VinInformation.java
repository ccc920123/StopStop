package com.cdjysd.stopstop.utils;


import com.cdjysd.stopstop.bean.ConfigParamsModel;
import com.cdjysd.stopstop.bean.TempleModel;

/**
 * 
 * @类名 :VinInformation.java
 * @author Administrator user=chenpan
 * @功能描述:配置vin界面参数的类
 * @创建时间:2016-9-13
 */
public class VinInformation
{
  public String[] typeStrings = { "", "", "", "", "", "", "", "", "", "" };
  public String[] duedateStrings = { "", "", "", "", "", "", "", "", "", "" };
  public String[] sumStrings = { "", "", "", "", "", "", "", "", "", "" };
  public String androidPlatform;
  public TempleModel template;
  public ConfigParamsModel fieldType;


  
  public VinInformation(){
      androidPlatform="yes";
	 typeStrings[0]="17";
	  TempleModel templeModel = new TempleModel();
	  templeModel.templateType = "003";
      templeModel.templateName = "VIN码";
      templeModel.isSelected = true;
     template=templeModel;
      ConfigParamsModel model = new ConfigParamsModel();
      model.width = Float.valueOf("0.7").floatValue();
      model.height = Float.valueOf("0.15").floatValue();
      model.color = "255_159_242_74";
      model.name = "VIN码";
      model.nameTextSize = "40";
      model.nameTextColor = "255_255_255_255";
      model.namePositionX = Float.valueOf("0.38").floatValue();
      model.namePositionY = Float.valueOf("0.35").floatValue();
      model.ocrId = "SV_ID_VIN_CARWINDOW";
     fieldType=model;
  }
}