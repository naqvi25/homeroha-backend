package com.homeroha.service;

import com.homeroha.dto.UserDashboardDTO;

public interface UserService {
    UserDashboardDTO getDashboardInfo(String userEmail);
    Long getUserIdByEmail(String email);

}
