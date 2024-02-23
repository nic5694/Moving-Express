package com.example.backend.shipmentsubdomain.datalayer.Address;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.Getter;

@Data
@Embeddable
@Getter
public class AddressIdentifier {
    private String addressId;
    public AddressIdentifier(){
        this.addressId= java.util.UUID.randomUUID().toString();
    }
}
