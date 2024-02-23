package com.example.backend.usersubdomain.buisnesslayer;

import com.example.backend.usersubdomain.datalayer.User;
import com.example.backend.usersubdomain.datalayer.UserRepository;
import com.example.backend.usersubdomain.datamapperlayer.UserRequestMapper;
import com.example.backend.usersubdomain.datamapperlayer.UserResponseMapper;
import com.example.backend.usersubdomain.presentationlayer.UserRequestModel;
import com.example.backend.usersubdomain.presentationlayer.UserResponseModel;
import com.example.backend.shipmentsubdomain.datalayer.shipment.ShipmentRepository;
import com.example.backend.util.exceptions.UserNotFoundException;
import com.example.backend.util.exceptions.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;
    private final UserRequestMapper userRequestMapper;
    @Override
    public UserResponseModel getUserByUserId(String userId) {
        User user = userRepository.findUserByUserId(userId);
        if (user == null)
            throw new UserNotFoundException("User with userId: " + userId + " could not be found.");

        return userResponseMapper.toCustomerResponse(user);
    }
    @Override
    public UserResponseModel addUser(UserRequestModel customerRequest) {
        if (userRepository.existsByUserId(customerRequest.getUserId()))
            throw new InvalidRequestException("User with userId: " + customerRequest.getUserId() + " already exists.");
        User user = userRequestMapper.toCustomer(customerRequest);
        user.setUserId(customerRequest.getUserId());
        userRepository.save(user);
        return userResponseMapper.toCustomerResponse(user);
    }

    @Override
    public UserResponseModel updateUser(UserRequestModel customerRequest, String userId) {
        User user = userRepository.findUserByUserId(userId);
        if (user == null) {
            throw new UserNotFoundException("User with userId" + userId + " was not found.");
        }

        user.setFirstName(customerRequest.getFirstName() != null ? customerRequest.getFirstName() : user.getFirstName());
        user.setLastName(customerRequest.getLastName() != null ? customerRequest.getLastName() : user.getLastName());
        user.setEmail(customerRequest.getEmail() != null ? customerRequest.getEmail() : user.getEmail());
        user.setPhoneNumber(customerRequest.getPhoneNumber() != null ? customerRequest.getPhoneNumber() : user.getPhoneNumber());
        user.setProfilePictureUrl(customerRequest.getProfilePictureUrl() != null ? customerRequest.getProfilePictureUrl() : user.getProfilePictureUrl());

        userRepository.save(user);

        return userResponseMapper.toCustomerResponse(user);
    }

    //This is commented out because it is not used in the application but it is a metod that is implemnted everywhere in the application just not used
    //Keep it for the client if they want to remove a user
//    @Override
//    @Transactional
//    public void deleteCustomer(String userId) {
//        if (userRepository.existsByUserId(userId))
//            throw new InvalidRequestException("User does not exist, could not be deleted.");
//        shipmentRepository.deleteAll(shipmentRepository.findShipmentByUserId(userId));
////        shipmentRepository.findShipmentByUserId(userId).forEach(shipment -> {
////            if(shipment.getStatus().equals(Status.TRANSIT) || shipment.getStatus().equals(Status.LOADING) || shipment.getStatus().equals(Status.QUOTED)){
////                throw new InvalidRequestException("User has pending shipments, could not be deleted.");
////            } else if (shipment.getStatus().equals(Status.DELIVERED)) {
////                shipmentRepository.delete(shipment);
////            }
////        });
//        userRepository.deleteCustomerByUserId(userId);
//        if (!userRepository.existsByUserId(userId))
//            throw new InvalidRequestException("User could not be deleted.");
//
//    }
    @Override
    public boolean checkIfUserExistsByUserId(String userId) {
        return userRepository.existsByUserId(userId);
    }

    @Override
    public UserResponseModel getUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null)
            throw new UserNotFoundException("User with email: " + email + " could not be found.");

        return userResponseMapper.toCustomerResponse(user);
    }
}

