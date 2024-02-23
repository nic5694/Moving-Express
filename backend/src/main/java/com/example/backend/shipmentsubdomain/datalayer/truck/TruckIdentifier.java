package com.example.backend.shipmentsubdomain.datalayer.truck;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class TruckIdentifier {
        private String vin;
        public TruckIdentifier(String vin){
            this.vin = vin;
        }
}
