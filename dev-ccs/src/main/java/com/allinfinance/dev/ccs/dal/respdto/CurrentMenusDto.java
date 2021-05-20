package com.allinfinance.dev.ccs.dal.respdto;

import java.io.Serializable;
import java.util.List;

public class CurrentMenusDto implements Serializable {
   private String path;
   private String name;
   private String icon;
   private String component;
   private String[] authority;
   private List<CurrentMenusDto> children;

   public String getPath() {
      return path;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getIcon() {
      return icon;
   }

   public void setIcon(String icon) {
      this.icon = icon;
   }

   public String getComponent() {
      return component;
   }

   public void setComponent(String component) {
      this.component = component;
   }

   public String[] getAuthority() {
      return authority;
   }

   public void setAuthority(String[] authority) {
      this.authority = authority;
   }

   public List<CurrentMenusDto> getChildren() {
      return children;
   }

   public void setChildren(List<CurrentMenusDto> children) {
      this.children = children;
   }
}
