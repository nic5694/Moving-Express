package com.example.backend.usersubdomain.datamapperlayer;

import com.example.backend.usersubdomain.datalayer.User;
import com.example.backend.usersubdomain.presentationlayer.UserResponseModel;
import lombok.Generated;
import org.mapstruct.Mapper;

@Generated
@Mapper(componentModel = "spring")
public interface UserResponseMapper {
    UserResponseModel toCustomerResponse(User user);
}
