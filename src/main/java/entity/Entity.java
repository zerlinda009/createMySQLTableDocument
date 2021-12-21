package entity;

import lombok.Data;

@Data
public class Entity {
      private String filed;
      private String type;
      private String collation;
      private String isNull;
      private String key;
      private String defaultValue;
      private String extra;
      private String privileges;
      private String comment;
}

