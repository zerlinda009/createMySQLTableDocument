import entity.Entity;
import mapper.Mapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.awt.Color;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static  Double a(Double x, Double y, Double x1, Double y1) {
        Double x3 = (x - x1) * (x - x1);
        Double y3 = (y - y1) * (y - y1);
        return Math.sqrt(x3 + y3);
    }

    public static void main(String[] args) throws IOException {
        String tables = "tower_anomaly,tower_construction_site,tower_crane,tower_crane_application,tower_crane_detect,tower_crane_disassembly_file,tower_crane_heightening,tower_crane_install_application,tower_crane_maintenance,tower_crane_state_of_day,tower_director,tower_driver,tower_file,tower_warning,user,user_wechat";

        List<Map<String, Object>> mapList = getData(tables);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet1 = workbook.createSheet("表结构");

        // 写入数据
        int rowIndex = 0;
        int colNums = Entity.class.getDeclaredFields().length;
        for (Map<String, Object> map : mapList) {
            List<Entity> list = (List<Entity>) map.get("entityList");

            // 创建表名栏
            {
                XSSFRow row = sheet1.createRow(rowIndex++);
                // 创建单元格
                {
                    for (int i = 0; i < colNums; i++) {
                        row.createCell(i);
                    }
                }

                // 合并
                {
                    sheet1.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, 0, colNums - 1));
                }

                // 样式
                {
                    XSSFFont font;
                    {
                        font = workbook.createFont();
                        font.setFontName("Constantia");
                        // 加粗
                        font.setBold(true);
                        // 大小
                        font.setFontHeightInPoints((short) 20);
                        // 颜色
                        font.setColor(new XSSFColor(new Color(0, 158, 71)));
                    }
                    XSSFCellStyle cellStyle;
                    {
                        cellStyle = workbook.createCellStyle();
                        cellStyle.setFont(font);
                        setCentered(cellStyle);
                        setBorder(cellStyle);
                        // 背景色
                        cellStyle.setFillForegroundColor(new XSSFColor(new Color(22, 22, 22)));
                        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    }
//                              for ( int i = 0 ; i < colNums ; i++ ) {
//                              }
                    row.getCell(0).setCellStyle(cellStyle);
                    row.setHeight((short) (35 * 20));
                }

                // 插值
                {
                    String tableComment = null == map.get("tableComment") || ((String) map.get("tableComment")).trim().length() == 0 ? "该表名未定义注释" : (String) map.get("tableComment");
                    String value = String.format("%s ( %s )", map.get("tableName"), tableComment);
                    row.getCell(0).setCellValue(value);
                }
            }

            // 创建属性栏
            {
                XSSFRow row = sheet1.createRow(rowIndex++);
                // 创建单元格
                for (int i = 0; i < colNums; i++) {
                    row.createCell(i);
                }
                // 样式
                {
                    row.setHeight((short) (23 * 20));

                    XSSFFont font;
                    {
                        font = workbook.createFont();
                        // 字体名
                        font.setFontName("Arial Black");
                        // 加粗
                        font.setBold(true);
                        // 大小
                        font.setFontHeightInPoints((short) 12);
                        //
                        font.setColor(new XSSFColor(new Color(191, 143, 0)));
                    }

                    XSSFCellStyle cellStyle;
                    {
                        cellStyle = workbook.createCellStyle();
                        setCentered(cellStyle);
                        setBorder(cellStyle);
                        cellStyle.setFont(font);
                        // 背景色
                        cellStyle.setFillForegroundColor(new XSSFColor(new Color(22, 22, 22)));
                        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    }
                    for (int i = 0; i < colNums; i++) {
                        row.getCell(i).setCellStyle(cellStyle);
                    }
                }
                //插值
                {
                    for (int i = 0; i < colNums; i++) {
                        String value;
                        {
                            value = Entity.class.getDeclaredFields()[i].getName();
                            value = value.equals("isNull") ? "null" : value;
                            value = value.equals("defaultValue") ? "default" : value;
                        }

                        row.getCell(i).setCellValue(value.toUpperCase());
                    }
                }
            }

            // 数据栏
            {
                XSSFFont font;
                {
                    font = workbook.createFont();
                    font.setFontName("Meiryo");
                    font.setBold(true);
                    font.setColor(new XSSFColor(new Color(176, 176, 176)));
                }

                // 样式
                XSSFCellStyle cellStyle;
                {
                    cellStyle = workbook.createCellStyle();
                    setCentered(cellStyle);
                    setBorder(cellStyle);
                    // 自动换行
                    cellStyle.setWrapText(true);
                    // 背景色
                    cellStyle.setFillForegroundColor(new XSSFColor(new Color(38, 38, 38)));
                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    cellStyle.setFont(font);
                }

                for (Entity entity : list) {
                    XSSFRow row = sheet1.createRow(rowIndex++);

                    // 创建单元格
                    for (int i = 0; i < colNums; i++) {
                        row.createCell(i);
                    }

                    // 写入样式
                    {
                        row.setHeight((short) (23 * 20));
                        for (int i = 0; i < colNums; i++) {
                            row.getCell(i).setCellStyle(cellStyle);
                        }
                    }

                    // 插值
                    {
                        int col = 0;
                        row.getCell(col++).setCellValue(entity.getFiled());
                        row.getCell(col++).setCellValue(entity.getType());
                        row.getCell(col++).setCellValue(entity.getCollation());
                        row.getCell(col++).setCellValue(entity.getIsNull());
                        row.getCell(col++).setCellValue(entity.getKey());
                        row.getCell(col++).setCellValue(entity.getDefaultValue());
                        row.getCell(col++).setCellValue(entity.getExtra());
                        row.getCell(col++).setCellValue(entity.getPrivileges());
                        row.getCell(col).setCellValue(entity.getComment());
                    }
                }
            }

            // 加空行
            sheet1.createRow(rowIndex++);
            sheet1.createRow(rowIndex++);
        }

        // 设置列宽
        {
            for (int i = 0; i < colNums; i++) {
                if (i == 8) {
                    sheet1.setColumnWidth(i, 28 * 256);
                    continue;
                }
                sheet1.autoSizeColumn(i, true);
            }
        }

        File file;
        {
            file = new File("C:\\Users\\wxy_0\\Desktop\\a.xlsx");
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        }
        OutputStream outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        outputStream.close();
    }

    static List<Map<String, Object>> getData(String tables) throws IOException {
        Mapper mapper;
        {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            SqlSession session = sqlSessionFactory.openSession();
            mapper = session.getMapper(Mapper.class);
        }

        List<Map<String, Object>> list;
        {
            list = new ArrayList<>();
            String[] tablesArr = tables.split(",");

            for (String table : tablesArr) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("tableName", table);
                map.put("entityList", mapper.tableDesc(table));
                map.put("tableComment", mapper.getTableComment(table));
                list.add(map);
            }
        }
        return list;
    }

    // 加边框
    static void setBorder(XSSFCellStyle cellStyle) {
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
    }

    // 左右垂直居中
    static void setCentered(XSSFCellStyle cellStyle) {
        //设置水平对齐的样式为居中对齐;
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直对齐的样式为居中对齐;
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    }
}
