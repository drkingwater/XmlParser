package com.pxq.library.xml;

import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Xml;

import com.pxq.library.xml.annotations.Attribute;
import com.pxq.library.xml.annotations.XmlTag;
import com.pxq.library.xml.element.ElementAttr;
import com.pxq.library.xml.element.ElementTag;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {

    private static final String TAG = "XmlParser";

    @Nullable
    public static String toXml(Object obj) {
        List<ElementTag> elementTags = new ArrayList<>();
        List<ElementAttr> elementAttrs = new ArrayList<>();
        Class<?> clazz = obj.getClass();
        initField(clazz, elementTags, elementAttrs);
        XmlSerializer serializer = Xml.newSerializer();
        try {
            StringWriter stringWriter = new StringWriter();
            serializer.setOutput(stringWriter);
            serializer.startDocument("utf-8", true);

            //TODO 生成TAG
            for (ElementTag elementTag : elementTags) {
                if (elementTag.isRootTag) {
                    setTag(obj, serializer, elementTag, elementTags, elementAttrs);
                }
            }
            serializer.endDocument();

            serializer.flush();
            return stringWriter.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void setTag(Object object, XmlSerializer serializer, ElementTag tag, List<ElementTag> elementTags, List<ElementAttr> elementAttrs) throws IOException, IllegalAccessException {
        if (tag == null)
            return;
        Log.d(TAG, "setTag: " + tag.name);
        serializer.startTag("", tag.name);
        //设置属性
        setAttribute(object, serializer, tag, elementAttrs);
        //设置子Tag
        Log.d(TAG, "setTag: child : " + tag.childTags.length);
        if (tag.childTags != null && tag.childTags.length > 0) {
            for (String childTag : tag.childTags) {
                setTag(object, serializer, getElementTag(childTag, elementTags), elementTags, elementAttrs);
            }
        } else {
            //设置值
            String value = (String) tag.field.get(object);
            Log.d(TAG, "setTag: value " + value);
            serializer.text(value);
        }
        serializer.endTag("", tag.name);
    }

    private static void setAttribute(Object object, XmlSerializer serializer, ElementTag tag, List<ElementAttr> elementAttrs) throws IllegalAccessException, IOException {
        for (String attribute : tag.attributes) {
            ElementAttr elementAttr = getElementAttr(attribute, elementAttrs);
            if (elementAttr != null) {
                serializer.attribute("", elementAttr.name, (String) elementAttr.field.get(object));
            }
        }
    }

    private static ElementAttr getElementAttr(String name, List<ElementAttr> elementAttrs) {
        if (elementAttrs != null) {
            for (ElementAttr elementAttr : elementAttrs) {
                if (elementAttr.name.equals(name)) {
                    return elementAttr;
                }
            }
        }
        return null;
    }

    private static ElementTag getElementTag(String name, List<ElementTag> elementTags) {
        if (elementTags != null) {
            for (ElementTag elementTag : elementTags) {
                if (elementTag.name.equals(name)) {
                    return elementTag;
                }
            }
        }
        return null;
    }

    @Nullable
    public static <T> T fromXml(String xml, Class<T> clazz) {

        List<ElementTag> elementTags = new ArrayList<>();
        List<ElementAttr> elementAttrs = new ArrayList<>();
        initField(clazz, elementTags, elementAttrs);
        try {
            T instance = clazz.newInstance();
            for (ElementTag elementTag : elementTags) {
                Log.d(TAG, "tag: " + elementTag.name);
                elementTag.field.setAccessible(true);
                elementTag.field.set(instance, "testname");
            }

            for (ElementAttr elementAttr : elementAttrs) {
                Log.d(TAG, "attr: " + elementAttr.name);
                elementAttr.field.setAccessible(true);
                elementAttr.field.set(instance, "testattr");
            }
            Log.d(TAG, "object: " + instance.getClass().getName());
            return instance;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static <T> void initField(Class<T> clazz, List<ElementTag> elementTags, List<ElementAttr> elementAttrs) {


        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Log.d(TAG, "getName: " + field.getName());
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                Log.d(TAG, "annotation: " + annotation.toString());
                if (annotation instanceof XmlTag) {
                    XmlTag xmlTag = (XmlTag) annotation;
                    ElementTag elementTag = new ElementTag();
                    elementTag.attributes = xmlTag.attributes();
                    elementTag.childTags = xmlTag.childTags();
                    elementTag.name = xmlTag.name();
                    elementTag.field = field;
                    elementTag.isRootTag = xmlTag.rootTag();
                    elementTags.add(elementTag);
                }
                if (annotation instanceof Attribute) {
                    Attribute attribute = (Attribute) annotation;
                    ElementAttr elementAttr = new ElementAttr();
                    elementAttr.field = field;
                    elementAttr.name = attribute.name();
                    elementAttrs.add(elementAttr);
                }
            }
        }
    }

}
