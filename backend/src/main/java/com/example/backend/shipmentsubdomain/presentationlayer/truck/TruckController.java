package com.example.backend.shipmentsubdomain.presentationlayer.truck;

import com.example.backend.shipmentsubdomain.businesslayer.truck.TruckService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/movingexpress/trucks")
@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "https://movingexpress.systems"}, allowCredentials = "true")
public class TruckController {
    private final TruckService truckService;

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('Truck_Driver')")
    public List<TruckResponseModel> getAllTrucks() {
        return truckService.getAllTrucks();
    }
}
