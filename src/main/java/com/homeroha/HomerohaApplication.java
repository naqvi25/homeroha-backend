package com.homeroha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HomerohaApplication {
	public static void main(String[] args) {
		SpringApplication.run(HomerohaApplication.class, args);
	}
}

//src/
//		└── main/
//		├── java/com/homeroha/
//		│   ├── config/                 → Security & JWT configs
//    │   ├── controller/             → AuthController
//    │   ├── dto/                    → UserDTO, AuthRequest, etc.
//    │   ├── exception/              → Custom exceptions
//    │   ├── model/                  → User entity
//    │   ├── repository/             → UserRepository
//    │   ├── security/               → JWTFilter, TokenProvider
//    │   ├── service/                → UserService, AuthService
//    │   └── HomerohaApplication.java
//    └── resources/
//		└── application.properties