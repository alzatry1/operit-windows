package com.tom_roush.pdfbox.pdmodel

/** 门面对象：包装 org.apache.pdfbox.pdmodel.PDPage（见 PDDocument 说明）。 */
class PDPage {
    internal val real: org.apache.pdfbox.pdmodel.PDPage = org.apache.pdfbox.pdmodel.PDPage()
}
