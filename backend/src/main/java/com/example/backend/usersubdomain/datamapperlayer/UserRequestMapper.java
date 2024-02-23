package com.example.backend.usersubdomain.datamapperlayer;

import com.example.backend.usersubdomain.datalayer.User;
import com.example.backend.usersubdomain.presentationlayer.UserRequestModel;
import lombok.Generated;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Generated
@Mapper(componentModel = "spring")
public interface UserRequestMapper {
    @Mapping(ignore = true, target = "userId")
    @Mapping(ignore = true, target = "id")
    User toCustomer(UserRequestModel customerRequest);
}
