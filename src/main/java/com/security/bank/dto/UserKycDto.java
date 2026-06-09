package com.security.bank.dto;

/**
 * DTO for returning KYC information of a user.
 * Manual getters/setters provided (no Lombok).
 */
public class UserKycDto {
    private String name;
    private String address;
    private Long number;
    private String identityProof;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getIdentityProof() {
        return identityProof;
    }

    public void setIdentityProof(String identityProof) {
        this.identityProof = identityProof;
    }
}