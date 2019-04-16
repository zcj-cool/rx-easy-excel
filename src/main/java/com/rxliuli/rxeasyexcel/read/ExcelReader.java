package com.rxliuli.rxeasyexcel.read;


import com.rxliuli.rxeasyexcel.domain.ExcelReadContext;
import com.rxliuli.rxeasyexcel.domain.ImportDomain;

/**
 * @author Quding Ding
 * @since 2018/6/29
 */
public interface ExcelReader extends AutoCloseable {

    /**
     * 解析一张sheet
     *
     * @param context 该sheet对应的上下文
     * @param <T>     要解析出来的类型
     * @return 结果，包含正确数据与错误数据（格式/非业务相关）
     */
    <T> ImportDomain<T> resolve(ExcelReadContext<T> context);

    /**
     * 读取完释放资源
     */
    @Override
    void close();
}
