package com.nplekhanov.cars2.v2.auto2;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

/**
 * @author nplekhanov
 */
public class CardDetails {private String card_id;
    private String card_owner_uid;
    private Long card_rid;
    private Long card_date_created;
    private Long card_date_updated;
    private String card_mark;
    private String card_year;
    private Long card_price;
    private Long card_run;
    private Long card_owners_count;
    private Boolean card_vin;
    private Boolean card_checked;
    private String card_type;
    private String card_state;
    private String card_model;
    private String card_gearbox;
    private Long page_index;
    private String page_type;
    private String card_from;
    private String portal_rid;
    private String rid;
    private Long card_serp_id;
    private Long card_owner_dealer_uid;
    private String card_generation;
    private String card_services;

    public String getCard_services() {
        return card_services;
    }

    public void setCard_services(String card_services) {
        this.card_services = card_services;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getCard_owner_uid() {
        return card_owner_uid;
    }

    public void setCard_owner_uid(String card_owner_uid) {
        this.card_owner_uid = card_owner_uid;
    }

    public Long getCard_rid() {
        return card_rid;
    }

    public void setCard_rid(Long card_rid) {
        this.card_rid = card_rid;
    }

    public Long getCard_date_created() {
        return card_date_created;
    }

    public void setCard_date_created(Long card_date_created) {
        this.card_date_created = card_date_created;
    }

    public Long getCard_date_updated() {
        return card_date_updated;
    }

    public void setCard_date_updated(Long card_date_updated) {
        this.card_date_updated = card_date_updated;
    }

    public String getCard_mark() {
        return card_mark;
    }

    public void setCard_mark(String card_mark) {
        this.card_mark = card_mark;
    }

    public String getCard_year() {
        return card_year;
    }

    public void setCard_year(String card_year) {
        this.card_year = card_year;
    }

    public Long getCard_price() {
        return card_price;
    }

    public void setCard_price(Long card_price) {
        this.card_price = card_price;
    }

    public Long getCard_run() {
        return card_run;
    }

    public void setCard_run(Long card_run) {
        this.card_run = card_run;
    }

    public Long getCard_owners_count() {
        return card_owners_count;
    }

    public void setCard_owners_count(Long card_owners_count) {
        this.card_owners_count = card_owners_count;
    }

    public Boolean getCard_vin() {
        return card_vin;
    }

    public void setCard_vin(Boolean card_vin) {
        this.card_vin = card_vin;
    }

    public Boolean getCard_checked() {
        return card_checked;
    }

    public void setCard_checked(Boolean card_checked) {
        this.card_checked = card_checked;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getCard_state() {
        return card_state;
    }

    public void setCard_state(String card_state) {
        this.card_state = card_state;
    }

    public String getCard_model() {
        return card_model;
    }

    public void setCard_model(String card_model) {
        this.card_model = card_model;
    }

    public String getCard_gearbox() {
        return card_gearbox;
    }

    public void setCard_gearbox(String card_gearbox) {
        this.card_gearbox = card_gearbox;
    }

    public Long getPage_index() {
        return page_index;
    }

    public void setPage_index(Long page_index) {
        this.page_index = page_index;
    }

    public String getPage_type() {
        return page_type;
    }

    public void setPage_type(String page_type) {
        this.page_type = page_type;
    }

    public String getCard_from() {
        return card_from;
    }

    public void setCard_from(String card_from) {
        this.card_from = card_from;
    }

    public String getPortal_rid() {
        return portal_rid;
    }

    public void setPortal_rid(String portal_rid) {
        this.portal_rid = portal_rid;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public Long getCard_serp_id() {
        return card_serp_id;
    }

    public void setCard_serp_id(Long card_serp_id) {
        this.card_serp_id = card_serp_id;
    }

    public Long getCard_owner_dealer_uid() {
        return card_owner_dealer_uid;
    }

    public void setCard_owner_dealer_uid(Long card_owner_dealer_uid) {
        this.card_owner_dealer_uid = card_owner_dealer_uid;
    }

    public String getCard_generation() {
        return card_generation;
    }

    public void setCard_generation(String card_generation) {
        this.card_generation = card_generation;
    }
}
