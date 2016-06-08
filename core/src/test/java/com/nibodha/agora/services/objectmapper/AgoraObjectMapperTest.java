package com.nibodha.agora.services.objectmapper;

import com.nibodha.agora.services.objectmapper.support.ConverterContext;
import com.nibodha.agora.services.objectmapper.transformer.AbstractTypeConverter;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/spring/objectmapper-test-context.xml"})
public class AgoraObjectMapperTest {

    @BeforeClass
    public static void setup() {
    }

    @Autowired
    @Qualifier("objectToObjectMapper")
    private AgoraObjectMapper objectToObjectMapper;

    @Autowired
    @Qualifier("xlsToObjectMapper")
    private AgoraObjectMapper xlsToObjectMapper;

    @Test
    public void transformObjectToObject() throws Exception {
        final SourceClass san = new SourceClass("San", "27", Collections.singletonList(new SourceClass("Abin", "19", null, null, null)), new TargetClass("John", 15, null, null, null), new String[]{"9947", "9995"});

        final String customName = "Custom Name";
        final ConverterContext context = new ConverterContext().put("Your Name", customName);

        final TargetClass targetObj = objectToObjectMapper.mapToBean(san, "source-to-target", context);

        Assert.assertEquals("San", targetObj.getFullName());
        Assert.assertEquals(Integer.valueOf(12), targetObj.getYears());

        Assert.assertEquals("Abin", targetObj.getColleagues().get(0).getFullName());
        Assert.assertEquals(Integer.valueOf(19), targetObj.getColleagues().get(0).getYears());
        Assert.assertEquals(customName, targetObj.getColleagues().get(0).getCustomTargetValue());

        Assert.assertEquals("John", targetObj.getEngineer().getName());
        Assert.assertEquals("15", targetObj.getEngineer().getAge());

        Assert.assertEquals(Integer.valueOf(9947), targetObj.getMobiles().get(0));
        Assert.assertEquals(Integer.valueOf(9995), targetObj.getMobiles().get(1));
    }

    @Test
    public void transformXlsToBean() throws Exception {
        final Resource xlsStream = new ClassPathResource("customer.xlsx");
        final List<Customer> customers = xlsToObjectMapper.mapToBean(xlsStream.getFile(), "xls-to-pojo");
        Assert.assertNotNull(customers);
        Assert.assertEquals(3, customers.size());

        final Customer customer1 = customers.get(0);
        Assert.assertEquals(Float.valueOf("0.35"), customer1.getPrice());
        Assert.assertEquals(Integer.valueOf("6"), customer1.getQuantity());
        //Assert.assertEquals(Float.valueOf("2"), customer1.getTotal());
        Assert.assertEquals("Some gasket", customer1.getName());
        Assert.assertEquals("6234", customer1.getId());
    }

    @Test
    public void transformXlsToMapByDefault() throws Exception {
        final Resource xlsStream = new ClassPathResource("customer.xlsx");
        final List<Map<String, String>> customers = xlsToObjectMapper.mapToBean(xlsStream.getFile(), "xls-to-map");
        Assert.assertNotNull(customers);
        Assert.assertEquals(3, customers.size());

        final Map<String, String> customer1 = customers.get(0);
        Assert.assertEquals("0.35", customer1.get("Price"));
        Assert.assertEquals("6", customer1.get("Quantity"));
        //Assert.assertEquals(Float.valueOf("2"), customer1.getTotal());
        Assert.assertEquals("Some gasket", customer1.get("Name"));
        Assert.assertEquals("6234", customer1.get("ID"));
    }

    public static class MyTypeConverter extends AbstractTypeConverter {

        @Override
        public Object convert(final Object source, final Object parentObject) {
            return getParameter("Your Name");
        }

    }

    public static class ThisToListConverter extends AbstractTypeConverter {

        @Override
        public Object convert(final Object source, final Object parentObject) {
            final SourceClass result = new SourceClass();
            result.setName(((SourceClass)source).getName());
            result.setAge(((SourceClass)source).getAge());
            return Arrays.asList(result);
        }

    }

}