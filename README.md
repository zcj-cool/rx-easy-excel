# rx-easy-excel

## 快速导出报表工具

> fork 自 [mrdear/easy-excel](https://github.com/mrdear/easy-excel)，修改代码以复合项目需求，此仓库仅用于修改后快速发布版本。

## maven

添加仓库

```xml
<repository>
  <snapshots>
    <enabled>false</enabled>
  </snapshots>
  <id>central</id>
  <name>bintray</name>
  <url>http://jcenter.bintray.com</url>
</repository>
```

添加依赖

```xml
<dependency>
  <groupId>com.rxliuli</groupId>
  <artifactId>rx-easy-excel</artifactId>
  <version>1.0.4</version>
</dependency>
```

## 核心类

- **EasyExcel** : 入口类，所有对外的操作都是由该类发起，主要有 export 与 read 两个操作。
- **ExcelWriter**：导出类，其方法分为非终端操作与终端操作，终端操作会输出并关闭该流，非终端操作则可以继续接着读取，应对一张 excel 中含有多个 sheet 的情况。
- **ExcelReader**：读取类，与上述`ExcelWriter`一样的操作。
- **ExcelField**：修饰实体类注解，Excel 中最麻烦的是 header，因此提倡每一张报表单独对应一个 POJO 类，使用注解标识相应字段。
- **ExcelWriteContext**：针对导出过程中一张 sheet 的配置，使用 Builder 模式构建。
- **ExcelReadContext**：针对读取过程中一张 sheet 的配置，使用 Builder 模式构建。
- **ExcelIgnore**: 修饰字段,可以指定忽略某一字段

## Example

**实体类**
实体类使用注解标识字段，不使用的话则属性名会作为对应的 `columnName`。

> 默认忽略没有使用注解 `@ExcelField` 的字段

```java
public class UserWithAnnotation {

  @ExcelField(columnName = "用户名")
  private String username;

  @ExcelField(columnName = "用户密码")
  private String passwd;

  @ExcelField(columnName = "登录日期",
      writerConvert = DateToStringConvert.class,
      readerConvert = StringToDateConvert.class)
  private Date date;
}
```

### 单张表

**export**: 导出

```java
@Test
 public void testSimpleWithAnnotationExport() {
   List<UserWithAnnotation> users = mockUserWithAnnotation(5);
   EasyExcel.export("/tmp/test.xlsx")
       .export(ExcelWriteContext.builder()
           .datasource(users)
           .sheetName("user")
           .build())
       .write();
}
```

**import**: 导入

```java
@Test
public void testRead2() {
  InputStream inputStream = SimpleExcelReaderTest.class
      .getClassLoader().getResourceAsStream("user2.xlsx");
  ExcelReader reader = EasyExcel.read(inputStream);

  List<UserWithAnnotation> result = reader.resolve(ExcelReadContext.<UserWithAnnotation>builder()
      .clazz(UserWithAnnotation.class)
      .build())
      .getData();

  Assert.assertEquals(result.size(), 5);
  Assert.assertEquals(result.get(0).getPasswd(), "0b6df627-5975-417b-abc9-1f2bad5ca1e2");
  Assert.assertEquals(result.get(1).getUsername(), "张三1");

  reader.close();
}
```

### 下拉框

实体类

```java
public static class Person {
    @ExcelField(columnName = "姓名", order = 1, prompt = "请输入真实姓名", type = ExcelColumnType.SELECT, select = PersonSelect.UsernameMap.class)
    private String username;
    @ExcelField(columnName = "日期", order = 2, prompt = "请输入一个正确的日期，格式为 yyyy-MM-dd。例如 2018-12-11")
    private Date date;
    @ExcelField(columnName = "本地日期", order = 3, errMsg = "本地日期错误，请务必输入正确的日期。例如 2018-12-11")
    private LocalDate localDate;
    @ExcelField(columnName = "本地时间", order = 4)
    private LocalTime localTime;

    // getter/setter...
}
```

导出

```java
@Test
void exportDateList() {
    final List<Person> users = mockUser(count);
    EasyExcel.export(fileName)
            .export(ExcelWriteContext.builder()
                    .datasource(users)
                    .sheetName("user")
                    .build())
            .write();
}
```

导入

```java
@Test
void importDateList() {
    ImportDomain<Person> result = new ImportDomain<>();
    try (InputStream is = new FileInputStream(fileName);
         final ExcelReader reader = EasyExcel.read(is)) {
        result = reader.resolve(ExcelReadContext.<Person>builder()
                .clazz(Person.class)
                .build());
    } catch (IOException e) {
        e.printStackTrace();
    }
    final List<Person> data = result.getData();
    System.out.println(join(data));
    List<ExcelImportError> errorList = result.getErrors();
    System.out.println(join(errorList));
    assertThat(data)
            .hasSize(count);
}
```

### 多张表+自定义 header

由于自定义的 title 往往非常复杂且多变，很难做到通用，因此这里是直接抛出一个钩子，可以自己实现自己想要的任何操作。

**export**: 导出

```java
@Test
public void testCustom() {
  List<UserWithAnnotation> users = mockUserWithAnnotation(5);
  EasyExcel.export("/tmp/test.xlsx")
      .export(ExcelWriteContext.builder()
          .datasource(users)
          .sheetName("user1")
          .createSheetHook((sheet, context) -> {
            Row row = sheet.createRow(0);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
            Cell cell = row.createCell(0);
            cell.setCellValue("custom header");
          })
          .startRow(1)
          .build())
      .export(ExcelWriteContext.builder()
          .datasource(users)
          .sheetName("user2")
          .build())
      .write();
}
```

**import**: 导入

```java
@Test
public void testCustom() {
  InputStream inputStream = SimpleExcelReaderTest.class
      .getClassLoader().getResourceAsStream("user3.xlsx");
  ExcelReader reader = EasyExcel.read(inputStream);

  List<UserWithAnnotation> sheet1Result = reader.resolve(ExcelReadContext.<UserWithAnnotation>builder()
      .clazz(UserWithAnnotation.class)
      .headerStart(1)
      .sheetIndex(0)
      .readSheetHook((sheet, context) -> {
        Row row = sheet.getRow(0);
        Assert.assertEquals(row.getCell(0).getStringCellValue(), "custom header");
      })
      .build())
      .getData();

  Assert.assertEquals(sheet1Result.size(), 5);
  Assert.assertEquals(sheet1Result.get(1).getUsername(), "张三1");


  List<UserWithAnnotation> sheet2Result = reader.resolve(ExcelReadContext.<UserWithAnnotation>builder()
      .clazz(UserWithAnnotation.class)
      .sheetIndex(1)
      .build());

  Assert.assertEquals(sheet2Result.size(), 5);
  Assert.assertEquals(sheet2Result.get(1).getUsername(), "张三1");

}
```

### 写入 HttpServletResponse

提供`ResponseHelper`从`HttpServletResponse`获取对应的输出流，然后放入

```java
OutputStream outputStream = ResponseHelper.wrapper(response, "order.xlsx");
EasyExcel.export(outputStream)....
```
