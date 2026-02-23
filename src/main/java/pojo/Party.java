package pojo;

public class Party {
    private String business_unit;
    private String party_type;
	private boolean party_alert;
	private boolean party_is_used;
	private String firstname;
	private String surname;
	private String middle_name;
	private String previous_surname;
	private String date_of_birth;
	private String country_of_birth;
	private String nationality;
	private String country_of_residence;
	private String gender;
	private String profession;
	private int monthly_income;
	private String date_of_last_income;
	private String id_number;
	private String nationality2;
	private String nationality3;
	private String passport;
	private String passport_country;
	private String tax_registration_number;
	private String primary_tax_residence;
	private String foreign_tin;
	private String foreign_tin_issuing_country;
	private String reason_for_transaction;
	private String product_type;
	private String risk_class;
	private String business_relationship;
	private String source_of_funds;
	private String account_number;
	private int transaction_amount;
	private String transaction_date;
	private String inception_date;
	private String authorised_by;
	private String termination_date;
	private String registered_name;
	private String registration_number;
	private String date_of_registration;
	private String country_of_registration;
	private String industry_type;
	private String additional_tax_residence;
	private String vat_registration_number;
	private String np_residential_address;
	private String np_postal_address;
	private String np_pobox_address;
	private String le_postal_address;
	private String le_pobox_address;
	private String le_registered_address;
	private String le_gcoheadoffice_address;
	private String le_operational_address;
    private String parent_account_number;

    

    public Party(String business_unit, String party_type, boolean party_alert, boolean party_is_used, String firstname,
            String surname,String middle_name, String previous_surname, String date_of_birth, String country_of_birth,
            String nationality, String country_of_residence, String gender, String profession, int monthly_income,
            String date_of_last_income, String id_number, String nationality2, String nationality3, String passport,
            String passport_country, String tax_registration_number, String primary_tax_residence, String foreign_tin,
            String foreign_tin_issuing_country, String reason_for_transaction, String product_type, String risk_class,
            String business_relationship, String source_of_funds, String account_number, int transaction_amount,
            String transaction_date, String inception_date, String authorised_by, String termination_date,
            String registered_name, String registration_number, String date_of_registration,
            String country_of_registration, String industry_type, String additional_tax_residence,
            String vat_registration_number, String np_residential_address, String np_postal_address,
            String np_pobox_address, String le_postal_address, String le_pobox_address, String le_registered_address,
            String le_gcoheadoffice_address, String le_operational_address, String parent_account_number) {
        this.business_unit = business_unit; 
        this.party_type = party_type;
        this.party_alert = party_alert;
        this.party_is_used = party_is_used;
        this.firstname = firstname;
        this.surname = surname;
        this.middle_name = middle_name;
        this.previous_surname = previous_surname;
        this.date_of_birth = date_of_birth;
        this.country_of_birth = country_of_birth;
        this.nationality = nationality;
        this.country_of_residence = country_of_residence;
        this.gender = gender;
        this.profession = profession;
        this.monthly_income = monthly_income;
        this.date_of_last_income = date_of_last_income;
        this.id_number = id_number;
        this.nationality2 = nationality2;
        this.nationality3 = nationality3;
        this.passport = passport;
        this.passport_country = passport_country;
        this.tax_registration_number = tax_registration_number;
        this.primary_tax_residence = primary_tax_residence;
        this.foreign_tin = foreign_tin;
        this.foreign_tin_issuing_country = foreign_tin_issuing_country;
        this.reason_for_transaction = reason_for_transaction;
        this.product_type = product_type;
        this.risk_class = risk_class;
        this.business_relationship = business_relationship;
        this.source_of_funds = source_of_funds;
        this.account_number = account_number;
        this.transaction_amount = transaction_amount;
        this.transaction_date = transaction_date;
        this.inception_date = inception_date;
        this.authorised_by = authorised_by;
        this.termination_date = termination_date;
        this.registered_name = registered_name;
        this.registration_number = registration_number;
        this.date_of_registration = date_of_registration;
        this.country_of_registration = country_of_registration;
        this.industry_type = industry_type;
        this.additional_tax_residence = additional_tax_residence;
        this.vat_registration_number = vat_registration_number;
        this.np_residential_address = np_residential_address;
        this.np_postal_address = np_postal_address;
        this.np_pobox_address = np_pobox_address;
        this.le_postal_address = le_postal_address;
        this.le_pobox_address = le_pobox_address;
        this.le_registered_address = le_registered_address;
        this.le_gcoheadoffice_address = le_gcoheadoffice_address;
        this.le_operational_address = le_operational_address;
        this.parent_account_number = parent_account_number;
    }

    public String getParent_account_number() {
        return parent_account_number;
    }

    public void setParent_account_number(String parent_account_number) {
        this.parent_account_number = parent_account_number;
    }

    public String getParty_type() {
        return party_type;
    }
    public void setParty_type(String party_type) {
        this.party_type = party_type;
    }
    public boolean isParty_alert() {
        return party_alert;
    }
    public void setParty_alert(boolean party_alert) {
        this.party_alert = party_alert;
    }
    public boolean isParty_is_used() {
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
    public String getDate_of_birth() {
        return date_of_birth;
    }
    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }
    public String getCountry_of_birth() {
        return country_of_birth;
    }
    public void setCountry_of_birth(String country_of_birth) {
        this.country_of_birth = country_of_birth;
    }
    public String getNationality() {
        return nationality;
    }
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    public String getCountry_of_residence() {
        return country_of_residence;
    }
    public void setCountry_of_residence(String country_of_residence) {
        this.country_of_residence = country_of_residence;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getProfession() {
        return profession;
    }
    public void setProfession(String profession) {
        this.profession = profession;
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
    public String getId_number() {
        return id_number;
    }
    public void setId_number(String id_number) {
        this.id_number = id_number;
    }
    public String getNationality2() {
        return nationality2;
    }
    public void setNationality2(String nationality2) {
        this.nationality2 = nationality2;
    }
    public String getNationality3() {
        return nationality3;
    }
    public void setNationality3(String nationality3) {
        this.nationality3 = nationality3;
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
    public String getTax_registration_number() {
        return tax_registration_number;
    }
    public void setTax_registration_number(String tax_registration_number) {
        this.tax_registration_number = tax_registration_number;
    }
    public String getPrimary_tax_residence() {
        return primary_tax_residence;
    }
    public void setPrimary_tax_residence(String primary_tax_residence) {
        this.primary_tax_residence = primary_tax_residence;
    }
    public String getForeign_tin() {
        return foreign_tin;
    }
    public void setForeign_tin(String foreign_tin) {
        this.foreign_tin = foreign_tin;
    }
    public String getForeign_tin_issuing_country() {
        return foreign_tin_issuing_country;
    }
    public void setForeign_tin_issuing_country(String foreign_tin_issuing_country) {
        this.foreign_tin_issuing_country = foreign_tin_issuing_country;
    }
    public String getReason_for_transaction() {
        return reason_for_transaction;
    }
    public void setReason_for_transaction(String reason_for_transaction) {
        this.reason_for_transaction = reason_for_transaction;
    }
    public String getProduct_type() {
        return product_type;
    }
    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }
    public String getRisk_class() {
        return risk_class;
    }
    public void setRisk_class(String risk_class) {
        this.risk_class = risk_class;
    }
    public String getBusiness_relationship() {
        return business_relationship;
    }
    public void setBusiness_relationship(String business_relationship) {
        this.business_relationship = business_relationship;
    }
    public String getSource_of_funds() {
        return source_of_funds;
    }
    public void setSource_of_funds(String source_of_funds) {
        this.source_of_funds = source_of_funds;
    }
    public String getAccount_number() {
        return account_number;
    }
    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }
    public int getTransaction_amount() {
        return transaction_amount;
    }
    public void setTransaction_amount(int transaction_amount) {
        this.transaction_amount = transaction_amount;
    }
    public String getTransaction_date() {
        return transaction_date;
    }
    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }
    public String getInception_date() {
        return inception_date;
    }
    public void setInception_date(String inception_date) {
        this.inception_date = inception_date;
    }
    public String getAuthorised_by() {
        return authorised_by;
    }
    public void setAuthorised_by(String authorised_by) {
        this.authorised_by = authorised_by;
    }
    public String getTermination_date() {
        return termination_date;
    }
    public void setTermination_date(String termination_date) {
        this.termination_date = termination_date;
    }
    public String getRegistered_name() {
        return registered_name;
    }
    public void setRegistered_name(String registered_name) {
        this.registered_name = registered_name;
    }
    public String getRegistration_number() {
        return registration_number;
    }
    public void setRegistration_number(String registration_number) {
        this.registration_number = registration_number;
    }
    public String getDate_of_registration() {
        return date_of_registration;
    }
    public void setDate_of_registration(String date_of_registration) {
        this.date_of_registration = date_of_registration;
    }
    public String getCountry_of_registration() {
        return country_of_registration;
    }
    public void setCountry_of_registration(String country_of_registration) {
        this.country_of_registration = country_of_registration;
    }
    public String getIndustry_type() {
        return industry_type;
    }
    public void setIndustry_type(String industry_type) {
        this.industry_type = industry_type;
    }
    public String getAdditional_tax_residence() {
        return additional_tax_residence;
    }
    public void setAdditional_tax_residence(String additional_tax_residence) {
        this.additional_tax_residence = additional_tax_residence;
    }
    public String getVat_registration_number() {
        return vat_registration_number;
    }
    public void setVat_registration_number(String vat_registration_number) {
        this.vat_registration_number = vat_registration_number;
    }
    public String getNp_residential_address() {
        return np_residential_address;
    }
    public void setNp_residential_address(String np_residential_address) {
        this.np_residential_address = np_residential_address;
    }
    public String getNp_postal_address() {
        return np_postal_address;
    }
    public void setNp_postal_address(String np_postal_address) {
        this.np_postal_address = np_postal_address;
    }
    public String getNp_pobox_address() {
        return np_pobox_address;
    }
    public void setNp_pobox_address(String np_pobox_address) {
        this.np_pobox_address = np_pobox_address;
    }
    public String getLe_postal_address() {
        return le_postal_address;
    }
    public void setLe_postal_address(String le_postal_address) {
        this.le_postal_address = le_postal_address;
    }
    public String getLe_pobox_address() {
        return le_pobox_address;
    }
    public void setLe_pobox_address(String le_pobox_address) {
        this.le_pobox_address = le_pobox_address;
    }
    public String getLe_registered_address() {
        return le_registered_address;
    }
    public void setLe_registered_address(String le_registered_address) {
        this.le_registered_address = le_registered_address;
    }
    public String getLe_gcoheadoffice_address() {
        return le_gcoheadoffice_address;
    }
    public void setLe_gcoheadoffice_address(String le_gcoheadoffice_address) {
        this.le_gcoheadoffice_address = le_gcoheadoffice_address;
    }
    public String getLe_operational_address() {
        return le_operational_address;
    }
    public void setLe_operational_address(String le_operational_address) {
        this.le_operational_address = le_operational_address;
    }

    public String getBusiness_unit() {
        return business_unit;
    }

    public void setBusiness_unit(String business_unit) {
        this.business_unit = business_unit;
    }
}
