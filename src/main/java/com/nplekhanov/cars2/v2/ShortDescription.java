package com.nplekhanov.cars2.v2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author nplekhanov
 */
public class ShortDescription {
    private String requestedMark;
    private String url;
    private String title;
    private String subTitle;
    private String mark;
    private String model;
    private String shortDetails;
    private String price;
    private String currency;
    private String year;
    private String run;
    private String color;
    private String bodyType;
    private String region;
    private String dataStatParams;
    private String invalidHtml;
    private String exception;

    public String getInvalidHtml() {
        return invalidHtml;
    }

    public void setInvalidHtml(String invalidHtml) {
        this.invalidHtml = invalidHtml;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getShortDetails() {
        return shortDetails;
    }

    public void setShortDetails(String shortDetails) {
        this.shortDetails = shortDetails;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRun() {
        return run;
    }

    public void setRun(String run) {
        this.run = run;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDataStatParams() {
        return dataStatParams;
    }

    public void setDataStatParams(String dataStatParams) {
        this.dataStatParams = dataStatParams;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRequestedMark() {
        return requestedMark;
    }

    public void setRequestedMark(String requestedMark) {
        this.requestedMark = requestedMark;
    }

    public static void main(String[] args) throws IOException {

        String html  = FileUtils.readFileToString(new File("samples/Продажа транспортных средств.html"));
        Document document = Jsoup.parse(html);
        parse(document, null).stream().map(ShortDescription::toJson).forEach(System.out::println);
    }

    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static List<ShortDescription> parse(Document document, String requestedMark) {
        List<ShortDescription> list = new ArrayList<ShortDescription>();
        try {
            document.select("table.sales-list-table tr.in-logging").forEach(tr -> {
                ShortDescription d = new ShortDescription();
                try {
                    if (tr == null) {
                        return;
                    }
                    d.setRequestedMark(requestedMark);
                    d.setDataStatParams(tr.attr("data-stat_params"));
                    d.setTitle(tr.select("td.sales-list-cell.sales-list-cell_mark_id > h3 > a").first().ownText());
                    d.setSubTitle(tr.select("td.sales-list-cell.sales-list-cell_mark_id").first().ownText());
                    d.setUrl(tr.select("td.sales-list-cell.sales-list-cell_mark_id > h3 > a").attr("href"));
                    d.setShortDetails(tr.select("td.sales-list-cell.sales-list-cell_mark_id > h3").first().ownText());
                    d.setPrice(tr.select("td.sales-list-cell.sales-list-cell_price div.sales-list-price").first().ownText());
                    d.setCurrency(tr.select("td.sales-list-cell.sales-list-cell_price div.sales-list-price span").first().ownText());
                    d.setYear(tr.select("td.sales-list-cell.sales-list-cell_year").first().ownText());
                    d.setRun(tr.select("td.sales-list-cell.sales-list-cell_run").first().ownText());
                    d.setColor(tr.select("td.sales-list-cell.sales-list-cell_body_type div.body-type-text span.sales-list-color").first().ownText());
                    d.setBodyType(tr.select("td.sales-list-cell.sales-list-cell_body_type div.body-type-text span.sales-list-body-name").first().ownText());
                    d.setRegion(tr.select("td.sales-list-cell.sales-list-cell_poi_id div.sales-list-region").first().ownText());
                } catch (Exception e) {
//                    System.out.println(e);
                    d.setInvalidHtml(tr.toString());
                    d.setException(ExceptionUtils.getFullStackTrace(e));
                }
//                System.out.println(d);
                list.add(d);
            });
        } catch (NullPointerException e) {
            throw e;
        }
        return list;
    }

    @Override
    public String toString() {
        return "ShortDescription{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", mark='" + mark + '\'' +
                ", model='" + model + '\'' +
                ", shortDetails='" + shortDetails + '\'' +
                ", price='" + price + '\'' +
                ", currency='" + currency + '\'' +
                ", year='" + year + '\'' +
                ", run='" + run + '\'' +
                ", color='" + color + '\'' +
                ", bodyType='" + bodyType + '\'' +
                ", region='" + region + '\'' +
                ", dataStatParams='" + dataStatParams + "' ("+readDataStatParams()+")" +
                '}';
    }

    private Object readDataStatParams() {
        try {
            return new ObjectMapper().readValue(dataStatParams, Map.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getException() {
        return exception;
    }
}
