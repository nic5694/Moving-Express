package com.example.backend.usersubdomain.buisnesslayer;

import com.example.backend.usersubdomain.presentationlayer.UserRequestModel;
import com.example.backend.usersubdomain.presentationlayer.UserResponseModel;

public interface UserService {
    UserResponseModel getUserByUserId(String userId);
    UserResponseModel addUser(UserRequestModel userRequestModel);
    UserResponseModel updateUser(UserRequestModel userRequestModel, String userId);
//    void deleteCustomer(String userId);
    boolean checkIfUserExistsByUserId(String userId);
    UserResponseModel getUserByEmail(String email);
}
