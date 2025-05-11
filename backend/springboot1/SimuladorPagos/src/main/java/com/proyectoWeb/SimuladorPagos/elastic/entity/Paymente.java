package com.proyectoWeb.SimuladorPagos.elastic.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.UUID;

/**
 * Represents a payment stored in Elasticsearch.
 * <p>
 * This class is mapped to the Elasticsearch index "payments2" and includes details
 * about the sender, receiver, amount, state, and date of the payment.
 */
@Document(indexName = "payments2")
public class Paymente {

    public Paymente(Integer id, Integer sender, Integer receiver, Double amount, Integer state, Date date) {
        this.id = UUID.randomUUID().toString();
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.state = state;
        this.date = date;
    }

    @Id
    private String id;

    private Integer sender;

    private Integer receiver;

    private Double amount;

    private Integer state;

    @Field(type = FieldType.Date, format = {}, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSSX")
    private Date date;

    public String getId() {
        return id;
    }

    public Integer getSender() {
        return sender;
    }

    public Integer getReceiver() {
        return receiver;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getState() {
        return state;
    }

    public Date getDate() {
        return date;
    }
}
