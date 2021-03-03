package com.allinfinance.dev.core.util.xml.jaxb;

import com.allinfinance.dev.core.util.validate.BeanConvertValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author 张勇
 * @description
 * @date 2020/12/8 11:37
 */
public class JAXBUtils {
    private static final Logger logger = LoggerFactory.getLogger(JAXBUtils.class);

    //将xml解析成对象
    public static <T> T xmlToBean(String xml, Class<T> load) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(load);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        T t = (T) unmarshaller.unmarshal(new StringReader(xml));

        try {
            BeanConvertValidator.beanVerify(t, "UTF-8");
        } catch (IllegalArgumentException e) {
            logger.error("xml -> bean, 字段校验异常!");
            throw e;
        }
        return t;
    }

    //将对象转换成xml
    public static String beanToXml(Object object, String encoding) throws JAXBException {
        try {
            BeanConvertValidator.beanVerify(object, encoding);
        } catch (IllegalArgumentException e) {
            logger.error("bean -> xml, 字段校验异常!");
            throw e;
        }
        JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        StringWriter stringWriter = new StringWriter();
        marshaller.marshal(object, stringWriter);

        return stringWriter.toString();
    }
}
