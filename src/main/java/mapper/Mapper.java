package mapper;

import entity.Entity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface Mapper {
      List< Entity > tableDesc ( @Param ( "tableName" ) String tableName );

      String getTableComment ( @Param ( "tableName" ) String tableName );
}
