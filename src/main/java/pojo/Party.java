package pojo;

public class Party {
    private boolean party_alert;
    private boolean party_is_used;
    private String firstname;
    private String surname;
    private String middle_name;
    private String previous_surname;
    private String emptyValue1;
    private String emptyValue2;
    private String id_number;
    private String emptyValue3;
    private String gender;
    private String passport;
    private String passport_country;
    private String date_of_birth;
    private String np_residential_address;
    private String country_of_residence;
    private String nationality;
    private String country_of_birth;
    private int monthly_income;
    private String date_of_last_income;
    private String emptyValue4;
    private String emptyValue5;
    private String emptyValue6;
    private String np_postal_address;
    private String emptyValue7;
    private String registered_name;
    private String le_operational_address;
    private String emptyValue8;
    private String emptyValue9;
    private String emptyValue10;
    private String source_of_wealth   ; //-> DON'T HAVE (Use 'COMMISSION');
    private String source_of_funds;
    private String profession;
    private String emptyValue11;
    private String registered_name1;
    private String registration_number;
    private String legal_form ; //-> DON'T HAVE (Use TRS);
    private String registered_address;
    private String emptyValue12;
    private String emptyValue13;
    private String emptyValue14;
    private String emptyValue15;
    private String emptyValue16;
    private String nationality3;
    private String vat_registration_number;
    private String emptyValue17;
    private String emptyValue18;
    private String emptyValue19;
    private String relationship_to_client;	//-> DON'T HAVE (Use 'ANNUITANT');
    private String reason_for_transaction;
    private String transaction_date;
    private int transaction_amount;
    private String authorised_by;
    private String emptyValue20;
    private String tax_registration_number;
    private String foreign_tin;
    private String emptyValue_20;
    private String nationality2;
    private String le_operational_address1;
    private String le_operational_address2;
    private String emptyValue21;
    private String emptyValue22;
    private String emptyValue23;
    private String emptyValue24;
    private String emptyValue25;
    private String emptyValue26;
    private String emptyValue27;
    private String emptyValue28;
    private String emptyValue29;
    private String emptyValue30;
    private String emptyValue31;
    private String nature_of_income;	//-> DON'T HAVE (use 'OTHER' as default value);
    private String business_relationship;
    private String emptyValue32;
    private String primary_tax_residence;
    private String additional_tax_residence;
    private String emptyValue33;
    private String emptyValue34;
    private String le_gcoheadoffice_address;
    private String emptyValue35;
    private String emptyValue36;
    private String industry_type;
    private String emptyValue37;
    private String account_type;	//-> Don't have, fill it in.;
    private String account_number;
    private String product_type;	//-> will be pulled from front-end;
    private String business_unit;	//-> DON'T HAVE. Will be pulled from front-end;
    private String risk_class;	//-> Will be pulled from front-end;
    private String party_type;	//-> (use N or L);
    private String parent_account_number;
    private String relationship_to_party;	//-> DON'T HAVE (Use 'BO');
    private String party_status;;	//-> DON'T HAVE (use 'A' for default value);
    private String inception_date;
    private String termination_date;

    public Party(boolean party_alert, boolean party_is_used, String firstname, String surname, String middle_name,
            String previous_surname, String emptyValue1, String emptyValue2, String id_number, String emptyValue3,
            String gender, String passport, String passport_country, String date_of_birth,
            String np_residential_address, String country_of_residence, String nationality, String country_of_birth,
            int monthly_income, String date_of_last_income, String emptyValue4, String emptyValue5, String emptyValue6,
            String np_postal_address, String emptyValue7, String registered_name, String le_operational_address,
            String emptyValue8, String emptyValue9, String emptyValue10, String source_of_wealth,
            String source_of_funds, String profession, String emptyValue11, String registered_name1,
            String registration_number, String legal_form, String registered_address, String emptyValue12,
            String emptyValue13, String emptyValue14, String emptyValue15, String emptyValue16, String nationality3,
            String vat_registration_number, String emptyValue17, String emptyValue18, String emptyValue19,
            String relationship_to_client, String reason_for_transaction, String transaction_date,
            int transaction_amount, String authorised_by, String emptyValue20, String tax_registration_number,
            String foreign_tin, String emptyValue_20, String nationality2, String le_operational_address1,
            String le_operational_address2, String emptyValue21, String emptyValue22, String emptyValue23,
            String emptyValue24, String emptyValue25, String emptyValue26, String emptyValue27, String emptyValue28,
            String emptyValue29, String emptyValue30, String emptyValue31, String nature_of_income,
            String business_relationship, String emptyValue32, String primary_tax_residence,
            String additional_tax_residence, String emptyValue33, String emptyValue34, String le_gcoheadoffice_address,
            String emptyValue35, String emptyValue36, String industry_type, String emptyValue37, String account_type,
            String account_number, String product_type, String business_unit, String risk_class, String party_type,
            String parent_account_number, String relationship_to_party, String party_status, String inception_date,
            String termination_date) {
        this.party_alert = party_alert;
        this.party_is_used = party_is_used;
        this.firstname = firstname;
        this.surname = surname;
        this.middle_name = middle_name;
        this.previous_surname = previous_surname;
        this.emptyValue1 = emptyValue1;
        this.emptyValue2 = emptyValue2;
        this.id_number = id_number;
        this.emptyValue3 = emptyValue3;
        this.gender = gender;
        this.passport = passport;
        this.passport_country = passport_country;
        this.date_of_birth = date_of_birth;
        this.np_residential_address = np_residential_address;
        this.country_of_residence = country_of_residence;
        this.nationality = nationality;
        this.country_of_birth = country_of_birth;
        this.monthly_income = monthly_income;
        this.date_of_last_income = date_of_last_income;
        this.emptyValue4 = emptyValue4;
        this.emptyValue5 = emptyValue5;
        this.emptyValue6 = emptyValue6;
        this.np_postal_address = np_postal_address;
        this.emptyValue7 = emptyValue7;
        this.registered_name = registered_name;
        this.le_operational_address = le_operational_address;
        this.emptyValue8 = emptyValue8;
        this.emptyValue9 = emptyValue9;
        this.emptyValue10 = emptyValue10;
        this.source_of_wealth = source_of_wealth;
        this.source_of_funds = source_of_funds;
        this.profession = profession;
        this.emptyValue11 = emptyValue11;
        this.registered_name1 = registered_name1;
        this.registration_number = registration_number;
        this.legal_form = legal_form;
        this.registered_address = registered_address;
        this.emptyValue12 = emptyValue12;
        this.emptyValue13 = emptyValue13;
        this.emptyValue14 = emptyValue14;
        this.emptyValue15 = emptyValue15;
        this.emptyValue16 = emptyValue16;
        this.nationality3 = nationality3;
        this.vat_registration_number = vat_registration_number;
        this.emptyValue17 = emptyValue17;
        this.emptyValue18 = emptyValue18;
        this.emptyValue19 = emptyValue19;
        this.relationship_to_client = relationship_to_client;
        this.reason_for_transaction = reason_for_transaction;
        this.transaction_date = transaction_date;
        this.transaction_amount = transaction_amount;
        this.authorised_by = authorised_by;
        this.emptyValue20 = emptyValue20;
        this.tax_registration_number = tax_registration_number;
        this.foreign_tin = foreign_tin;
        this.emptyValue_20 = emptyValue_20;
        this.nationality2 = nationality2;
        this.le_operational_address1 = le_operational_address1;
        this.le_operational_address2 = le_operational_address2;
        this.emptyValue21 = emptyValue21;
        this.emptyValue22 = emptyValue22;
        this.emptyValue23 = emptyValue23;
        this.emptyValue24 = emptyValue24;
        this.emptyValue25 = emptyValue25;
        this.emptyValue26 = emptyValue26;
        this.emptyValue27 = emptyValue27;
        this.emptyValue28 = emptyValue28;
        this.emptyValue29 = emptyValue29;
        this.emptyValue30 = emptyValue30;
        this.emptyValue31 = emptyValue31;
        this.nature_of_income = nature_of_income;
        this.business_relationship = business_relationship;
        this.emptyValue32 = emptyValue32;
        this.primary_tax_residence = primary_tax_residence;
        this.additional_tax_residence = additional_tax_residence;
        this.emptyValue33 = emptyValue33;
        this.emptyValue34 = emptyValue34;
        this.le_gcoheadoffice_address = le_gcoheadoffice_address;
        this.emptyValue35 = emptyValue35;
        this.emptyValue36 = emptyValue36;
        this.industry_type = industry_type;
        this.emptyValue37 = emptyValue37;
        this.account_type = account_type;
        this.account_number = account_number;
        this.product_type = product_type;
        this.business_unit = business_unit;
        this.risk_class = risk_class;
        this.party_type = party_type;
        this.parent_account_number = parent_account_number;
        this.relationship_to_party = relationship_to_party;
        this.party_status = party_status;
        this.inception_date = inception_date;
        this.termination_date = termination_date;
    }

    public boolean getParty_alert() {
        return party_alert;
    }

    public void setParty_alert(boolean party_alert) {
        this.party_alert = party_alert;
    }

    public boolean getParty_is_used() {
        return party_is_used;
    }

    public void setParty_is_used(boolean party_is_used) {
        this.party_is_used = party_is_used;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getPrevious_surname() {
        return previous_surname;
    }

    public void setPrevious_surname(String previous_surname) {
        this.previous_surname = previous_surname;
    }

    public String getEmptyValue1() {
        return emptyValue1;
    }

    public void setEmptyValue1(String emptyValue1) {
        this.emptyValue1 = emptyValue1;
    }

    public String getEmptyValue2() {
        return emptyValue2;
    }

    public void setEmptyValue2(String emptyValue2) {
        this.emptyValue2 = emptyValue2;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getEmptyValue3() {
        return emptyValue3;
    }

    public void setEmptyValue3(String emptyValue3) {
        this.emptyValue3 = emptyValue3;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getPassport_country() {
        return passport_country;
    }

    public void setPassport_country(String passport_country) {
        this.passport_country = passport_country;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getNp_residential_address() {
        return np_residential_address;
    }

    public void setNp_residential_address(String np_residential_address) {
        this.np_residential_address = np_residential_address;
    }

    public String getCountry_of_residence() {
        return country_of_residence;
    }

    public void setCountry_of_residence(String country_of_residence) {
        this.country_of_residence = country_of_residence;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getCountry_of_birth() {
        return country_of_birth;
    }

    public void setCountry_of_birth(String country_of_birth) {
        this.country_of_birth = country_of_birth;
    }

    public int getMonthly_income() {
        return monthly_income;
    }

    public void setMonthly_income(int monthly_income) {
        this.monthly_income = monthly_income;
    }

    public String getDate_of_last_income() {
        return date_of_last_income;
    }

    public void setDate_of_last_income(String date_of_last_income) {
        this.date_of_last_income = date_of_last_income;
    }

    public String getEmptyValue4() {
        return emptyValue4;
    }

    public void setEmptyValue4(String emptyValue4) {
        this.emptyValue4 = emptyValue4;
    }

    public String getEmptyValue5() {
        return emptyValue5;
    }

    public void setEmptyValue5(String emptyValue5) {
        this.emptyValue5 = emptyValue5;
    }

    public String getEmptyValue6() {
        return emptyValue6;
    }

    public void setEmptyValue6(String emptyValue6) {
        this.emptyValue6 = emptyValue6;
    }

    public String getNp_postal_address() {
        return np_postal_address;
    }

    public void setNp_postal_address(String np_postal_address) {
        this.np_postal_address = np_postal_address;
    }

    public String getEmptyValue7() {
        return emptyValue7;
    }

    public void setEmptyValue7(String emptyValue7) {
        this.emptyValue7 = emptyValue7;
    }

    public String getRegistered_name() {
        return registered_name;
    }

    public void setRegistered_name(String registered_name) {
        this.registered_name = registered_name;
    }

    public String getLe_operational_address() {
        return le_operational_address;
    }

    public void setLe_operational_address(String le_operational_address) {
        this.le_operational_address = le_operational_address;
    }

    public String getEmptyValue8() {
        return emptyValue8;
    }

    public void setEmptyValue8(String emptyValue8) {
        this.emptyValue8 = emptyValue8;
    }

    public String getEmptyValue9() {
        return emptyValue9;
    }

    public void setEmptyValue9(String emptyValue9) {
        this.emptyValue9 = emptyValue9;
    }

    public String getEmptyValue10() {
        return emptyValue10;
    }

    public void setEmptyValue10(String emptyValue10) {
        this.emptyValue10 = emptyValue10;
    }

    public String getSource_of_wealth() {
        return source_of_wealth;
    }

    public void setSource_of_wealth(String source_of_wealth) {
        this.source_of_wealth = source_of_wealth;
    }

    public String getSource_of_funds() {
        return source_of_funds;
    }

    public void setSource_of_funds(String source_of_funds) {
        this.source_of_funds = source_of_funds;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getEmptyValue11() {
        return emptyValue11;
    }

    public void setEmptyValue11(String emptyValue11) {
        this.emptyValue11 = emptyValue11;
    }

    public String getRegistered_name1() {
        return registered_name1;
    }

    public void setRegistered_name1(String registered_name1) {
        this.registered_name1 = registered_name1;
    }

    public String getRegistration_number() {
        return registration_number;
    }

    public void setRegistration_number(String registration_number) {
        this.registration_number = registration_number;
    }

    public String getLegal_form() {
        return legal_form;
    }

    public void setLegal_form(String legal_form) {
        this.legal_form = legal_form;
    }

    public String getRegistered_address() {
        return registered_address;
    }

    public void setRegistered_address(String registered_address) {
        this.registered_address = registered_address;
    }

    public String getEmptyValue12() {
        return emptyValue12;
    }

    public void setEmptyValue12(String emptyValue12) {
        this.emptyValue12 = emptyValue12;
    }

    public String getEmptyValue13() {
        return emptyValue13;
    }

    public void setEmptyValue13(String emptyValue13) {
        this.emptyValue13 = emptyValue13;
    }

    public String getEmptyValue14() {
        return emptyValue14;
    }

    public void setEmptyValue14(String emptyValue14) {
        this.emptyValue14 = emptyValue14;
    }

    public String getEmptyValue15() {
        return emptyValue15;
    }

    public void setEmptyValue15(String emptyValue15) {
        this.emptyValue15 = emptyValue15;
    }

    public String getEmptyValue16() {
        return emptyValue16;
    }

    public void setEmptyValue16(String emptyValue16) {
        this.emptyValue16 = emptyValue16;
    }

    public String getNationality3() {
        return nationality3;
    }

    public void setNationality3(String nationality3) {
        this.nationality3 = nationality3;
    }

    public String getVat_registration_number() {
        return vat_registration_number;
    }

    public void setVat_registration_number(String vat_registration_number) {
        this.vat_registration_number = vat_registration_number;
    }

    public String getEmptyValue17() {
        return emptyValue17;
    }

    public void setEmptyValue17(String emptyValue17) {
        this.emptyValue17 = emptyValue17;
    }

    public String getEmptyValue18() {
        return emptyValue18;
    }

    public void setEmptyValue18(String emptyValue18) {
        this.emptyValue18 = emptyValue18;
    }

    public String getEmptyValue19() {
        return emptyValue19;
    }

    public void setEmptyValue19(String emptyValue19) {
        this.emptyValue19 = emptyValue19;
    }

    public String getRelationship_to_client() {
        return relationship_to_client;
    }

    public void setRelationship_to_client(String relationship_to_client) {
        this.relationship_to_client = relationship_to_client;
    }

    public String getReason_for_transaction() {
        return reason_for_transaction;
    }

    public void setReason_for_transaction(String reason_for_transaction) {
        this.reason_for_transaction = reason_for_transaction;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

    public int getTransaction_amount() {
        return transaction_amount;
    }

    public void setTransaction_amount(int transaction_amount) {
        this.transaction_amount = transaction_amount;
    }

    public String getAuthorised_by() {
        return authorised_by;
    }

    public void setAuthorised_by(String authorised_by) {
        this.authorised_by = authorised_by;
    }

    public String getEmptyValue20() {
        return emptyValue20;
    }

    public void setEmptyValue20(String emptyValue20) {
        this.emptyValue20 = emptyValue20;
    }

    public String getTax_registration_number() {
        return tax_registration_number;
    }

    public void setTax_registration_number(String tax_registration_number) {
        this.tax_registration_number = tax_registration_number;
    }

    public String getForeign_tin() {
        return foreign_tin;
    }

    public void setForeign_tin(String foreign_tin) {
        this.foreign_tin = foreign_tin;
    }

    public String getEmptyValue_20() {
        return emptyValue_20;
    }

    public void setEmptyValue_20(String emptyValue_20) {
        this.emptyValue_20 = emptyValue_20;
    }

    public String getNationality2() {
        return nationality2;
    }

    public void setNationality2(String nationality2) {
        this.nationality2 = nationality2;
    }

    public String getLe_operational_address1() {
        return le_operational_address1;
    }

    public void setLe_operational_address1(String le_operational_address1) {
        this.le_operational_address1 = le_operational_address1;
    }

    public String getLe_operational_address2() {
        return le_operational_address2;
    }

    public void setLe_operational_address2(String le_operational_address2) {
        this.le_operational_address2 = le_operational_address2;
    }

    public String getEmptyValue21() {
        return emptyValue21;
    }

    public void setEmptyValue21(String emptyValue21) {
        this.emptyValue21 = emptyValue21;
    }

    public String getEmptyValue22() {
        return emptyValue22;
    }

    public void setEmptyValue22(String emptyValue22) {
        this.emptyValue22 = emptyValue22;
    }

    public String getEmptyValue23() {
        return emptyValue23;
    }

    public void setEmptyValue23(String emptyValue23) {
        this.emptyValue23 = emptyValue23;
    }

    public String getEmptyValue24() {
        return emptyValue24;
    }

    public void setEmptyValue24(String emptyValue24) {
        this.emptyValue24 = emptyValue24;
    }

    public String getEmptyValue25() {
        return emptyValue25;
    }

    public void setEmptyValue25(String emptyValue25) {
        this.emptyValue25 = emptyValue25;
    }

    public String getEmptyValue26() {
        return emptyValue26;
    }

    public void setEmptyValue26(String emptyValue26) {
        this.emptyValue26 = emptyValue26;
    }

    public String getEmptyValue27() {
        return emptyValue27;
    }

    public void setEmptyValue27(String emptyValue27) {
        this.emptyValue27 = emptyValue27;
    }

    public String getEmptyValue28() {
        return emptyValue28;
    }

    public void setEmptyValue28(String emptyValue28) {
        this.emptyValue28 = emptyValue28;
    }

    public String getEmptyValue29() {
        return emptyValue29;
    }

    public void setEmptyValue29(String emptyValue29) {
        this.emptyValue29 = emptyValue29;
    }

    public String getEmptyValue30() {
        return emptyValue30;
    }

    public void setEmptyValue30(String emptyValue30) {
        this.emptyValue30 = emptyValue30;
    }

    public String getEmptyValue31() {
        return emptyValue31;
    }

    public void setEmptyValue31(String emptyValue31) {
        this.emptyValue31 = emptyValue31;
    }

    public String getNature_of_income() {
        return nature_of_income;
    }

    public void setNature_of_income(String nature_of_income) {
        this.nature_of_income = nature_of_income;
    }

    public String getBusiness_relationship() {
        return business_relationship;
    }

    public void setBusiness_relationship(String business_relationship) {
        this.business_relationship = business_relationship;
    }

    public String getEmptyValue32() {
        return emptyValue32;
    }

    public void setEmptyValue32(String emptyValue32) {
        this.emptyValue32 = emptyValue32;
    }

    public String getPrimary_tax_residence() {
        return primary_tax_residence;
    }

    public void setPrimary_tax_residence(String primary_tax_residence) {
        this.primary_tax_residence = primary_tax_residence;
    }

    public String getAdditional_tax_residence() {
        return additional_tax_residence;
    }

    public void setAdditional_tax_residence(String additional_tax_residence) {
        this.additional_tax_residence = additional_tax_residence;
    }

    public String getEmptyValue33() {
        return emptyValue33;
    }

    public void setEmptyValue33(String emptyValue33) {
        this.emptyValue33 = emptyValue33;
    }

    public String getEmptyValue34() {
        return emptyValue34;
    }

    public void setEmptyValue34(String emptyValue34) {
        this.emptyValue34 = emptyValue34;
    }

    public String getLe_gcoheadoffice_address() {
        return le_gcoheadoffice_address;
    }

    public void setLe_gcoheadoffice_address(String le_gcoheadoffice_address) {
        this.le_gcoheadoffice_address = le_gcoheadoffice_address;
    }

    public String getEmptyValue35() {
        return emptyValue35;
    }

    public void setEmptyValue35(String emptyValue35) {
        this.emptyValue35 = emptyValue35;
    }

    public String getEmptyValue36() {
        return emptyValue36;
    }

    public void setEmptyValue36(String emptyValue36) {
        this.emptyValue36 = emptyValue36;
    }

    public String getIndustry_type() {
        return industry_type;
    }

    public void setIndustry_type(String industry_type) {
        this.industry_type = industry_type;
    }

    public String getEmptyValue37() {
        return emptyValue37;
    }

    public void setEmptyValue37(String emptyValue37) {
        this.emptyValue37 = emptyValue37;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getBusiness_unit() {
        return business_unit;
    }

    public void setBusiness_unit(String business_unit) {
        this.business_unit = business_unit;
    }

    public String getRisk_class() {
        return risk_class;
    }

    public void setRisk_class(String risk_class) {
        this.risk_class = risk_class;
    }

    public String getParty_type() {
        return party_type;
    }

    public void setParty_type(String party_type) {
        this.party_type = party_type;
    }

    public String getParent_account_number() {
        return parent_account_number;
    }

    public void setParent_account_number(String parent_account_number) {
        this.parent_account_number = parent_account_number;
    }

    public String getRelationship_to_party() {
        return relationship_to_party;
    }

    public void setRelationship_to_party(String relationship_to_party) {
        this.relationship_to_party = relationship_to_party;
    }

    public String getParty_status() {
        return party_status;
    }

    public void setParty_status(String party_status) {
        this.party_status = party_status;
    }

    public String getInception_date() {
        return inception_date;
    }

    public void setInception_date(String inception_date) {
        this.inception_date = inception_date;
    }

    public String getTermination_date() {
        return termination_date;
    }

    public void setTermination_date(String termination_date) {
        this.termination_date = termination_date;
    }

    @Override
    public String toString() {
        return "Party [party_alert=" + party_alert + ", party_is_used=" + party_is_used + ", firstname=" + firstname
                + ", surname=" + surname + ", middle_name=" + middle_name + ", previous_surname=" + previous_surname
                + ", emptyValue1=" + emptyValue1 + ", emptyValue2=" + emptyValue2 + ", id_number=" + id_number
                + ", emptyValue3=" + emptyValue3 + ", gender=" + gender + ", passport=" + passport
                + ", passport_country=" + passport_country + ", date_of_birth=" + date_of_birth
                + ", np_residential_address=" + np_residential_address + ", country_of_residence="
                + country_of_residence + ", nationality=" + nationality + ", country_of_birth=" + country_of_birth
                + ", monthly_income=" + monthly_income + ", date_of_last_income=" + date_of_last_income
                + ", emptyValue4=" + emptyValue4 + ", emptyValue5=" + emptyValue5 + ", emptyValue6=" + emptyValue6
                + ", np_postal_address=" + np_postal_address + ", emptyValue7=" + emptyValue7 + ", registered_name="
                + registered_name + ", le_operational_address=" + le_operational_address + ", emptyValue8="
                + emptyValue8 + ", emptyValue9=" + emptyValue9 + ", emptyValue10=" + emptyValue10
                + ", source_of_wealth=" + source_of_wealth + ", source_of_funds=" + source_of_funds + ", profession="
                + profession + ", emptyValue11=" + emptyValue11 + ", registered_name1=" + registered_name1
                + ", registration_number=" + registration_number + ", legal_form=" + legal_form
                + ", registered_address=" + registered_address + ", emptyValue12=" + emptyValue12 + ", emptyValue13="
                + emptyValue13 + ", emptyValue14=" + emptyValue14 + ", emptyValue15=" + emptyValue15 + ", emptyValue16="
                + emptyValue16 + ", nationality3=" + nationality3 + ", vat_registration_number="
                + vat_registration_number + ", emptyValue17=" + emptyValue17 + ", emptyValue18=" + emptyValue18
                + ", emptyValue19=" + emptyValue19 + ", relationship_to_client=" + relationship_to_client
                + ", reason_for_transaction=" + reason_for_transaction + ", transaction_date=" + transaction_date
                + ", transaction_amount=" + transaction_amount + ", authorised_by=" + authorised_by + ", emptyValue20="
                + emptyValue20 + ", tax_registration_number=" + tax_registration_number + ", foreign_tin=" + foreign_tin
                + ", emptyValue_20=" + emptyValue_20 + ", nationality2=" + nationality2 + ", le_operational_address1="
                + le_operational_address1 + ", le_operational_address2=" + le_operational_address2 + ", emptyValue21="
                + emptyValue21 + ", emptyValue22=" + emptyValue22 + ", emptyValue23=" + emptyValue23 + ", emptyValue24="
                + emptyValue24 + ", emptyValue25=" + emptyValue25 + ", emptyValue26=" + emptyValue26 + ", emptyValue27="
                + emptyValue27 + ", emptyValue28=" + emptyValue28 + ", emptyValue29=" + emptyValue29 + ", emptyValue30="
                + emptyValue30 + ", emptyValue31=" + emptyValue31 + ", nature_of_income=" + nature_of_income
                + ", business_relationship=" + business_relationship + ", emptyValue32=" + emptyValue32
                + ", primary_tax_residence=" + primary_tax_residence + ", additional_tax_residence="
                + additional_tax_residence + ", emptyValue33=" + emptyValue33 + ", emptyValue34=" + emptyValue34
                + ", le_gcoheadoffice_address=" + le_gcoheadoffice_address + ", emptyValue35=" + emptyValue35
                + ", emptyValue36=" + emptyValue36 + ", industry_type=" + industry_type + ", emptyValue37="
                + emptyValue37 + ", account_type=" + account_type + ", account_number=" + account_number
                + ", product_type=" + product_type + ", business_unit=" + business_unit + ", risk_class=" + risk_class
                + ", party_type=" + party_type + ", parent_account_number=" + parent_account_number
                + ", relationship_to_party=" + relationship_to_party + ", party_status=" + party_status
                + ", inception_date=" + inception_date + ", termination_date=" + termination_date + ", getClass()="
                + getClass() + ", hashCode()=" + hashCode() + ", getParty_alert()=" + getParty_alert()
                + ", getParty_is_used()=" + getParty_is_used() + ", getFirstname()=" + getFirstname()
                + ", getSurname()=" + getSurname() + ", getMiddle_name()=" + getMiddle_name() + ", toString()="
                + super.toString() + ", getPrevious_surname()=" + getPrevious_surname() + ", getEmptyValue1()="
                + getEmptyValue1() + ", getEmptyValue2()=" + getEmptyValue2() + ", getId_number()=" + getId_number()
                + ", getEmptyValue3()=" + getEmptyValue3() + ", getGender()=" + getGender() + ", getPassport()="
                + getPassport() + ", getPassport_country()=" + getPassport_country() + ", getDate_of_birth()="
                + getDate_of_birth() + ", getNp_residential_address()=" + getNp_residential_address()
                + ", getCountry_of_residence()=" + getCountry_of_residence() + ", getNationality()=" + getNationality()
                + ", getCountry_of_birth()=" + getCountry_of_birth() + ", getMonthly_income()=" + getMonthly_income()
                + ", getDate_of_last_income()=" + getDate_of_last_income() + ", getEmptyValue4()=" + getEmptyValue4()
                + ", getEmptyValue5()=" + getEmptyValue5() + ", getEmptyValue6()=" + getEmptyValue6()
                + ", getNp_postal_address()=" + getNp_postal_address() + ", getEmptyValue7()=" + getEmptyValue7()
                + ", getRegistered_name()=" + getRegistered_name() + ", getLe_operational_address()="
                + getLe_operational_address() + ", getEmptyValue8()=" + getEmptyValue8() + ", getEmptyValue9()="
                + getEmptyValue9() + ", getEmptyValue10()=" + getEmptyValue10() + ", getSource_of_wealth()="
                + getSource_of_wealth() + ", getSource_of_funds()=" + getSource_of_funds() + ", getProfession()="
                + getProfession() + ", getEmptyValue11()=" + getEmptyValue11() + ", getRegistered_name1()="
                + getRegistered_name1() + ", getRegistration_number()=" + getRegistration_number()
                + ", getLegal_form()=" + getLegal_form() + ", getRegistered_address()=" + getRegistered_address()
                + ", getEmptyValue12()=" + getEmptyValue12() + ", getEmptyValue13()=" + getEmptyValue13()
                + ", getEmptyValue14()=" + getEmptyValue14() + ", getEmptyValue15()=" + getEmptyValue15()
                + ", getEmptyValue16()=" + getEmptyValue16() + ", getNationality3()=" + getNationality3()
                + ", getVat_registration_number()=" + getVat_registration_number() + ", getEmptyValue17()="
                + getEmptyValue17() + ", getEmptyValue18()=" + getEmptyValue18() + ", getEmptyValue19()="
                + getEmptyValue19() + ", getRelationship_to_client()=" + getRelationship_to_client()
                + ", getReason_for_transaction()=" + getReason_for_transaction() + ", getTransaction_date()="
                + getTransaction_date() + ", getTransaction_amount()=" + getTransaction_amount()
                + ", getAuthorised_by()=" + getAuthorised_by() + ", getEmptyValue20()=" + getEmptyValue20()
                + ", getTax_registration_number()=" + getTax_registration_number() + ", getForeign_tin()="
                + getForeign_tin() + ", getEmptyValue_20()=" + getEmptyValue_20() + ", getNationality2()="
                + getNationality2() + ", getLe_operational_address1()=" + getLe_operational_address1()
                + ", getLe_operational_address2()=" + getLe_operational_address2() + ", getEmptyValue21()="
                + getEmptyValue21() + ", getEmptyValue22()=" + getEmptyValue22() + ", getEmptyValue23()="
                + getEmptyValue23() + ", getEmptyValue24()=" + getEmptyValue24() + ", getEmptyValue25()="
                + getEmptyValue25() + ", getEmptyValue26()=" + getEmptyValue26() + ", getEmptyValue27()="
                + getEmptyValue27() + ", getEmptyValue28()=" + getEmptyValue28() + ", getEmptyValue29()="
                + getEmptyValue29() + ", getEmptyValue30()=" + getEmptyValue30() + ", getEmptyValue31()="
                + getEmptyValue31() + ", getNature_of_income()=" + getNature_of_income()
                + ", getBusiness_relationship()=" + getBusiness_relationship() + ", getEmptyValue32()="
                + getEmptyValue32() + ", getPrimary_tax_residence()=" + getPrimary_tax_residence()
                + ", getAdditional_tax_residence()=" + getAdditional_tax_residence() + ", getEmptyValue33()="
                + getEmptyValue33() + ", getEmptyValue34()=" + getEmptyValue34() + ", getLe_gcoheadoffice_address()="
                + getLe_gcoheadoffice_address() + ", getEmptyValue35()=" + getEmptyValue35() + ", getEmptyValue36()="
                + getEmptyValue36() + ", getIndustry_type()=" + getIndustry_type() + ", getEmptyValue37()="
                + getEmptyValue37() + ", getAccount_type()=" + getAccount_type() + ", getAccount_number()="
                + getAccount_number() + ", getProduct_type()=" + getProduct_type() + ", getBusiness_unit()="
                + getBusiness_unit() + ", getRisk_class()=" + getRisk_class() + ", getParty_type()=" + getParty_type()
                + ", getParent_account_number()=" + getParent_account_number() + ", getRelationship_to_party()="
                + getRelationship_to_party() + ", getParty_status()=" + getParty_status() + ", getInception_date()="
                + getInception_date() + ", getTermination_date()=" + getTermination_date() + "]";
    }
    
    
}