package com.rxliuli.rxeasyexcel.writer;

import com.rxliuli.rxeasyexcel.EasyExcel;
import com.rxliuli.rxeasyexcel.annotation.ExcelField;
import com.rxliuli.rxeasyexcel.domain.ExcelWriteContext;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import static com.rxliuli.rx.easy.test.util.ListGenerator.generate;
import static com.rxliuli.rx.easy.test.util.RandomDataUtil.random;

/**
 * 测试父类
 *
 * @author rxliuli
 */
class ExcelBeanSuperClassTest {
    private String fileName = ExcelBeanSuperClassTest.class.getClassLoader().getResource(".").getPath() + "/ExcelBeanSuperClassTest.xlsx";

    @Test
    void export() throws FileNotFoundException {
        EasyExcel.export(new FileOutputStream(fileName))
                .export(ExcelWriteContext.builder()
                        .datasource(generate(10, i -> random(DemoB.class)))
                        .build()
                )
                .write();
    }

    public static class DemoA {
        @ExcelField(columnName = "名字", order = 1)
        private String name;

        public String getName() {
            return name;
        }

        public DemoA setName(String name) {
            this.name = name;
            return this;
        }
    }

    public static class DemoB extends DemoA {
        @ExcelField(columnName = "年龄", order = 2)
        private Integer age;

        public Integer getAge() {
            return age;
        }

        public DemoB setAge(Integer age) {
            this.age = age;
            return this;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("name", getName())
                    .append("age", age)
                    .toString();
        }
    }
}
