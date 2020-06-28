package com.hik.core.android.view.webview;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import com.hik.core.android.view.ViewUtil;

import org.xml.sax.XMLReader;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * html格式处理
 *
 * @author wangtao55
 * @date 2019/9/23
 * @mail wangtao55@hikcreate.com
 */
public class HtmlTagHandler implements Html.TagHandler {

    private Context context;

    public static final String SIZE_FLAG_NORMAL = "1";
    public static final String SIZE_FLAG_SMALL = "2";
    public static final float rate = 0.8f;
    private String tagName;
    private int startIndex = 0;
    private float normalSize;
    private final HashMap<String, String> attributes = new HashMap<>();

    public HtmlTagHandler(Context context, String tagName,float normalSize) {
        this.context = context;
        this.tagName = tagName;
        this.normalSize = normalSize;
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if (tag.equalsIgnoreCase(tagName)) {
            parseAttributes(xmlReader);
            if (opening) {
                startHandleTag(output);
            } else {
                endEndHandleTag(output);
            }
        }
    }

    private void startHandleTag(Editable output) {
        startIndex = output.length();
    }

    private void endEndHandleTag(Editable output) {
        int endIndex = output.length();
        String color = attributes.get("color");
        String size = attributes.get("size");
        if (!TextUtils.isEmpty(size)) {
            size = size.split("px")[0];
        }
        if(normalSize == 0){
            normalSize = ViewUtil.dip2px(context, 13);
        }
        if (!TextUtils.isEmpty(color)) {
            output.setSpan(new ForegroundColorSpan(Color.parseColor(color)), startIndex, endIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (!TextUtils.isEmpty(size)) {
            if (SIZE_FLAG_NORMAL.equals(size)) {
                output.setSpan(new AbsoluteSizeSpan((int) normalSize), startIndex, endIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else {
                output.setSpan(new AbsoluteSizeSpan((int) (normalSize * rate)), startIndex, endIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    /**
     * 解析所有属性值
     *
     * @param xmlReader
     */
    private void parseAttributes(final XMLReader xmlReader) {
        try {
            Field elementField = xmlReader.getClass().getDeclaredField("theNewElement");
            elementField.setAccessible(true);
            Object element = elementField.get(xmlReader);
            if(element != null){
                Field attsField = element.getClass().getDeclaredField("theAtts");
                attsField.setAccessible(true);
                Object atts = attsField.get(element);
                Field dataField = atts.getClass().getDeclaredField("data");
                dataField.setAccessible(true);
                String[] data = (String[]) dataField.get(atts);
                Field lengthField = atts.getClass().getDeclaredField("length");
                lengthField.setAccessible(true);
                int len = (Integer) lengthField.get(atts);
                for (int i = 0; i < len; i++) {
                    attributes.put(data[i * 5 + 1], data[i * 5 + 4]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
