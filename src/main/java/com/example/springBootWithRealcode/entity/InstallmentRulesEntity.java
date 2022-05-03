package com.example.springBootWithRealcode.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="PTP_INSTALLMENT_RULES",schema="citsel")
//@TypeDef(name="json",typeClass = JsonStringType.class)
public class InstallmentRulesEntity {

    @Id
    @Column(name="channel_name")
    private String channelName;

    @Column(name="franchise")
    private String franchise;

    @Type(type = "json")
    @Column(name="recommended_payment_list")
    private InstallementConfiguration installementConfiguration;




}
