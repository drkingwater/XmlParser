package com.pxq.library.bean;

import com.pxq.library.xml.annotations.Attribute;
import com.pxq.library.xml.annotations.XmlTag;

public class XmlTest {

    @XmlTag(name = "name", attributes = {"subname"}, childTags = {"name1", "name2"}, rootTag = true)
    public String name;

    @Attribute(name = "subname")
    public String subName;

    @XmlTag(name = "name1", attributes = {"subname1"})
    public String name1;

    @Attribute(name = "subname1")
    public String subName1;

    @XmlTag(name = "name2", attributes = {"subname2"})
    public String name2;

    @Attribute(name = "subname2")
    public String subName2;

    @Override
    public String toString() {
        return "XmlTest{" +
                "name='" + name + '\'' +
                ", subName='" + subName + '\'' +
                '}';
    }
}
