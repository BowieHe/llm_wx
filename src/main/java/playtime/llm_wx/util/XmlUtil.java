package playtime.llm_wx.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class XmlUtil {

    public static Map<String, Object> parseXml(String xml) {
        Map<String, Object> map = new HashMap<>();

        try {
            Document document = DocumentHelper.parseText(xml);

            Element rootElement = document.getRootElement();

            for (Element element : rootElement.elements()) {

                String elementName = element.getName();

                // element text might contains \n
                String elementText = element.getText().trim();

                map.put(elementName, elementText);

            }
        } catch (DocumentException e) {
            log.error("failed to parse xml: {}", xml, e);
        }

        return map;
    }

    public static <T> T parseXml(String xml, Class<T> clazz) {
        Map<String, Object> map = parseXml(xml);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(map, clazz);
    }
}
