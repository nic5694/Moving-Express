package com.example.backend.shipmentsubdomain.businesslayer.truck;

import com.example.backend.shipmentsubdomain.datalayer.truck.TruckRepository;
import com.example.backend.shipmentsubdomain.datamapperlayer.truck.TruckResponseMapper;
import com.example.backend.shipmentsubdomain.presentationlayer.truck.TruckResponseModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TruckServiceImpl implements TruckService{
    private final TruckRepository truckRepository;
    private final TruckResponseMapper truckResponseMapper;

    @Override
    public List<TruckResponseModel> getAllTrucks() {
        return(truckResponseMapper.toTruckResponseModelList(truckRepository.findAll()));
    }
}
